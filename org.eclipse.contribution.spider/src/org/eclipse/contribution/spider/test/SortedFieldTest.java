package org.eclipse.contribution.spider.test;

import java.util.Iterator;

import junit.framework.TestCase;

import org.eclipse.contribution.spider.ActionComparator;
import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ISubject;
import org.eclipse.contribution.spider.reflect.RawSubject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

public class SortedFieldTest extends TestCase {

	public SortedFieldTest(String name) {
		super(name);
	}

	// This is simply fucking ugly that I have to fake so much stuff just to test one little method.
	// The design definitely needs some work. "Don't be too proud of this technological
	// terror you have created."
	public void testSortedFields() {
		IAction a= new Action("a") {public void run() {}};
		IAction b= new Action("b") {public void run() {}};
		assertEquals(-1, new ActionComparator().compare(a, b));
	}

	// I can't actually test if the icons come out right, since I'd need Eclipse loaded and running
	public void testFieldIcons() {
		ISubject subject= new RawSubject(new VariousVisibility());
		for (Iterator all= subject.getFields(); all.hasNext();) {
			IField each= (IField) all.next();
			if (each.getName().equals("priv")) assertTrue(each.isPrivate());
			if (each.getName().equals("prot")) assertTrue(each.isProtected());
			if (each.getName().equals("pack")) assertTrue(each.isPackage());
			if (each.getName().equals("pub")) assertTrue(each.isPublic());
		} 
	}

	// I can't actually test if the icons come out right, since I'd need Eclipse loaded and running
	public void testAttributeIcons() {
		ISubject subject= new RawSubject(new VariousVisibility());
		for (Iterator all= subject.getAttributes(); all.hasNext();) {
			IField each= (IField) all.next();
			if (each.getName().equals("priv()")) assertTrue(each.isPrivate());
			if (each.getName().equals("prot()")) assertTrue(each.isProtected());
			if (each.getName().equals("pack()")) assertTrue(each.isPackage());
			if (each.getName().equals("pub()")) assertTrue(each.isPublic());
		} 
	}

	public class VariousVisibility {
		private int priv;
		protected int prot;
		int pack;
		public int pub;
		
		private int priv() {return 0;};
		protected int prot() {return 0;};
		int pack() {return 0;};
		public int pub() {return 0;};
	}
}
