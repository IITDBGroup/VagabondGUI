package org.vagabond.rcp.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.vagabond.rcp.gui.views.ExplRankView;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.util.LoggerUtil;


public class PrevExplAction extends SelectionListenerAction implements PropertyChangeListener {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			PrevExplAction.class);
	
	private ExplRankView view;
	
	public PrevExplAction(ExplRankView view) {
		super("");
		this.view = view;
		setText("Previous");
		try {
			setImageDescriptor(SWTResourceManager.getImageDescriptor("verysmall_go-previous.png"));
		} catch (IOException e) {
			LoggerUtil.logException(e, log);
		}
		setToolTipText("Show previous ranked explanation");
		setEnabled(false);
	}
	
	// For now, it just resets the collection iterator
	public void run() {
		if (ContentProvider.getInstance().getExplCol().hasPrevious())
			view.updateView(ContentProvider.getInstance().getExplCol().previous());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		setEnabled(ContentProvider.getInstance().getExplCol().hasPrevious());
	}

}
