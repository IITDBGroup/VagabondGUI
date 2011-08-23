package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.List;

public class AttributeGraphNode implements Node {
	private List<Connection> sourceConnections, targetConnections;
	private String name;
	private Node parent;
	private boolean isPK;
	
	public AttributeGraphNode(String name, Node parent){ 
		this.name = name;
		this.parent = parent;
		this.isPK = false;
	}
	
	public List<Connection> getSourceConnections() {
		if (sourceConnections == null)
			sourceConnections = new ArrayList<Connection>();
		return sourceConnections;
	}
	
	public List<Connection> getTargetConnections() {
		if (targetConnections == null)
			targetConnections = new ArrayList<Connection>();
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

	public String getName() {
		return name; 
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public void setParent(Node node) {
		this.parent = node;
	}
	
	public String toString () {
		StringBuilder result = new StringBuilder ();
		
		result.append("Attr(");
		result.append(getName());
		result.append(')');
		
		return result.toString();
	}

	public boolean isPK() {
		return isPK;
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}
}	