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

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.contribution.junit.internal.core.TestSearcher;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.texteditor.ITextEditor;

public class ExcludeTestAction extends Action {

	private ITextEditor editor;

	public ExcludeTestAction() {
		setText(JUnitMessages.getString("ExcludeTestAction.label"));  //$NON-NLS-1$
		setToolTipText(JUnitMessages.getString("ExcludeTestAction.tooltip"));  //$NON-NLS-1$
		setImageDescriptor(createImage("icons/test.gif"));  //$NON-NLS-1$
	}
	
	public void setActiveEditor(ITextEditor target) {
		editor= target;
	}
	
	public void run() {
		IType type= selectTest();
		if (type != null)
			appendTest(type.getFullyQualifiedName());	
	}

	private IType selectTest() {
		IEditorInput input= editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			IFile file= ((IFileEditorInput)input).getFile();
			IJavaProject jproject= JavaCore.create(file.getProject());
			try {
				IType[] types= new TestSearcher().findAll(jproject, new NullProgressMonitor());
				return showDialog(types);
			} catch (JavaModelException e) {
			}
		}
		return null;
	}

	private IType showDialog(IType[] types) {
		ElementListSelectionDialog dialog= new ElementListSelectionDialog(
			editor.getSite().getShell(), 
			new JavaElementLabelProvider());
		dialog.setTitle(JUnitMessages.getString("ExcludeTestAction.dialog.title"));  //$NON-NLS-1$
		dialog.setMessage(JUnitMessages.getString("ExcludeTestAction.dialog.message"));  //$NON-NLS-1$
		dialog.setElements(types);
		if (dialog.open() == Window.OK) 
			return (IType)dialog.getFirstResult();
		return null;
	}

	private void appendTest(String testName) {
		IDocument document= editor.getDocumentProvider().getDocument(editor.getEditorInput());
		try {
			if (document == null)
				return;
			document.replace(document.getLength(), 0, testName+"\n"); //$NON-NLS-1$
		} catch (BadLocationException e) {
		}
	}
	
	private ImageDescriptor createImage(String path) {
		URL url= JUnitPlugin.getPlugin().getDescriptor().getInstallURL();
		ImageDescriptor descriptor= null;
		try {
			descriptor= ImageDescriptor.createFromURL(new URL(url, path));
		} catch (MalformedURLException e) {
			descriptor= ImageDescriptor.getMissingImageDescriptor();
		}
		return descriptor;  
	}
}