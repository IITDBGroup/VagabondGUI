package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.view.MappingFigure;
import org.vagabond.rcp.mapview.view.RelationFigure;
import org.vagabond.rcp.mapview.view.SelectableFigure;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.PluginLogProvider;

public class MappingNodeEditPart extends AbstractGraphicalEditPart 
		implements VagaSelectionEventProvider {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			MappingNodeEditPart.class);
	
	private boolean nonUserSelect = false;
	private boolean selectionShown = isSelected();
	
	public MappingNodeEditPart(Node node) { 
		setModel(node);
	}
	
	@Override
	protected IFigure createFigure() {
//		MappingGraphNode node = (MappingGraphNode) getModel();
		IFigure figure = new MappingFigure();		

		return figure;
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
		super.deactivate();
	}

	protected void refreshVisuals() {
		MappingFigure figure = (MappingFigure)getFigure();
		MappingGraphNode node = (MappingGraphNode) getModel();
		GraphEditPart parent = (GraphEditPart)getParent();
		Rectangle r;
		
		figure.setNameText(node.getName());
		
		r = new Rectangle(figure.getBounds());
		r.height = -1;
		r.width = -1;
		log.debug("Mapping" + node.getName() +  "constraints is " + r.toString());
		parent.setLayoutConstraint(this, figure, r);
//		Rectangle r = new Rectangle(node.getConstraint());
//		parent.setLayoutConstraint(this, figure, r);
	}

	public void setConstraint (Rectangle r) {
		RelationFigure figure = (RelationFigure)getFigure();
		figure.setConstraint(figure, r);
	}

	protected List<?> getModelChildren(){
		return ((MappingGraphNode)getModel()).getAttributes();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectionFeedbackPolicy());
	}

	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()
	{
		MappingFigure figure = (MappingFigure) getFigure();
		return figure.getAttrsFigure();
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
		SelectableFigure fig = (SelectableFigure) getFigure();
		fig.setSelection(selection);
		fig.revalidate();
	}
	
	public boolean isSelected () {
		return getSelected() != EditPart.SELECTED_NONE;
	}
	
	@Override
	public void fireSelectionEvent(boolean selected) {
		MappingGraphNode map = (MappingGraphNode) getModel();
	
		if (selected)
			GlobalSelectionController.fireModelSelection(new VagaSelectionEvent(
				ModelType.Mapping, map.getName()));
		else
			GlobalSelectionController.fireModelSelection(VagaSelectionEvent.DESELECT);
	}

	public boolean wasUserInteraction() {
		return !nonUserSelect;
	}

}