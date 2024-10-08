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

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.texteditor.BasicTextEditorActionContributor;
import org.eclipse.ui.texteditor.ITextEditor;

public class TestExclusionEditorActionContributor extends BasicTextEditorActionContributor {
	private ExcludeTestAction excludeAction;
	
	public TestExclusionEditorActionContributor() {
		super();
		excludeAction= new ExcludeTestAction();
	}

	public void contributeToToolBar(IToolBarManager manager) {
		super.contributeToToolBar(manager);
		manager.add(new Separator());
		manager.add(excludeAction);
	}
	
	public void contributeToMenu(IMenuManager menu) {
		super.contributeToMenu(menu);
		IMenuManager editMenu= menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
			editMenu.add(excludeAction);
		}
	}
	
	public void setActiveEditor(IEditorPart target) {
		super.setActiveEditor(target);
		ITextEditor editor= null;
		if (target instanceof ITextEditor)
			editor= (ITextEditor)target;
		excludeAction.setActiveEditor(editor); 
	}
	
	public void dispose() {
		super.dispose();
		excludeAction.setActiveEditor(null); 
	}
}
