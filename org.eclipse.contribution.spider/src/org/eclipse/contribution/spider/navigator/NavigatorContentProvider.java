package org.eclipse.contribution.spider.navigator;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

class NavigatorContentProvider implements IStructuredContentProvider,  ITreeContentProvider, IPartListener {

	private TreeViewer fTreeViewer; 
	private IWorkbenchPage fInput;
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		fTreeViewer= (TreeViewer)viewer;
		if (oldInput == null && newInput != null) {
			fInput= getActiveWorkbenchWindow().getActivePage();
			addPartListener(); 
		} else if (oldInput != null && newInput == null) {
			removePartListener(); 
			fInput= null;
		}  
	}
	
	private void removePartListener() {
		fInput.removePartListener(this);
	}
	
	private void addPartListener() {
		fInput.addPartListener(this);
	}
	
	public void dispose() {
		// no resources to dispose
	}
	
	public Object[] getElements(Object parent) {
		return getChildren(parent);
	}
	
	public Object getParent(Object child) {
		return null;
	}
	
	public Object [] getChildren(Object parent) {
		if (parent.equals("ROOT"))
			return new Object[]{"Views", "Editors", "Workbench Window", "Workspace", "Platform", "Menubar"};
		if (parent.equals("Views"))
			return fInput.getViews();
		if (parent.equals("Editors"))
			return fInput.getEditors();
		if (parent.equals("Workbench Window"))
			return new Object[]{fInput.getWorkbenchWindow()};
		if (parent.equals("Menubar")) 
			return fInput.getWorkbenchWindow().getShell().getMenuBar().getItems();
		if (parent.equals("Platform"))
			return new Object[] { new Platform() }; 
		if (parent.equals("Workspace"))
			return new Object[]{ResourcesPlugin.getWorkspace()}; 
		if (parent instanceof MenuItem) {
			Menu menu= ((MenuItem)parent).getMenu();
			if (menu != null)
				return menu.getItems();
		}	
		return new Object[0];
	}
	
	
	public boolean hasChildren(Object parent) {
		return getChildren(parent).length > 0;
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	public void partActivated(IWorkbenchPart arg0) {
	}
	
	public void partBroughtToTop(IWorkbenchPart arg0) {
	}
	
	public void partClosed(IWorkbenchPart arg0) {
		fTreeViewer.refresh();
	}
	
	public void partDeactivated(IWorkbenchPart arg0) {
	}
	
	public void partOpened(IWorkbenchPart arg0) {
		fTreeViewer.refresh();
	}
}