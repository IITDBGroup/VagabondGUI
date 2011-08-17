package org.vagabond.rcp.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.vagabond.rcp.gui.views.ExplRankView;


public class PrevExplAction extends SelectionListenerAction implements PropertyChangeListener {
	private ExplRankView view;
	
	public PrevExplAction(ExplRankView view) {
		super("");
		this.view = view;
		setText("Previous");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
		setToolTipText("Show previous ranked explanation");
		setEnabled(false);
	}
	
	// For now, it just resets the collection iterator
	public void run() {
		view.getExplCollection().resetIter();
		view.updateView();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		setEnabled(true);
	}

}