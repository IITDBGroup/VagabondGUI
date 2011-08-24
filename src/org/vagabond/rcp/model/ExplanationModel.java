package org.vagabond.rcp.model;

import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.model.ExplanationCollection;


/**
 * Wrapper to store an explanation collection and connected information
 * 
 * @author lord_pretzel
 *
 */
public class ExplanationModel {

	private ExplanationCollection col;
	private IMarkerSet markers;
	
	public ExplanationModel () {
		col = null;
		markers = null;
	}
	
	public boolean hasExpl() {
		return col != null;
	}

	public ExplanationCollection getCol() {
		return col;
	}

	public void setCol(ExplanationCollection col) {
		this.col = col;
	}

	public IMarkerSet getMarkers() {
		return markers;
	}

	public void setMarkers(IMarkerSet markers) {
		this.markers = markers;
	}

	
}
