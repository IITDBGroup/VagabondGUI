package org.vagabond.rcp.mapview.model;

import org.apache.log4j.Logger;
import org.vagabond.rcp.util.PluginLogProvider;

public class MapConnection implements Connection {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(MapConnection.class);
	
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
		log.debug(this.toString());
		log.debug("source parent is " + getSource().getParent().getClass());
		return ((getSource().getParent() instanceof RelationGraphNode) ? false : false);
	}

	@Override
	public boolean getTargetAttachLeft() {
		// connection from rel to map?
		log.debug(this.toString());
		log.debug("target parent is " + getTarget().getParent().getClass());
		return ((getTarget().getParent() instanceof RelationGraphNode) ? true : true);
	}
	
	@Override
	public String toString () {
		StringBuilder result = new StringBuilder ();
		
		result.append("MapConn from ");
		result.append(source.toString());
		result.append(" to ");
		result.append(target.toString());
		
		return result.toString();
	}
}
