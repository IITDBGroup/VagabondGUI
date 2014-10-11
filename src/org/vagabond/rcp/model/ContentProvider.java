package org.vagabond.rcp.model;

import org.apache.log4j.Logger;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.ranking.AStarExplanationRanker;
import org.vagabond.explanation.ranking.DummyRanker;
import org.vagabond.explanation.ranking.scoring.SideEffectSizeScore;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.MappingGraph;
import org.vagabond.rcp.mapview.model.AttributeGraphNode;
import org.vagabond.rcp.mapview.model.Connection;
import org.vagabond.rcp.mapview.model.Correspondence;
import org.vagabond.rcp.mapview.model.ForeignKeyConnection;
import org.vagabond.rcp.mapview.model.Graph;
import org.vagabond.rcp.mapview.model.MapConnection;
import org.vagabond.rcp.mapview.model.MappingGraphNode;
import org.vagabond.rcp.mapview.model.Node;
import org.vagabond.rcp.mapview.model.RelationGraphNode;
import org.vagabond.rcp.mapview.model.Schema;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.xmlmodel.AttrDefType;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.CorrespondencesType;
import org.vagabond.xmlmodel.ForeignKeyType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.MappingsType;
import org.vagabond.xmlmodel.RelAtomType;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

/**
 * Global container for model information, also generates the 
 * graph model.
 * 
 * @author lord_pretzel
 *
 */

public class ContentProvider {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			ContentProvider.class);
	
	private static ContentProvider instance = new ContentProvider();
	private Graph graph;
	private ExplanationModel expls;
	private boolean useRanking = false;
	
	public static ContentProvider getInstance() {
		return instance;
	}
	
	private ContentProvider() {
		graph = new Graph();
		setExpls(new ExplanationModel());
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
			generateForeignKeys(holder, true);
			generateForeignKeys(holder, false);
			generateMappings(holder);
			generateCorrespondences(holder);
			generateMapConnections(holder);
		}
		
		return graph;
	}
	
	private void generateRelations (MapScenarioHolder holder, Schema schema) {
		SchemaType mapSchema;
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
		
		for (RelationType rel : mapSchema.getRelationArray()) {
			relName = schemaPrefix + rel.getName();
			node = newRelation(relName);
			log.debug("create relation " + relName);
			
			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				attrNode = newAttribute(attrName, node);
				node.addAttribute(attrName, attrNode);
			}

			this.graph.addRelation(node, schema);
			
			// handle primary key
			if (rel.getPrimaryKey() != null) {
				for(String attr: rel.getPrimaryKey().getAttrArray()) {
					node.getAttribute(attr).setPK(true);
				}
			}
		}		
	}

	
	private void generateMappings(MapScenarioHolder holder) throws Exception {
		MappingsType maps = holder.getScenario().getMappings();
		String mapName, varName;
		MappingGraphNode node;
		AttributeGraphNode attrNode;
		MappingGraph mapGraph;
				
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
			
			this.graph.addMapping(mapName, node);
		}
	}
	
	private void generateForeignKeys (MapScenarioHolder holder, boolean source) {
		SchemaType schema = source ?
				holder.getScenario().getSchemas().getSourceSchema():
					holder.getScenario().getSchemas().getTargetSchema();
		AttributeGraphNode[] sources, targets;
		
		for(ForeignKeyType fk: schema.getForeignKeyArray()) {
			sources = graph.getAttrs(fk.getFrom(), source);
			targets = graph.getAttrs(fk.getTo(), source);
			
			for (int i = 0; i < sources.length; i++) {
				new ForeignKeyConnection(fk.getId(),sources[i], 
						targets[i]);
			}
		}
	}
	
	private void generateCorrespondences(MapScenarioHolder holder) {
		CorrespondencesType corrs = holder.getScenario().getCorrespondences();
		AttributeGraphNode sourceN, targetN;
		String corrName, sourceRel, sourceAttr, targetRel, targetAttr;

		for (CorrespondenceType corr : corrs.getCorrespondenceArray()) {
			corrName = corr.getId().toUpperCase();
			
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
		String mapName;
		int i=0,j=0;
		
		AttributeGraphNode sourceN, targetN;
		String sourceRel, sourceAttr, targetRel, targetAttr;
		
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
		Correspondence result = new Correspondence();
		
		result.setSource((AttributeGraphNode) source);
		result.setTarget((AttributeGraphNode) target);
		result.setName(name);
		log.debug("generated correspondence: " + result.getName() + " from "
				+ source.getName() + " to " + target.getName());
		graph.addCorrepondence(result);
		
		return result;
	}
	
	public ExplanationCollection getExplCol () {
		return expls.getCol();
	}
	
	public IMarkerSet getErrorMarkers () {
		return expls.getMarkers();
	}

	public ExplanationModel getExplModel () {
		return expls;
	}
	
	public void setExpls(ExplanationModel expls) {
		this.expls = expls;
	}

	public boolean isUseRanking() {
		return useRanking;
	}

	public void setUseRanking(boolean useRanking) {
		this.useRanking = useRanking;
	}
	
	public void switchRanking () {
		this.useRanking = !this.useRanking;
		log.debug("use ranking? " + this.useRanking);
		if (expls.getCol() != null)
			createRanker();
	}

	public void createRanker() {
		log.debug("create ranker");
		if (ContentProvider.getInstance().isUseRanking())
			expls.getCol().createRanker(new AStarExplanationRanker(SideEffectSizeScore.inst));
		else
			expls.getCol().createRanker(new DummyRanker());
	}
	
}
