package org.vagabond.rcp.gui.views.modelWidgets;

import org.eclipse.swt.widgets.Composite;

public class TargetRelationViewIDList extends RelationViewIDList {

	public TargetRelationViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Target Relations:";
	}
}
