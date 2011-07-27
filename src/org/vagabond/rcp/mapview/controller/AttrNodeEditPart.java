package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.AttributeGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.draw2d.geometry.Rectangle;

import org.vagabond.rcp.mapview.view.AttributeFigure;

public class AttrNodeEditPart extends AbstractGraphicalEditPart {

	public AttrNodeEditPart(Node node) { 
		setModel(node);
	}
	
	@Override
	protected IFigure createFigure() {
		IFigure node = new AttributeFigure();		
		return node;
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
	}

	protected void refreshVisuals() {
		AttributeFigure figure = (AttributeFigure)getFigure();
		AttributeGraphNode node = (AttributeGraphNode) getModel();
		AbstractEditPart parent = (AbstractEditPart) getParent();
		figure.getLabel().setText(node.getName());
		Rectangle r = new Rectangle(figure.getBounds());
		
		if (parent instanceof RelationNodeEditPart) 
			((RelationNodeEditPart)parent).setLayoutConstraint(this, figure, r);
		else
			((MappingNodeEditPart)parent).setLayoutConstraint(this, figure, r);
		
	}

	protected List getModelSourceConnections() {
		return ((AttributeGraphNode)getModel()).getSourceConnections();
	}
	
	protected List getModelTargetConnections() {
		return ((AttributeGraphNode)getModel()).getTargetConnections();
	}
	
	@Override
	protected void createEditPolicies() {
		// Not editing, so keep empty...
	}
}