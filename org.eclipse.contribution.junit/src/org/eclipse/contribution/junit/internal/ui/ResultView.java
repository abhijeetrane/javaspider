/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Erich Gamma (erich_gamma@ch.ibm.com) and
 * 	   Kent Beck (kent@threeriversinstitute.org)
 *******************************************************************************/
package org.eclipse.contribution.junit.internal.ui;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

public class ResultView extends ViewPart {

	private Control control;
	
	private ITestRunListener listener; 
	private DirtyListener dirtyListener;
	private Action rerunAction;
	
	public MenuManager menuManager;

	private class Listener implements ITestRunListener {

		private boolean success;

		public void testsStarted(IJavaProject project, int testCount) {
			success= true;
		}

		public void testsFinished(IJavaProject project) {
			changeColor(success);
		}

		public void testStarted(IJavaProject project, String klass, String methodName) {
		}

		public void testFailed(IJavaProject project, String klass, String method, String trace) {
			success= false;
			changeColor(success);
		}

	}
	
	private class RerunTestAction extends Action {
		private RerunTestAction() {
			setText("Re-run");  //$NON-NLS-1$
		}
		
		public void run(){
			rerunTest();
		}
	}

	private class DirtyListener implements IElementChangedListener {
		public void elementChanged(ElementChangedEvent event) {
			processDelta(event.getDelta());				
		}
		
		private boolean processDelta(IJavaElementDelta delta) { 
			int kind= delta.getKind();
			int type= delta.getElement().getElementType();
			
			switch (type) {
				case IJavaElement.JAVA_MODEL:
				case IJavaElement.JAVA_PROJECT:
				case IJavaElement.PACKAGE_FRAGMENT_ROOT:
				case IJavaElement.PACKAGE_FRAGMENT:
					if (kind == IJavaElementDelta.ADDED || kind == IJavaElementDelta.REMOVED) 
						return testResultDirty();
					break;
				case IJavaElement.COMPILATION_UNIT:
					ICompilationUnit unit= (ICompilationUnit)delta.getElement();
					if (unit.isWorkingCopy())
						return true;
					return testResultDirty();
				default:
					return testResultDirty();
			}
			IJavaElementDelta[] affectedChildren= delta.getAffectedChildren();	
			for (int i= 0; i < affectedChildren.length; i++) 
				if (!processDelta(affectedChildren[i]))
					return false;
			return true;			
		}
	}

	public Control getControl() {
		return control;
	}

	public void createPartControl(Composite parent) {
		listener= new Listener();
		JUnitPlugin.getPlugin().addTestListener(listener);
		dirtyListener= new DirtyListener();
		JavaCore.addElementChangedListener(dirtyListener, ElementChangedEvent.PRE_AUTO_BUILD);
		control= new Label(parent, SWT.NONE);
		createContextMenu();
		rerunAction= new RerunTestAction();
		
	}

	private void createContextMenu() {
		menuManager= new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});

		Menu menu= menuManager.createContextMenu(control);
		control.setMenu(menu);
		getSite().registerContextMenu(menuManager, null);
	}

	protected void fillContextMenu(IMenuManager menu) {
		menu.add(rerunAction);
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end")); //$NON-NLS-1$
	}
	
	private void rerunTest() {
		//TODO not implemented yet
		System.out.println("rerun");
	}

	public void setFocus() {
		control.setFocus();
	}

	public void dispose() {
		if (listener != null)
			JUnitPlugin.getPlugin().removeTestListener(listener);
		if (dirtyListener != null)
			JavaCore.removeElementChangedListener(dirtyListener);
		listener= null;
		dirtyListener= null;
	}

	private boolean testResultDirty() {
		final Display display= getSite().getShell().getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!control.isDisposed())  {
					Color background= display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
					control.setBackground(background);
				}
			}
		});
		return false;
	}
	
	private void changeColor(final boolean success) {
		Display display= getSite().getShell().getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (control.isDisposed())
					return;
				if (success) {
					Color green= control.getDisplay().getSystemColor(SWT.COLOR_GREEN);
					control.setBackground(green);
				} else  {
					Color red= control.getDisplay().getSystemColor(SWT.COLOR_RED);
					control.setBackground(red);				
				}
			}
		});
	}
	
	public ITestRunListener getListener() {
		return listener;
	}

}
