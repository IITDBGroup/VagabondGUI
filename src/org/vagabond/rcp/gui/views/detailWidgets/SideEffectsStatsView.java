package org.vagabond.rcp.gui.views.detailWidgets;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;

public class SideEffectsStatsView {

	static Logger log = PluginLogProvider.getInstance().getLogger(SideEffectsStatsView.class);
	
	private Composite comp;
	private Map<String,Label> nameLabels;
	private Map<String,Label> valueLabels;
	
	public SideEffectsStatsView (Composite parent, int flags) {
		comp = new Composite (parent, flags);
		createGui();
	}
	
	private void createGui () {
		comp.setLayout(new RowLayout());
		
		nameLabels = new HashMap<String, Label> ();
		valueLabels = new HashMap<String, Label> ();
		
		createNameAndValueLabel("Source", "SourceData:");
		createNameAndValueLabel("Target", "TargetData:");
		createNameAndValueLabel("Map", "Mappings:");
		createNameAndValueLabel("Corr", "Correspondences:");
		createNameAndValueLabel("Trans", "Transformations:");
	}
	
	private void setValueLabel (String key, int value) {
		valueLabels.get(key).setText(value + "");
	}
	
	private void createNameAndValueLabel (String key, String text) {
		Label newLabel;
		
		newLabel = new Label(comp, SWT.NONE);
		newLabel.setText(text);
		newLabel.setFont(SWTResourceManager.getSystemFont(10, true));
		nameLabels.put(key, newLabel);
		
		newLabel = new Label(comp, SWT.NONE);
		newLabel.setFont(SWTResourceManager.getSystemFont(10, false));
		valueLabels.put(key, newLabel);
	}
	
	public void updateModel (IBasicExplanation expl) {
		setValueLabel("Source", expl.getSourceSideEffectSize());
		setValueLabel("Target", expl.getRealTargetSideEffectSize());
		setValueLabel("Map", expl.getMappingSideEffectSize());
		setValueLabel("Corr", expl.getCorrSideEffectSize());
		setValueLabel("Trans", expl.getTransformationSideEffectSize());
	}
	
	public void setLayoutData (Object layoutData) {
		comp.setLayoutData(layoutData);
	}
	
	public void layout() {
		comp.layout();
	}
}
