package org.vagabond.rcp.mapview.controller;

import org.apache.log4j.Logger;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.vagabond.rcp.mapview.view.SelectableFigure;
import org.vagabond.rcp.util.PluginLogProvider;

public class SelectionFeedbackPolicy extends SelectionEditPolicy {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			SelectionFeedbackPolicy.class);
	
	@Override
	protected void showSelection() {
		VagaSelectionEventProvider eventHost;
		
		log.debug("selection... " + getHost().getModel().getClass().toString());
		
		eventHost = (VagaSelectionEventProvider) getHost();
		eventHost.fireSelectionEvent(true);
	}
	
	@Override
	protected void hideSelection() {
		VagaSelectionEventProvider eventHost;
		
		log.debug("deselect... " + getHost().getModel().getClass().toString());
		
		eventHost = (VagaSelectionEventProvider) getHost();
		eventHost.fireSelectionEvent(false);		
	}
	
	

}
