package org.eclipse.contribution.junit.ui;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.JUnitPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

public class JUnitView extends ViewPart implements ITestRunListener, IElementChangedListener {
	private Label fTest;
	private IType fTestClass;

	public void createPartControl(Composite parent) {
		fTest= new Label(parent, SWT.NONE);
		Button run= new Button(parent, SWT.PUSH);
		run.setText("Run");
		run.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				runTest();
			}
		});
		JUnitPlugin.getDefault().addListener(this);
		JavaCore.addElementChangedListener(this);

		MenuManager menuMgr= new MenuManager();
		Menu menu= menuMgr.createContextMenu(fTest);
		fTest.setMenu(menu);
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(menuMgr, getSite().getSelectionProvider());
		if (fState != null)
			restoreState();
	}

	private void runTest() {
		if (fTestClass == null)
			return;
		try {
			addTestableNature();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		JUnitPlugin.getDefault().runTest(fTestClass);
	}

	private void addTestableNature() throws CoreException {
		IProject project= fTestClass.getJavaProject().getProject();
		IProjectDescription description= project.getDescription();
		String[] prevNatures= description.getNatureIds();
		String[] newNatures= new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length]= "org.eclipse.contribution.junit.ui.testableNature";
		description.setNatureIds(newNatures);
		project.setDescription(description, new NullProgressMonitor());
	}


	public void testRunFinished(IType testClass, boolean success) {
		this.fTestClass= testClass;
		fTest.setText(testClass.getFullyQualifiedName());
		int background= success ? SWT.COLOR_GREEN : SWT.COLOR_RED;
		fTest.setBackground(fTest.getDisplay().getSystemColor(background));
	}

	public void setFocus() {
	}

	public void saveState(IMemento state) {
		if (fTestClass == null)
			return;
		state.putString("test", fTestClass.getHandleIdentifier());
	}

	protected void restoreState() {
		IType test= (IType) JavaCore.create(fState.getString("test"));
		if (test != null) {
			fTestClass= test;
			fTest.setText(fTestClass.getFullyQualifiedName());
		} else {
			String id= JUnitPlugin.getDefault().getDescriptor().getUniqueIdentifier();
			int code= 0;
			String message= "Trouble restoring test class: " + fState.getString("test");
			IStatus status= new Status(IStatus.WARNING, id, code, message, null);
			JUnitPlugin.getDefault().getLog().log(status);
		}
	}

	IMemento fState;
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		fState= memento;
	}

	public void elementChanged(ElementChangedEvent event) {
		if (fTest.isDisposed())
			return;
		fTest.getDisplay().syncExec(new Runnable() {
			public void run() {
				if (fTest.isDisposed())
					return;
				fTest.setBackground(fTest.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			}
		});
	}

	public void dispose() {
		JavaCore.removeElementChangedListener(this);
		super.dispose();
	}

}
