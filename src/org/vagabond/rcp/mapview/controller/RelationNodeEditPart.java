package org.vagabond.rcp.mapview.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.view.RelationFigure;
import org.vagabond.rcp.util.PluginLogProvider;

public class RelationNodeEditPart extends AbstractGraphicalEditPart {

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
//		figure.invalidate();
		return figure;
	}

	@Override
	public void activate() {
		super.activate();
	}

	@Override
	public void deactivate() {
	}

	protected void refreshVisuals() {
		RelationFigure figure = (RelationFigure)getFigure();
		RelationGraphNode node = (RelationGraphNode) getModel();
		GraphEditPart parent = (GraphEditPart)getParent();
		Rectangle r;
//		figure.revalidatePrefferedSize();
//		figure.revalidate();
		r = new Rectangle(figure.getBounds());
		r.height = -1;
		r.width = -1;
		log.debug("Relation " + node.getName() +  "constraints is " + r.toString());
		parent.setLayoutConstraint(this, figure, r);
	}

//	public void setConstraint (Rectangle r) {
//		RelationFigure figure = (RelationFigure)getFigure();
//		figure.setConstraint(figure, r);
//	}
	
	/**
	 * @return the Content pane for adding or removing child figures
	 */
	public IFigure getContentPane()
	{
		RelationFigure figure = (RelationFigure) getFigure();
		return figure.getAttrsFigure();
	}

	
	//Return the lower elements of the model
	protected List getModelChildren(){
		return ((RelationGraphNode)getModel()).getAttributes();
	}
	
//	protected List getModelSourceConnections() {
//		return ((RelationGraphNode)getModel()).getSourceConnections();
//	}
//	
//	protected List getModelTargetConnections() {
//		return ((RelationGraphNode)getModel()).getTargetConnections();
//	}
	
	@Override
	protected void createEditPolicies() {
		// Not editing, so keep empty...
	}

}