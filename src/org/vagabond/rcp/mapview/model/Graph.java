package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vagabond.xmlmodel.AttrRefType;


public class Graph { 
	private Map<String, MappingGraphNode> mappings;
	private List<MappingGraphNode> maps;
	private Map<String, Correspondence> corrMap;
	private List<Correspondence> corr;
	
	private Schema source;
	private Schema target;
	
	private List<Node> children;
	
	private String name; 
	
	public Graph() {
		init();
	}
	
	public Graph(String name){
		init();
		this.name = name; 
	}
	
	private void init() {
		source = new Schema(true);
		target = new Schema(false);
		mappings = new HashMap<String, MappingGraphNode>();
		maps = new ArrayList<MappingGraphNode> ();
		corrMap = new HashMap<String, Correspondence> ();
		corr = new ArrayList<Correspondence> (); 
		children = new ArrayList<Node> ();
	}
	
	public List<RelationGraphNode> getRelations() {
		List<RelationGraphNode> result = new ArrayList<RelationGraphNode>();
		result.addAll(getSourceRelations());
		result.addAll(getTargetRelations());

		return result;
	}
	
	public List<Node> getChildren() {
		return children;
	}
	
	public Schema getSourceSchema () {
		return source;
	}
	
	public Schema getTargetSchema () {
		return target;
	}
	
	public void addRelation (Node rel, Schema schema) {
		assert(rel instanceof RelationGraphNode);
		schema.addRel((RelationGraphNode) rel);
		children.add(rel);
	}
	
	public void addSourceRelation(String name, Node rel){
		assert(rel instanceof RelationGraphNode);
		source.addRel((RelationGraphNode) rel);
		children.add(rel);
	}
	
	public List<RelationGraphNode> getSourceRelations() {
		return source.getRels(); 
	}
	
	public RelationGraphNode getSourceRelation(String name){
		return source.getRel(name); 
	}
	
	public void addTargetRelation(String name, Node rel){
		assert(rel instanceof RelationGraphNode);
		target.addRel((RelationGraphNode) rel);
		children.add(rel);
	}
	
	public List<RelationGraphNode> getTargetRelations(){
		return target.getRels(); 
	}
	
	public RelationGraphNode getTargetRelation(String name){
		return target.getRel(name); 
	}
	
	public void addMapping(String name, Node rel){
		assert(rel instanceof MappingGraphNode);
		MappingGraphNode map = (MappingGraphNode) rel;
		mappings.put(name, map);
		maps.add(map);
		children.add(map);
	}
	
	public void addCorrepondence (Correspondence corr) {
		this.corr.add(corr);
		corrMap.put(corr.getName().toUpperCase(), corr);
	}
	
	public Correspondence getCorrespondence (String id) {
		return corrMap.get(id);
	}
	
	public List<MappingGraphNode> getMappings() {
		return maps; 
	}
	
	public MappingGraphNode getMapping(String name){
		return mappings.get(name); 
	}
	
	public String getName() {
		return name; 
	}
	
	public boolean isSourceRelation (RelationGraphNode rel) {
		return source.hasRel(rel);
	}

	public boolean isTargetRelation(RelationGraphNode rel) {
		return target.hasRel(rel);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public AttributeGraphNode getAttr (AttrRefType attrRef, boolean source) {
		RelationGraphNode rel = source ? 
				getSourceRelation(attrRef.getTableref()) :
				getTargetRelation(attrRef.getTableref());
		return rel.getAttribute(attrRef.getAttrArray()[0]);
	}
	
	public AttributeGraphNode[] getAttrs (AttrRefType attrRef, boolean source) {
		AttributeGraphNode[] result = 
			new AttributeGraphNode[attrRef.getAttrArray().length];
		String schemaPrefix = (source) ? "source." : "target.";
		String fullRelName = schemaPrefix + attrRef.getTableref();
		RelationGraphNode rel = (source ?  
				getSourceRelation(fullRelName) :
				getTargetRelation(fullRelName));
		
		for (int i = 0; i < result.length; i++) {
			result[i] = rel.getAttribute(attrRef.getAttrArray()[i]);
		}
	
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Node) || obj == null) {
			return false;
		} else {
			Node widget = (Node)obj;
			if (widget.getName() != null && name != null) {
				return name.equals(widget.getName());
			} else {
				return false;
			}
		}
	}

}