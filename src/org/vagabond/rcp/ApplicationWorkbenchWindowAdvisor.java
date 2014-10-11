package org.vagabond.rcp;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
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
		// init status line
		IStatusLineManager statusLine = getWindowConfigurer()
				.getActionBarConfigurer().getStatusLineManager();
		StatusLineController.getInstance().setStatusLine(statusLine);
		statusLine.setMessage("No scenario loaded");
		
		cleanUpPreferences();
//		cleanUpCoolbar();
	}
	

	
	private void cleanUpCoolbar() {
		ICoolBarManager coolBar = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
		
		for(IContributionItem item: coolBar.getItems()) {
			log.debug("item " + item.getId());
			
			if (!item.getId().startsWith("org.vagabond.rpc")) {
				coolBar.remove(item);
			}
		}
		
	}

	private void cleanUpPreferences() {
		PreferenceManager preferenceManager = PlatformUI.getWorkbench()
				.getPreferenceManager();

		for(Object elem : preferenceManager.getElements(
				PreferenceManager.POST_ORDER)){
			if(elem instanceof IPreferenceNode){
				log.debug("pref page: " + ((IPreferenceNode)elem).getId());
				if(!((IPreferenceNode)elem).getId().startsWith(
						"org.vagabond.rcp.preferences")) {
					preferenceManager.remove((IPreferenceNode) elem) ;
				}
			}
		}
	}

	@Override
	public void postWindowClose () {
		log.debug("post window close");
	}
}
