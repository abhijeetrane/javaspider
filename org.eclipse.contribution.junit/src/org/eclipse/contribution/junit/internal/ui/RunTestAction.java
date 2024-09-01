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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;

public class RunTestAction implements IActionDelegate {
	ISelection selection;
	
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection= selection;
	}

	public void run(IAction action) {
		if (! (selection instanceof IStructuredSelection))
			return;
		IStructuredSelection structured= (IStructuredSelection) selection;
		IType type= (IType) structured.getFirstElement();
		try {
			JUnitPlugin.getPlugin().run(type);
		} catch (CoreException e) {
			ErrorDialog.openError(null, JUnitMessages.getString("RunTestAction.error.title"), JUnitMessages.getString("RunTestAction.error.message"), e.getStatus()); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
