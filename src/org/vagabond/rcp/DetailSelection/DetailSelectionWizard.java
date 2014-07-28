package org.vagabond.rcp.DetailSelection;

import org.eclipse.jface.wizard.Wizard;
import org.vagabond.rcp.util.PluginLogProvider;
import org.apache.log4j.Logger;


public class DetailSelectionWizard extends Wizard{
	static Logger log = PluginLogProvider.getInstance().getLogger(
			DetailSelectionWizard.class);
	protected DetailSelectionPage ds;
	public DetailSelectionWizard(DetailSelectionPage ds1){
		ds=ds1;
	}
	@Override
	public boolean performFinish() {
		try {
			ds.performFinish();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public void addPages() {
		addPage(ds);
	}
}
