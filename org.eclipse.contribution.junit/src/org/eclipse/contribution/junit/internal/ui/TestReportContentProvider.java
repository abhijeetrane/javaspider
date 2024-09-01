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

import java.util.List;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;

class TestReportContentProvider implements IStructuredContentProvider, ITestRunListener {

	private TableViewer viewer;
	private TestResult currentResult;
	
	public TestReportContentProvider() {
		JUnitPlugin.getPlugin().addTestListener(this);
	}

	public void dispose() { 
		JUnitPlugin.getPlugin().removeTestListener(this);
	}
	
	public Object[] getElements(Object inputElement) {
		return ((List)inputElement).toArray();
	}
	
	public void inputChanged(Viewer tableViewer, Object oldInput, Object newInput) { 
		viewer= (TableViewer)tableViewer;
	}

	public void testsStarted(IJavaProject project, int testCount) {
		getTestResults().clear();
		currentResult= null;
		Control ctrl= viewer.getControl();
		if (ctrl == null || ctrl.isDisposed())
			return;
		viewer.getControl().getDisplay().syncExec(new Runnable() {
			public void run() {
				if (!viewer.getControl().isDisposed())
					viewer.refresh();
			}
		});
	}

	private List getTestResults() {
		return (List)viewer.getInput();
	}

	public void testStarted(IJavaProject project, String klass, String methodName) {
		addLastResult();
		currentResult= new TestResult(project, klass, methodName, TestResult.OK, System.currentTimeMillis());
	}


	public void testFailed(IJavaProject project, String klass, String method, String trace) {
		currentResult.testFailed();
	}
	
	public void testsFinished(IJavaProject project) {
		addLastResult();
	}

	private void addLastResult() {
		if (currentResult != null)  {
			currentResult.testFinished();
			getTestResults().add(currentResult);
			Control ctrl= viewer.getControl();
			if (ctrl == null || ctrl.isDisposed())
				return;
			viewer.getControl().getDisplay().syncExec(new Runnable() {
				public void run() {
					if (!viewer.getControl().isDisposed())
						viewer.insert(currentResult, -1);
				}
			});
		}
	}
}