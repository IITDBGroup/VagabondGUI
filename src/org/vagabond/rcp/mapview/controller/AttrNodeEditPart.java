package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.vagabond.rcp.mapview.model.AttributeGraphNode;
import org.vagabond.rcp.mapview.model.MapConnection;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.draw2d.geometry.Rectangle;

import org.vagabond.rcp.mapview.view.AttributeFigure;
import org.vagabond.rcp.mapview.view.LeftRightParentBoxFigureAnchor;
import org.vagabond.rcp.util.PluginLogProvider;

public class AttrNodeEditPart extends AbstractGraphicalEditPart implements NodeEditPart {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			AttrNodeEditPart.class);
	
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
		AttributeFigure figure = (AttributeFigure) getFigure();
		AttributeGraphNode node = (AttributeGraphNode) getModel();
//		GraphicalEditPart parent = (GraphicalEditPart) getParent();
		figure.setAttrName(node.getName());
//		Rectangle r = new Rectangle(figure.getBounds());
		
//		log.debug("Attr: " + node.getName() + " will be at " + r.toString());
//		parent.setLayoutConstraint(this, figure, r);
//		figure.revalidate();
//		if (parent instanceof RelationNodeEditPart) 
//			((RelationNodeEditPart)parent).setLayoutConstraint(this, figure, r);
//		else
//			((MappingNodeEditPart)parent).setLayoutConstraint(this, figure, r);
		
	}

	protected List getModelSourceConnections() {
		return ((AttributeGraphNode)getModel()).getSourceConnections();
	}
	
	protected List getModelTargetConnections() {
		return ((AttributeGraphNode)getModel()).getTargetConnections();
	}
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		// correspondence edit part connect source on left
		if (connection instanceof CorrespondenceEditPart)
			return new LeftRightParentBoxFigureAnchor(getFigure(), false);
		if (connection instanceof MapConnectionEditPart) {
			MapConnection mapConnModel = (MapConnection) connection.getModel();
			return new LeftRightParentBoxFigureAnchor(getFigure(), 
					mapConnModel.getSourceAttachLeft());
		}
		throw new RuntimeException ("unkown connection type " 
				+ connection.getClass().getName());
//        return new LeftRightParentBoxFigureAnchor(getFigure(), false);
    }
	
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor (ConnectionEditPart connection) {
		// correspondence edit part connect source on left
		if (connection instanceof CorrespondenceEditPart)
			return new LeftRightParentBoxFigureAnchor(getFigure(), true);
		if (connection instanceof MapConnectionEditPart) {
			MapConnection mapConnModel = (MapConnection) connection.getModel();
			return new LeftRightParentBoxFigureAnchor(getFigure(), 
					mapConnModel.getTargetAttachLeft());
		}
		throw new RuntimeException ("unkown connection type " 
				+ connection.getClass().getName());
//		return new LeftRightParentBoxFigureAnchor(getFigure(), true);
	}
	
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void createEditPolicies() {
		// Not editing, so keep empty...
	}

	
	
	
}