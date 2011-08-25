package org.vagabond.rcp.gui.views.detailWidgets;

import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.model.basic.CopySourceError;
import org.vagabond.explanation.model.basic.CorrespondenceError;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.explanation.model.basic.InfluenceSourceError;
import org.vagabond.explanation.model.basic.SourceSkeletonMappingError;
import org.vagabond.explanation.model.basic.SuperflousMappingError;
import org.vagabond.explanation.model.basic.TargetSkeletonMappingError;
import org.vagabond.rcp.gui.views.modelWidgets.SideEffectViewer;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.MappingType;

public class ExplanationDetailView implements IModelElementDetailView {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			ExplanationDetailView.class);
	
	protected Composite comp;
	protected Group group;
	private SideEffectViewer sides;
	private SideEffectsStatsView stats;
	protected Text overview;
	protected boolean addExplains;
	private boolean selected = false;
	private String id = null;
	
	public ExplanationDetailView (Composite parent, int flags, boolean addExplains) {
		comp = new Composite(parent, flags);
		this.addExplains = addExplains;
		createGui ();
	}
	
	private void createGui () {
		comp.setLayout(new GridLayout(1,false));
		
		group = new Group(comp, SWT.NONE);
		group.setLayout(new GridLayout(1, false));
		group.setFont(SWTResourceManager.getBoldSystemFont(12));
		group.setLayoutData(getFillData(1));
		
		overview = new Text(group, SWT.NONE);
		overview.setEditable(false);
		overview.setEnabled(false);
		overview.setBackground(comp.getBackground());
		overview.setLayoutData(getGridData(1));
		
		addGuiElements();
		
		sides = new SideEffectViewer(group);
		GridData gridData = getGridData(1);
		sides.setLayoutData(gridData);
		
		stats = new SideEffectsStatsView (group, SWT.NONE);
		stats.setLayoutData(getGridData(1));
	}
	
	protected String getToolTip (IBasicExplanation expl) {
		if (expl instanceof CopySourceError) {
			return "Data in the source is incorrect.";
		} else if (expl instanceof InfluenceSourceError) {
			return "Incorrect data in the source caused an incorrect tuple to be joined";
		} else if (expl instanceof CorrespondenceError) {
			return "A corresponce is not correct and should be removed";
		} else if (expl instanceof SuperflousMappingError) {
			return "A superfluous mapping is one that should not have " +
					"been created in the first place.";
		} else if (expl instanceof SourceSkeletonMappingError) {
			return "The mapping joins source relations in an incorrect way";
		} else if (expl instanceof TargetSkeletonMappingError) {
			return "The mapping joins target relations in an incorrect way";
		}
		log.error("unknown explanation type: " + expl.toString());
		return null;
	}
	
	protected GridData getFillData (int size) {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = size;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		
		return gridData;		
	}
	
	protected GridData getGridData (int size) {
		GridData gridData;
		
		gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = size;
		
		return gridData;
	}
	
	protected void addGuiElements() {
		
	}
	
	public void updateModel (IBasicExplanation expl) {
		updateElements(expl);
		group.setToolTipText(getToolTip(expl));
		overview.setToolTipText(getToolTip(expl));	
		updateHeader (expl);
		updateOverview (expl);
		sides.updateModel(expl);
		stats.updateModel(expl);
		createId(expl);
		
//		overview.pack();
//		sides.pack();
//		sides.layout();
//		group.pack();
		sides.layout();
		group.layout(true,true);
		comp.layout(true,true);
	}
	
	protected void updateOverview (IBasicExplanation expl) {
		StringBuilder buf = new StringBuilder();
		
		if (expl instanceof CopySourceError) {
			IMarkerSet sourceSE = (IMarkerSet) expl.getExplanation();
			overview.setText(sourceSE.toString() );
		} else if (expl instanceof InfluenceSourceError) {
			IMarkerSet sourceSE = (IMarkerSet) expl.getExplanation();
			overview.setText(sourceSE.toString());
		} else if (expl instanceof CorrespondenceError) {
			Set<CorrespondenceType> corrs = (Set<CorrespondenceType>) expl.getExplanation();
			for(CorrespondenceType corr: corrs)
				buf.append(corr.getId().toUpperCase() + ",");
			buf.append("A corresponce is not correct and should be removed\n");
			buf.append("A corresponce is not correct and should be removed\n");
			buf.append("A corresponce is not correct and should be removed\n");
			buf.append("A corresponce is not correct and should be removed\n");
			buf.append("A corresponce is not correct and should be removed\n");
			buf.deleteCharAt(buf.length() - 1);
			overview.setText(buf.toString());
		} else if (expl instanceof SuperflousMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			overview.setText(buf.toString());
		} else if (expl instanceof SourceSkeletonMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			overview.setText(buf.toString());
		} else if (expl instanceof TargetSkeletonMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			overview.setText(buf.toString());
		}
	}
	
	protected void updateHeader (IBasicExplanation expl) {
		group.setText(getTypeText(expl)  + " "
				+ (addExplains ? "explains " + expl.explains().toString() : "") + " "
				+ explToText(expl));
	}
	
	protected String explToText (IBasicExplanation expl) {
		StringBuilder buf = new StringBuilder();
		
		if (expl instanceof CopySourceError) {
			IMarkerSet sourceSE = (IMarkerSet) expl.getExplanation();
			return "(" + sourceSE.size() + ")";
		} else if (expl instanceof InfluenceSourceError) {
			IMarkerSet sourceSE = (IMarkerSet) expl.getExplanation();
			return "(" + sourceSE.size() + ")";
		} else if (expl instanceof CorrespondenceError) {
			Set<CorrespondenceType> corrs = (Set<CorrespondenceType>) expl.getExplanation();
			for(CorrespondenceType corr: corrs)
				buf.append(corr.getId().toUpperCase()	 + ",");
			buf.deleteCharAt(buf.length() - 1);
			return " (" + buf.toString() + ") ";
		} else if (expl instanceof SuperflousMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			return " (" + buf.toString() + ") ";
		} else if (expl instanceof SourceSkeletonMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			return " (" + buf.toString() + ") ";
		} else if (expl instanceof TargetSkeletonMappingError) {
			Set<MappingType> maps = (Set<MappingType>) expl.getExplanation();
			for(MappingType map: maps)
				buf.append(map.getId() + ",");
			buf.deleteCharAt(buf.length() - 1);
			return " (" + buf.toString() + ") ";
		}
		log.error("unknown explanation type: " + expl.toString());
		return "";
	}
	
	protected String getTypeText (IBasicExplanation expl) {
		if (expl instanceof CopySourceError) {
			return "Copy Source Error";
		} else if (expl instanceof InfluenceSourceError) {
			return "Source Data Join Error";
		} else if (expl instanceof CorrespondenceError) {
			return "Correspondence Error";
		} else if (expl instanceof SuperflousMappingError) {
			return "Superfluous Mapping";
		} else if (expl instanceof SourceSkeletonMappingError) {
			return "Source Schema Join Error";
		} else if (expl instanceof TargetSkeletonMappingError) {
			return "Target Schema Join Error";
		}
		log.error("unknown explanation type: " + expl.toString());
		return "";
	}
	
	public void updateElements (IBasicExplanation expl) {
		
	}
	
	public void setLayoutData (Object layoutData) {
		comp.setLayoutData(layoutData);
	}
	
	public void layout() {
		comp.layout();
	}

	@Override
	public void showElem(Object data) {
		IBasicExplanation expl = (IBasicExplanation) data;
		updateModel(expl);
	}

	@Override
	public void addSelectionListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		comp.dispose();
	}

	@Override
	public void setSelection(boolean selected) {
		if (this.selected != selected) {
			this.selected = selected;
			group.setBackground(selected ? 
					SWTResourceManager.getColor(new RGB(200, 200, 255)) : 
					SWTResourceManager.getColor("Background"));
		}
	}

	private void createId(IBasicExplanation expl) {
		StringBuilder newId = new StringBuilder();
		
		newId.append(expl.explains().toString());
		newId.append('|');
		newId.append(expl.getType().toString());
		newId.append('|');
		newId.append(expl.getExplanation().hashCode());
		this.id = newId.toString();
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}
}
