package org.vagabond.rcp.controller;


import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.gui.views.MapView;

public class MetadataController {
	private static MetadataController instance;
	
	public MetadataController() {
	}
	
	public static MetadataController getInstance() {
		if (instance == null) {
			instance = new MetadataController ();
		}
		
		return instance;
	}
	
	public void updateView() {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();
		MapView mapview = (MapView) page.findView(MapView.ID);
		
		MapScenarioHolder holder = MapScenarioHolder.getInstance();
		String[][] s = getSourceMetadata(holder);
		String[][] t = getTargetMetadata(holder);
		
		mapview.load(s, t);
	}
	
	private String[][] getSourceMetadata(MapScenarioHolder holder) {
		int numSource = holder.getScenario().getSchemas().getSourceSchema().getRelationArray().length;
		int attributeSize = 0;
		int max = 0;
		
		for (int i = 0; i < numSource; i++) {
			attributeSize = holder.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getAttrArray().length;
			if (attributeSize > max)
				max = attributeSize;
		}
		
		String[][] data = new String[numSource][max];
		for(int i = 0 ; i < numSource; i++)
        {
			data[i][0] = holder.getScenario().getSchemas().getSourceSchema().getRelationArray()[i].getName();
			attributeSize = holder.getScenario().getSchemas().getSourceSchema().getRelationArray()[i].getAttrArray().length;
			for(int j = 1 ; j<attributeSize+1 ; j++) {
				data[i][j] = "\t- " + holder.getScenario().getSchemas().getSourceSchema().getRelationArray()[i].getAttrArray(j-1).getName();
			}
        }
		
		return data;
	}
	
	private String[][] getTargetMetadata(MapScenarioHolder holder) {
		int numSource = holder.getScenario().getSchemas().getTargetSchema().getRelationArray().length;
		int attributeSize = 0;
		int max = 0;
		
		for (int i = 0; i < numSource; i++) {
			attributeSize = holder.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getAttrArray().length;
			if (attributeSize > max)
				max = attributeSize;
		}
		
		String[][] data = new String[numSource][max];
		for(int i = 0 ; i < numSource; i++)
        {
			data[i][0] = holder.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getName();
			attributeSize = holder.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getAttrArray().length;
			for(int j = 1 ; j<attributeSize+1 ; j++) {
				data[i][j] = "\t- " + holder.getScenario().getSchemas().getTargetSchema().getRelationArray()[i].getAttrArray(j-1).getName();
			}
        }
		
		return data;
	}
	
}
