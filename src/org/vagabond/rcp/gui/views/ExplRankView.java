package org.vagabond.rcp.gui.views;

import org.apache.log4j.Logger;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.explanation.model.IExplanationSet;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.rcp.controller.ExplViewActionGroup;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.ExplainDetailViewFactory;
import org.vagabond.rcp.util.PluginLogProvider;

public class ExplRankView extends ViewPart {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			ExplRankView.class);
	
	public static final String ID = "org.vagabond.rcp.gui.views.explrankview";

	private DetailViewList<IBasicExplanation> viewer;
	protected ExplViewActionGroup actionGroup;
	private IExplanationSet curSet = null;
	

	public static ExplRankView getInstance() {
		return (ExplRankView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	public void createPartControl(Composite parent) {
		initActions();
		setLayout(parent);
		createViewer(parent);
	}
	
	public void initActions() {
        this.actionGroup = new ExplViewActionGroup(this);

        IActionBars actionBars = getViewSite().getActionBars();
        this.actionGroup.fillActionBars(actionBars);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, true);
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		viewer = new DetailViewList<IBasicExplanation> (parent, 
				ExplainDetailViewFactory.withExplains);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.setLayoutData(gridData);
	}

	public void updateView (IExplanationSet explSet) {
		this.curSet = explSet;
		viewer.updateModel(curSet);
		this.actionGroup.updateActionBars();
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.setFocus();
	}
	
}