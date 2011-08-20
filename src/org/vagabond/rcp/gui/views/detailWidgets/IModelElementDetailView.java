package org.vagabond.rcp.gui.views.detailWidgets;

public interface IModelElementDetailView {
	
	public abstract void showElem (Object data);
	public abstract void addSelectionListener();
	
	public void setLayoutData (Object layoutData);
	public void layout ();
	public void dispose ();

	public void setSelection (boolean selected);
	
	public String getId();
	public void setId(String id);
}
