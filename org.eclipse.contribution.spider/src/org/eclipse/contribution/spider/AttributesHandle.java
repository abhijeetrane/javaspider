package org.eclipse.contribution.spider;

import java.util.Iterator;

import org.eclipse.contribution.minidraw.IModel;

// Seems like a silly reason to have a subclass
public class AttributesHandle extends FieldsHandle {
	
	public AttributesHandle() {
	}
	
	public AttributesHandle(ObjectFigure figure, IModel model) {
		super(figure, model);
	}

	protected String textLabel() {
		return "a()";
	}

	protected Iterator getFields() {
		return subject.getAttributes();
	}

}
