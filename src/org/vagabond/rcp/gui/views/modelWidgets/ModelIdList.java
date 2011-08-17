package org.vagabond.rcp.gui.views.modelWidgets;


import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;

public abstract class ModelIdList {
	
	protected Composite comp;
	protected Label typeLabel;
	private Vector<Label> labels;
	private Vector<String> ids;
	
	public ModelIdList(Composite parent, int style) {
		comp = new Composite(parent, style);
		
		labels = new Vector<Label> ();
		ids = new Vector<String> ();
		createGui(parent);
	}
	
	private void createGui (Composite parent) {
		comp.setLayout(new RowLayout());
		
		typeLabel = new Label(comp, SWT.NONE);
		//typeLabel.setFont(SWTResourceManager.getFont("BoldArial", "Arial", 12, true));
		typeLabel.setText(getTypeString());
	}
	
	public void setLayoutData (Object data) {
		comp.setLayoutData(data);
	}
	
	protected abstract String getTypeString ();
	
	public void adaptLabels (String ... ids) {
		Label curLabel;
		
		for(Label oldLabel: labels)
			oldLabel.dispose();
		labels.clear();
		this.ids.clear();
		
		// create labels for mappings
		for(String id: ids) {
			curLabel = getLabel(id);
			addListener(curLabel);
			labels.add(curLabel);
		}
		
		comp.layout();
	}
	
	protected void addListener (Label l) {
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown (MouseEvent e) {
				Label label = (Label) e.getSource();
				
				// inform global selection controller
				GlobalSelectionController
						.fireModelSelection(new VagaSelectionEvent(
								getType(), label.getText()));
			}
		});
	}
	
	public abstract ModelType getType();
	
	protected abstract Label getLabel (String id);
	
	protected Label getColoredLable(String id, Color col) {
		Label result = new Label(comp, SWT.NONE);
		result.setText(id);
		result.setBackground(col);
		
		return result;
	}
	
}
