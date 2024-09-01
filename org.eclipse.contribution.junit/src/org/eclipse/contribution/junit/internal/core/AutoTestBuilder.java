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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaModelMarker;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;

public class AutoTestBuilder extends IncrementalProjectBuilder {
	private static boolean enabled= true;
	private static boolean trace= false;
	
	public AutoTestBuilder() {
	}

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (hasBuildErrors() || !AutoTestBuilder.enabled)
			return null;
		IJavaProject javaProject= JavaCore.create(getProject());
		IType[] types= new TestSearcher().findAll(javaProject, monitor);
		types= exclude(types);
		if (AutoTestBuilder.trace)
			printTestTypes(types);
		JUnitPlugin.getPlugin().run(types, javaProject);
		return null;
	}


	public static void setEnabled(boolean isEnabled) {
		AutoTestBuilder.enabled= isEnabled;
	}
	
	private boolean hasBuildErrors() throws CoreException {
		IMarker[] markers= getProject().findMarkers(IJavaModelMarker.JAVA_MODEL_PROBLEM_MARKER, false, IResource.DEPTH_INFINITE);
		for (int i= 0; i < markers.length; i++) {
			IMarker marker= markers[i];
			if (marker.getAttribute(IMarker.SEVERITY, 0) == IMarker.SEVERITY_ERROR)
				return true;
		}
		return false;
	}

	static {
		String value= Platform.getDebugOption("org.eclipse.contribution.junit/trace/testfinding"); //$NON-NLS-1$
		if (value != null && value.equalsIgnoreCase("true")) //$NON-NLS-1$
			AutoTestBuilder.trace= true;
	}

	private static void printTestTypes(IType[] tests) {
		System.out.println("Auto Test: "); //$NON-NLS-1$
		for (int i= 0; i < tests.length; i++) {
			System.out.println("\t"+tests[i].getFullyQualifiedName()); //$NON-NLS-1$
		}
	}
	
	private IType[] exclude(IType[] types) {
		try {
			Set exclusions= readExclusions(getProject().getFile(new Path("test.exclusions"))); //$NON-NLS-1$
			List result= new ArrayList(types.length);
			for (int i= 0; i < types.length; i++) {
				IType type= types[i];
				String typeName= type.getFullyQualifiedName();
				if (!exclusions.contains(typeName))
					result.add(type);
				return (IType[])(result.toArray(new IType[result.size()]));
			}
		} catch (Exception e) {
			// fall through
		}
		return types;
	}
	
	private Set readExclusions(IFile file) throws IOException, CoreException {
		Set result= new HashSet();
		BufferedReader br= new BufferedReader(new InputStreamReader(file.getContents()));
		try {
			String line;
			while ((line= br.readLine()) != null) {
				line= line.trim();
				if (line.length() > 0)
					result.add(line);
			}
		} finally {
			br.close();
		}
		return result;
	}
}
