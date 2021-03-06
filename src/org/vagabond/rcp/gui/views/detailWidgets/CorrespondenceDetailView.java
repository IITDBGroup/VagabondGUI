package org.vagabond.rcp.gui.views.detailWidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.gui.views.modelWidgets.MappingViewIDList;
import org.vagabond.rcp.gui.views.modelWidgets.SourceRelationViewIDList;
import org.vagabond.rcp.gui.views.modelWidgets.TargetRelationViewIDList;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.xmlmodel.CorrespondenceType;

public class CorrespondenceDetailView extends ModelElementDetailView {

	private Label overviewLabel;
	private SourceRelationViewIDList sources;
	private TargetRelationViewIDList targets;
	private MappingViewIDList maps;
	private CorrMouseListener labelListener;
	private CorrMouseListener groupListener;
	
	public class CorrMouseListener extends MouseAdapter implements MouseListener {

		private CorrespondenceDetailView myView;
		
		public CorrMouseListener (CorrespondenceDetailView myView) {
			this.myView = myView;
		}

		@Override
		public void mouseDown(MouseEvent e) {
			myView.fireSelectionEvent(ModelType.Correspondence);
		}
		
	}
	
	public CorrespondenceDetailView(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void createGui() {
		group.setLayout(new GridLayout (1, false));
		
		overviewLabel = new Label(group, SWT.NONE);
		overviewLabel.setLayoutData(getGridData());
		
		maps = new MappingViewIDList(group, SWT.NONE);
		maps.setLayoutData(getGridData());
		
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
		CorrespondenceType corr = (CorrespondenceType) data;

		setId(corr.getId().toUpperCase());
		group.setText(getId());
		overviewLabel.setText(corrToString(corr));
		
		maps.adaptLabels(MapScenarioHolder.getInstance().getMapsForCorr(corr));
		sources.adaptLabels(corr.getFrom().getTableref());
		targets.adaptLabels(corr.getTo().getTableref());
		
		layout();
	}
	
	private String corrToString (CorrespondenceType corr) {
		String sourceRel = "source." + corr.getFrom().getTableref();
		String sourceAttr = corr.getFrom().getAttrArray(0);
		String targetRel = "target." + corr.getTo().getTableref();
		String targetAttr = corr.getTo().getAttrArray(0);
		
		return  "From "+ sourceRel + ", " + sourceAttr + " to " + targetRel 
				+ ", " + targetAttr;
	}

	@Override
	public void addSelectionListener() {
		labelListener = new CorrMouseListener(this);
		groupListener = new CorrMouseListener(this);
		overviewLabel.addMouseListener(labelListener);
		group.addMouseListener(groupListener);
	}
	
	public void removeSelectionListener() {
		overviewLabel.removeMouseListener(labelListener);
		group.removeMouseListener(groupListener);
	}
}
