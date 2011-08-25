package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.view.RelationFigure;
import org.vagabond.rcp.mapview.view.SelectableFigure;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.PluginLogProvider;

public class RelationNodeEditPart extends AbstractGraphicalEditPart 
		implements VagaSelectionEventProvider {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			RelationNodeEditPart.class);
	
	public RelationNodeEditPart(Node node) { 
		setModel(node);
	}
	
		
	@Override
	protected IFigure createFigure() {
		RelationGraphNode node = (RelationGraphNode) getModel();
		RelationFigure figure = new RelationFigure();
		figure.setNameText(node.getName());
		
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
		RelationFigure figure = (RelationFigure)getFigure();
		RelationGraphNode node = (RelationGraphNode) getModel();
		GraphEditPart parent = (GraphEditPart)getParent();
		Rectangle r;
		
		r = new Rectangle(figure.getBounds());
		r.height = -1;
		r.width = -1;
		log.debug("Relation " + node.getName() +  "constraints is " + r.toString());
		parent.setLayoutConstraint(this, figure, r);
	}
	
	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()
	{
		RelationFigure figure = (RelationFigure) getFigure();
		return figure.getAttrsFigure();
	}

	
	//Return the lower elements of the model
	protected List<?> getModelChildren(){
		return ((RelationGraphNode)getModel()).getAttributes();
	}
	
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new SelectionFeedbackPolicy());
	}

	private void changeSelection (boolean selection) {
		SelectableFigure fig = (SelectableFigure) getFigure();
		fig.setSelection(selection);
		fig.revalidate();
	}

	@Override
	public void fireSelectionEvent(boolean selected) {
		RelationGraphNode rel = (RelationGraphNode) getModel();
		ModelType type = (rel.isSourceRel()) ? ModelType.SourceRelation 
				: ModelType.TargetRelation;
		
		changeSelection(selected);
		if (selected)
			GlobalSelectionController.fireModelSelection(new VagaSelectionEvent(
					type, rel.getUnqualName()));
		else
			GlobalSelectionController.fireModelSelection(VagaSelectionEvent.DESELECT);
	}

}