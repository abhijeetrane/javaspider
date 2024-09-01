package org.eclipse.contribution.minidraw.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite result= new TestSuite("MiniDraw Tests");
		result.addTestSuite(DrawingTest.class);
		result.addTestSuite(GroupTest.class);
		result.addTestSuite(SelectionTest.class);
		result.addTestSuite(InteractionTest.class);
		result.addTestSuite(HandleGroupTest.class);
		return result;
	}
}
