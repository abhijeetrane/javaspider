package org.eclipse.contribution.spider;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.contribution.minidraw.Figure;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IFigureListener;
import org.eclipse.contribution.minidraw.IGroup;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;

public class BindingFigure extends Figure implements IFigureListener {
	protected ObjectFigure fromFigure;
	protected FieldFigure fieldFigure;
	protected ObjectFigure toFigure;
	private static final int ArrowheadLength= 7;
	
	public BindingFigure(ObjectFigure fromFigure, FieldFigure fieldFigure, ObjectFigure toFigure) {
		this.fromFigure= fromFigure;
		this.fieldFigure= fieldFigure;
		fromFigure.addListener(this); 
		this.toFigure= toFigure;
		toFigure.addListener(this);
	}

	public void paint(GC gc) {
		gc.setLineWidth(lineWidth());
		gc.drawLine(fromX(), fromY(), toX(), toY());
		paintArrowhead(gc);
	}

	public void paintArrowhead(GC gc) {
		double theta= theta(fromX(), fromY(), toX(), toY());
		Point leftHead= point(ArrowheadLength, theta - Math.toRadians(10.0));
		gc.drawLine(toX(), toY(), toX() - leftHead.x, toY() - leftHead.y);
		Point rightHead= point(ArrowheadLength, theta + Math.toRadians(10.0));
		gc.drawLine(toX(), toY(), toX() - rightHead.x, toY() - rightHead.y);
	}

	private Point point(int r, double theta) {
		return new Point((int) Math.round(r * Math.cos(theta)), (int) Math.round(r * Math.sin(theta)));
	}

	private double theta(int x1, int y1, int x2, int y2) {
		int dx= x2 - x1;
		int dy= y2 - y1;
		if (dx == 0) {
			// Something is wrong here making arrowheads on vertical lines go goofy
			return (dy == 0)
				? Math.PI * 0.5
				: Math.PI * 1.5;
		} else {
			double theta= Math.atan((double) dy / (double) dx);
			if (dx < 0) return theta + Math.PI;
			if (theta < 0) return theta + Math.PI * 2;
			return theta;
		}
	}

	public Rectangle bounds() {
		// Expand rectangle by 3 to be sure to include the arrowhead
		return new Rectangle(x() - 3, y() - 3, width() + 6, height() + 6);
	}

	private int height() {
		return Math.abs(fromY()-toY()) + lineWidth();
	}

	private int width() {
		return Math.abs(fromX()-toX()) + lineWidth();
	}

	private int y() {
		return Math.min(fromY(), toY());
	}

	private int x() {
		return Math.min(fromX(), toX());
	}

	private int lineWidth() {
		return 1;
	}
	
	public int fromX() {
		return fieldFigure.bounds().x + fieldFigure.bounds().width;
	}

	public int fromY() {
		return fieldFigure.bounds().y + fieldFigure.bounds().height / 2;
	}

	public int toX() {
		return toFigure.bounds().x;
	}

	public int toY() {
		return toFigure.bounds().y;
	}

	// One of the figures on which we depend has changed, so we must change, too
	public void changed(IFigure figure) {
		// This is kind of ugly. Is it possible to short circuit this in the caller?
		if (figure == fromFigure || figure == toFigure || figure == this)	
			broadcastChange(this);
	}

	public IFigure figureAt(Point where) {
		// TODO cool match
		// IFigure figure= super.figureAt(where);
		//return null; // For now. Later do the cool math
		return super.figureAt(where);
	}
	
	public void removedFrom(IGroup drawing) { 
		super.removedFrom(drawing);
		fromFigure.remove(fieldFigure); 
		fromFigure.removeListener(this);
		toFigure.removeListener(this);
	} 

	public void collectCallsOnField(IModel model) {
		 collectCallsOnFields(fieldFigure, model);
} 
	private void collectCallsOnFields(FieldFigure fieldFigure, IModel model) {
		String fieldName= fieldFigure.getText();
		IType type= findTypeInWorkspace(fieldFigure.getSubject().getQualifiedClassName());
		if (type == null) {
			MessageDialog.openError(null, "Find Calls", "Type not found in workspace");
			return;
		}
		SortedSet result= new TreeSet();
		collectReferences(type, fieldName, result);
		
		// collect references in supertypes
		ITypeHierarchy hierarchy= null;
		try {
			hierarchy= type.newTypeHierarchy(new NullProgressMonitor());
		} catch (JavaModelException e) {
		}
		if (hierarchy != null) {
			IType[] types= hierarchy.getAllSuperclasses(type);
			for (int i= 0; i < types.length; i++) 
				collectReferences(types[i], fieldName, result);
		}
		showContract(result, model);
	}

	private void collectReferences(IType type, String fieldName, SortedSet result) {
		ICompilationUnit unit= type.getCompilationUnit();
		CompilationUnit cu= null;
		if (unit != null) { // source file
			cu= AST.parseCompilationUnit(unit, true);
		} else {	// class file
			IClassFile classFile= type.getClassFile();
			String source= null;
			try {
				source= classFile.getSource();
			} catch (JavaModelException e) {
			}
			if (source != null) 
				cu= AST.parseCompilationUnit(source.toCharArray());
		}
		if (cu != null) {
			TypeDeclaration td= findTypeDeclaration(cu, type.getElementName());
			if (td != null) {
				FieldCallsVisitor visitor= new FieldCallsVisitor(fieldName);
				td.accept(visitor);
				resolveArguments(visitor.getMatches(), result);
			}
		}
	}
	
	private void showContract(SortedSet result, IModel model) {
		MenuManager menuMgr= new MenuManager();
		Menu menu= menuMgr.createContextMenu(((SpiderView)model).getCanvas());
		if (result.size() == 0) {
			menuMgr.add(new Action("No Method Calls Found") {
			});
		} else {
			for (Iterator each= result.iterator(); each.hasNext();) {
				String element= (String) each.next();
				menuMgr.add(new Action(element) {
				});
			}
		}
		menu.setVisible(true);		
	}
	
	private void resolveArguments(Set methodInvocations, Set result) {
		for (Iterator each= methodInvocations.iterator(); each.hasNext();) {
			MethodInvocation mi= (MethodInvocation) each.next();
			StringBuffer sb= new StringBuffer(mi.getName()+"(");
			int count= 0;
			for (Iterator each2= mi.arguments().iterator(); each2.hasNext();) {
				if (count > 0)
					sb.append(", ");
				Expression expr= (Expression) each2.next();
				ITypeBinding binding= expr.resolveTypeBinding();
				if (binding == null)
					sb.append(expr);
				else
					sb.append(binding.getName());
				count++;
			}
			sb.append(")");	
			result.add(sb.toString());		
		}
	}
	
	private TypeDeclaration findTypeDeclaration(CompilationUnit astRoot, String simpleTypeName) {
		List types= astRoot.types();
		for (int i= 0; i < types.size(); i++) {
			TypeDeclaration elem= (TypeDeclaration) types.get(i);
			if (simpleTypeName.equals(elem.getName().getIdentifier())) {
				return elem;
			}
		}
		return null;
	}
		
	private IType findTypeInWorkspace(String qualifiedName) {
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
			for (int i= 0; i < projects.length; i++) {
				IJavaProject project= projects[i];
				IType type= project.findType(qualifiedName);
				if (type != null) {
					return type;
				}
			}
		} catch (JavaModelException e) {
		}
		return null;
	}
	
} 
