package org.vagabond.rcp.mapview.controller;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.zest.core.widgets.internal.AligningBendpointLocator;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.model.Correspondence;
import org.vagabond.rcp.mapview.view.routing.RouterContainer;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;

public class CorrespondenceEditPart extends AbstractConnectionEditPart 
		implements VagaSelectionEventProvider {
	
	public CorrespondenceEditPart(Connection connection) { 
		setModel(connection);
	}

	protected IFigure createFigure() {
		PolylineConnectionEx c = new PolylineConnectionEx();
		c.setForegroundColor(SWTResourceManager.nameColor("CorrLineColor", 
				new RGB (0,150,0)));
		
		// create the name label
		AligningBendpointLocator relationshipLocator = 
            new AligningBendpointLocator(c);
		
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
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectionFeedbackPolicy());
	}

	@Override
	public void fireSelectionEvent(boolean isSelected) {
		PolylineConnectionEx c = (PolylineConnectionEx) getFigure();
		Correspondence cor = (Correspondence) getModel();
		
		c.setLineWidth(isSelected ? 4 : 2);
		if (isSelected)
			GlobalSelectionController.fireModelSelection(new VagaSelectionEvent(
					ModelType.Correspondence, cor.getName()));
		else
			GlobalSelectionController.fireModelSelection(VagaSelectionEvent.DESELECT);
	}

}
