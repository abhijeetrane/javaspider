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
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.junit.ITestRunListener;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;

public class JUnitPlugin extends Plugin {
	private static final String LISTENER_ID= "org.eclipse.contribution.junit.listeners"; //$NON-NLS-1$
	public static final String AUTO_TEST_BUILDER= "org.eclipse.contribution.junit.autoTestBuilder"; //$NON-NLS-1$
	public static final String AUTO_TEST_NATURE= "org.eclipse.contribution.junit.autoTestNature"; //$NON-NLS-1$

	private static JUnitPlugin instance;

	public JUnitPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		instance= this;
	}

	public static JUnitPlugin getPlugin() {
		return instance;
	}

	private List listeners;

	public void addTestListener(ITestRunListener listener) {
		getListeners().add(listener);
	}

	public void removeTestListener(ITestRunListener listener) {
		getListeners().remove(listener);
	}

	public List getListeners() {
		if (listeners == null)
			listeners= computeListeners();
		return listeners;
	}

	private List computeListeners() {
		IPluginRegistry registry= Platform.getPluginRegistry();
		IExtensionPoint extensionPoint= registry.getExtensionPoint(LISTENER_ID);
		IExtension[] extensions= extensionPoint.getExtensions();
		ArrayList results= new ArrayList();
		for (int i= 0; i < extensions.length; i++) {
			IConfigurationElement[] elements= extensions[i].getConfigurationElements();
			for (int j= 0; j < elements.length; j++) {
				try {
					Object listener= elements[j].createExecutableExtension("class"); //$NON-NLS-1$
					if (listener instanceof ITestRunListener)
						results.add(listener);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		return results;
	}
	
	public abstract class SafeRunnable implements ISafeRunnable {
		public void handleException(Throwable exception) {
		}
	}

	public void fireTestsStarted(final IJavaProject project, final int count) { // Only public for testing purposes
		for (Iterator all= getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each= (ITestRunListener) all.next();
			Platform.run(new SafeRunnable() {
				public void run() throws Exception {
					each.testsStarted(project, count);
				}
			});
		}
	}

	public void fireTestsFinished(final IJavaProject project) {
		for (Iterator all= getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each= (ITestRunListener) all.next();
			Platform.run(new SafeRunnable() {
				public void run() throws Exception {
					each.testsFinished(project);
				}
			});
		}
	}

	public void fireTestStarted(final IJavaProject project, final String klass, final String methodName) {
		for (Iterator all= getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each= (ITestRunListener) all.next();
			Platform.run(new SafeRunnable() {
				public void run() throws Exception {
					each.testStarted(project, klass, methodName);
				}
			});
		}
	}

	public void fireTestFailed(final IJavaProject project, final String klass, final String method, final String trace) {
		for (Iterator all= getListeners().iterator(); all.hasNext();) {
			final ITestRunListener each= (ITestRunListener) all.next();
			Platform.run(new SafeRunnable() {
				public void run() throws Exception {
					each.testFailed(project, klass, method, trace);
				}
			});
		}
	}

	public void run(IType type) throws CoreException  {
		run(new IType[] {type}, type.getJavaProject());
	}

	
	public void run(IType[] classes, IJavaProject project) throws CoreException {
		if (classes.length == 0)
			return;
		ITestRunListener listener= new MarkerCreator(project);
		addTestListener(listener);
		try {
			new TestRunner(project).run(classes);
		} finally {
			removeTestListener(listener);
		}
	}

	public void addAutoBuildNature(IProject project) throws CoreException {
		if (project.hasNature(AUTO_TEST_NATURE)) 
			return;

		IProjectDescription description = project.getDescription();
		String[] ids= description.getNatureIds();
		String[] newIds= new String[ids.length + 1];
		System.arraycopy(ids, 0, newIds, 0, ids.length);
		newIds[ids.length]= AUTO_TEST_NATURE;
		description.setNatureIds(newIds);
		project.setDescription(description, null);
	}
	
	public void removeAutoBuildNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] ids = description.getNatureIds();
		for (int i = 0; i < ids.length; ++i) {
			if (ids[i].equals(AUTO_TEST_NATURE)) {
				String[] newIds = new String[ids.length - 1];
				System.arraycopy(ids, 0, newIds, 0, i);
				System.arraycopy(ids, i + 1, newIds, i, ids.length - i - 1);
				description.setNatureIds(newIds);
				project.setDescription(description, null);
				return;
			}
		}	
	}
}
