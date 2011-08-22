package org.vagabond.rcp.mapview.model;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.MappingGraph;
import org.vagabond.rcp.mapview.view.MapGraphView;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.xmlmodel.AttrDefType;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.CorrespondencesType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.MappingsType;
import org.vagabond.xmlmodel.RelAtomType;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

public class ContentProvider {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			ContentProvider.class);
	
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
	
	public Graph generateGraph() throws Exception {
		MapScenarioHolder holder = MapScenarioHolder.getInstance();
		
		log.debug("creating Graph");
		if (holder.getDocument() != null) {
			graph = new Graph();
			log.debug("MapScenarioHolder has document");
			generateRelations(holder, graph.getSourceSchema());
			generateRelations(holder, graph.getTargetSchema());
//			generateTargetRelations(holder);
			generateMappings(holder);
			generateCorrespondences(holder);
			generateMapConnections(holder);
		}
		
		return graph;
	}
	
//	private void generateSourceRelations(MapScenarioHolder holder) {
//		SchemaType source = holder.getScenario().getSchemas().getSourceSchema();
//		int width, height, xPos, yPos, yGap;
//		String relName, attrName;
//		RelationGraphNode node;
//		AttributeGraphNode attrNode;
//		
//		xPos = 10; yPos = 10; yGap = 20;
//		width = 100; height = 14;
//		
//		for (RelationType rel : source.getRelationArray()) {
//			relName = "source." + rel.getName();
//			node = newRelation(relName);
//
//			for (AttrDefType attr : rel.getAttrArray()) {
//				attrName = attr.getName();
//				attrNode = newAttribute(attrName, node);
//				node.addAttribute(attrName, attrNode);
//			}
//			
//			node.setConstraint(new Rectangle(new Point(xPos, yPos), 
//					new Dimension(width, height*(node.getNumAttributes()+1))));
//			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
//			this.graph.addSourceRelation(relName, node);
//		}
//	}
	
	private void generateRelations (MapScenarioHolder holder, Schema schema) {
		SchemaType mapSchema;
		int width, height, xPos, yPos, yGap;
		String relName, attrName;
		RelationGraphNode node;
		AttributeGraphNode attrNode;
		String schemaPrefix;
		// get schema from XML model
		mapSchema = (schema.isSource()) ? 
				holder.getScenario().getSchemas().getSourceSchema() :
					holder.getScenario().getSchemas().getTargetSchema()	;
		schemaPrefix = (schema.isSource()) ?
				"source." : "target.";
				
		xPos = 10; yPos = 10; yGap = 20;
		width = 100; height = 14;
		
		for (RelationType rel : mapSchema.getRelationArray()) {
			relName = schemaPrefix + rel.getName();
			node = newRelation(relName);
			log.debug("create relation " + relName);
			
			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				attrNode = newAttribute(attrName, node);
				node.addAttribute(attrName, attrNode);
			}
			
//			node.setConstraint(new Rectangle(xPos, yPos, -1,-1));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
			this.graph.addRelation(node, schema);
		}		
	}
	
//	private void generateTargetRelations(MapScenarioHolder holder) {
//		SchemaType target = holder.getScenario().getSchemas().getTargetSchema();
//		int width, height, xPos, yPos, yGap;
//		String relName, attrName;
//		RelationGraphNode node;
//		AttributeGraphNode attrNode;
//		
//		xPos = View.getInstance().getViewer().getControl().getBounds().width - 130;
//		yPos = 10;
//		yGap = 20;
//		width = 100; height = 14;
//		
//		for (RelationType rel : target.getRelationArray()) {
//			relName = "target." + rel.getName();
//			node = newRelation(relName);
//			
//			for (AttrDefType attr : rel.getAttrArray()) {
//				attrName = attr.getName();
//				attrNode = newAttribute(attrName, node);
//				node.addAttribute(attrName, attrNode);
//			}
//			
//			node.setConstraint(new Rectangle(new Point(xPos, yPos), 
//					new Dimension(width, height*(node.getNumAttributes()+1))));
//			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
//			this.graph.addTargetRelation(relName, node);
//		}
//	}
	
	private void generateMappings(MapScenarioHolder holder) throws Exception {
		MappingsType maps = holder.getScenario().getMappings();
		int width, height, xPos, yPos, yGap;
		String mapName, varName;
		MappingGraphNode node;
		AttributeGraphNode attrNode;
		MappingGraph mapGraph;
		
//		xPos = MapGraphView.getInstance().getViewer().getControl().getBounds().width/2 - 55; 
		xPos = 30;
		yPos = 70; yGap = 20;
		width = 100; height = 14;
		
		for (MappingType map: maps.getMappingArray()) {
 			mapName = map.getId();
			node = newMapping(mapName);
			
			log.debug("generate mapping " + mapName);
			
			mapGraph = holder.getGraphForMapping(map);

			for (String var : mapGraph.getVarsOrdered()) {
				log.debug("\tadd var:" + var);
 				varName = var;
				attrNode = newAttribute(varName, node);
				node.addAttribute(varName, attrNode);
			}
			
//			node.setConstraint(new Rectangle(new Point(xPos, yPos), 
//					new Dimension(width, height*(node.getNumAttributes()+1))));
			yPos = yPos + yGap + height*(node.getNumAttributes()+1);
			this.graph.addMapping(mapName, node);
		}
	}
	
	private void generateCorrespondences(MapScenarioHolder holder) {
		CorrespondencesType corrs = holder.getScenario().getCorrespondences();
		AttributeGraphNode sourceN, targetN;
		String corrName, sourceRel, sourceAttr, targetRel, targetAttr;

		for (CorrespondenceType corr : corrs.getCorrespondenceArray()) {
			corrName = corr.getId();
			
			sourceRel = "source." + corr.getFrom().getTableref();
			sourceAttr = corr.getFrom().getAttrArray(0);
			sourceN = (AttributeGraphNode) this.graph.getSourceRelation(sourceRel).getAttribute(sourceAttr);
			targetRel = "target." + corr.getTo().getTableref();
			targetAttr = corr.getTo().getAttrArray(0);
			targetN = (AttributeGraphNode) this.graph.getTargetRelation(targetRel).getAttribute(targetAttr);
			
			newCorrespondence(sourceN, targetN, corrName);
		}
	}
	
	private void generateMapConnections(MapScenarioHolder holder) {
		MappingsType maps = holder.getScenario().getMappings();
		SchemaType source = holder.getScenario().getSchemas().getSourceSchema();
		SchemaType target = holder.getScenario().getSchemas().getTargetSchema();
		String mapName;
		int i=0,j=0;
		
		AttributeGraphNode sourceN, targetN;
		String connName = "", sourceRel, sourceAttr, targetRel, targetAttr;
		
		for (MappingType map: maps.getMappingArray()) {
			mapName = map.getId();
			log.debug("create connections for mapping " + mapName);
			for (RelAtomType atom : map.getForeach().getAtomArray()) {
				sourceRel = "source." + atom.getTableref();
				log.debug("handling atom " + atom.getTableref());
				for (String var : atom.getVarArray()) {
					log.debug("\tcreate connections for var <" + var + ">");
					
					sourceAttr = this.graph.getSourceRelation(sourceRel).getAttributes().get(j).getName();
//					sourceAttr = source.getRelationArray(i).getAttrArray(j).getName();
					sourceN = (AttributeGraphNode) this.graph.getSourceRelation(sourceRel).getAttribute(sourceAttr);
					
					targetRel = mapName;
					targetAttr = var;
					targetN = (AttributeGraphNode) this.graph.getMapping(targetRel).getAttribute(targetAttr);
					
					newMapConnection(sourceN, targetN);
					
					j++;
				}
				j=0;
				i++;
			}
			i=0;
			
			for (RelAtomType atom : map.getExists().getAtomArray()) {
				targetRel = "target." + atom.getTableref();
				log.debug("handling atom " + atom.getTableref());
				for (String var : atom.getVarArray()) {
					log.debug("\tcreate connections for var <" + var + ">");
					
					targetAttr = this.graph.getTargetRelation(targetRel).getAttributes().get(j).getName();
					targetN = (AttributeGraphNode) this.graph.getTargetRelation(targetRel).getAttribute(targetAttr);
					
					sourceRel = mapName;
					sourceAttr = var;
					sourceN = (AttributeGraphNode) this.graph.getMapping(sourceRel).getAttribute(sourceAttr);
					
					newMapConnection(sourceN, targetN);
					
					j++;
				}
				j=0;
				i++;
			}
		}
		
	}
	
	// Create a model factory
	private RelationGraphNode newRelation(String name) {
		RelationGraphNode result = new RelationGraphNode(name);
		return result;
	}
	
	private AttributeGraphNode newAttribute(String name, Node parent) {
		AttributeGraphNode result = new AttributeGraphNode(name, parent);
		return result;
	}
	
	private MappingGraphNode newMapping(String name) {
		MappingGraphNode result = new MappingGraphNode(name);
		return result;
	}
	
	private Connection newMapConnection (Node source, Node target) {
		Connection result = new MapConnection();
		
		result.setSource((AttributeGraphNode) source);
		result.setTarget((AttributeGraphNode) target);
		
		log.debug("created map connection " + source.getName() 
				+ " to " + target.getName());
		
		return result;
	}
	
	private Connection newCorrespondence(Node source, Node target, String name) {
		Connection result = new Correspondence();
		
		result.setSource((AttributeGraphNode) source);
		result.setTarget((AttributeGraphNode) target);
		result.setName(name);
		log.debug("generated correspondence: " + result.getName() + " from "
				+ source.getName() + " to " + target.getName());
		
		return result;
	}
}
