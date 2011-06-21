package org.vagabond.rcp.controller;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.*;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.model.ValidationException;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.gui.views.*;
import org.vagabond.util.ConnectionManager;

import com.quantum.actions.BaseExecuteAction;
import com.quantum.model.Bookmark;
import com.quantum.model.JDBCDriver;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResults;

public class StartHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		if (!connectToDB()) {
			MessageDialog.openInformation(shell, "Error", "Failed to connect to database");
			return null;
		}
		MessageDialog.openInformation(shell, "Notice", "Successfully connected to database");
		
		if (!loadSchemaFile()) {
			MessageDialog.openInformation(shell, "Error", "Failed to load schema");
			return null;
		}
		MessageDialog.openInformation(shell, "Notice", "Successfully loaded schema");
		
		
		
		
		
		
		MetadataController.getInstance().updateView();
		
		return null;
	}
	
	// Connect to the database specified by the details in the preference pane
	private boolean connectToDB() {
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");
		Connection connection = null;

		try {
			connection = ConnectionManager.getInstance().getConnection(hostString, databaseString, usernameString, passwordString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// Load the file specified in the preference pane and load the scenario to the database
	private boolean loadSchemaFile() {
		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		MapScenarioHolder h = null;
		
		try {
			h = ModelLoader.getInstance().load(new File(filePath));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		MapScenarioHolder.getInstance().setDocument(h.getDocument());
		SchemaResolver.getInstance().setSchemas(
				h.getScenario().getSchemas().getSourceSchema(),
				h.getScenario().getSchemas().getTargetSchema());
		Connection c;
		
		try {
			c = ConnectionManager.getInstance().getConnection();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		try {
			DatabaseScenarioLoader.getInstance().loadScenario(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		Bookmark bookmark = new Bookmark();
		
		// Connect to QuantumDB's connection manager
		// Create a bookmark
		bookmark.setName("Tramptest");
		bookmark.setConnection(c);
		
		
		int numSource = h.getScenario().getSchemas().getSourceSchema().getRelationArray().length;
		String query;
		for(int i = 0 ; i < numSource; i++)
        {
			query = "select * from source." + h.getScenario().getSchemas().getSourceSchema().getRelationArray()[i].getName();
			bookmark.addQuery(query);
        }
		int numTarget = h.getScenario().getSchemas().getTargetSchema().getRelationArray().length;
		for(int i = 0 ; i < numTarget; i++)
        {
			query = "select * from target." + h.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getName();
			bookmark.addQuery(query);
        }
		
		List<String> queries = bookmark.getQueries();
		
		MultiSQLServer server = MultiSQLServer.getInstance();
		SQLResults results = null;
		for (int i=0; i<queries.size(); i++) {
			try {
				results = server.execute(bookmark, c, queries.get(i));
				if (results.isResultSet()) {
					SQLResultSetCollection.getInstance()
					.addSQLResultSet(
							(SQLResultSetResults) results);
				}
			} catch (SQLException e) {
				return false;
			}
		}
		
		return true;
	}
	
}