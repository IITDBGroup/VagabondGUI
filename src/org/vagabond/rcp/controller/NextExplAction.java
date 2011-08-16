package org.vagabond.rcp.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.vagabond.rcp.gui.views.ExplRankView;


public class NextExplAction extends SelectionListenerAction implements PropertyChangeListener {
	private ExplRankView view;
	
	public NextExplAction(ExplRankView view) {
		super("");
		this.view = view;
		setText("Next");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		setToolTipText("Show next ranked explanation");
		setEnabled(false);
	}
	
	public void run() {
		view.updateView();
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		setEnabled(ExplRankView.getInstance().getExplCollection().hasNext());
	}

}
