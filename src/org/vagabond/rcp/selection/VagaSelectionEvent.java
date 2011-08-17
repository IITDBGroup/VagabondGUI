package org.vagabond.rcp.selection;

import java.util.Collection;
import java.util.HashSet;

import org.vagabond.util.CollectionUtils;
import org.vagabond.util.LoggerUtil;

public class VagaSelectionEvent {

	public enum ModelType {
		Relation,
		Correspondence,
		Mapping,
		Transformation,
		Explanation,
		None
	}
	
	// single element used to indicate reset all selections
	public static VagaSelectionEvent DESELECT = new VagaSelectionEvent(ModelType.None);
	
	private ModelType elementType;
	private Collection<String> elementIds;
	private String toString = null;
	
	public VagaSelectionEvent (ModelType elementType, String ... elementIds) {
		this.elementIds = new HashSet<String> ();
		this.elementType = elementType;
		
		for(String id: elementIds)
			this.elementIds.add(id);
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
