package org.eclipse.contribution.spider.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ISubject;

public class NamedField implements IField {
	private Field field;
	private Object subject; // Suspicious

	public NamedField(Field field, Object subject) {
		this.field= field;
		this.subject= subject;
	}

	public ISubject getValue() {
		return RawSubject.create(evaluate());
	}

	protected Object evaluate() {
		try {
			return field.get(subject);
		} catch (IllegalAccessException e) {
			// Later return a special InaccessibleSubject
			e.printStackTrace();
			return null;
		}
	}

	public String getName() {
		return field.getName();
	}

	public boolean isPrimitive() {
		return valueType().isPrimitive();
	}

	public Class valueType() {
		return field.getType();
	}

	public boolean isString() {
		return valueType() == java.lang.String.class;
	}

	public boolean isNull() {
		return evaluate() == null;
	}

	public String getText() {
		if (isNull())
			return "null";
		if (isString())
			return "\"" + (String) evaluate() + "\"";
		return evaluate().toString(); // Conversion should happen elsewhere
	}
	
	public boolean isPrivate() {
		return Modifier.isPrivate(field.getModifiers());
	}
	
	public boolean isProtected() {
		return Modifier.isProtected(field.getModifiers());
	}
	
	public boolean isPackage() {
		return ! isPrivate() && ! isProtected() && ! isPublic();
	}
	
	public boolean isPublic() {
		return Modifier.isPublic(field.getModifiers());
	}
}
