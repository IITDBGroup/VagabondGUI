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
	public static VagaSelectionEvent RESET_SCOPE = new VagaSelectionEvent(
			ModelType.None, true);
	
	protected ModelType elementType;
	protected Collection<String> elementIds;
	protected String toString = null;
	protected boolean limitScope;
	protected VagaSelectionEvent subEvent;
	
	public VagaSelectionEvent (boolean limitedScope) {
		this.limitScope = limitedScope;
	}
	
	public VagaSelectionEvent (ModelType elementType, boolean limitScope, 
			VagaSelectionEvent subEvent, String ... elementIds) {
		setValues (elementType, limitScope, subEvent, elementIds);
	}
	
	public VagaSelectionEvent (ModelType elementType, boolean limitScope,
			String ... elementIds) {
		setValues (elementType, limitScope, null, elementIds);
	}
	
	public VagaSelectionEvent (ModelType elementType, String element) {
		setValues(elementType, false, null, element);
	}
	
	public VagaSelectionEvent (VagaSelectionEvent e) {
		setValues (e.elementType, e.limitScope, e.subEvent, e.elementIds);
	}
	
	private void setValues (ModelType elementType, boolean limitScope,
			VagaSelectionEvent subEvent, Collection<String> elementIds) {
		this.elementIds = new HashSet<String> (elementIds);
		this.elementType = elementType;
		this.limitScope = limitScope;
		this.subEvent = subEvent;
	}
	
	private void setValues (ModelType elementType, boolean limitScope,
			VagaSelectionEvent subEvent, String ... elementIds) {
		this.elementIds = new HashSet<String> ();
		this.elementType = elementType;
		this.limitScope = limitScope;
		this.subEvent = subEvent;
		
		for(String id: elementIds)
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
		return elementType.equals(ModelType.None) && !limitScope;
	}
	
	public boolean isReset () {
		return elementType.equals(ModelType.None) && limitScope;
	}
	
	public boolean isLimitScope () {
		return limitScope;
	}
	
	public void setLimitScope (boolean newScope) {
		this.limitScope = newScope;
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

	public VagaSelectionEvent getSubEvent() {
		return subEvent;
	}

	public void setSubEvent(VagaSelectionEvent subEvent) {
		this.subEvent = subEvent;
	}

	public String toUserString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append(elementType.toString());
		buf.append(" [");
		buf.append(LoggerUtil.stringColToString(elementIds));
		buf.append(']');
		
		return buf.toString();
	}
	
}
