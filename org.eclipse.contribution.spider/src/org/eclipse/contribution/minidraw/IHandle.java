package org.eclipse.contribution.minidraw;

public interface IHandle {
	boolean hasTarget(IFigure figure);
	void initialize(IFigure selection, IModel model);
}