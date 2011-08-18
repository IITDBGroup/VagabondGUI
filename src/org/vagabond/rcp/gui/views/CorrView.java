package org.vagabond.rcp.gui.views;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.rcp.gui.views.detailWidgets.CorrespondenceDetailView;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewFactory;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.ModelElementDetailView;
import org.vagabond.rcp.selection.EventUtil;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.selection.VagaSelectionListener;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;
import org.vagabond.xmlmodel.CorrespondenceType;

public class CorrView extends ViewPart implements VagaSelectionListener, DetailViewFactory {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			CorrView.class);
	
	public static final Set<ModelType> interest;
	
	public static final String ID = "org.vagabond.rcp.gui.views.corrview";
	
	static {
		interest = new HashSet<ModelType> ();
		interest.add(ModelType.Correspondence);
	}
	
	private DetailViewList<CorrespondenceType> viewer;
	
	public static CorrView getInstance() {
		return (CorrView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView(ID);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		setLayout(parent);
		createViewer(parent);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, false);
		
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		viewer = new DetailViewList<CorrespondenceType>(parent, this);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.setLayoutData(gridData);
	}
	
	public void setCorrespondences(CorrespondenceType[] corrs) {
		GlobalSelectionController.addSelectionListener(this);
		viewer.updateModel(corrs);
		viewer.layout();
	}

	public void selectCorrespondence (String id) {
		viewer.selectElement(id);
	}
	
	@Override
	public void setFocus() {
		viewer.setFocus();
	}

	@Override
	public void event(VagaSelectionEvent e) {
		if(e.isEmpty())
			return;
		
		if (e.isLimitScope()) {
			try {
				setCorrespondences(EventUtil.getInstance()
						.getCorrespondencesForIds(e.getElementIds()));
			} catch (Exception e1) {
				LoggerUtil.logException(e1, log);
			}
		}
		else {
			try {
				selectCorrespondence(e.getElementIds().iterator().next());
			} catch (Exception e1) {
				LoggerUtil.logException(e1, log);
			}			
		}
	}

	@Override
	public Set<ModelType> interestedIn() {
		return interest;
	}

	@Override
	public ModelElementDetailView createView(Composite parent) {
		return new CorrespondenceDetailView(parent, SWT.NONE);
	}

}
