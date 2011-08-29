package org.vagabond.rcp.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.vagabond.rcp.controller.StatusLineController;
import org.vagabond.rcp.selection.GlobalSelectionController;

public class ChangeNavigationStyleHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		GlobalSelectionController.switchMode();
		StatusLineController.setStatus("changed navigation style to " 
				+ GlobalSelectionController.getModeAsString());
		return null;
	}

}
