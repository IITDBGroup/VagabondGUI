package org.vagabond.rcp.mapview.model;

import org.vagabond.rcp.mapview.model.RelationGraphNode;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.xmlmodel.AttrDefType;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.CorrespondencesType;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

public class ContentProvider {
	private static ContentProvider instance = new ContentProvider();
	private Graph graph;

	public static ContentProvider getInstance() {
		return instance;
	}
	
	private ContentProvider() {
		graph = new Graph();
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public Graph generateGraph() {
		MapScenarioHolder holder = MapScenarioHolder.getInstance();
		
		SchemaType source = holder.getScenario().getSchemas().getSourceSchema();
		SchemaType target = holder.getScenario().getSchemas().getTargetSchema();
		CorrespondencesType corrs = holder.getScenario().getCorrespondences();
		String relName, attrName;
		RelationGraphNode node;
		AttributeGraphNode attrNode;
		AttributeGraphNode sourceN, targetN;
		
		String corrName, sourceRel, sourceAttr, targetRel, targetAttr;
		int width, height, xPos, yPos, yGap;
		xPos = 10;
		yPos = 10;
		yGap = 20;
		width = 90; height = 15;
		
		for (RelationType rel : source.getRelationArray()) {
			relName = "source." + rel.getName();
			node = newRelation(xPos, yPos, width, height, relName);
			this.graph.addSourceRelation(relName, node);
			
			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				attrNode = newAttribute(0, 0, 50, 10, attrName);
				node.addAttribute(attrName, attrNode);
			}
			
			node.setConstraint(new Rectangle(new Point(xPos, yPos), new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
		}
		
		xPos = xPos + width + 90;
		yPos = 10;
		yGap = 20;
		width = 90; height = 15;
		
		for (RelationType rel : target.getRelationArray()) {
			relName = "target." + rel.getName();
			node = newRelation(xPos, yPos, width, height, relName);
			this.graph.addTargetRelation(relName, node);
			
			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				attrNode = newAttribute(0, 15, 50, 10, attrName);
				node.addAttribute(attrName, attrNode);
			}
			
			node.setConstraint(new Rectangle(new Point(xPos, yPos), new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
		}
		
		for (CorrespondenceType corr : corrs.getCorrespondenceArray()) {
			corrName = corr.getId();
			
			sourceRel = "source." + corr.getFrom().getTableref();
			sourceAttr = corr.getFrom().getAttrArray(0);
			sourceN = (AttributeGraphNode) this.graph.getSourceRelation(sourceRel).getAttribute(sourceAttr);
			targetRel = "target." + corr.getTo().getTableref();
			targetAttr = corr.getTo().getAttrArray(0);
			targetN = (AttributeGraphNode) this.graph.getTargetRelation(targetRel).getAttribute(targetAttr);
			
			newConnection(sourceN, targetN, corrName);
		}
		
		return graph;
	}
	
	private RelationGraphNode newRelation(int x, int y, int width, int height, String name) {
		RelationGraphNode result = new RelationGraphNode(name);
		//((RelationGraphNode)result).setConstraint(new Rectangle(new Point(x, y), new Dimension(width, height)));
		return result;
	}
	
	private AttributeGraphNode newAttribute(int x, int y, int width, int height, String name) {
		AttributeGraphNode result = new AttributeGraphNode(name);
		((AttributeGraphNode)result).setConstraint(new Rectangle(new Point(x, y), new Dimension(width, height)));
		return result;
	}
	
	private Connection newConnection(Node source, Node target, String name) {
		Connection result = new Connection();
		result.setSource((AttributeGraphNode) source);
		result.setTarget((AttributeGraphNode) target);
		result.setName(name);
		return result;
	}
}
