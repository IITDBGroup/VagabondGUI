package org.vagabond.rcp.gui.views.detailWidgets;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.vagabond.rcp.util.SWTResourceManager;

public class DetailViewList<T> {

	private List<IModelElementDetailView> views;
	private DetailViewFactory fac;
	private ScrolledComposite sc;
	private Composite child;
	private int curSelection = -1;
	
	public DetailViewList (Composite parent, DetailViewFactory fac) {
		this.fac = fac;
		views = new LinkedList<IModelElementDetailView> ();
		createGui(parent);
	}
	
	private void createGui (Composite parent) {
		// Create the ScrolledComposite to scroll horizontally and vertically
	    sc = new ScrolledComposite(parent, SWT.H_SCROLL
	        | SWT.V_SCROLL);

	    // Create a child composite to hold the controls
	    child = new Composite(sc, SWT.NONE);
	    child.setLayout(new GridLayout(1, false));
	    SWTResourceManager.nameColor("Background", child.getBackground().getRGB());

	    // Set the child as the scrolled content of the ScrolledComposite
	    sc.setContent(child);
	    sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	}
	
	public void updateModel (Collection<T> elements) {
		int i = 0;
		adaptListLength(elements.size());
		
		for(T element: elements) {
			IModelElementDetailView view = views.get(i++);
			view.showElem(element);
			view.addSelectionListener();
		}
		sc.setMinSize(child.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		layout();
	}
	
	public void updateModel (T[] elements) {
		adaptListLength(elements.length);
		
		for(int i = 0; i < elements.length; i++) {
			IModelElementDetailView view = views.get(i);
			view.showElem(elements[i]);
			view.addSelectionListener();
		}
		curSelection = -1;
		child.layout(true, true);
		sc.setMinSize(child.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	private void adaptListLength (int size) {
		while (size > views.size()) {
			IModelElementDetailView newView = fac.createView(child);
			newView.setLayoutData(getGridData());
			newView.setSelection(false);
			views.add(newView);
		}
		while (size < views.size()) {
			views.get(0).setSelection(false);
			views.get(0).dispose();
			views.remove(0);
		}
	}
	
	private GridData getGridData () {
		GridData result = new GridData();
		
		result.grabExcessHorizontalSpace = true;
		result.horizontalAlignment = GridData.FILL;
		
		return result;
	}
	
	public void clearSelection () {
		if (curSelection != -1) {
			views.get(curSelection).setSelection(false);
		}
		curSelection = -1;
	}
	
	public void selectElement (int pos) {
		if (curSelection != -1) {
			views.get(curSelection).setSelection(false);
		}
		curSelection = pos;
		views.get(pos).setSelection(true);
	}
	
	public void selectElement (String id) {
		for(int i = 0; i < views.size(); i++)
			if (views.get(i).getId().equals(id))
				selectElement(i);
		//TODO throw
	}
	
	public void layout() {
		sc.layout(true, true);
	}
	
	public void setLayoutData(Object layoutData) {
		sc.setLayoutData(layoutData);
	}

	public void setFocus() {
		sc.setFocus();
	}
	
}
