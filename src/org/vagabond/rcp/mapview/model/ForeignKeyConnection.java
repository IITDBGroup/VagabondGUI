package org.vagabond.rcp.mapview.model;

public class ForeignKeyConnection implements Connection {

	private String name;
	private AttributeGraphNode source;
	private AttributeGraphNode target;
	
	public ForeignKeyConnection (String name, AttributeGraphNode source, 
			AttributeGraphNode target) {
		this.name = name;
		setSource(source);
		setTarget(target);
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Node getSource() {
		return source;
	}

	@Override
	public void setSource(AttributeGraphNode source) {
		assert(source.getParent() instanceof RelationGraphNode);
		if (this.source != null)
			source.removeSourceConnection(this);
		this.source = source;
		source.addSourceConnection(this);
	}

	@Override
	public Node getTarget() {
		return target;
	}

	@Override
	public void setTarget(AttributeGraphNode target) {
		assert(target.getParent() instanceof RelationGraphNode);
		if (this.target != null)
			target.removeTargetConnection(this);
		this.target = target;
		target.addTargetConnection(this);
	}

	@Override
	public boolean getSourceAttachLeft() {
		RelationGraphNode sourceRel = (RelationGraphNode) source.getParent();
		return sourceRel.isSourceRel();
	}

	@Override
	public boolean getTargetAttachLeft() {
		RelationGraphNode sourceRel = (RelationGraphNode) source.getParent();
		return sourceRel.isSourceRel();
	}

}
