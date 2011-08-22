package org.vagabond.rcp.mapview.controller;

import java.util.List;
import java.util.Vector;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.view.MyConnectionRouter;
import org.vagabond.rcp.mapview.view.MapGraphView;
import org.vagabond.rcp.util.PluginLogProvider;

import org.apache.log4j.Logger;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class GraphEditPart extends AbstractGraphicalEditPart {

	static Logger log = PluginLogProvider.getInstance().getLogger(GraphEditPart.class);
	
	public GraphEditPart(Graph graph) {
		setModel(graph);
	}
	
	@Override
	/**
	* Create root figure. Use standard FreeformLayer figure here.
	*/
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer();
		layer.setOpaque(true);
		layer.setLayoutManager(new FreeformLayout()); 
		layer.setBorder(new LineBorder(1));
		
		ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
		connLayer.setConnectionRouter(new MyConnectionRouter(layer));
		
//		layer.addMouseListener(new MouseListener() {
//
//			@Override
//			public void mousePressed(MouseEvent me) {
//				// TODO Auto-generated method stub
//				ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
//				connLayer.setVisible(false);
//			}
//
//			@Override
//			public void mouseReleased(MouseEvent me) {
//				// TODO Auto-generated method stub
//				ConnectionLayer connLayer = (ConnectionLayer)getLayer(LayerConstants.CONNECTION_LAYER);
//				connLayer.setVisible(true);
//				
//			}
//
//			@Override
//			public void mouseDoubleClicked(MouseEvent me) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//		});
		
		return layer;
	}

	//Return the lower elements of the model
	protected List getModelChildren() {
		return ((Graph)getModel()).getChildren();
	}
	
	@Override
	protected void createEditPolicies() {
	// Not editing, so keep empty...
	}
	
	public void setLayoutConstraints () {
		Graph graphModel = (Graph) this.getModel();
		Vector<RelationNodeEditPart> sourceRelEdits;
		Vector<RelationNodeEditPart> targetRelEdits;
		Vector<MappingNodeEditPart> mappingEdits;
		int curX;
		
		sourceRelEdits = new Vector<RelationNodeEditPart>();
		targetRelEdits = new Vector<RelationNodeEditPart>();
		mappingEdits = new Vector<MappingNodeEditPart>();
		
		for(Object child: children) {
			if (child instanceof RelationNodeEditPart) {
				RelationNodeEditPart childRel = (RelationNodeEditPart) child;
				RelationGraphNode modelElem = (RelationGraphNode) childRel.getModel();
				if (graphModel.isSourceRelation(modelElem))
					sourceRelEdits.add(childRel);
				else
					targetRelEdits.add(childRel);
			} else if (child instanceof MappingNodeEditPart) {
				mappingEdits.add((MappingNodeEditPart)child);
			}
		}
		
		curX = layoutSourceRelations (sourceRelEdits);
		curX = layoutMappings(curX, mappingEdits);
		curX = layoutTargetRelations (curX, targetRelEdits);
		
		
	}
	
	private int layoutSourceRelations (Vector<RelationNodeEditPart> sourceRels) {
		int maxWidth = 0, height, xPos, yPos, yGap;
		
		xPos = 10; yPos = 10; yGap = 20; height = 14;
		
		for(RelationNodeEditPart relEdit: sourceRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			Rectangle nodeBounds = relEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1);
			maxWidth = Math.max(maxWidth, nodeBounds.width);
			
			this.setLayoutConstraint(relEdit, relEdit.getFigure(), newBounds);
			log.debug ("replace bounds " + nodeBounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
			yPos = yPos + yGap + nodeBounds.height;
//			relEdit.getFigure().repaint();
		}
		
		return maxWidth;
	}
	
	private int layoutTargetRelations (int curX, Vector<RelationNodeEditPart> targetRels) {
		int maxWidth, height, xPos, yPos, yGap;
		
		xPos = curX + 50;
		yPos = 10;
		yGap = 20;
		maxWidth = 0; height = 14;
		
		for(RelationNodeEditPart relEdit: targetRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			Rectangle bounds = relEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1);
			
			this.setLayoutConstraint(relEdit, relEdit.getFigure(), 
					newBounds);
//			relEdit.getFigure().setBounds(new Rectangle(new Point(xPos, yPos), 
//					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + bounds.height;
			
			log.debug ("replace bounds " + bounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
//			relEdit.getFigure().repaint();
		}
		
		return maxWidth + curX;
	}
	
	private int layoutMappings (int curX, Vector<MappingNodeEditPart> mappings) {
		int maxWidth, height, xPos, yPos, yGap;
		
		xPos = curX + 50 ; yPos = 70; yGap = 20;
		maxWidth = 0; height = 14;
		
		for(MappingNodeEditPart mapEdit: mappings) {
			MappingGraphNode node = (MappingGraphNode) mapEdit.getModel();
			Rectangle bounds = mapEdit.getFigure().getBounds();
			Rectangle newBounds = new Rectangle(xPos, yPos, -1, -1); 
			this.setLayoutConstraint(mapEdit, mapEdit.getFigure(), newBounds);
			
			log.debug ("replace bounds " + bounds.toString() + " for " 
					+ node.getName() + " are " + newBounds.toString());
			maxWidth = Math.max(maxWidth, bounds.width);
//			mapEdit.getFigure().setBounds(new Rectangle(new Point(xPos, yPos), 
//					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + bounds.height;
//			mapEdit.getFigure().repaint();
		}
		
		return maxWidth + curX + 50;
	}

}