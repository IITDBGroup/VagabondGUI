package org.vagabond.rcp.util;


import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import org.apache.xmlbeans.XmlException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.model.ValidationException;
import org.vagabond.rcp.Activator;
import org.vagabond.util.ConnectionManager;


public class ShowPreferenceValues extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
//		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
//		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
//		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
//		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");
//		Boolean connectionResult = false;
//		
//		try {
//			Connection c = ConnectionManager.getInstance().getConnection(hostString, databaseString, usernameString, passwordString);
//			connectionResult = true;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		MessageDialog.openInformation(shell, "Info", connectionResult.toString());
//		

		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		Boolean holderResult = false;
		
		try {
			MapScenarioHolder h = ModelLoader.getInstance().load(new File(filePath));
			holderResult = true;
		} catch (Exception e) {
			
		}
		MessageDialog.openInformation(shell, "Info", holderResult.toString());

		return null;
	}

}