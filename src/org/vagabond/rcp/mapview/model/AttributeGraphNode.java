package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.List;

public class AttributeGraphNode extends RelationGraphNode implements Node {
	private List sourceConnections, targetConnections;
	
	public AttributeGraphNode(String name){ 
		super(name);
	}
	
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
}	