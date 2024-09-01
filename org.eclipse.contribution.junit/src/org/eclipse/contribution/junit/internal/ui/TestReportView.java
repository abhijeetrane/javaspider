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

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.ViewPart;

public class TestReportView extends ViewPart {
	
	private TableViewer viewer;

	private class GotoTestAction extends Action {
		private GotoTestAction() {
			setText(JUnitMessages.getString("TestReportView.gototest.label"));  //$NON-NLS-1$
			setToolTipText(JUnitMessages.getString("TestReportView.gototest.tooltip"));  //$NON-NLS-1$
			ImageDescriptor descriptor= PlatformUI.getWorkbench().
				getSharedImages().getImageDescriptor(ISharedImages.IMG_OPEN_MARKER);
			setImageDescriptor(descriptor); 
		}
		
		public void run(){
			handleDefaultSelected();
		}
	}
	
	public void createPartControl(Composite parent) {
		Table table= new Table(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn column= new TableColumn(table, SWT.NONE, 0);
		column.setText(JUnitMessages.getString("TestReportView.header.test")); //$NON-NLS-1$
		column.setWidth(300);
		column.setAlignment(SWT.LEFT);

		column= new TableColumn(table, SWT.NONE, 1);
		column.setText(JUnitMessages.getString("TestReportView.header.time")); //$NON-NLS-1$
		column.setWidth(100);
		column.setAlignment(SWT.RIGHT);
		
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				handleDefaultSelected();
			}
		});

		viewer= new TableViewer(table);
		viewer.setContentProvider(new TestReportContentProvider());
		viewer.setLabelProvider(new TestReportLabelProvider());
		viewer.setInput(new ArrayList());
		
		getSite().setSelectionProvider(viewer);
		
		IActionBars actionBars= getViewSite().getActionBars();
		actionBars.getToolBarManager().add(new GotoTestAction());
		
		WorkbenchHelp.setHelp(viewer.getControl(), "org.eclipse.contribution.junit.autoTestContext"); //$NON-NLS-1$
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public TableViewer getViewer() {
		return viewer;
	}

	public void handleDefaultSelected() {
		IStructuredSelection s= (IStructuredSelection)viewer.getSelection();
		Object firstElement= s.getFirstElement();
		if (firstElement != null)
			openTest((TestResult)firstElement);
	}

	private void openTest(TestResult result) {
		try {
			IType type= result.project.findType(result.klass);
			IJavaElement testMethod= type.getMethod(result.method, new String[0]);
			IEditorPart part= JavaUI.openInEditor(type);
			JavaUI.revealInEditor(part, testMethod);
		} catch (CoreException e) {
			ErrorDialog.openError(getSite().getShell(), 
				JUnitMessages.getString("PropertyPage.error.title"), //$NON-NLS-1$
				JUnitMessages.getString("PropertyPage.error.cannotSetProperty"),  //$NON-NLS-2$
				e.getStatus()); 
		}
	}
}
