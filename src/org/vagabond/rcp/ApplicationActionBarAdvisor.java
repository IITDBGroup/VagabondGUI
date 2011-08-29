package org.vagabond.rcp;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.vagabond.rcp.util.PluginLogProvider;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			ApplicationActionBarAdvisor.class);
	
	// Actions - important to allocate these only in makeActions, and then use
	// them
	// in the fill methods. This ensures that the actions aren't recreated
	// when fillActionBars is called with FILL_PROXY.

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}
	
	@Override
	protected void fillCoolBar (ICoolBarManager coolBar) {
		log.debug("we should fill the coolbar");
		for(IContributionItem item: coolBar.getItems()) {
			log.debug("item " + item.getId());
			if (item.getId().startsWith("org.vagabond.rcp"))
				log.debug("my own");
		}
	}

}
