package org.vagabond.rcp.mapview.controller;



import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.view.routing.RouterContainer;
import org.vagabond.rcp.util.SWTResourceManager;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.ShortestPathRouter;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.zest.core.widgets.internal.AligningBendpointLocator;

public class CorrespondenceEditPart extends AbstractConnectionEditPart {
	
	public CorrespondenceEditPart(Connection connection) { 
		setModel(connection);
	}

	protected IFigure createFigure() {
		PolylineConnectionEx c = new PolylineConnectionEx();
		c.setForegroundColor(SWTResourceManager.nameColor("CorrLineColor", 
				new RGB (0,150,0)));
		
		// create the name label
//		ConnectionEndpointLocator end;
		AligningBendpointLocator relationshipLocator = 
            new AligningBendpointLocator(c);
		
//		c.setConnectionRouter(new ManhattanConnectionRouter());
		Label relationshipLabel = new Label(((Connection)getModel()).getName());
		relationshipLabel.setOpaque(true);
		relationshipLabel.setBackgroundColor(
				org.eclipse.draw2d.ColorConstants.tooltipBackground);
		relationshipLabel.setForegroundColor(ColorConstants.black);
		relationshipLabel.setBorder(new LineBorder(ColorConstants.black, 1));

		c.add(relationshipLabel, relationshipLocator);
		c.setConnectionRouter(RouterContainer.getInstance()
				.getRouter("Correspondence"));
		
		return c;
	}
	
	@Override
	protected void createEditPolicies() {
		// TODO Auto-generated method stub
	}

}
