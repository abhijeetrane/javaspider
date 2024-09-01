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

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

/**
 * The central class to access the JUnit plug-ins functionality.  
 * This class cannot be instantiated; all functionality is provided by 
 * static methods.
 * 
 * Features provided:
 * <ul>
 * <li>Registering/unregistering listeners.</li>
 * <li>Running tests.</li>
 * </ul>
 */
public class JUnitCore {
	/**
	 * Adds a listener for test runs.
	 * 
	 * @param listener listener to be added
	 */
	public static void addTestListener(ITestRunListener listener) {
		JUnitPlugin.getPlugin().getListeners().add(listener);
	}

	/**
	 * Removes a listener for test runs.
	 * 
	 * @param listener listener to be removed
	 */
	public static void removeTestListener(ITestRunListener listener) {
		JUnitPlugin.getPlugin().getListeners().remove(listener);
	}

	/**
	 * Runs the tests contained in the given test case type.
	 * 
	 * @param type the type containing tests.
	 */
	public static void run(IType type) throws CoreException  {
		JUnitPlugin.getPlugin().run(type);
	}
	
	/**
	 * Runs a collection of tests.
	 * 
	 * @param types an array of test case types.
	 */
	public static void run(IType[] types, IJavaProject project) throws CoreException {
		JUnitPlugin.getPlugin().run(types, project);
	}
}
