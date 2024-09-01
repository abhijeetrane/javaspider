package org.eclipse.contribution.spider.reflect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RawArraySubject extends RawSubject {
	public RawArraySubject(Object subject) {
		super(subject);
	}

	public String getClassName() {
		String fullName= subject.getClass().getName();
		int start= fullName.lastIndexOf('.') + 1;
		return fullName.substring(start, fullName.length() - 1 /* ;? */) + "[]";
	}

	public String getQualifiedClassName() {
		return subject.getClass().getName()+"[]";
	}
	
	public Iterator getFields() {
		List results= new ArrayList();
		final Object[] arraySubject= (Object[]) subject;
		for (int i= 0; i < arraySubject.length; i++) {
			results.add(new IndexedField(arraySubject, i)); 
		}
		return results.iterator();
	}

}
