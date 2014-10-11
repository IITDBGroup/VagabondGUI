package org.vagabond.rcp.commands;


import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.vagabond.rcp.controller.LoaderUtil;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

public class StartHandler extends AbstractHandler {

	static Logger log = PluginLogProvider.getInstance().getLogger(StartHandler.class);
	
	
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event); 
		
		try {
			LoaderUtil.getInstance().connectToDB();
		} catch (Exception e) {
			showErrorDialog(e, shell, "Error could not connect to DB:\n");
			return null;
		}
		
		try {
			LoaderUtil.getInstance().loadSchemaFile();
		} catch (Exception e) {
			showErrorDialog(e, shell, "Unable to load scenario file:\n");
			return null;
		}
		MessageDialog.openInformation(shell, "Notice", "Successfully connected to " +
				"database and loaded schema");
		
		try {
			LoaderUtil.getInstance().updateViews();
		} catch (Exception e) {
			LoggerUtil.logException(e, log);
		}
		
		return null;
	}
	
	

	
	private void showErrorDialog (Exception e, Shell shell, String message) {
		LoggerUtil.logException(e, log);
		MessageDialog.openInformation(shell, message, e.getMessage());
	}

	
}