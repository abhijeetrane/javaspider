package org.eclipse.contribution.spider.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static Test suite() {
		TestSuite result= new TestSuite("Spider Tests");
		result.addTest(org.eclipse.contribution.minidraw.test.AllTests.suite());
		result.addTestSuite(ReflectionTest.class);
		result.addTestSuite(ClassFigureTest.class);
		result.addTestSuite(SpiderTest.class);
		result.addTestSuite(BindingTest.class);
		result.addTestSuite(BindingAddingTest.class);
		result.addTestSuite(SubjectTest.class);
		result.addTestSuite(ObjectFigureTest.class);
		result.addTestSuite(SortedFieldTest.class);
		return result;
	}
}
