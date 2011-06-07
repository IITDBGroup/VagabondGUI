package org.vagabond.rcp.util;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.model.ValidationException;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.rcp.Activator;
import org.vagabond.util.ConnectionManager;


public class StartHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");
		Boolean connectionResult = false;
		Connection c = null;
		MapScenarioHolder h = null;
		
		try {
			c = ConnectionManager.getInstance().getConnection(hostString, databaseString, usernameString, passwordString);
			connectionResult = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (connectionResult) {
			MessageDialog.openInformation(shell, "Notice", "Successfully connected to database");
		} else {
			MessageDialog.openInformation(shell, "Error", "Failed to connect to database");
			return null;
		}
		
		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		Boolean holderResult = false;
		
		try {
			h = ModelLoader.getInstance().load(new File(filePath));
			holderResult = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (holderResult) {
			MessageDialog.openInformation(shell, "Notice", "Successfully loaded schema");
		} else {
			MessageDialog.openInformation(shell, "Error", "Failed to load schema");
			return null;
		}

        MapScenarioHolder.getInstance().setDocument(h.getDocument());
		SchemaResolver.getInstance().setSchemas(
				h.getScenario().getSchemas().getSourceSchema(),
				h.getScenario().getSchemas().getTargetSchema());
		try {
			DatabaseScenarioLoader.getInstance().loadScenario(c);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}

}