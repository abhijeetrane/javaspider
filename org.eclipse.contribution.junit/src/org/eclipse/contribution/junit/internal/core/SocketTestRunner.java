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

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestListener;
import junit.framework.TestResult;
import junit.framework.TestSuite;
/**
 * A test runner that communicates the test results over a socket.
 * The socket protocol is string based. The following messages
 * are sent:
 * <pre>
 * <"starting tests "+number>
 *      test run started with the given number of tests
 * <"ending tests">: test run ended
 * <"starting test "+testname+"("+classname+")">
 * 		the given test has started
 * <"failing test "+testname+"("+classname+")">
 * 		test failed. The stack trace is sent
 * 		in the following lines and terminated by "END TRACE"	
 * </pre>
 */
public class SocketTestRunner implements TestListener {
	private int port;
	private Socket socket;
	private PrintWriter writer;

	/**
	 * The entry point for the test runner. 
	 * The arguments are:
	 * args[0]: the port number to connect to
	 * args[1-n]: the name of test classes 
	 */
	public static void main(String[] args) {
		new SocketTestRunner().runTests(args);
	}
	
	private void runTests(String[] args) {
		port= Integer.parseInt(args[0]);
		openClientSocket();
		try {
			TestSuite suite= new TestSuite();
			for (int i= 1; i < args.length; i++) 
				suite.addTestSuite(Class.forName(args[i]));
			writer.println("starting tests " + suite.countTestCases());  //$NON-NLS-1$
			TestResult result= new TestResult();
			result.addListener(this);
			suite.run(result);
			writer.println("ending tests");  //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			System.out.println("Not a class: " + args[1]);  //$NON-NLS-1$
		} finally {
			closeClientSocket();
		}
	}

	private void openClientSocket() {
		for (int i= 0; i < 10; i++) {
			try {
				socket= new Socket("localhost", port); //$NON-NLS-1$
				writer= new PrintWriter(socket.getOutputStream(), true);
				return;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
		}
	}

	private void closeClientSocket() {
		writer.close();
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addError(Test test, Throwable t) {
		writer.println("failing test " + test);  //$NON-NLS-1$
		t.printStackTrace(writer);
		writer.println("END TRACE");  //$NON-NLS-1$
	}

	public void addFailure(Test test, AssertionFailedError t) {
		addError(test, t);
	}

	public void endTest(Test test) {
	}

	public void startTest(Test test) {
		writer.println("starting test " + test);  //$NON-NLS-1$
	}
}
