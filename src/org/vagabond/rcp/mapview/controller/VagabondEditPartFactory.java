package org.vagabond.rcp.mapview.controller;

import org.vagabond.rcp.mapview.model.AttributeGraphNode;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.model.Correspondence;
import org.vagabond.rcp.mapview.model.ForeignKeyConnection;
import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MapConnection;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.util.PluginLogProvider;

import org.apache.log4j.Logger;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

public class VagabondEditPartFactory implements EditPartFactory {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			VagabondEditPartFactory.class);
	
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		
		if (model instanceof AttributeGraphNode){
			//Standard editpart instance for standard model element
			editPart = new AttrNodeEditPart((AttributeGraphNode) model);
		} else if (model instanceof RelationGraphNode){
			//Root instance of editpart for root model element
			editPart = new RelationNodeEditPart((RelationGraphNode)model);
		} else if (model instanceof Graph) {
			editPart = new GraphEditPart((Graph)model);
		} else if (model instanceof MappingGraphNode) {
			//Root instance of editpart for root model element
			editPart = new MappingNodeEditPart((MappingGraphNode)model);
		} else if (model instanceof Correspondence) {
			editPart = new CorrespondenceEditPart((Correspondence)model);
		} else if (model instanceof MapConnection) {
			editPart = new MapConnectionEditPart((MapConnection)model);
		} else if (model instanceof ForeignKeyConnection) {
			editPart = new ForeignKeyConnEditPart((ForeignKeyConnection) model);
		}
		
		log.debug(editPart.toString());
		
		return editPart;
	}
}
