package org.vagabond.rcp.mapview.view;

import org.vagabond.rcp.mapview.model.ContentProvider;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import org.vagabond.rcp.mapview.controller.GraphEditPart;
import org.vagabond.rcp.mapview.controller.VagabondEditPartFactory;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

public class MapGraphView extends ViewPart {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(MapGraphView.class);
	
	public static final String ID = "org.vagabond.rcp.mapview.view.view";

	// Use a standard Viewer for the Draw2d canvas
	private ScrollingGraphicalViewer viewer;
	// Use standard RootEditPart as holder for all other edit parts
	private RootEditPart rootEditPart;
	// Custom made EditPartFactory, will automatically be called to create edit
	// parts for model elements
	private EditPartFactory editPartFactory;

	public static MapGraphView getInstance() {
		return (MapGraphView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(ID);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		rootEditPart = new ScalableRootEditPart();
		editPartFactory = new VagabondEditPartFactory();
		// Initialize the viewer, 'parent' is the
		// enclosing RCP windowframe
		viewer.createControl(parent);
		viewer.setRootEditPart(rootEditPart);
		viewer.setEditPartFactory(editPartFactory);
		
		viewer.setContents(ContentProvider.getInstance().getGraph());
		// Set the view's background to white
		viewer.getControl().setBackground(new Color(null, 255, 255, 255));
				
		viewer.getControl().addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	try {
		    		log.debug("resize on map graph");
		    		
		    		GraphEditPart part = (GraphEditPart) viewer.getRootEditPart().getContents();
		    		part.setLayoutConstraints();
		    		
		    		viewer.getRootEditPart().getContents().refresh();
				} catch (Exception e1) {
					LoggerUtil.logException(e1, log);
				}
		    }
		});
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