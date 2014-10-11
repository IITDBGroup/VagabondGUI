package org.vagabond.rcp.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.vagabond.explanation.generation.prov.SourceProvParser;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.controller.TableNavHandler.SCHEMA_TYPE;
import org.vagabond.rcp.gui.views.ProvenanceView;
import org.vagabond.rcp.gui.views.SourceDBView;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.DetailSelection.*;
import org.vagabond.rcp.wizards.ExplGenPage;
import org.vagabond.rcp.wizards.ExplGenWizard;
import org.vagabond.util.ConnectionManager;
import org.vagabond.util.LoggerUtil;

import com.quantum.Messages;
import com.quantum.model.Bookmark;
import com.quantum.model.BookmarkCollection;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResults;
import com.quantum.view.tableview.TableView;

public class TargetDBViewActionGroup extends DBViewActionGroup {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(
			TargetDBViewActionGroup.class);
	private final TableView tableView;
	
	class ShowProvenanceAction extends Action {
		final Logger pLog = PluginLogProvider.getInstance().getLogger(
				ShowProvenanceAction.class);
		
		public ShowProvenanceAction() {
			setText(Messages.getString("tableview.provenance"));
		}

		@SuppressWarnings("unchecked")
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
			
			for (Iterator<SQLResultSetResults.Row> i = 
				(Iterator<SQLResultSetResults.Row>) selection.iterator(); 
				i.hasNext();) {
				SQLResultSetResults.Row r = i.next();
				if (relations != "" && tids != "") {
					if (!relations.contains(resultSet.getName()))
						relations = relations + ", target." + resultSet.getName();
					tids = tids + ", '" + r.get(1).toString() + "'"; 
				} else {
					relations = "target." + resultSet.getName();
					tids = "'" + r.get(1).toString() + "'";
				}
			}
			
			try {
				String query = provQuery(relations, tids);
				SourceProvParser parser = selectProvenance(query);
				SourceDBView.getInstance().highlightProvenance(parser);

				SQLResultSetResults results = provResultSet(query);
				ProvenanceView.getInstance().showProvenance(results);
			} catch (Exception e) {
				LoggerUtil.logException(e, pLog);
			}
		}
	};
	
	class ExplGenAction extends Action {
		public ExplGenAction() {
			setText(Messages.getString("tableview.expl"));
		}

		public void run() {
			SQLResultSetResults resultSet = getSelectedSQLResults();
			IStructuredSelection selection = getTableRowSelection();
			
			if (resultSet == null || resultSet.isMetaData() || selection.size() < 1) {
				MessageDialog.openInformation(
						tableView.getSite().getShell(),"Operation not allowed","Please select at least 1 row to view explanations.");
				return;
			}
			
			ExplGenPage page = new ExplGenPage(""); //$NON-NLS-1$
			ExplGenWizard wizard = new ExplGenWizard();
			wizard.init(Messages.getString("TableView.ExplGen"),
					page, resultSet, selection); //$NON-NLS-1$
			WizardDialog dialog =
				new WizardDialog(
					tableView.getSite().getShell(),
					wizard);	
			dialog.open();

			if (dialog.getReturnCode() == 0) {
				
			}
		}
	};
	//detailed selection
	class DetailSelection extends Action{
		public DetailSelection() {
			setText(Messages.getString("User-defined selection"));
		}
		
		public void run(){
			SQLResultSetResults rs = getSelectedSQLResults();
			String relname=rs.getName();
			
			TableNavHandler.getInstance();
			SQLResultSetResults resultSet = TableNavHandler.getResultSet(SCHEMA_TYPE.TARGET,relname);
			
			DetailSelectionPage dsp=new DetailSelectionPage("",resultSet,relname);
			DetailSelectionWizard wizard=new DetailSelectionWizard(dsp);
			WizardDialog dialog =
					new WizardDialog(
						tableView.getSite().getShell(),
						wizard);
			
				dialog.open();
		}
	};
	
	private ShowProvenanceAction showProvenanceAction;
	private ExplGenAction explGenAction;
	private DetailSelection detailSelection;

	public TargetDBViewActionGroup(TableView tableView) {
		super(tableView);
		this.tableView = tableView;
		this.showProvenanceAction = new ShowProvenanceAction();
		this.explGenAction = new ExplGenAction();
		this.detailSelection=new DetailSelection();
	}
	
	private SQLResultSetResults getSelectedSQLResults() {
		return this.tableView.getSelectedResultSet();
	}
	
//	private SQLResultSetResults.Row getSelection() {
//		return this.tableView.getSelectedFirstRow();
//	}
	
	private IStructuredSelection getTableRowSelection() {
		return this.tableView.getTableRowSelection();
	}
	
	public void fillContextMenu(IMenuManager menuManager) {
		menuManager.add(this.showProvenanceAction);
		menuManager.add(this.explGenAction);
		menuManager.add(this.detailSelection);
		menuManager.add(new Separator());
		super.fillContextMenu(menuManager);
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
