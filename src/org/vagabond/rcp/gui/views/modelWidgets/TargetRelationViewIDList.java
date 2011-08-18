package org.vagabond.rcp.gui.views.modelWidgets;

import org.eclipse.swt.widgets.Composite;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;

public class TargetRelationViewIDList extends RelationViewIDList {

	public TargetRelationViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Target Relations:";
	}
	
	@Override
	public ModelType getType() {
		return ModelType.TargetRelation;
	}

}
