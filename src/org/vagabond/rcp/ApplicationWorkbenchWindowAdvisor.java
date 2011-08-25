package org.vagabond.rcp;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.vagabond.rcp.controller.StatusLineController;
import org.vagabond.rcp.util.PluginLogProvider;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			ApplicationWorkbenchWindowAdvisor.class);
	
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800, 600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
	}
	
	@Override
	public void postWindowOpen() {
		IStatusLineManager statusLine = getWindowConfigurer()
				.getActionBarConfigurer().getStatusLineManager();
		StatusLineController.getInstance().setStatusLine(statusLine);
		statusLine.setMessage("No scenario loaded");
	}
	
	@Override
	public void postWindowClose () {
		log.debug("post window close");
	}
}
