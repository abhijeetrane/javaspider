package org.eclipse.contribution.spider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.minidraw.Group;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PartInitException;
 
public class ObjectFigure extends Group {
	protected ISubject subject;
	protected List fields= new ArrayList();
	protected IFigure classFigure;

	protected ObjectFigure() {
	}

	public ObjectFigure(ISubject subject, IRuler ruler, Point where) {
		this.subject= subject;
		classFigure= new ClassFigure(subject, ruler, where);
		add(classFigure);
	}

	public void showSource() {
		String qualifiedName= subject.getQualifiedClassName();
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
			for (int i= 0; i < projects.length; i++) {
				IJavaProject project= projects[i];
				IType type= project.findType(qualifiedName);
				if (type != null) {
					JavaUI.openInEditor(type);
					return;
				}
			}
		} catch (JavaModelException e) {
		} catch (PartInitException e) {
		}
		//TODO: set parent
		MessageDialog.openInformation(null, "Goto Source", "Source not found in the workspace.");
	}

	public FieldFigure addField(IField field, IRuler ruler) {
		FieldFigure result= new FieldFigure(subject, ruler, new Point(0, 0), field);
		fields.add(result);
		add(result);
		layoutFields();
		return result;
	}

	public ISubject getSubject() {
		return subject;
	}

	public List attributeActions(final SpiderView spider) {
		List results= new ArrayList();
		for (Iterator all= subject.getAttributes(); all.hasNext();) {
			final IField each= (IField) all.next();
			results.add(new Action(each.getName()) {
				public void run() {
					FieldFigure variable= addField(each, spider.getRuler());
					variable.expand(spider);
				}
			});
		};
		return results;
	}

	public List fieldActions(final SpiderView spider) {
		List results= new ArrayList();
		for (Iterator all= subject.getFields(); all.hasNext();) {
			final IField each= (IField) all.next();
			results.add(new Action(each.getName()) {
				public void run() {
					FieldFigure variable= addField(each, spider.getRuler());
					variable.expand(spider);
				}
			});
		};
		return results;
	}
 
	public void remove(IFigure figure) {
		super.remove(figure);
		if (fields.contains(figure)) {
			fields.remove(figure);
			layoutFields();
		}
	}

	private void layoutFields() {
		int y= classFigure.bounds().y + classFigure.bounds().height;
		for (Iterator all= fields.iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			each.moveTo(classFigure.bounds().x + 10, y);
			y += each.bounds().height;
		}
	}

}
