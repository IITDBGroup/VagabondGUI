package org.vagabond.rcp.gui.views.mapview;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.zest.core.widgets.GraphContainer;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.IContainer;

public class RelationGraphNode extends GraphContainer {	
	private Map<String,GraphNode> attrNodes;
	
	public RelationGraphNode(IContainer graph, int style, String name) {
		super(graph, style, name);
		attrNodes = new HashMap<String, GraphNode> ();
		// TODO Auto-generated constructor stub
	}
	
	public GraphNode getAttrNode (String name) {
		return attrNodes.get(name);
	}

	public void addAttrNode (String attrName, GraphNode newOne) {
		attrNodes.put(attrName, newOne);
	}
	
}
