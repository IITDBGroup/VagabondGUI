package org.vagabond.rcp.gui.views.modelWidgets;


import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.xmlmodel.RelationType;

public class RelationViewIDList extends ModelIdList {
		
	public RelationViewIDList(Composite parent, int style) {
		super(parent, style);
	}
	
	@Override
	protected String getTypeString() {
		return "Relations:";
	}

	@Override
	protected Label getLabel(String id) {
		return getColoredLable(id, SWTResourceManager.getColor(new RGB(100,255,100)));
	}
	
	public void adaptLabels(RelationType[] rels) {
		String[] ids = new String[rels.length];
		
		for(int i = 0; i < ids.length; i++)
			ids[i] = rels[i].getName();
		
		adaptLabels(ids);
	}

	@Override
	public ModelType getType() {
		return null;
	}


	

}
