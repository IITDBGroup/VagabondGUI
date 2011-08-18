package org.vagabond.rcp.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.gui.views.CorrView;
import org.vagabond.rcp.gui.views.ExplView;
import org.vagabond.rcp.gui.views.MappingsView;
import org.vagabond.rcp.gui.views.SourceDBView;
import org.vagabond.rcp.gui.views.TargetDBView;
import org.vagabond.rcp.gui.views.TransView;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

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
		inst.setViewsBasedOnType(e);
	}
	
	private void setViewsBasedOnType(VagaSelectionEvent e) {
		try {
			switch(e.getElementType()) {
			case None:
				break;
			case Mapping:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(MappingsView.ID);
				break;
			case Transformation:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(TransView.ID);
				break;
			case SourceRelation:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(SourceDBView.ID);
				SourceDBView.getInstance().setSelection(e.getElementIds().iterator().next());
				break;
			case TargetRelation:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(TargetDBView.ID);
				TargetDBView.getInstance().setSelection(e.getElementIds().iterator().next());
				break;
			case Correspondence:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(CorrView.ID);
				break;
			case Explanation:
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.showView(ExplView.ID);
				break;
			default:
				break;
			}
		} catch (Exception e1) {
			LoggerUtil.logException(e1, log);
		}
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
