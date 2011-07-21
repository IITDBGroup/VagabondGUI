package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.draw2d.geometry.Rectangle;

import org.vagabond.rcp.mapview.view.NodeFigure;

public class RelationNodeEditPart extends AbstractGraphicalEditPart {
	private Font widgetFont;
	
	public RelationNodeEditPart(Node node) { 
		setModel(node);
	}
	
	@Override
	protected IFigure createFigure() {
		return new NodeFigure();
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
	//Do cleanup of resources
		if (widgetFont != null) widgetFont.dispose();
			super.deactivate();
	}

	protected void refreshVisuals() {
		NodeFigure figure = (NodeFigure)getFigure();
		RelationGraphNode node = (RelationGraphNode) getModel();
		GraphEditPart parent = (GraphEditPart)getParent();
		figure.getLabel().setText(node.getName());
		Rectangle r = new Rectangle(node.getConstraint());
		parent.setLayoutConstraint(this, figure, r);
	}

	//Return the lower elements of the model
	protected List getModelChildren(){
		return ((RelationGraphNode)getModel()).getAttributes();
	}
	
//	protected List getModelSourceConnections() {
//		return ((RelationGraphNode)getModel()).getSourceConnections();
//	}
//	
//	protected List getModelTargetConnections() {
//		return ((RelationGraphNode)getModel()).getTargetConnections();
//	}
	
	@Override
	protected void createEditPolicies() {
		// Not editing, so keep empty...
	}

}