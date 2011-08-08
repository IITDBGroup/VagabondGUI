package org.vagabond.rcp.gui.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.MappingsType;
import org.vagabond.xmlmodel.RelAtomType;

public class MappingsView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.mappingsview";
	private TableViewer viewer;
	
	public static MappingsView getInstance() {
		return (MappingsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	@Override
	public void createPartControl(Composite parent) {		
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
	}
	
	public void setMappings() {
		viewer.getTable().clearAll();
		
		MappingsType maps = MapScenarioHolder.getInstance().getScenario().getMappings();
		String mapName, tableName, varNames;
		String foreach, exists;
		
		for (MappingType map: maps.getMappingArray()) {
			foreach = "";
			exists = "";
			
			mapName = map.getId();
			
			for (RelAtomType m : map.getForeach().getAtomArray()) {
				tableName = m.getTableref();
				varNames = implode(m.getVarArray(), ",");
				
				if (foreach == "")
					foreach = foreach + tableName + "(" + varNames + ")";
				else
					foreach = foreach + " \u2227 " + tableName + "(" + varNames + ")";
			}
			
			for (RelAtomType m : map.getExists().getAtomArray()) {
				tableName = m.getTableref();
				varNames = implode(m.getVarArray(), ",");
				
				if (exists == "")
					exists = exists + tableName + "(" + varNames + ")";
				else
					exists = exists + " \u2227 " + tableName + "(" + varNames + ")";
			}
			
			viewer.add(mapName.toUpperCase() + ": \u2200 " + foreach + " \u27F9 \u2203 " + exists);
		}
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	
	private static String implode(String[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}

}
