package org.eclipse.contribution.junit.test;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.ui.ExcludeTestAction;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ExclusionEditorTest extends TestCase {
	private TestProject testProject;

	protected void setUp() throws Exception {
		testProject= new TestProject();
	}
	
	protected void tearDown() throws Exception {
		getPage().closeAllEditors(false);
		testProject.dispose();
	}
	
	public void testEditorContribution() throws CoreException {
		IFile file= testProject.getProject().getFile(new Path("test.exclusion"));
		file.create(new ByteArrayInputStream("".getBytes()), true, null);
		IEditorPart part= getPage().openEditor(file);
		assertTrue(findContributedAction(part));
	}

	private boolean findContributedAction(IEditorPart part) {
		IToolBarManager manager= part.getEditorSite().getActionBars().getToolBarManager();
		IContributionItem[] items= manager.getItems();
		for (int i= 0; i < items.length; i++) {
			IContributionItem item= items[i];
			if (item instanceof ActionContributionItem) {
				IAction a=((ActionContributionItem)item).getAction();
				if (a.getClass().equals(ExcludeTestAction.class))
					return true;
			}
		}
		return false;
	}

	private IWorkbenchPage getPage() {
		IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow window= workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}
}
