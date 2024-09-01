package org.eclipse.contribution.minidraw;

import java.util.Iterator;

// Drawings in MiniDraw aren't things in themselves, they represent some underlying semantics. IModel is the beginnings of
// a representation for those semantics, mapping operations from the graphics world back to operations on the semantic world.
public interface IModel {
	SelectionDrawing getDrawing(); // The drawing really belongs in the model, so we can write headless tests
	void remove(IFigure subject);
	Iterator getHandlesFor(IFigure selection);

}
