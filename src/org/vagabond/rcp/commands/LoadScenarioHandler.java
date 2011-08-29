package org.vagabond.rcp.commands;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.controller.LoaderUtil;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

public class LoadScenarioHandler extends AbstractHandler {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			LoadScenarioHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		log.info("Load new scenario");

		Shell shell = PlatformUI.getWorkbench().
				getActiveWorkbenchWindow().getShell();

		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setFilterExtensions(new String[] {"*.xml"});
		dialog.setFilterNames(new String[] {"XML File"});
		String fileSelected = dialog.open();

		if (fileSelected != null) {
			// Perform Action, like open the file.
			log.debug("Selected file: " + fileSelected);
			try {
				LoaderUtil.getInstance().loadSchemaFileAndUpdate(fileSelected);
			} catch (Exception e) {
				MessageDialog.openInformation(shell, "Could not load scenario file", e.getMessage());
				LoggerUtil.logException(e, log);
			}
		}

		return null;
	}

}
