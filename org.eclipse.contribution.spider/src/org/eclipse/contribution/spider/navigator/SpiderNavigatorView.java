package org.eclipse.contribution.spider.navigator;

import org.eclipse.contribution.spider.SpiderPlugin;
import org.eclipse.contribution.spider.SpiderView;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * View that shows interesting entry points for run-time object exploration.
 */
public class SpiderNavigatorView extends ViewPart implements MouseListener  {
	TreeViewer fViewer;
	private Action fExploreAction;
	private Action fShowSource;
	private Action fDoubleClickAction;
	private Action fInspectAction;
	private Cursor fInspectCursor;
	boolean fGrab= false;
	 
	public SpiderNavigatorView() {
	}

	public void createPartControl(Composite parent) {
		fViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		fViewer.setContentProvider(new NavigatorContentProvider());
		fViewer.setLabelProvider(new NavigatorLabelProvider());
		fViewer.setInput("ROOT");
		fViewer.getControl().addMouseListener(this);
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SpiderNavigatorView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(fViewer.getControl());
		fViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, fViewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(fExploreAction);
		manager.add(fShowSource);
		manager.add(new Separator("Additions"));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(fInspectAction);
		manager.add(new Separator());
	}

	private void makeActions() {
		fExploreAction = new Action() {
			public void run() {
				ISelection selection = fViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				exploreObject(obj);
			}
		};
		fExploreAction.setText("Explore Object");
		
		fShowSource = new Action() {
			public void run() {
				ISelection selection = fViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showSource(obj);
			}
		};
		fShowSource.setText("Open");

		fDoubleClickAction = new Action() {
			public void run() {
				ISelection selection = fViewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				exploreObject(obj);
			}
		};
		
		fInspectAction = new InspectAction(this);
		fInspectAction.setText("Explore Clicked Object");
		fInspectAction.setToolTipText("Explore the Clicked Object");
		fInspectAction.setImageDescriptor(SpiderPlugin.getImageDescriptor("inspect.gif"));
	}

	Cursor getInspectCursor() {
		if (fInspectCursor == null)
			fInspectCursor= new Cursor(fViewer.getControl().getDisplay(), SWT.CURSOR_HELP);
		return fInspectCursor;
	}

	public void exploreObject(Object obj) {
		IWorkbenchWindow window= getActiveWorkbenchWindow();
		IWorkbenchPage page= window.getActivePage();
		SpiderView spider= null;
		if (page != null) {
			try { // show the result view if it isn't shown yet
				spider= (SpiderView)page.showView("org.eclipse.contribution.spider.view");
				spider.exploreObject(obj);
			} catch (PartInitException pie) {
				//TODO handle exception
			}
		}
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
	
	private void hookDoubleClickAction() {
		fViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				fDoubleClickAction.run();
			}
		});
	}

	public void showSource(Object obj) {
		String qualifiedName= obj.getClass().getName();
		IJavaProject[] projects;
		try {
			projects= JavaCore.create(getWorkspaceRoot()).getJavaProjects();
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
	}
	
	
	private IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	public void setFocus() {
		fViewer.getControl().setFocus();
	}
	
	public void mouseDoubleClick(MouseEvent e) {
	}
	
	public void mouseDown(MouseEvent e) {
	}
	
	public void mouseUp(MouseEvent e) {
		if (fGrab)	{
			Control control= e.display.getCursorControl();
			getSite().getWorkbenchWindow().getShell().setCursor(null);
			if (control != null) {
				exploreObject(narrow(e, control));
			}
		}
		fGrab= false;
	}
	
	private Object narrow(MouseEvent e, Control control) {
		Point globalLocation= e.display.getCursorLocation();
		if (control instanceof ToolBar) {
			ToolBar bar= (ToolBar)control;
			Point localLocation= control.toControl(globalLocation);
			Item item= bar.getItem(localLocation);
			if (item != null)
				return bar.getItem(localLocation);
		}
		if (control instanceof Tree) {
			Tree tree= (Tree)control;
			Point localLocation= control.toControl(globalLocation);
			Item item= tree.getItem(localLocation);
			if (item != null)
				return tree.getItem(localLocation);
		}	
		if (control instanceof Table) {
			Table table= (Table)control;
			Point localLocation= control.toControl(globalLocation);
			Item item= table.getItem(localLocation);
			if (item != null)
				return table.getItem(localLocation);
		}	
		if (control instanceof TableTree) {
			TableTree table= (TableTree)control;
			Point localLocation= control.toControl(globalLocation);
			Item item= table.getItem(localLocation);
			if (item != null)
				return table.getItem(localLocation);
		}	
		if (control instanceof CTabFolder) {
			CTabFolder ctabFolder= (CTabFolder)control;
			Point localLocation= control.toControl(globalLocation);
			Item item= ctabFolder.getItem(localLocation);
			if (item != null)
				return ctabFolder.getItem(localLocation);
		}	
		return control;
	}

	public void dispose() {
		if(fInspectCursor != null) 
			fInspectCursor.dispose();
		super.dispose();
	}

}