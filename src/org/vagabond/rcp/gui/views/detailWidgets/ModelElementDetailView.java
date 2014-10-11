package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
		comp.setLayout(new GridLayout(1,false));
		
		group = new Group(comp, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setLayoutData(getFillData(1));
		
		createGui();
	}
	
	protected GridData getFillData (int size) {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = size;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		
		return gridData;		
	}
	
	protected GridData getGridData (int size) {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = size;
		
		return gridData;
	}
	
	protected abstract void createGui ();
	
	public void setLayoutData (Object layoutData) {
		comp.setLayoutData(layoutData);
	}
	
	public abstract void showElem (Object data);
	public abstract void addSelectionListener();
	public abstract void removeSelectionListener();
	
	public void layout () {
		group.layout(true,true);
		comp.layout(true, true);
	}
	
	public void dispose () {
		removeSelectionListener();
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
