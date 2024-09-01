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
package org.eclipse.contribution.junit;

import org.eclipse.jdt.core.IJavaProject;

/**
 * A listener interface for observing the execution of a test run.
 * <p>
 * Clients contributing to the 
 * <code>org.eclipse.contribution.junit.listeners</code>
 * extension point implement this interface.
 * </p>
 */
public interface ITestRunListener {
	void testsStarted(IJavaProject project, int testCount);
	void testsFinished(IJavaProject project);
	void testStarted(IJavaProject project, String klass, String methodName);
	void testFailed(IJavaProject project, String klass, String method, String trace);
}
