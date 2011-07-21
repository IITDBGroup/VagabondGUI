package org.vagabond.rcp.mapview.controller;

import org.vagabond.rcp.mapview.model.Connection;

import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class ConnectionEditPart extends AbstractConnectionEditPart {
	public ConnectionEditPart(Connection connection) { 
		setModel(connection);
	}

	protected IFigure createFigure() {
		PolylineConnection c = new PolylineConnection();
		ConnectionEndpointLocator relationshipLocator = 
            new ConnectionEndpointLocator(c,true);
		relationshipLocator.setUDistance(40);
		relationshipLocator.setVDistance(-5);
		Label relationshipLabel = new Label(((Connection)getModel()).getName());
		c.add(relationshipLabel, relationshipLocator);
		return c;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

}
