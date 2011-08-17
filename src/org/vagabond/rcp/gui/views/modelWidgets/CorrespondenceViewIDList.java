package org.vagabond.rcp.gui.views.modelWidgets;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.xmlmodel.CorrespondenceType;

public class CorrespondenceViewIDList extends ModelIdList {
	
	public CorrespondenceViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Correspondences:";
	}

	@Override
	protected Label getLabel(String id) {
		return getColoredLable(id, SWTResourceManager.getColor(new RGB(100,100,100)));
	}

	public void adaptLabels(CorrespondenceType[] corrs) {
		String[] ids = new String[corrs.length];
		
		for(int i = 0; i < ids.length; i++)
			ids[i] = corrs[i].getId();
		
		adaptLabels(ids);
	}

	@Override
	public ModelType getType() {
		return ModelType.Correspondence;
	}
	
}
