package org.eclipse.contribution.spider;

import java.util.Iterator;

// The root of our meta-object model. Objects in a Spider get wrapped in
// a ISubject so we can deal with raw objects (same address space), imputed objects
// (generated from static information), and remote objects
public interface ISubject {
	String getClassName();
	String getQualifiedClassName();
	Iterator getFields();
	Iterator getAttributes();
}