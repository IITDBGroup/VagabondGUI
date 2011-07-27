package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class Graph { 
	private Map<String, Node> sourceRelations; 
	private Map<String, Node> targetRelations; 
	private Map<String, Node> mappings;
	private String name; 
	
	public Graph() {
		sourceRelations = new TreeMap<String, Node>();
		targetRelations = new TreeMap<String, Node>();
		mappings = new TreeMap<String, Node>();
	}
	
	public Graph(String name){
		sourceRelations = new TreeMap<String, Node>();
		targetRelations = new TreeMap<String, Node>();
		mappings = new TreeMap<String, Node>();
		this.name = name; 
	}
	
	public List getRelations() {
		List result = new ArrayList();
		result.addAll(getSourceRelations());
		result.addAll(getTargetRelations());

		return result;
	}
	
	public List getChildren() {
		List result = new ArrayList();
		result.addAll(getSourceRelations());
		result.addAll(getTargetRelations());
		result.addAll(getMappings());
		
		return result;
	}
	
	public void addSourceRelation(String name, Node rel){
		sourceRelations.put(name, rel);
	}
	
	public List getSourceRelations() {
		return new ArrayList<Node>(sourceRelations.values()); 
	}
	
	public RelationGraphNode getSourceRelation(String name){
		return (RelationGraphNode) sourceRelations.get(name); 
	}
	
	public void addTargetRelation(String name, Node rel){
		targetRelations.put(name, rel);
	}
	
	public List getTargetRelations(){
		return new ArrayList<Node>(targetRelations.values()); 
	}
	
	public RelationGraphNode getTargetRelation(String name){
		return (RelationGraphNode) targetRelations.get(name); 
	}
	
	public void addMapping(String name, Node rel){
		mappings.put(name, rel);
	}
	
	public List getMappings() {
		return new ArrayList<Node>(mappings.values()); 
	}
	
	public MappingGraphNode getMapping(String name){
		return (MappingGraphNode) mappings.get(name); 
	}
	
	public String getName() {
		return name; 
	}
	
	public boolean isSourceRelation (RelationGraphNode rel) {
		return sourceRelations.containsValue(rel);
	}

	public boolean isTargetRelation(RelationGraphNode rel) {
		return targetRelations.containsValue(rel);
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