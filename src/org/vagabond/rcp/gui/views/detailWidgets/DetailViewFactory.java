package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.widgets.Composite;

public interface DetailViewFactory {

	public IModelElementDetailView createView (Composite parent);	
}
