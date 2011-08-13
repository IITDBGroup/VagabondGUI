package org.vagabond.rcp.gui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.CorrespondencesType;

public class CorrView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.corrview";
	private TableViewer viewer;
	
	public static CorrView getInstance() {
		return (CorrView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		
//		public Image getImage(Object obj) {
//			return PlatformUI.getWorkbench().getSharedImages().getImage(
//					ISharedImages.IMG_DEF_VIEW);
//		}
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
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}
	
	public void setCorrespondences() {
		viewer.getTable().removeAll();
		
		CorrespondencesType corrs = MapScenarioHolder.getInstance().getScenario().getCorrespondences();
		String corrName, sourceRel, sourceAttr, targetRel, targetAttr;

		for (CorrespondenceType corr : corrs.getCorrespondenceArray()) {
			corrName = corr.getId();
			
			sourceRel = "source." + corr.getFrom().getTableref();
			sourceAttr = corr.getFrom().getAttrArray(0);
			targetRel = "target." + corr.getTo().getTableref();
			targetAttr = corr.getTo().getAttrArray(0);
			viewer.add(corrName.toUpperCase() +": From "+ sourceRel + ", " 
					+ sourceAttr + " to " + targetRel + ", " + targetAttr);
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

}
