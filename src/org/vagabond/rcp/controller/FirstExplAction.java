package org.vagabond.rcp.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.rcp.gui.views.ExplRankView;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.util.LoggerUtil;

public class FirstExplAction extends SelectionListenerAction implements PropertyChangeListener {

	static Logger log = PluginLogProvider.getInstance().getLogger(FirstExplAction.class);
	
	private ExplRankView view;
	
	protected FirstExplAction(ExplRankView view) {
		super("");
		this.view = view;
		setText("First");
		try {
			setImageDescriptor(SWTResourceManager.getImageDescriptor("verysmall_go-first.png"));
		} catch (IOException e) {
			LoggerUtil.logException(e, log);
		}
		setToolTipText("Show top ranked explanation");
		setEnabled(false);
	}
	
	// For now, it just resets the collection iterator
	public void run() {
		ExplanationCollection col = ContentProvider.getInstance().getExplCol(); 
		col.resetIter();
		
		if (col.hasNext())
			view.updateView(col.next());
		
		log.debug("at " + col.getIterPos() + " of " + col.getNumCombinations());
		
		StatusLineController.setStatus("Ranked Explanation " + col.getIterPos()
				+ " of app. " + col.getNumCombinations() 
				+ " (prefetched " + col.getNumPrefetched() + ")");
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		setEnabled(true);
	}

}
