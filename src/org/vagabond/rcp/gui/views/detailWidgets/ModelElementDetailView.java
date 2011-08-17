package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public abstract class ModelElementDetailView {

	protected Composite comp;
	protected Group group;
	private String id;
	
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
