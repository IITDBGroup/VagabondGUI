package org.vagabond.rcp.mapview.controller;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.zest.core.widgets.internal.AligningBendpointLocator;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.model.Correspondence;
import org.vagabond.rcp.mapview.view.SelectableFigure;
import org.vagabond.rcp.mapview.view.routing.RouterContainer;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;

public class CorrespondenceEditPart extends AbstractConnectionEditPart 
		implements VagaSelectionEventProvider {
	
	private boolean nonUserSelect = false;
	private boolean selectionShown = isSelected();
	
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
		c.setLineWidth(2);
		
		return c;
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectionFeedbackPolicy());
	}


	public void nonUserChangeSelection (boolean selected) {
		nonUserSelect = true;
		if (isSelected() != selected)
			this.setSelected(selected ? EditPart.SELECTED_PRIMARY 
					: EditPart.SELECTED_NONE);
		showSelection (selected);
		nonUserSelect = false;
	}
	
	public void showSelection (boolean selection) {
		if (selectionShown == selection)
			return;
		selectionShown = selection;
		PolylineConnectionEx c = (PolylineConnectionEx) getFigure();
		c.setLineWidth(selection ? 4 : 2);
		c.refreshLine();
	}
	
	public boolean isSelected () {
		return getSelected() != EditPart.SELECTED_NONE;
	}
	
	@Override
	public void fireSelectionEvent(boolean isSelected) {
		Correspondence cor = (Correspondence) getModel();
		
		if (isSelected)
			GlobalSelectionController.fireModelSelection(new VagaSelectionEvent(
					ModelType.Correspondence, cor.getName()));
		else
			GlobalSelectionController.fireModelSelection(VagaSelectionEvent.DESELECT);
	}

	public boolean wasUserInteraction() {
		return !nonUserSelect;
	}

}
