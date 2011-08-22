package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Graph { 
	private Map<String, MappingGraphNode> mappings;
	private List<Node> children;
	private Schema source;
	private Schema target;
	private List<MappingGraphNode> maps;
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
		mappings = new TreeMap<String, MappingGraphNode>();
		children = new ArrayList<Node> ();
		maps = new ArrayList<MappingGraphNode> ();
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
	
	public List getMappings() {
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