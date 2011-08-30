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
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewFactory;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.MappingDetailView;
import org.vagabond.rcp.gui.views.detailWidgets.ModelElementDetailView;
import org.vagabond.rcp.selection.EventUtil;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.selection.VagaSelectionListener;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;
import org.vagabond.xmlmodel.MappingType;

public class MappingsView extends ViewPart implements DetailViewFactory, VagaSelectionListener {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(MappingsView.class);

	public static final Set<ModelType> interest;
	public static final String ID = "org.vagabond.rcp.gui.views.mappingsview";
	
	static {
		interest = new HashSet<ModelType> ();
		interest.add(ModelType.Mapping);
		interest.add(ModelType.None);
	}
	
	private DetailViewList<MappingType> mapViewer;
	
	public static MappingsView getInstance() {
		return (MappingsView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(ID);
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
		mapViewer = new DetailViewList<MappingType>(parent, this);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		mapViewer.setLayoutData(gridData);
	}
	
	public void setMappings(MappingType[] maps) {
		GlobalSelectionController.addSelectionListener(this);
		mapViewer.updateModel(maps);
	}
	
	public void selectMapping(String id) {
		mapViewer.selectElement(id);
	}
	
	@Override
	public void setFocus() {
		mapViewer.setFocus();
	}

	@Override
	public ModelElementDetailView createView(Composite parent) {
		MappingDetailView map = new MappingDetailView(parent, SWT.NONE);
		return map;
	}

	@Override
	public void event(VagaSelectionEvent e) {
		if (e.isEmpty()) {
			mapViewer.clearSelection();
			return;
		} else if (e.isReset())  {
			setMappings(MapScenarioHolder
					.getInstance().getDocument().getMappingScenario()
					.getMappings().getMappingArray());
			return;
		}
		
		if (e.isLimitScope()) {
			try  {
				setMappings(EventUtil.getInstance().getMappingsForEvent(e));
			} catch (Exception e1) {
				LoggerUtil.logException(e1, log);
			}
		}
		else {
			try  {
				selectMapping(e.getElementIds().iterator().next());
			} catch (Exception e1) {
				LoggerUtil.logException(e1, log);
			}
		}
	}

	@Override
	public Set<ModelType> interestedIn() {
		return interest;
	}
}
