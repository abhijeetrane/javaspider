package org.eclipse.contribution.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.contribution.minidraw.DrawingCanvas;
import org.eclipse.contribution.minidraw.HandleGroup;
import org.eclipse.contribution.minidraw.IFigure;
import org.eclipse.contribution.minidraw.IHandle;
import org.eclipse.contribution.minidraw.IModel;
import org.eclipse.contribution.minidraw.IRuler;
import org.eclipse.contribution.minidraw.Ruler;
import org.eclipse.contribution.minidraw.SelectionDrawing;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.ViewPart;

public class SpiderView extends ViewPart implements IModel {
	protected DrawingCanvas canvas;
	protected SelectionDrawing drawing;
	protected Map objects= new HashMap();
	protected Map incoming= new HashMap();
	protected Map outgoing= new HashMap();
	protected IRuler ruler;
	
	// Must have a no-arg ctor to be restored
	public SpiderView() {
		super();
	}
	
	// For testing purposes only
	public SpiderView(IRuler ruler) {
		this.ruler= ruler;
		drawing= new SelectionDrawing(this); // No canvas in testing, so don't have canvas listen
	}
	
	public void createPartControl(Composite parent) {
		canvas= new DrawingCanvas(parent, this);
	}

	public void setFocus() {
	}

	public SelectionDrawing getDrawing() {
		if (drawing == null)
			drawing= computeDrawing();
		return drawing;
	}

	private SelectionDrawing computeDrawing() {
		SelectionDrawing result= new SelectionDrawing(this);
		result.addListener(canvas);
		return result;
	}
	
	public IRuler getRuler() {
		// Nasty order dependency-- canvas must be initialized before this is called. Pray.
		if (ruler == null) 
			ruler= new Ruler(new GC(canvas.getDisplay()));  
		return ruler;
	}
	
	// Return value for testing purposes primarily, I think
	public ObjectFigure exploreObject(Object subject) {
		// Later, do smarter default layout
		return getFigure(RawSubject.create(subject), new Point(50, 50));
	}
	
	// Return the ObjectFigure representing subject, or create one at where
	// There should also be protocol for strictly accessing
	public ObjectFigure getFigure(ISubject subject, Point where) {
		if (objects.containsKey(subject))
			return (ObjectFigure) objects.get(subject);
		ObjectFigure result= createObjectFigure(subject, where);
		objects.put(subject, result);
		getDrawing().add(result);
		return result;
	}

	// Testing purposes
	public ObjectFigure figureForObject(ISubject key) {
		return (ObjectFigure) objects.get(key);
	}

	private ObjectFigure objectFigure(ISubject subject) {
		return (ObjectFigure) objects.get(subject);
	}
	
	private ObjectFigure createObjectFigure(ISubject subject, Point where) {
		return new ObjectFigure(subject, getRuler(), where);
	}

	public void expandVariable(FieldFigure variableFigure, ISubject subject) {
		Rectangle bounds= variableFigure.bounds();
		Point where= new Point(bounds.x + bounds.width + 30, bounds.y + bounds.height / 2 + 30);
		ObjectFigure referred= getFigure(subject, where);
		ObjectFigure referring= objectFigure(variableFigure.getSubject());
		BindingFigure binding= new BindingFigure(referring, variableFigure, referred);
		getDrawing().add(binding);
		getIncomingBindings(subject).add(binding);
		getOutgoingBindings(variableFigure.getSubject()).add(binding); 
	}

	public void remove(ISubject subject) {
		IFigure figure= (IFigure) objects.remove(subject);
		getDrawing().remove(figure);
		removeIncomingBindings(subject);
		removeOutgoingBindings(subject);
	}

	public void removeOutgoingBindings(ISubject subject) {
		for (Iterator all= getOutgoingBindings(subject).iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			getDrawing().remove(each);
		}
		outgoing.remove(subject);
	}

	public void removeIncomingBindings(ISubject subject) {
		for (Iterator all= getIncomingBindings(subject).iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			getDrawing().remove(each);
		}
		incoming.remove(subject); 
	}

	private List getIncomingBindings(ISubject subject) {
		if (! incoming.containsKey(subject))
			incoming.put(subject, new ArrayList());
		return (List) incoming.get(subject);
	}
	
	private List getOutgoingBindings(ISubject subject) {
		if (! outgoing.containsKey(subject))
			outgoing.put(subject, new ArrayList());
		return (List) outgoing.get(subject);
	}

	// Hack-o-rama. Need specific protocol for this.
	public void remove(IFigure subject) {
		remove(((ObjectFigure) subject).getSubject());
	}

	public Control getCanvas() {
		return canvas;
	}

	public Iterator getHandlesFor(IFigure selection) {
		IPluginRegistry registry= Platform.getPluginRegistry();
		IExtensionPoint extensionPoint= registry.getExtensionPoint("org.eclipse.contribution.spider" + ".handles");
		IConfigurationElement[] configs= extensionPoint.getConfigurationElements(); 
		
		List handles= new ArrayList(configs.length);
		for (int i= 0; i < configs.length; i++) {
			try {
				String targetClassName= configs[i].getAttribute("targetClass");
				try {
					Class targetClass= Class.forName(targetClassName);
					if (targetClass.isAssignableFrom(selection.getClass())) {
						IHandle handle= (IHandle) configs[i].createExecutableExtension("class"); 
						handle.initialize(selection, this);
						handles.add(handle);
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		//return listeners;
		
		HandleGroup result= new HandleGroup(selection);
		for (Iterator all= handles.iterator(); all.hasNext();) {
			IFigure each= (IFigure) all.next();
			result.add(each);
		}
		List results= new ArrayList();
		results.add(result);
		return results.iterator();
	}
}
