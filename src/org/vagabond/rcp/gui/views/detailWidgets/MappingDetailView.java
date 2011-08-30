package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.gui.views.modelWidgets.CorrespondenceViewIDList;
import org.vagabond.rcp.gui.views.modelWidgets.SourceRelationViewIDList;
import org.vagabond.rcp.gui.views.modelWidgets.TargetRelationViewIDList;
import org.vagabond.rcp.gui.views.modelWidgets.TransformationViewIDList;
import org.vagabond.rcp.selection.EventUtil;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.util.LoggerUtil;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.RelAtomType;

public class MappingDetailView extends ModelElementDetailView {

	private Label overviewLabel;
	private CorrespondenceViewIDList corrs;
	private TransformationViewIDList trans;
	private SourceRelationViewIDList sources;
	private TargetRelationViewIDList targets;
	private final MouseListener labelListener;
	private final MouseListener groupListener;
	
	public MappingDetailView(Composite parent, int style) {
		super(parent, style);
		labelListener = new MouseAdapter() {
			@Override
			public void mouseDown (MouseEvent e) {
				fireSelectionEvent(ModelType.Mapping);
			}
		};
		groupListener = new MouseAdapter() {
			@Override
			public void mouseDown (MouseEvent e) {
				fireSelectionEvent(ModelType.Mapping);
			}
		};
	}

	@Override
	protected void createGui() {
		group.setLayout(new GridLayout (1, false));
		
		overviewLabel = new Label(group, SWT.NONE);
		overviewLabel.setLayoutData(getGridData());
		
		corrs = new CorrespondenceViewIDList(group, SWT.NONE);
		corrs.setLayoutData(getGridData());
		
		trans = new TransformationViewIDList(group, SWT.NONE);
		trans.setLayoutData(getGridData());
		
		sources = new SourceRelationViewIDList(group, SWT.NONE);
		sources.setLayoutData(getGridData());
		
		targets = new TargetRelationViewIDList(group, SWT.NONE);
		targets.setLayoutData(getGridData());		
	}
	
	private GridData getGridData () {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		
		return gridData;
	}

	@Override
	public void showElem(Object data) {
		MappingType map = (MappingType) data;
		String mapName;
		
		mapName = map.getId().toUpperCase();
		
		this.setId(map.getId());
		setOverviewLabel(map);
		group.setText(mapName);
		
		corrs.adaptLabels(EventUtil.getInstance().toUpper(
				map.getUses().getCorrespondenceArray()));
		trans.adaptLabels(MapScenarioHolder.getInstance().getTransForMap(map));
		sources.adaptLabels(atomToTest(map.getForeach().getAtomArray()));
		targets.adaptLabels(atomToTest(map.getExists().getAtomArray()));
		
		layout();
	}
	
	private String[] atomToTest (RelAtomType[] array) {
		String[] result = new String[array.length];
		
		for(int i = 0; i < result.length; i++)
			result[i] = array[i].getTableref();
			
		return result;
	}

	private void setOverviewLabel (MappingType map) {
		if (overviewLabel.getImage() != null)
			overviewLabel.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_ELEMENT));
		overviewLabel.setText(mapToText(map));
	}
	
	private String mapToText (MappingType map) {
		String foreach, exists;
		
		foreach = atomArrayToString (map.getForeach().getAtomArray());
		exists = atomArrayToString(map.getExists().getAtomArray());
		
		return  " \u2200 " + foreach + " \u27F9 \u2203 " + exists;
	}
	
	private String atomArrayToString (RelAtomType[] atoms) {
		String tableName, varNames, result;
		String and = "";
		
		result = "";
		for (RelAtomType m : atoms) {
			tableName = m.getTableref();
			varNames = LoggerUtil.arrayToString(m.getVarArray(), ",", false);
			result = result + and + tableName + "(" + varNames + ")";

			if (and.equals(""))
				and = " \u2227 ";
		}		
		
		return result;
	}

	@Override
	public void addSelectionListener() {
		overviewLabel.addMouseListener(labelListener);
		group.addMouseListener(groupListener);
	}
	
	@Override
	public void removeSelectionListener() {
		overviewLabel.removeMouseListener(labelListener);
		group.removeMouseListener(groupListener);
	}
	
	
}
