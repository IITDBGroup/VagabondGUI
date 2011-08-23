package org.vagabond.rcp.mapview.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schema implements Node {

	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	
	private boolean source;
	private List<RelationGraphNode> rels;
	private Map<String, RelationGraphNode> relNameMap;
	private Node parent = null;
	
	public Schema (boolean source) {
		this.source = source;
		rels = new ArrayList<RelationGraphNode> ();
		relNameMap = new HashMap<String, RelationGraphNode> ();
	}

	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	public List<RelationGraphNode> getRels() {
		return rels;
	}

	public void setRels(List<RelationGraphNode> rels) {
		this.rels = rels;
	}
	
	public void addRel(RelationGraphNode node) {
		node.setParent(this);
		rels.add(node);
		relNameMap.put(node.getName(), node);
	}
	
	public RelationGraphNode getRel (String name) {
		return relNameMap.get(name);
	}
	
	public boolean hasRel (RelationGraphNode rel) {
		return rels.contains(rel);
	}

	@Override
	public void setName(String name) {
		// do nothing
	}

	@Override
	public String getName() {
		if (source)
			return "source";
		return "target";
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
