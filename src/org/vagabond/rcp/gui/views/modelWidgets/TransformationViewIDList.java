package org.vagabond.rcp.gui.views.modelWidgets;

import java.util.Collection;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.TransformationType;

public class TransformationViewIDList extends ModelIdList {

	public TransformationViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected String getTypeString() {
		return "Transformations:";
	}

	@Override
	public ModelType getType() {
		return ModelType.Transformation;
	}

	@Override
	protected Label getLabel(String id) {
		return getColoredLable(id, SWTResourceManager.getColor(new RGB(0,0,255)));
	}

	public void adaptLabels(Collection<TransformationType> trans) {
		adaptLabels(trans.toArray(new TransformationType[] {}));
	}
	
	public void adaptLabels(TransformationType[] trans) {
		String[] ids = new String[trans.length];
		
		for(int i = 0; i < ids.length; i++)
			ids[i] = trans[i].getId();
		
		adaptLabels(ids);
	}
}
