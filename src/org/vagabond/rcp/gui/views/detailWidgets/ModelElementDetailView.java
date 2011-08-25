package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.SWTResourceManager;

public abstract class ModelElementDetailView implements 
		IModelElementDetailView {

	protected Composite comp;
	protected Group group;
	private String id;
	private boolean selected = false;
	
	public ModelElementDetailView (Composite parent, int style) {
		comp = new Composite(parent, style);
		comp.setLayout(new FillLayout());
		group = new Group(comp, SWT.NONE);
		createGui();
	}
	
	protected abstract void createGui ();
	
	public void setLayoutData (Object layoutData) {
		comp.setLayoutData(layoutData);
	}
	
	public abstract void showElem (Object data);
	public abstract void addSelectionListener();
	
	public void layout () {
		comp.layout();
	}
	
	public void dispose () {
		comp.dispose();
	}
	
	public void setSelection (boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			group.setBackground(selected ? 
					SWTResourceManager.getColor(new RGB(200, 200, 255)) : 
					SWTResourceManager.getColor("Background"));
		}
	}

	public boolean getSelection () {
		return selected;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	protected void fireSelectionEvent (ModelType type) {
		setSelection(!getSelection());
		if (getSelection()) {
			// inform global selection controller
			GlobalSelectionController
					.fireModelSelection(new VagaSelectionEvent(type, getId()));
		}
		else {
			GlobalSelectionController.fireModelSelection(
					VagaSelectionEvent.DESELECT);
		}
	}
}
