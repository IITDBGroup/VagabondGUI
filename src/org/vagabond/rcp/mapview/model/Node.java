package org.vagabond.rcp.mapview.model;

public interface Node { 

	public void setName(String name); 
	public String getName(); 
	public Node getParent();
	public void setParent(Node node);
}