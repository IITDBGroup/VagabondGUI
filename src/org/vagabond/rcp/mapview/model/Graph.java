package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Graph { 
	private Map<String, Node> sourceRelations; 
	private Map<String, Node> targetRelations; 
	private String name; 
	
	public Graph() {
		sourceRelations = new HashMap<String, Node>();
		targetRelations = new HashMap<String, Node>();
	}
	
	public Graph(String name){
		sourceRelations = new HashMap<String, Node>();
		targetRelations = new HashMap<String, Node>();
		this.name = name; 
	}
	
	public List getRelations() {
		List result = new ArrayList();
		result.addAll(getSourceRelations());
		result.addAll(getTargetRelations());

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
	
	public String getName() {
		return name; 
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