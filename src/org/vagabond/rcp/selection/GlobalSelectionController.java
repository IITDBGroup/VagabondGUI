package org.vagabond.rcp.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.PluginLogProvider;

/**
 * 
 * @author lord_pretzel
 *
 */
public class GlobalSelectionController {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			GlobalSelectionController.class);
	
	private static GlobalSelectionController inst = new GlobalSelectionController();
	
	private VagaSelectionSequence seq;
	private Map<ModelType, List<VagaSelectionListener>> listeners;
	
	/**
	 * Create the single GlobalSelectionController with an empty selection sequence
	 */
	
	private GlobalSelectionController () {
		seq = (VagaSelectionSequence) VagaSelectionSequence.EMPTY_SELECTION_SEQ.clone();
		listeners = new HashMap<ModelType, List<VagaSelectionListener>> ();
		for(ModelType type: ModelType.values())
			listeners.put(type, new ArrayList<VagaSelectionListener> ());
	}

	/**
	 * 
	 * @param e
	 */
	
	public static void fireModelSelection (VagaSelectionEvent e) {
		log.debug("got selection event: " + e.toString());
		log.debug("curren sequence is: " + inst.seq.toString());
		//TODO validateEvent();
		inst.informListeners(e); //TODO more complex, parts of the seq may be reduced need to tell listeneres about that
	}
	
	/**
	 * 
	 * @param e
	 */
	
	private void informListeners (VagaSelectionEvent e) {
		for(VagaSelectionListener listener: listeners.get(e.getElementType()))  {
			listener.event(e);
		}
	}
	
	/**
	 * 
	 * @param listener
	 */
	
	public static void addSelectionListener (VagaSelectionListener listener) {
		// add listener to the list of listeners for all events it is interested in
		for(ModelType interest: listener.interestedIn()) {
			if (!inst.listeners.get(interest).contains(listener))
				inst.listeners.get(interest).add(listener);
		}
	}
	
}
