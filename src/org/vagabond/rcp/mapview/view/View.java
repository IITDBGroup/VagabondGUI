package org.vagabond.rcp.mapview.view;

import org.vagabond.rcp.mapview.model.ContentProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.vagabond.rcp.mapview.controller.VagabondEditPartFactory;

public class View extends ViewPart {
	public static final String ID = "org.vagabond.rcp.mapview.view.view";

	//Use a standard Viewer for the Draw2d canvas
	private ScrollingGraphicalViewer viewer;
	//Use standard RootEditPart as holder for all other edit parts
	private RootEditPart rootEditPart;
	//Custom made EditPartFactory, will automatically be called to create edit
	// parts for model elements
	private EditPartFactory editPartFactory;

	public static View getInstance() {
		return (View) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	/**
	* This is a callback that will allow us to create the viewer and initialize
	* it.
	*/
	public void createPartControl(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		rootEditPart = new ScalableFreeformRootEditPart();
		editPartFactory = new VagabondEditPartFactory();
		//Initialize the viewer, 'parent' is the
		// enclosing RCP windowframe
		viewer.createControl(parent);
		viewer.setRootEditPart(rootEditPart);
		viewer.setEditPartFactory(editPartFactory);
		//Inject the model into the viewer, the viewer will
		// traverse the model automatically
		//viewer.setContents(model);
		viewer.setContents(ContentProvider.getInstance().getGraph());
		//Set the view's background to white
		viewer.getControl().setBackground(new Color(null, 255,255,255));
	}

	/**
	* Passing the focus request to the viewer's control.
	*/
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public GraphicalViewer getViewer() {
		return viewer;
	}
}