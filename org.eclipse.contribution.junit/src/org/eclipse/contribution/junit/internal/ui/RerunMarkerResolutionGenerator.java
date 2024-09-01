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

import java.lang.reflect.InvocationTargetException;

import org.eclipse.contribution.junit.internal.core.AutoTestBuilder;
import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class RerunMarkerResolutionGenerator implements IMarkerResolutionGenerator2 {
	public RerunMarkerResolutionGenerator() {
	}

	public IMarkerResolution [] getResolutions(IMarker marker)  {
		IMarkerResolution resolution= new IMarkerResolution() {
			public String getLabel() {
				return JUnitMessages.getString("RerunMarkerResolutionGenerator.label"); //$NON-NLS-1$
			}
			public void run(final IMarker m) {
				if (!save())
					return;
				if (!build())
					return;				
				IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
					public void run(IProgressMonitor monitor) throws CoreException {
						IType type= findTest(m);
						JUnitPlugin.getPlugin().run(type);
					}
				};
				try  {
					ResourcesPlugin.getWorkspace().run(runnable, null);
				} catch (CoreException e) {
					IStatus status= new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "Problems rerunning tests", e); //$NON-NLS-1$
					JUnitPlugin.getPlugin().getLog().log(status);
				}
			}
		};
		return new IMarkerResolution[] { resolution };
	}

	protected IType findTest(IMarker m) {
		IResource resource= m.getResource();
		ICompilationUnit cu= (ICompilationUnit)JavaCore.create(resource);
		return cu.findPrimaryType();
	}

	protected boolean build() {
		ProgressMonitorDialog dialog= new ProgressMonitorDialog(getShell());
		try {
			dialog.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException {
					AutoTestBuilder.setEnabled(false);
					try {
						ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					} finally  {
						AutoTestBuilder.setEnabled(true);
					}
				}
			});
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			IStatus status= new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "Problems building workspace", e.getTargetException()); //$NON-NLS-1$
			JUnitPlugin.getPlugin().getLog().log(status);
			return false;
		}
		return true;
	}

	private boolean save() {
		IWorkbench workbench= PlatformUI.getWorkbench();
		return workbench.saveAllEditors(false);
	}
	
	private Shell getShell()  {
		IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getShell();
	}

	public boolean hasResolutions(IMarker marker) {
		return true;
	}
}
