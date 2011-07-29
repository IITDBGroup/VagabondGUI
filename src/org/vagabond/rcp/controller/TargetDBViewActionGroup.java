package org.vagabond.rcp.controller;

import java.sql.ResultSet;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.vagabond.explanation.generation.prov.SourceProvParser;
import org.vagabond.explanation.marker.TupleMarker;
import org.vagabond.rcp.gui.views.SourceDBView;
import org.vagabond.util.ConnectionManager;

import com.quantum.Messages;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;
import com.quantum.view.tableview.TableView;
import com.quantum.view.tableview.TableViewActionGroup;
import com.quantum.wizards.DeleteRowPage;
import com.quantum.wizards.SQLRowWizard;

public class TargetDBViewActionGroup extends TableViewActionGroup {
	private final TableView tableView;
	
	class ShowProvenanceAction extends Action {
		public ShowProvenanceAction() {
			setText(Messages.getString("tableview.provenance"));
		}

		public void run() {
			SQLResultSetResults resultSet = getSelectedSQLResults();
			IStructuredSelection selection = getTableRowSelection();
			
			if (resultSet == null || resultSet.isMetaData() || selection.size() < 1) {
				MessageDialog.openInformation(
						tableView.getSite().getShell(),"Operation not allowed","Please select at least 1 row to view provenance.");
				return;
			}
			
			SourceDBView.getInstance().resetSelections();
			
			for (Iterator<SQLResultSetResults.Row> i = selection.iterator(); i.hasNext();) {
				SQLResultSetResults.Row r = i.next();
				
				try {
					SourceProvParser parser = selectProvenance(resultSet.getName(), (String)r.get(1));
					SourceDBView.getInstance().highlightProvenance(parser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	
	private ShowProvenanceAction showProvenanceAction;

	public TargetDBViewActionGroup(TableView tableView) {
		super(tableView);
		this.tableView = tableView;
		this.showProvenanceAction = new ShowProvenanceAction();
	}

	private SQLResultSetResults getSelectedSQLResults() {
		return this.tableView.getSelectedResultSet();
	}
	
	private SQLResultSetResults.Row getSelection() {
		return this.tableView.getSelectedFirstRow();
	}
	
	private IStructuredSelection getTableRowSelection() {
		return this.tableView.getTableRowSelection();
	}
	
	public void fillContextMenu(IMenuManager menuManager) {
		menuManager.add(this.showProvenanceAction);
		menuManager.add(new Separator());
		super.fillContextMenu(menuManager);
	}

	public SourceProvParser selectProvenance(String tableName, String tid) throws Exception {
		String query = "SELECT PROVENANCE * FROM target."+
		tableName+" WHERE tid='"+ tid +"'";
		System.out.println(query);
		ResultSet rs = ConnectionManager.getInstance().execQuery(query);
		SourceProvParser parser = new SourceProvParser(rs);

		return parser;
	}
}
