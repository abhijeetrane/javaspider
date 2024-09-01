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
package org.eclipse.contribution.junit.internal.core;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.runner.BaseTestRunner;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class MarkerCreator implements ITestRunListener {

	private IJavaProject project;

	public MarkerCreator(IJavaProject project) {
		this.project= project;
	}

	public void testsStarted(IJavaProject project, int testCount) {
		try {
			project.getUnderlyingResource().deleteMarkers("org.eclipse.contribution.junit.failure", false, IResource.DEPTH_INFINITE); //$NON-NLS-1$
		} catch (CoreException e) {
			IStatus status= new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "Problems deleting markers", e); //$NON-NLS-1$
			JUnitPlugin.getPlugin().getLog().log(status);
		}
	}

	public void testsFinished(IJavaProject project) {
	}

	public void testStarted(IJavaProject project, String klass, String methodName) {
	}
 
	public void testFailed(IJavaProject testProject, String klass, String method, String trace) {
		if (! project.equals(testProject))
			return;
		IType type= null;
		try {
			type= project.findType(klass);
		} catch (JavaModelException e) { 
			IStatus status= new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "Problems creating markers", e); //$NON-NLS-1$
			JUnitPlugin.getPlugin().getLog().log(status);
		}
		if (type == null) 
			return;
		try {
			IResource resource= type.getUnderlyingResource();
			if (resource == null)
				createProjectMarker(testProject, trace);
			else
				createFileMarker(method, trace, type);
		} catch (CoreException e) {
			IStatus status= new Status(IStatus.ERROR, JUnitPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "Problems creating markers", e); //$NON-NLS-1$
			JUnitPlugin.getPlugin().getLog().log(status);
		}
	}

	private void createFileMarker(final String method, final String trace, final IType type) throws JavaModelException, CoreException {
		final IResource resource= type.getUnderlyingResource();
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker= resource.createMarker("org.eclipse.contribution.junit.failure"); //$NON-NLS-1$
				IMethod testMethod= type.getMethod(method, new String[0]); 
				ISourceRange range= testMethod.getSourceRange();
				Map map= new HashMap(4);
				map.put(IMarker.CHAR_START, new Integer(range.getOffset()));
				map.put(IMarker.CHAR_END, new Integer(range.getOffset() + range.getLength()));
				map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_ERROR));
				map.put(IMarker.MESSAGE, extractMessage(trace));
				map.put("trace", trace); //$NON-NLS-1$
				marker.setAttributes(map);
			}
		};
		resource.getWorkspace().run(runnable, null);
	}

	private String extractMessage(String trace) {
		String filteredTrace= BaseTestRunner.getFilteredTrace(trace);
		BufferedReader br= new BufferedReader(new StringReader(filteredTrace));
		String line, message= trace;
		try {
			if ((line= br.readLine()) != null)  {
				message= line;
				if ((line= br.readLine()) != null)  
				message+= " - "+line; //$NON-NLS-1$
			}
			return message.replace('\t', ' ');
		} catch (Exception IOException) {
		}
		return message;
	}

	private void createProjectMarker(IJavaProject testProject, final String trace) throws JavaModelException, CoreException {
		final IResource resource;
		resource= testProject.getUnderlyingResource();
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker= resource.createMarker("org.eclipse.contribution.junit.failure"); //$NON-NLS-1$
				Map map= new HashMap(4);
				map.put(IMarker.SEVERITY, new Integer(IMarker.SEVERITY_WARNING));
				map.put(IMarker.MESSAGE, extractMessage(trace));
				map.put("trace", trace); //$NON-NLS-1$
				marker.setAttributes(map);		
			}
		};
		resource.getWorkspace().run(runnable, null);
	}
}
