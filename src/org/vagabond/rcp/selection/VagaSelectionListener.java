package org.vagabond.rcp.selection;

import java.util.Set;

import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;

public interface VagaSelectionListener {

	// act on event
	public void event (VagaSelectionEvent e);
	// the listener declares in which types of selection events he is interested in
	public Set<ModelType> interestedIn ();
}
