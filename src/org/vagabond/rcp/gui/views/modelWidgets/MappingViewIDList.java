package org.vagabond.rcp.gui.views.modelWidgets;

import java.util.Collection;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.StringRefType;

public class MappingViewIDList extends ModelIdList {
	
	public MappingViewIDList(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Label getLabel(String id) {
		return getColoredLable(id, SWTResourceManager.getColor(new RGB(255,0,0)));
	}

	@Override
	protected String getTypeString() {
		return "Mappings:";
	}

	public void adaptLabels(Collection<MappingType> elements) {
		adaptLabels(elements.toArray(new MappingType[] {}));
	}
	
	public void adaptLabels(MappingType[] elements) {
		String[] ids = new String[elements.length];
		
		for(int i = 0; i < elements.length; i++)
			ids[i] = elements[i].getId();
		
		adaptLabels(ids);
	}

	@Override
	public ModelType getType() {
		return ModelType.Mapping;
	}

	public void adaptLabels(StringRefType[] mappingArray) {
		String[] ids = new String[mappingArray.length];
		
		for(int i = 0; i < ids.length; i++) {
			StringRefType m = mappingArray[i];
			ids[i] = m.getRef();
		}
			
		adaptLabels(ids);
	}

}
