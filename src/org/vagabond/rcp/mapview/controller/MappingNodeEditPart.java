package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.draw2d.geometry.Rectangle;

import org.vagabond.rcp.mapview.view.MappingFigure;
import org.vagabond.rcp.mapview.view.RelationFigure;
import org.vagabond.rcp.util.PluginLogProvider;

public class MappingNodeEditPart extends AbstractGraphicalEditPart {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			MappingNodeEditPart.class);
	
	public MappingNodeEditPart(Node node) { 
		setModel(node);
	}
	
	@Override
	protected IFigure createFigure() {
		MappingGraphNode node = (MappingGraphNode) getModel();
		IFigure figure = new MappingFigure();		

		return figure;
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
	}

	protected void refreshVisuals() {
		MappingFigure figure = (MappingFigure)getFigure();
		MappingGraphNode node = (MappingGraphNode) getModel();
		GraphEditPart parent = (GraphEditPart)getParent();
		Rectangle r;
		
		figure.setNameText(node.getName());
		
		r = new Rectangle(figure.getBounds());
		r.height = -1;
		r.width = -1;
		log.debug("Mapping" + node.getName() +  "constraints is " + r.toString());
		parent.setLayoutConstraint(this, figure, r);
//		Rectangle r = new Rectangle(node.getConstraint());
//		parent.setLayoutConstraint(this, figure, r);
	}

	public void setConstraint (Rectangle r) {
		RelationFigure figure = (RelationFigure)getFigure();
		figure.setConstraint(figure, r);
	}

	protected List getModelChildren(){
		return ((MappingGraphNode)getModel()).getAttributes();
	}

	@Override
	protected void createEditPolicies() {
		// Not editing, so keep empty...
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()
	{
		MappingFigure figure = (MappingFigure) getFigure();
		return figure.getAttrsFigure();
	}
}