package org.vagabond.rcp.controller;


import java.io.File;
import java.sql.Connection;
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
import org.vagabond.rcp.mapview.controller.GraphEditPart;
import org.vagabond.rcp.mapview.model.ContentProvider;
import org.vagabond.rcp.mapview.view.View;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.util.ConnectionManager;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

import com.quantum.actions.BookmarkSelectionUtil;
import com.quantum.adapters.AdapterFactory;
import com.quantum.model.Bookmark;
import com.quantum.model.BookmarkCollection;
import com.quantum.model.JDBCDriver;
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
		
		try {
			View.getInstance().getViewer().setContents(ContentProvider.getInstance().generateGraph());
//	    	GraphEditPart graph = (GraphEditPart) View.getInstance().getViewer().getRootEditPart().getChildren().get(0);
//	    	graph.setLayoutConstraints();
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
		
		return null;
	}
	
	// Connect to the database specified by the details in the preference pane
	private void connectToDB() throws Exception {
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");

		Connection c = ConnectionManager.getInstance().getConnection(hostString, databaseString, usernameString, passwordString);
		
		// Connect to QuantumDB's connection manager
		// Create a bookmark
		Bookmark bookmark = new Bookmark();
		bookmark.setName(databaseString);
		bookmark.setUsername(usernameString);
		bookmark.setPassword(passwordString);
		bookmark.setConnect("jdbc:postgresql://" + hostString + ":5432/" + databaseString);
		bookmark.setJDBCDriver(new JDBCDriver("org.postgresql.Driver", new String[0], AdapterFactory.POSTGRES));
		bookmark.setConnection(c);
		BookmarkSelectionUtil.getInstance().setLastUsedBookmark(bookmark);
		BookmarkCollection.getInstance().addBookmark(bookmark);
	}
	
	// Load the file specified in the preference pane and load the scenario to the database
	private void loadSchemaFile() throws Exception {
		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		
		MapScenarioHolder h = ModelLoader.getInstance().load(new File(filePath));
		MapScenarioHolder.getInstance().setDocument(h.getDocument());
		SchemaResolver.getInstance().setSchemas(
				h.getScenario().getSchemas().getSourceSchema(),
				h.getScenario().getSchemas().getTargetSchema());

		Connection c = ConnectionManager.getInstance().getConnection();

		// Load scenario into db
		DatabaseScenarioLoader.getInstance().loadScenario(c);

		Bookmark bookmark = BookmarkCollection.getInstance().find(databaseString);
		
		// Generate queries
		SchemaType source = h.getScenario().getSchemas().getSourceSchema();
		SchemaType target = h.getScenario().getSchemas().getTargetSchema();
		String query;
		SQLResults results = null;
		
		for (RelationType rel : source.getRelationArray()) {
			query = "select * from source." + rel.getName();
			bookmark.addQuery(query);
			results = MultiSQLServer.getInstance().execute(bookmark, c, query);
			if (results.isResultSet()) {
				((SQLResultSetResults)results).setName(rel.getName());
				
				SQLResultSetCollection.getInstance()
				.addSQLResultSet(TableViewManager.getInstance().getManagerId(), "Source", (SQLResultSetResults) results);
			}
        }
		
		for (RelationType rel : target.getRelationArray()) {
			query = "select * from target." + rel.getName();
			bookmark.addQuery(query);
			results = MultiSQLServer.getInstance().execute(bookmark, c, query);
			if (results.isResultSet()) {
				((SQLResultSetResults)results).setName(rel.getName());
				
				SQLResultSetCollection.getInstance()
				.addSQLResultSet(TableViewManager.getInstance().getManagerId(), "Target", (SQLResultSetResults) results);
			}
        }
	}
	
}