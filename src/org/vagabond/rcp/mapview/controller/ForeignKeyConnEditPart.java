package org.vagabond.rcp.mapview.controller;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.view.routing.RouterContainer;

public class ForeignKeyConnEditPart extends AbstractConnectionEditPart {

	public ForeignKeyConnEditPart(Connection connection) { 
		setModel(connection);
	}

	protected IFigure createFigure() {
		PolylineConnectionEx c = new PolylineConnectionEx();
		c.setForegroundColor(ColorConstants.black);

		PolygonDecoration decoration = new PolygonDecoration();
		c.setTargetDecoration(decoration);		
		
		c.setConnectionRouter(RouterContainer.getInstance()
				.getRouter("ForeignKey"));

		
		return c;
	}

	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

}



