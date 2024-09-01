package org.eclipse.contribution.spider.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ISubject;

public class Attribute implements IField {
	protected Method method;
	protected Object subject;
	
	public Attribute(Method method, Object subject) {
		this.method= method;
		this.subject= subject;
	}

	public ISubject getValue() {
		return RawSubject.create(evaluate());
	}

	// this is a weird error handling strategy. it will result in Exceptions appearing in the diagram where some other type is expected
	public Object evaluate() {
		try {
			return method.invoke(subject, new Object[] {});
		} catch (IllegalAccessException e) {
			return e;
		} catch (InvocationTargetException e) {
			return e;
		}
	}

	public String getName() {
		return method.getName() + "()";
	}

	public boolean isPrimitive() {
		return valueType().isPrimitive();
	}

	public boolean isNull() {
		return evaluate() == null;
	}

	public String getText() {
		if (isNull()) return "null";
		if (isString()) return "\"" + (String) evaluate() + "\"";
		return evaluate().toString();// Conversion should happen elsewhere
	}

	public boolean isString() {
		return valueType() == java.lang.String.class;
	}

	private Class valueType() {
		return method.getReturnType();
	}
	
	public boolean isPrivate() {
		return Modifier.isPrivate(method.getModifiers());
	}
	
	public boolean isProtected() {
		return Modifier.isProtected(method.getModifiers());
	}
	
	public boolean isPackage() {
		return ! isPrivate() && ! isProtected() && ! isPublic();
	}
	
	public boolean isPublic() {
		return Modifier.isPublic(method.getModifiers());
	}
}
