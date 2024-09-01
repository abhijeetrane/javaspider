package org.eclipse.contribution.spider.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.contribution.spider.ISubject;

public class RawSubject implements ISubject {
	protected Object subject;
	
	public RawSubject(Object subject) {
		this.subject= subject;
	}

	public static RawSubject create(Object subject) {
		return (subject.getClass().isArray())
			? new RawArraySubject(subject)
			: new RawSubject(subject);
	} 

	public String getClassName() {
		String fullName= subject.getClass().getName();
		int start= fullName.lastIndexOf('.') + 1;
		return fullName.substring(start, fullName.length());
	}

	public String getQualifiedClassName() {
		return subject.getClass().getName();
	}

	public Iterator getFields() {
		List results= new ArrayList();
		Class clazz= subject.getClass();
		while (clazz != null) { // Run up the class chain
			Field[] fields= clazz.getDeclaredFields(); // Get all fields, regardless of protection
			for (int i= 0; i < fields.length; i++) {
				final Field field= fields[i];
				field.setAccessible(true);
				results.add(new NamedField(field, subject));
			}
			clazz= clazz.getSuperclass();
		}
		return results.iterator();
	}

	public Iterator getAttributes() {
		List results= new ArrayList();
		Class clazz= subject.getClass();
		while (clazz != null) { // Run up the class chain
			Method[] methods= clazz.getDeclaredMethods(); // Get all methods, regardless of protection
			for (int i= 0; i < methods.length; i++) {
				final Method method= methods[i];
				method.setAccessible(true);
				if (isAttribute(method)) {
					Attribute attribute= new Attribute(method, subject);
					boolean found= false;
					for (Iterator all= results.iterator(); all.hasNext();) {
						Attribute each= (Attribute) all.next();
						found= found || each.getName().equals(attribute.getName());
					}
					if (!found) results.add(attribute);
				}
			}
			clazz= clazz.getSuperclass();
		}
		return results.iterator();
	}

	public Object getRawSubject() {
		return subject; // Testing purposes only, I think
	}
 
 	// Two RawSubjects are equal iff their subjects are identical
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (! (obj instanceof RawSubject)) return false;
		RawSubject other= (RawSubject) obj;
		return subject == other.getRawSubject();
	}

	public int hashCode() {
		return subject.hashCode();
	}
	
	public String toString() {
		return "Subject(" + subject + ")";
	}

	private boolean isAttribute(Method method) {
		return method.getParameterTypes().length == 0
			&& method.getReturnType() != Void.TYPE;
	}
}
