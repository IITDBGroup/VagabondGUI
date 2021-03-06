package org.vagabond.rcp.mapview.controller;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.swt.SWT;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.util.SWTResourceManager;

public class MapConnectionEditPart extends AbstractConnectionEditPart {
	public MapConnectionEditPart(Connection connection) { 
		setModel(connection);
	}

	protected IFigure createFigure() {
		PolylineConnection c = new PolylineConnection();
		c.setForegroundColor(SWTResourceManager.getColor(178,56,44));
		
		// create label
//		MidpointLocator relationshipLocator = 
//            new MidpointLocator(c,0);
//		Label relationshipLabel = new Label(((Connection)getModel()).getName());
//		c.add(relationshipLabel, relationshipLocator);
		
		// create the arrowhead
		PolygonDecoration decoration = new PolygonDecoration();
		c.setTargetDecoration(decoration);
		
		c.setLineStyle(SWT.LINE_DASH);
		
		return c;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

}
