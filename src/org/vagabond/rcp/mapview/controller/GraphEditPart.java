package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.Rectangle;

public class GraphEditPart extends AbstractGraphicalEditPart {
	
	public GraphEditPart(Graph graph) {
		setModel(graph);
	}
	
	@Override
	/**
	* Create root figure. Use standard FreeformLayer figure here.
	*/
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer(); 
		layer.setLayoutManager(new FreeformLayout()); 
		layer.setBorder(new LineBorder(1));

		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new ManhattanConnectionRouter());
		
		return layer;
	}

	//Return the lower elements of the model
	protected List getModelChildren() {
		return ((Graph)getModel()).getRelations();
	}
	@Override
	protected void createEditPolicies() {
	// Not editing, so keep empty...
	}

}