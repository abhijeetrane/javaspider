package org.eclipse.contribution.spider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.minidraw.Handle;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.contribution.minidraw.MouseEvent;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

public class FieldsHandle extends Handle {
	protected SpiderView model;
	protected ISubject subject;
	protected int x;
	protected int y;

	public FieldsHandle() {
	}
	
	public FieldsHandle(ObjectFigure figure, IModel model) {
		initialize(figure, model);
	}

	public void initialize(IFigure figure, IModel model) {
		super.initialize(figure, model);
		this.subject= ((ObjectFigure) figure).getSubject();
		this.model= (SpiderView) model;
	}

	public Rectangle bounds() {
		return new Rectangle(x, y, width(), height());
	}
	
	public void mouseUp(MouseEvent e) {
		MenuManager menuMgr= new MenuManager();
		Menu menu= menuMgr.createContextMenu(model.getCanvas());
		for (Iterator all= actions().iterator(); all.hasNext();) {
			IAction each= (IAction) all.next();
			menuMgr.add(each);
		}
		menu.setVisible(true);		
	}
	
	public List actions() {
// TODO: Would it be better to have a redundant variable here whose type was ObjectFigure. It would always contain the same value as target, but would avoid the cast. Cast or redundancy, which is worse?
		final ObjectFigure figure= (ObjectFigure) target;
		List results= new ArrayList();
		for (Iterator all= getFields(); all.hasNext();) {
			final IField each= (IField) all.next();
			Action action= new Action(each.getName()) {
				public void run() {
					FieldFigure variable= figure.addField(each, model.getRuler());
					variable.expand(model);
				}
			};
			action.setImageDescriptor(image(each));
			results.add(action);
		};
		Collections.sort(results, new ActionComparator());
		return results;
	}

	protected Iterator getFields() {
		return subject.getFields();
	}
	
	private ImageDescriptor image(IField each) {
		String key= null;
		if (each.isPrivate()) key= ISharedImages.IMG_OBJS_PRIVATE;
		if (each.isProtected()) key= ISharedImages.IMG_OBJS_PROTECTED;
		if (each.isPackage()) key= ISharedImages.IMG_OBJS_DEFAULT;
		if (each.isPublic()) key= ISharedImages.IMG_OBJS_PUBLIC;
		return JavaUI.getSharedImages().getImageDescriptor(key);
	}

	public void paint(GC gc) {
		Rectangle bounds= bounds();
		int indent= (bounds.width - textExtent().x) / 2;
		int x= bounds.x + indent;
		gc.drawText(textLabel(), x, bounds.y + 6); // Fudge because part of the image is blank
		gc.drawImage(getImage(), bounds.x, bounds.y + textExtent().y);
	}

	public void silentlyMoveTo(int x, int y) {
		this.x= x;
		this.y= y;
	}

	protected int height() {
		return textExtent().y + getImage().getBounds().height;
	}
	
	protected int width() {
		return Math.max(textExtent().x, getImage().getBounds().width);
	}
	
	protected Point textExtent() {
		return model.getRuler().measure(textLabel());
	}
	
	protected String textLabel() {
		return "f";
	}
	
	protected Image getImage() {
		return PlatformUI.getWorkbench().getSharedImages().getImage("IMG_LCL_VIEW_MENU");
	}

}
