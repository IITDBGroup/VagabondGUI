package org.vagabond.rcp.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.vagabond.explanation.generation.prov.SourceProvParser;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.gui.views.ProvenanceView;
import org.vagabond.rcp.gui.views.SourceDBView;
import org.vagabond.util.ConnectionManager;

import com.quantum.Messages;
import com.quantum.model.Bookmark;
import com.quantum.model.BookmarkCollection;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResults;
import com.quantum.view.tableview.TableView;

public class TargetDBViewActionGroup extends DBViewActionGroup {
	private final TableView tableView;
	
	class ShowProvenanceAction extends Action {
		public ShowProvenanceAction() {
			setText(Messages.getString("tableview.provenance"));
		}

		public void run() {
			SQLResultSetResults resultSet = getSelectedSQLResults();
			IStructuredSelection selection = getTableRowSelection();
			String relations = "";
			String tids = "";
			
			if (resultSet == null || resultSet.isMetaData() || selection.size() < 1) {
				MessageDialog.openInformation(
						tableView.getSite().getShell(),"Operation not allowed","Please select at least 1 row to view provenance.");
				return;
			}
			
			SourceDBView.getInstance().resetSelections();
			
			for (Iterator<SQLResultSetResults.Row> i = selection.iterator(); i.hasNext();) {
				SQLResultSetResults.Row r = i.next();
				if (relations != "" && tids != "") {
					if (!relations.contains(resultSet.getName()))
						relations = relations + ", target." + resultSet.getName();
					tids = tids + ", '" + (String)r.get(1) + "'"; 
				} else {
					relations = "target." + resultSet.getName();
					tids = "'" + (String)r.get(1) + "'";
				}
			}
			
			try {
				String query = provQuery(relations, tids);
				SourceProvParser parser = selectProvenance(query);
				SourceDBView.getInstance().highlightProvenance(parser);

				SQLResultSetResults results = provResultSet(query);
				ProvenanceView.getInstance().showProvenance(results);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
		//super.fillContextMenu(menuManager);
	}

	private String provQuery(String tableNames, String tids) {
		String query = "SELECT PROVENANCE * FROM "+
		tableNames +" WHERE tid IN ("+ tids +")";
		
		return query;
	}
	
	private SourceProvParser selectProvenance(String query) throws Exception {
		ResultSet rs = ConnectionManager.getInstance().execQuery(query);
		SourceProvParser parser = new SourceProvParser(rs);
		
		return parser;
	}
	
	private SQLResultSetResults provResultSet(String query) throws Exception {
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		Bookmark bookmark = BookmarkCollection.getInstance().find(databaseString);
		Connection c = ConnectionManager.getInstance().getConnection();
		
		SQLResults results = MultiSQLServer.getInstance().execute(bookmark, c, query);
		((SQLResultSetResults)results).setName("Provenance");
		
		return (SQLResultSetResults)results;
	}
}
