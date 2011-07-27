package org.vagabond.rcp.mapview.controller;

import java.util.List;
import java.util.Vector;

import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.view.MyConnectionRouter;
import org.vagabond.rcp.mapview.view.View;

import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class GraphEditPart extends AbstractGraphicalEditPart {
	
	public GraphEditPart(Graph graph) {
		setModel(graph);
	}
	
	@Override
	/**
	* Create root figure. Use standard FreeformLayer figure here.
	*/
	protected IFigure createFigure() {
		FreeformLayer layer = new FreeformLayer(); 
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
		
		layoutSourceRelations (sourceRelEdits);
		layoutTargetRelations (targetRelEdits);
		layoutMappings(mappingEdits);
		
	}
	
	private void layoutSourceRelations (Vector<RelationNodeEditPart> sourceRels) {
		int width, height, xPos, yPos, yGap;
		
		xPos = 10; yPos = 10; yGap = 20;
		width = 100; height = 14;
		
		for(RelationNodeEditPart relEdit: sourceRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			relEdit.getFigure().setBounds(new Rectangle(new Point(xPos, yPos), 
					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
			relEdit.getFigure().repaint();
		}
	}
	
	private void layoutTargetRelations (Vector<RelationNodeEditPart> targetRels) {
		int width, height, xPos, yPos, yGap;
		
		xPos = View.getInstance().getViewer().getControl().getBounds().width - 110;
		yPos = 10;
		yGap = 20;
		width = 100; height = 14;
		
		for(RelationNodeEditPart relEdit: targetRels) {
			RelationGraphNode node = (RelationGraphNode) relEdit.getModel();
			relEdit.getFigure().setBounds(new Rectangle(new Point(xPos, yPos), 
					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
			relEdit.getFigure().repaint();
		}
	}
	
	private void layoutMappings (Vector<MappingNodeEditPart> mappings) {
		int width, height, xPos, yPos, yGap;
		
		xPos = View.getInstance().getViewer().getControl().getBounds().width/2 - 55; yPos = 70; yGap = 20;
		width = 100; height = 14;
		
		for(MappingNodeEditPart mapEdit: mappings) {
			MappingGraphNode node = (MappingGraphNode) mapEdit.getModel();
			mapEdit.getFigure().setBounds(new Rectangle(new Point(xPos, yPos), 
					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
			mapEdit.getFigure().repaint();
		}
	}

}