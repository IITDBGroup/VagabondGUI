package org.vagabond.rcp.gui.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.ExplainDetailViewFactory;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.util.PluginLogProvider;

public class ExplView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.explview";

	static Logger log = PluginLogProvider.getInstance().getLogger(ExplView.class);
	
	private Combo combo;
	private DetailViewList<IBasicExplanation> viewer;

	public static ExplView getInstance() {
		return (ExplView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	public void createPartControl(Composite parent) {
		setLayout(parent);
		createViewer(parent);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, true);
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);

		viewer = new DetailViewList<IBasicExplanation> (parent, 
				ExplainDetailViewFactory.withoutExplains);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.setLayoutData(gridData);
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		combo.setLayoutData(gridData);
		
		combo.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  int selection = combo.getSelectionIndex();
		    	  viewer.updateModel(ContentProvider.getInstance().getExplCol().
		    			  getErrorExplMap().get(
		    					  getMarkerForSelection(selection)));
		        }
			});
		
	}

	protected IAttributeValueMarker getMarkerForSelection (int pos) {
		return (IAttributeValueMarker) ContentProvider.getInstance()
				.getExplCol().getErrorIdMap().get(pos);
	}
	
	public void updateView() {
		IAttributeValueMarker marker;
		ExplanationCollection col;
		
		col = ContentProvider.getInstance().getExplCol();
		
		combo.removeAll();
		
		int i = 1;
		for (int j=0; j<col.getErrorIdMap().getSize(); j++) {
			marker = (IAttributeValueMarker)col.getErrorIdMap().get(j);
			
			combo.add("E"+Integer.toString(i)+" ("+marker.getRel()+", "+
					marker.getTid()+", "+marker.getAttrName()+")", j);
			i++;
		}

		combo.select(0);
		marker = (IAttributeValueMarker)col.getErrorIdMap().get(0);
		viewer.updateModel(col.getErrorExplMap().get(marker));
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.setFocus();
	}
	
}