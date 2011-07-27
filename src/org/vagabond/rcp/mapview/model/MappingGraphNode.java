package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.draw2d.geometry.Rectangle;


public class MappingGraphNode implements Node { 
	private Map<String, Node> attributes; 
	private String name; 

	private Rectangle constraint;
	
	public MappingGraphNode(String name) {
		this.attributes =  new TreeMap<String, Node>();
		this.name = name; 
	}
	
	
	public void addAttribute(String name, Node attr){
		attributes.put(name, attr); 
	}
	
	public Node getAttribute(String name) {
		return attributes.get(name);
	}
	
	public List<Node> getAttributes(){
		return new ArrayList<Node>(attributes.values());
	}
	
	public int getNumAttributes() {
		return attributes.size();
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
	
	public void setConstraint(Rectangle r) {
		this.constraint = r;
	}
	
	public Rectangle getConstraint() {
		return this.constraint;
	}
}
