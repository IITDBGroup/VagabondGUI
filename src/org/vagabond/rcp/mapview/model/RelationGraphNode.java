package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.eclipse.draw2d.geometry.Rectangle;


public class RelationGraphNode implements Node { 
	
	private List<Node> attributes;
	private Map<String, Node> attrMap; 
	private String name; 
	private Node parent = null;
	private Rectangle constraint;
	
	public RelationGraphNode(String name) {
		this.attrMap =  new TreeMap<String, Node>();
		this.attributes = new ArrayList<Node> ();
		this.name = name; 
	}
	
	
	public void addAttribute(String name, Node attr){
		attrMap.put(name, attr); 
		attributes.add(attr);
	}
	
	public Node getAttribute(String name) {
		return attrMap.get(name);
	}
	
	public List<Node> getAttributes(){
		return attributes;
	}
	
	public int getNumAttributes() {
		return attrMap.size();
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
	
	private List sourceConnections, targetConnections;
	
	public List getSourceConnections() {
		if (sourceConnections == null)
			sourceConnections = new ArrayList();
		return sourceConnections;
	}
	
	public List getTargetConnections() {
		if (targetConnections == null)
			targetConnections = new ArrayList();
		return targetConnections;
	}
	
	
	public void addSourceConnection(Connection connection) { 
		getSourceConnections().add(connection);
	}
	
	public void addTargetConnection(Connection connection) {
		getTargetConnections().add(connection);
	}
	
	public void removeSourceConnection(Connection connection) {
		getSourceConnections().remove(connection);
	}

	public void removeTargetConnection(Connection connection) {
		getTargetConnections().remove(connection);
	}
	
	public void setConstraint(Rectangle r) {
		this.constraint = r;
	}
	
	public Rectangle getConstraint() {
		return this.constraint;
	}
	
	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public void setParent(Node node) {
		this.parent = node;
	}
}
