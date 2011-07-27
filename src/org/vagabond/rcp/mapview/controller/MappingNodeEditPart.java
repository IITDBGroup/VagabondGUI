package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

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

public class MappingNodeEditPart extends AbstractGraphicalEditPart {
	
	public MappingNodeEditPart(Node node) { 
		setModel(node);
	}
	
	@Override
	protected IFigure createFigure() {
		MappingGraphNode node = (MappingGraphNode) getModel();
		IFigure figure = new MappingFigure(new Label(node.getName()));		

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
		Rectangle r = new Rectangle(node.getConstraint());
		parent.setLayoutConstraint(this, figure, r);
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

}