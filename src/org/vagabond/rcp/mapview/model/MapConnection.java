package org.vagabond.rcp.mapview.model;

public class MapConnection implements Connection {
	private Node source, target;
	private String name;
	
	public String getName() {
		return name; 
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Node getSource() {
		return source;
	}
	
	public void setSource(AttributeGraphNode source) {
		if (this.source != null)
			source.removeSourceConnection(this);
		this.source = source;
		source.addSourceConnection(this);
	}
	
	public Node getTarget() {
		return target;
	}
	
	public void setTarget(AttributeGraphNode target) {
		if (this.target != null)
			target.removeTargetConnection(this);
		this.target = target;
		target.addTargetConnection(this);
	}

	@Override
	public boolean getSourceAttachLeft() {
		// connection from rel to map
		return (getSource().getParent() instanceof RelationGraphNode) ? false : true;
	}

	@Override
	public boolean getTargetAttachLeft() {
		// connection from rel to map?
		return (getSource().getParent() instanceof RelationGraphNode) ? true : false;
	}
}
