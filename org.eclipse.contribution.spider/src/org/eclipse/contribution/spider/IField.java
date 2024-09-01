package org.eclipse.contribution.spider;

// A related sub-part of a ISubject. Hides the difference between array elements,
// fields, computed values, and remote references
public interface IField {
	ISubject getValue();
	String getName();
	String getText();
	boolean isPrimitive();
	boolean isNull();
	boolean isString(); // This doesn't actually belong here. The presentation should decide what to inline.
	boolean isPrivate();
	boolean isProtected();
	boolean isPackage();
	boolean isPublic();
}