package org.vagabond.rcp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.controller.DBViewActionGroup;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;

public class ProvenanceView extends GenericTableView {
	public static final String ID = "org.vagabond.rcp.gui.views.provview";
	protected ResultSetViewer provViewer;
	
	public ProvenanceView() {
		
	}
	
	public static ProvenanceView getInstance() {
		return (ProvenanceView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	public void createPartControl(Composite parent) {
        this.tabs = new TabFolder(parent, SWT.NONE);
		this.tabs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				fireSelectionChangedEvent();
			}
		});
		
		this.actionGroup = new DBViewActionGroup(this);
	}
	
	public void showProvenance(SQLResultSetResults results) {
		if (this.provViewer != null)
			this.provViewer.dispose();
		this.provViewer = new ResultSetViewer(this, results);
		
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ProvenanceView.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}
