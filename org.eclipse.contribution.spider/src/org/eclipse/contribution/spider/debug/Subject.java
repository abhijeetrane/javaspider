package org.eclipse.contribution.spider.debug;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.spider.ISubject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;

public class Subject implements ISubject {
	protected IJavaValue subject;

	public static ISubject create(IJavaValue subject) {
		return new Subject(subject);
	}
 
	public Subject(IJavaValue subject) {
		this.subject= subject;
	}

	public String getClassName() {
		try {
			String fullName= subject.getJavaType().getName();
			int start= fullName.lastIndexOf('.') + 1;
			return fullName.substring(start, fullName.length());
		} catch (DebugException e) {
			return "Unreachable";
		}
	}

	public String getQualifiedClassName() {
		try {
			return subject.getJavaType().getName();
		} catch (DebugException e) {
			return "Unreachable";
		}
	}	

	public Iterator getFields() {
		List results= new ArrayList();
		try {
			IVariable[] variables= subject.getVariables();
			for (int i= 0; i < variables.length; i++) {
				IJavaVariable each= (IJavaVariable) variables[i]; // Limits this to Java
				results.add(new Field(each));
			}
		} catch (DebugException e) {
		}
		return results.iterator();
	}

	public Iterator getAttributes() {
		ArrayList results= new ArrayList();
//		just quick, I couldn't go to sleep without checking into the attributes. There is no public API to invoke a method, 
//		so you will have to downcast the IJavaValue to an JDIObjectValue. 
//		It provides a sendMessage method. Not sure that this already the solution but something to chew on - gnight
		//subject.getJavaType().
		//Be careful not to return duplicate attributes from superclasses (cf reflect.RawSubject)
		return results.iterator(); //TODO: Later
	}

	// I can't believe this will really work. However, access is blocked to getUnderlyingObject
	public boolean equals(Object obj) {
		if (! (obj instanceof Subject)) return false;
		return subject.hashCode() == obj.hashCode();
	}

	public int hashCode() {
		return subject.hashCode();
	}

}
