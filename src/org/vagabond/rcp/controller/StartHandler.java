package org.vagabond.rcp.controller;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.util.ConnectionManager;

import com.quantum.actions.BookmarkSelectionUtil;
import com.quantum.model.Bookmark;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResults;

public class StartHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		try {
			connectToDB();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(shell, "Error", e.getMessage());
			return null;
		}
		MessageDialog.openInformation(shell, "Notice", "Successfully connected to database");
		
		try {
			loadSchemaFile();
		} catch (Exception e) {
			e.printStackTrace();
			MessageDialog.openInformation(shell, "Error", e.getMessage());
			return null;
		}
		MessageDialog.openInformation(shell, "Notice", "Successfully loaded schema");
		
		
		
		
		MetadataController.getInstance().updateView();
		
		return null;
	}
	
	// Connect to the database specified by the details in the preference pane
	private void connectToDB() throws Exception {
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");

		ConnectionManager.getInstance().getConnection(hostString, databaseString, usernameString, passwordString);
	}
	
	// Load the file specified in the preference pane and load the scenario to the database
	private void loadSchemaFile() throws Exception {
		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		
		MapScenarioHolder h = ModelLoader.getInstance().load(new File(filePath));
		MapScenarioHolder.getInstance().setDocument(h.getDocument());
		SchemaResolver.getInstance().setSchemas(
				h.getScenario().getSchemas().getSourceSchema(),
				h.getScenario().getSchemas().getTargetSchema());

		Connection c = ConnectionManager.getInstance().getConnection();

		// Load scenario into db
		DatabaseScenarioLoader.getInstance().loadScenario(c);

	
		// Connect to QuantumDB's connection manager
		// Create a bookmark
		Bookmark bookmark = new Bookmark();
		bookmark.setName("Tramptest");
		bookmark.setConnection(c);
		BookmarkSelectionUtil.getInstance().setLastUsedBookmark(bookmark);
		
		
		// Generate queries
		int numSource = h.getScenario().getSchemas().getSourceSchema().getRelationArray().length;
		String query;
		
		for(int i = 0 ; i < numSource; i++)
        {
			query = "select * from source." + h.getScenario().getSchemas().getSourceSchema().getRelationArray()[i].getName();
			bookmark.addQuery(query);
        }
		
		SQLResults results = null;
		List<String> queries = bookmark.getQueries();
		for (int i=0; i<queries.size(); i++) {
			results = MultiSQLServer.getInstance().execute(bookmark, c, queries.get(i));
			if (results.isResultSet()) {
				SQLResultSetCollection.getInstance()
				.addSQLResultSet(TableViewManager.getInstance().getManagerId(), "Source", (SQLResultSetResults) results);
			}
		}
		int n = queries.size();
		
		int numTarget = h.getScenario().getSchemas().getTargetSchema().getRelationArray().length;
		for(int i = 0 ; i < numTarget; i++)
        {
			query = "select * from target." + h.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getName();
			bookmark.addQuery(query);
        }
		
		queries = bookmark.getQueries();
		for (int i=n; i<queries.size(); i++) {
			results = MultiSQLServer.getInstance().execute(bookmark, c, queries.get(i));
			if (results.isResultSet()) {
				SQLResultSetCollection.getInstance()
				.addSQLResultSet(TableViewManager.getInstance().getManagerId(), "Target", (SQLResultSetResults) results);
			}
		}
	}
	
}