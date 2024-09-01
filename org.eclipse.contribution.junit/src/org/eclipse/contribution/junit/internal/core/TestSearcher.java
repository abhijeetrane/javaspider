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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class TestSearcher {
	
	public IType[] findAll(IJavaProject project, IProgressMonitor pm) throws JavaModelException {
		IType[] candidates= getAllTestCaseSubclasses(project, pm);
		return collectTestsInProject(candidates, project);
	}

	private IType[] collectTestsInProject(IType[] candidates, IJavaProject project) {
		List result= new ArrayList();
		for (int i= 0; i < candidates.length; i++) {
			try {
				if (isTestInProject(candidates[i], project))
					result.add(candidates[i]);
			} catch (JavaModelException e) {
				// Fall through
			}
		}
		return (IType[]) result.toArray(new IType[result.size()]);
	}

	private IType[] getAllTestCaseSubclasses(IJavaProject project, IProgressMonitor pm) throws JavaModelException {
		IType testCase= project.findType("junit.framework.TestCase"); //$NON-NLS-1$
		if (testCase == null)
			return new IType[0];
		ITypeHierarchy hierarchy= testCase.newTypeHierarchy(project, pm);
		return hierarchy.getAllSubtypes(testCase);
	}

	private boolean isTestInProject(IType type, IJavaProject project) throws JavaModelException {
		IResource resource= type.getUnderlyingResource();
		if (resource == null) 
			return false;
		if (! resource.getProject().equals(project.getProject())) 
			return false; // Is it possible to remove this, or will we find test cases in prerequisite projects?
		return ! Flags.isAbstract(type.getFlags());
	}
	
}
