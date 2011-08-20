package org.vagabond.rcp.selection;

import java.util.HashSet;

import org.vagabond.explanation.marker.IMarkerSet;

public class RelationSideEffectEvent extends VagaSelectionEvent {

	private String rel;
	private IMarkerSet markers;
	private boolean source;
	
	public RelationSideEffectEvent(boolean limitScope) {
		super(limitScope);
		this.elementType = ModelType.SourceRelation;
	}

	public RelationSideEffectEvent (boolean limitScope, boolean source, 
			String rel, IMarkerSet markers) {
		super(limitScope);
		this.elementIds = new HashSet<String> ();
		this.elementIds.add(rel);
		this.rel = rel;
		this.markers = markers;
		this.source = source;
		this.elementType = source ? ModelType.SourceRelation : 
			ModelType.TargetRelation;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public IMarkerSet getMarkers() {
		return markers;
	}

	public void setMarkers(IMarkerSet markers) {
		this.markers = markers;
	}
	
	public boolean isSource() {
		return source;
	}

	public void setSource(boolean source) {
		this.source = source;
	}

	@Override
	public String toString () {
		StringBuffer buf;
		
		if (toString != null)
			return toString;
		
		buf = new StringBuffer();
		buf.append("EVENT(" + elementType.toString() + ")[");
		buf.append(rel + "| ");
		buf.append(markers.toString());
		
		toString = buf.toString();
		return toString;
	}	
	
	
}
