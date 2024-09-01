package org.eclipse.contribution.spider;
 
public class ToDo {
	/***********************Later***********************/
	//TODO: Selection provider
	//TODO: Icons
	//TODO: Spider picture in update site
	//TODO: Collapse objects to singularities (as an alternative to ellision)
	//TODO: Move contract stuff in BindingFigure to ShowContractHandle
	//TODO: Eliminate DrawingCanvas (move everything into the model)
	//TODO: The whole question of drawing semantics needs a better answer than IModel
	//TODO: Differentiate handles during multiple select (delete would distribute, but text sizing only would if every figure was text-sizeable)
	//TODO: Remote attributes
	//TODO: Don't pop up menu items for already expanded fields
	//TODO: Ellision
	//TODO: Scroll bars
	//TODO: Save/export
	//TODO: Print
	//TODO: Primitive arrays
	//TODO: Preferences for fonts, colors, line width/style
	//TODO: Full class name tool tip
	//TODO: Field tool tip (declaration?)
	//TODO: Push everything down from Figure and then refactor
	//TODO: Filters for statics and final on local tool bar
	//TODO: Deal better with statics and final statics
	//TODO: FieldFigures contribute menu items (e.g. delete) -- better to have field-specific handles?
	//TODO: Contributed object renderers
	//TODO: Immediate rendering
	//TODO: Construct drawings from static code. Will require new implementation of ISubject and IField
	//TODO: Annotate BindingFigure with actual contract (reify contract)
	//TODO: Bendy binding figures
	//TODO: Better array element selection UI
	//TODO: GUI log file so I can analyze usage
	//TODO: Debugger interface ("explore" everywhere "inspect" is)
	//TODO: Elements for Collections
	//TODO: Update when execution stops
	//TODO: Arrowheads go googoo when pointing straight down
	//TODO: Long strings
	//TODO: Introductory article
	//TODO: Group select
	//TODO: Coherent error handling strategy for fields. Start with SpiderTest.
	//TODO: It'd be nice to have a general solution to the persistent GC change problem (copyable GCs)
	//TODO: ShowContractHandle contributed to BindingFigure
	//TODO: 
	
	/*******************Done******************/
	//Icons for visibility of fields and attributes
	//Layout handles horizontally (LayoutGroup, also useful for ObjectFigure)
	//Annotate fields and attributes with visibility icons
	//Sort fields and attributes in menus
	//Push Drawing stuff down into SelectionDrawing, since the factoring isn't working out yet
	//Re-layout ObjectFigure when FieldFigure is deleted
	//Default model for SelectionDrawing (?) instead of null
	//Test to makes sure that when bindings are deleted the listeners go away, too
	//Clean up AttributesHandle and FieldsHandle
	//Get rid of double click pop up menu garbage
	//Figure pulldown menu handle
	//Figure delete handle
	//Separate MiniDraw tests and OE tests
	//Class name correct for remote values
	//Array remote values
	//Fields for remote values
	//Separate MiniDraw package
	//TextMeasurer so I can unit test text-rendering figures (GC cached in DrawingCanvas)
	//IObjectExplorer as the global reference for add and remove menu items (well, not global)
	//ObjectExplorerView should be able to operate widget-less
	//More ObjectExplorer tests
	//Outgoing bindings
	//Arrow heads
	//Narrow from Figure to IFigure and Drawing to IGroup
	//Unify field figures
	//Unify ObjectFigure and ArrayFigure
	//Attributes (computed references)
	//Render Strings in place

}
