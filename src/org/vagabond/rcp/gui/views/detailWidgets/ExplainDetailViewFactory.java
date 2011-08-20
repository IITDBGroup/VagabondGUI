package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.vagabond.explanation.model.basic.CopySourceError;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.explanation.model.basic.SuperflousMappingError;

public class ExplainDetailViewFactory implements DetailViewFactory {

	public static final ExplainDetailViewFactory withExplains = 
			new ExplainDetailViewFactory(true);
	public static final ExplainDetailViewFactory withoutExplains = 
			new ExplainDetailViewFactory(false);
	
	private boolean addExplains = false;
	
	private ExplainDetailViewFactory (boolean addExplains) {
		this.addExplains = addExplains;
	}

	@Override
	public IModelElementDetailView createView(Composite parent) {
		return new ExplanationDetailView (parent, SWT.NONE, addExplains);
	}
	
}
