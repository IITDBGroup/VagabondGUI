package org.vagabond.rcp.gui.views.modelWidgets;

import org.eclipse.swt.widgets.Composite;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;

public class SourceRelationViewIDList extends RelationViewIDList {

	public SourceRelationViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Source Relations:";
	}
	
	@Override
	public ModelType getType() {
		return ModelType.SourceRelation;
	}
	
	
	
}
