package org.eclipse.contribution.spider;

import org.eclipse.contribution.minidraw.Handle;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.contribution.minidraw.MouseEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

public class GotoSourceHandle extends Handle {
	protected SpiderView model;
	protected ISubject subject;
	protected int x;
	protected int y;
	protected static Image image;

	public GotoSourceHandle() {
	}
	
	public GotoSourceHandle(ObjectFigure figure, IModel model) {
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
		((ObjectFigure)target).showSource();
	}
	
	public void paint(GC gc) {
		Rectangle bounds= bounds();
		gc.drawImage(getImage(), bounds.x, bounds.y);
	}

	public void silentlyMoveTo(int x, int y) {
		this.x= x;
		this.y= y;
	}

	protected int height() {
		return getImage().getBounds().height;
	}
	
	protected int width() {
		return getImage().getBounds().width;
	}
		
	protected Image getImage() {
		if (image == null)
			image= createImage("gotosrc.gif");
		return image;
	}
	
	protected static Image createImage(String path) {
		ImageDescriptor id= SpiderPlugin.getImageDescriptor(path);
		return id.createImage();  
	}

}
