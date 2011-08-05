package org.vagabond.rcp.controller;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.vagabond.rcp.controller.TargetDBViewActionGroup.ShowProvenanceAction;

import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.TableView;
import com.quantum.view.tableview.TableViewActionGroup;

public class DBViewActionGroup extends TableViewActionGroup {
	private final TableView tableView;
	
	public DBViewActionGroup(TableView tableView) {
		super(tableView);
		this.tableView = tableView;
		this.mouseDblClk = null;
	}
	
	public void fillContextMenu(IMenuManager menuManager) {
		//super.fillContextMenu(menuManager);
	}

}
