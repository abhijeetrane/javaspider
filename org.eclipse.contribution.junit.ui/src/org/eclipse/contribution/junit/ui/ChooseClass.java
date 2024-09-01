package org.eclipse.contribution.junit.ui;

import org.eclipse.contribution.junit.JUnitPlugin;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.dialogs.SelectionDialog;

public class ChooseClass implements IViewActionDelegate {

	public void init(IViewPart view) {
	}

	public void run(IAction action) {
		Shell shell= JavaPlugin.getActiveWorkbenchShell();
		SelectionDialog dialog= null;
		try {
			dialog= JavaUI.createTypeDialog(shell, new ProgressMonitorDialog(shell), 
				SearchEngine.createWorkspaceScope(), IJavaElementSearchConstants.CONSIDER_TYPES, false);
		} catch (JavaModelException e) {
			// TODO: Better error handling
		}

		dialog.setTitle("Choose Test Class");
		dialog.setMessage("Class to Run");
		if (dialog.open() == IDialogConstants.CANCEL_ID) {
			return;
		}

		Object[] types= dialog.getResult();
		if (types == null || types.length == 0)
			return;
		JUnitPlugin.getDefault().runTest((IType) types[0]);
	}

	public void selectionChanged(IAction action, ISelection selection) {
	}

}
