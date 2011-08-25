package org.vagabond.rcp.selection;

import java.util.Collection;
import java.util.HashSet;

import org.vagabond.util.LoggerUtil;

public class VagaSelectionEvent {

	public enum ModelType {
		SourceRelation,
		TargetRelation,
		Correspondence,
		Mapping,
		Transformation,
		Explanation,
		None
	}
	
	// single element used to indicate reset all selections
	public static VagaSelectionEvent DESELECT = new VagaSelectionEvent(
			ModelType.None, false);
	
	protected ModelType elementType;
	protected Collection<String> elementIds;
	protected String toString = null;
	protected boolean limitScope;
	
	public VagaSelectionEvent (boolean limitedScope) {
		this.limitScope = limitedScope;
	}
	
	public VagaSelectionEvent (ModelType elementType, boolean limitScope,
			String ... elementIds) {
		this.elementIds = new HashSet<String> ();
		this.elementType = elementType;
		this.limitScope = limitScope;
		
		for(String id: elementIds)
			this.elementIds.add(id);
	}
	
	public VagaSelectionEvent (ModelType elementType, String element) {
		this.elementIds = new HashSet<String> ();
		elementIds.add(element);
		this.elementType = elementType;
		this.limitScope = false;
	}
	
	public VagaSelectionEvent (VagaSelectionEvent e) {
		this.elementIds = new HashSet<String> ();
		this.elementType = e.elementType;
		
		for(String id: e.elementIds)
			this.elementIds.add(id);
	}

	public ModelType getElementType() {
		return elementType;
	}

	public void setElementType(ModelType elementType) {
		this.elementType = elementType;
		toString = null;
	}

	public Collection<String> getElementIds() {
		return elementIds;
	}
	
	public boolean isEmpty () {
		return elementType.equals(ModelType.None) || elementIds.isEmpty();
	}
	
	public boolean isLimitScope () {
		return limitScope;
	}

	public void setElementIds(Collection<String> elementIds) {
		this.elementIds = elementIds;
		toString = null;
	}
	
	@Override
	public String toString () {
		StringBuffer buf;
		
		if (toString != null)
			return toString;
		
		buf = new StringBuffer();
		buf.append("EVENT(" + elementType.toString() + ")[");
		
		buf.append(LoggerUtil.stringColToString(elementIds) + "]");
		
		toString = buf.toString();
		return toString;
	}
	
}
