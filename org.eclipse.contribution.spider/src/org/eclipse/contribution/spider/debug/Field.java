package org.eclipse.contribution.spider.debug;

import org.eclipse.contribution.spider.IField;
import org.eclipse.contribution.spider.ISubject;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.core.model.JDIArrayType;
import org.eclipse.jdt.internal.debug.core.model.JDIClassType;
import org.eclipse.jdt.internal.debug.core.model.JDINullValue;

public class Field implements IField {
	private IJavaVariable variable;
	
	public Field(IJavaVariable variable) {
		this.variable= variable;
	}

	public ISubject getValue() {
		try {
			return Subject.create((IJavaValue) variable.getValue());
		} catch (DebugException e) {
			return null; // Should probably return an UnreachableSubject or some such
		}
	}

	public String getName() {
		try {
			return variable.getName();
		} catch (DebugException e) {
			return "Cannot access name";
		}
	}

	// Ugly shit, but the public protocols don't give me a good way of finding out
	public boolean isPrimitive() {
		try {
			return ! (variable.getJavaType() instanceof JDIClassType) || (variable.getJavaType() instanceof JDIArrayType) ;
		} catch (DebugException e) {
			return false;  // Probably should throw an unchecked exception here
		}
	}

	// Ugly shit, but the public protocols don't give me a good way of finding out
	public boolean isNull() {
		try {
			return variable.getValue() instanceof JDINullValue;
		} catch (DebugException e) {
			return false;
		}
	}

	public String getText() {
		try {
			if (isString())
				return "\"" + variable.getValue().getValueString() + "\"";
			return variable.getValue().getValueString();
		} catch (DebugException e) {
			return "Cannot fetch value";
		}
	}

	public boolean isString() {
		try {
			return variable.getJavaType().getName().equals("java.lang.String");
		} catch (DebugException e) {
			return false;
		} 
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
		return false;
	}

}
