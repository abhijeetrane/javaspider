package org.eclipse.contribution.junit.test;

import junit.framework.TestCase;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.ui.TestReportView;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class TestReportTest extends TestCase {
	private TestReportView view;
	private TestProject testProject;
	private IPackageFragment pack;
	private IType type;

	protected void setUp() throws Exception {
		testProject= new TestProject();
		testProject.addJar("org.junit", "junit.jar");
		pack= testProject.createPackage("pack1");
		type= testProject.createType(pack, "FailTest.java", "public class FailTest extends junit.framework.TestCase { public void testFailure() {fail();}}");
	}
	
	protected void tearDown() throws Exception {
		getPage().closeAllEditors(false);
		testProject.dispose();
	}
	
	public void testReportView() throws Exception {
		showView();
		JUnitPlugin.getPlugin().run(type);
		Table table= (Table)view.getViewer().getControl();
		assertEquals(1, table.getItemCount());
		TableItem item= table.getItem(0);
		assertEquals("testFailure - pack1.FailTest", item.getText(0));
	}

	public void testOpenEditor() throws Exception {
		showView();
		JUnitPlugin.getPlugin().run(type);
		Table table= (Table)view.getViewer().getControl();
		table.select(0);
		
		view.handleDefaultSelected();
		
		IEditorReference editor= assertEditorOpen();
		assertEditorInput(editor);
	}
	
	private void assertEditorInput(IEditorReference editor) throws JavaModelException {
		IEditorInput expected= new FileEditorInput((IFile)type.getUnderlyingResource());
		IEditorPart part= editor.getEditor(true);
		assertEquals(expected, part.getEditorInput());
	}

	private IEditorReference assertEditorOpen() {
		IWorkbenchPage page= getPage();
		IEditorReference[] editors= page.getEditorReferences();
		assertEquals(1, editors.length);
		return editors[0];
	}

	public void testActionInToolbar() throws Exception {
		showView();
		IToolBarManager manager= view.getViewSite().getActionBars().getToolBarManager();		
		assertEquals(1, manager.getItems().length);
	}

	private void showView() throws PartInitException {
		view= (TestReportView) getPage().showView("org.eclipse.contribution.junit.testReportView");
	}

	private IWorkbenchPage getPage() {
		IWorkbench workbench= PlatformUI.getWorkbench();
		IWorkbenchWindow window= workbench.getActiveWorkbenchWindow();
		return window.getActivePage();
	}

}
