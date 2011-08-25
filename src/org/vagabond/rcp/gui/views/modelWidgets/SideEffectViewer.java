package org.vagabond.rcp.gui.views.modelWidgets;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;

public class SideEffectViewer {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			SideEffectViewer.class);
	
	private Composite comp;
	private Label header;
	private MappingViewIDList maps = null;
	private CorrespondenceViewIDList corrs = null;
	private TransformationViewIDList trans = null;
	private RelationSideEffectViewer sources = null;
	private RelationSideEffectViewer targets = null;
	
	
	public SideEffectViewer (Composite parent) {
		comp = new Composite(parent, SWT.NONE);
		createGui ();
	}
	
	private void createGui () {
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		comp.setLayout(layout);
		
		header = new Label(comp, SWT.NONE);
		header.setFont(SWTResourceManager.getBoldSystemFont(11));
		header.setText("SideEffects:");
		header.setLayoutData(getGridData(false, 0));
	}
	
	public void  setLayoutData (Object layoutData) {
		comp.setLayoutData(layoutData);
	}

	public void pack () {
		comp.pack();
	}
	
	public void layout() {
		comp.layout(true,true);
	}
	
	private GridData getGridData (boolean grap, int indent) {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = grap;
		gridData.horizontalAlignment = grap ? GridData.FILL : GridData.BEGINNING;
		gridData.horizontalIndent = indent;
		
		
		return gridData;
	}
	
	public void updateModel (IBasicExplanation expl) {
		disposeOldElements();
		
		if (expl.getMappingSideEffectSize() != 0) {
			maps = new MappingViewIDList(comp, SWT.NONE);
			maps.adaptLabels(expl.getMappingSideEffects());
		}
		if (expl.getTransformationSideEffectSize() != 0) {
			trans = new TransformationViewIDList(comp, SWT.NONE);
			trans.adaptLabels(expl.getTransformationSideEffects());
		}
		if (expl.getCorrSideEffectSize() != 0) {
			corrs = new CorrespondenceViewIDList(comp, SWT.NONE);
			corrs.adaptLabels(expl.getCorrespondenceSideEffects());
		}
		if (expl.getSourceSideEffectSize() != 0) {
			sources = new RelationSideEffectViewer(comp, SWT.NONE, true);
			sources.updateModel(expl);
		}
		if (expl.getTargetSideEffectSize() != 0) {
			targets = new RelationSideEffectViewer(comp, SWT.NONE, false);
			targets.updateModel(expl);
		}
		
		comp.layout();
	}
	
	private void disposeOldElements () {
		if (maps != null)
			maps.dispose();
		if (corrs != null)
			corrs.dispose();
		if (trans != null)
			trans.dispose();
		if (sources != null)
			sources.dispose();
		if (targets != null)
			targets.dispose();
	}
}
