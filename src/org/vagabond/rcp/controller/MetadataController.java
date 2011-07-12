package org.vagabond.rcp.controller;


import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.gui.views.MapView;
import org.vagabond.xmlmodel.AttrDefType;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.CorrespondencesType;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

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
		MapScenarioHolder holder = MapScenarioHolder.getInstance();
		loadSourceMetadata(holder);
		loadTargetMetadata(holder);
		loadCorrespondences(holder);
	}
	
	private void loadSourceMetadata(MapScenarioHolder holder) {
		SchemaType source = holder.getScenario().getSchemas().getSourceSchema();
		MapView mapview = MapView.getInstance();
		String relName, attrName;
		
		for (RelationType rel : source.getRelationArray()) {
			relName = "source." + rel.getName();
			mapview.addRel(relName);
			
			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				mapview.addAttr(relName, attrName);
			}
        }
	}
	
	private void loadTargetMetadata(MapScenarioHolder holder) {
		SchemaType target = holder.getScenario().getSchemas().getTargetSchema();
		MapView mapview = MapView.getInstance();
		String relName, attrName;
		
		for (RelationType rel : target.getRelationArray()) {
			relName = "target." + rel.getName();
			mapview.addRel(relName);

			for (AttrDefType attr : rel.getAttrArray()) {
				attrName = attr.getName();
				mapview.addAttr(relName, attrName);
			}
        }
	}
	
	private void loadCorrespondences(MapScenarioHolder holder) {
		int corrSize = holder.getScenario().getCorrespondences().sizeOfCorrespondenceArray();
		MapView mapview = MapView.getInstance();
		CorrespondencesType corrs = holder.getScenario().getCorrespondences();
		CorrespondenceType corr;
		String corrName, sourceRel, sourceAttr, targetRel, targetAttr;
		
		for (int i=0; i<corrSize; i++) {
			corr = corrs.getCorrespondenceArray(i);
			corrName = corr.getId();
			
			sourceRel = "source." + corr.getFrom().getTableref();
			sourceAttr = corr.getFrom().getAttrArray(0);
			targetRel = "target." + corr.getTo().getTableref();
			targetAttr = corr.getTo().getAttrArray(0);
			
			mapview.addCorrespondence(corrName, sourceRel, sourceAttr, targetRel, targetAttr);
		}
	}
}
