package org.eclipse.contribution.spider.reflect;

import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ISubject;


public class IndexedField implements IField {
	protected Object[] subject;
	protected int index;
	
	public IndexedField(Object[] subject, int index) {
		this.subject= subject;
		this.index= index;
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isNull() {
		return evaluate() == null;
	}

	protected Object evaluate() {
		return subject[index];
	}
	
	public boolean isString() {
		return valueType() == java.lang.String.class;
	}

	public Class valueType() {
		return subject.getClass().getComponentType();
	}

	public ISubject getValue() {
		return RawSubject.create(evaluate());
	}

	public String getName() {
		return Integer.toString(index);
	}
	
	public String getText() {
		if (isNull()) return "null";
		if (isString()) return "\"" + (String) evaluate() + "\"";
		return evaluate().toString();// Conversion should happen elsewhere
	}
	
	public boolean isPackage() {
		return false;
	}

	public boolean isPrivate() {
		return false;
	}

	public boolean isProtected() {
		return false;
	}

	public boolean isPublic() {
		return true;
	}

}
