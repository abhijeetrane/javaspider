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

import org.eclipse.jdt.core.IJavaProject;

public class TestResult {
	public final static int OK= 0;
	public final static int FAILED= 1;
	public IJavaProject project;
	public String klass;
	public String method;
	public int status;
	public long startTime;
	public long endTime;
	
	public TestResult(IJavaProject project, String klass, String method, int status, long startTime) {
		this.project= project;
		this.klass= klass;
		this.method= method;
		this.status= status;
		this.startTime= startTime;
	}
	
	public void testFailed()  {
		status= FAILED;
	}
	
	public boolean isFailure()  {
		return status == FAILED;
	}
	
	public void testFinished()  {
		endTime= System.currentTimeMillis();
	}
	
	public long testDuration()  {
		return endTime-startTime;
	}
}
