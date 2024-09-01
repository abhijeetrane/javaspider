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

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public class AutoTestPropertyPage extends PropertyPage {

	Button autoTest;
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		Control composite= addControl(parent);
		try {
			autoTest.setSelection(getProject().hasNature(JUnitPlugin.AUTO_TEST_NATURE));
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(), JUnitMessages.getString("PropertyPage.error.title"), JUnitMessages.getString("PropertyPage.error.cannotOpen"), e.getStatus()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return composite;
	}

	private Control addControl(Composite parent) {
		Composite composite= new Composite(parent, SWT.NONE);
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		composite.setLayout(layout);
		GridData data= new GridData();
		data.verticalAlignment= GridData.FILL;
		data.horizontalAlignment= GridData.FILL;
		composite.setLayoutData(data);

		Font font= parent.getFont();
		Label label= new Label(composite, SWT.NONE);
		label.setText(JUnitMessages.getString("PropertyPage.description")); //$NON-NLS-1$
		label.setFont(font);
		autoTest= new Button(composite, SWT.CHECK);
		autoTest.setText(JUnitMessages.getString("PropertyPage.autotest.label")); //$NON-NLS-1$
		autoTest.setFont(font);
		return composite;
	}

	public boolean performOk() {
		try {
			if (autoTest.getSelection())
				JUnitPlugin.getPlugin().addAutoBuildNature(getProject());
			else
				JUnitPlugin.getPlugin().removeAutoBuildNature(getProject());
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(), JUnitMessages.getString("PropertyPage.error.title"), JUnitMessages.getString("PropertyPage.error.cannotSetProperty"), e.getStatus()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return true;
	}

	private IProject getProject() {
		return (IProject) getElement();
	}

}
