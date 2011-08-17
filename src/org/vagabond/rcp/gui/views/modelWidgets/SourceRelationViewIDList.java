package org.vagabond.rcp.gui.views.modelWidgets;

import org.eclipse.swt.widgets.Composite;

public class SourceRelationViewIDList extends RelationViewIDList {

	public SourceRelationViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Source Relations:";
	}
	
}
