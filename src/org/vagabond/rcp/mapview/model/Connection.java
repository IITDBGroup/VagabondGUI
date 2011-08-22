package org.vagabond.rcp.mapview.model;

public interface Connection {
	
	public String getName();
	
	public void setName(String name);
	
	public Node getSource();
	
	public void setSource(AttributeGraphNode source);
	
	public Node getTarget();
	
	public void setTarget(AttributeGraphNode target);
	
	public boolean getSourceAttachLeft();
	public boolean getTargetAttachLeft();
}
