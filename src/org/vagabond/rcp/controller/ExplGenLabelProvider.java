package org.vagabond.rcp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.ISingleMarker;
import org.vagabond.explanation.model.basic.CopySourceError;
import org.vagabond.explanation.model.basic.CorrespondenceError;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.explanation.model.basic.InfluenceSourceError;
import org.vagabond.explanation.model.basic.SourceSkeletonMappingError;
import org.vagabond.explanation.model.basic.SuperflousMappingError;
import org.vagabond.explanation.model.basic.TargetSkeletonMappingError;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.TransformationType;

public class ExplGenLabelProvider extends LabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof IBasicExplanation) {
			IBasicExplanation expl = (IBasicExplanation) element;
			if (element instanceof CopySourceError) {
				IMarkerSet m = (IMarkerSet) expl.getExplanation();
				IAttributeValueMarker a = (IAttributeValueMarker) m.getElemList().get(0);
				
				return expl.getType().name() + " (" + a.getRel() + ", " + 
							a.getTid() + ", " + a.getAttrName() + ")";
			} else if (element instanceof CorrespondenceError) {
				Set<CorrespondenceType> set = (Set<CorrespondenceType>) expl.getExplanation();
				String ids = "";
				for (Iterator<CorrespondenceType> i = set.iterator(); i.hasNext();) {
					if (!ids.equals(""))
						ids = ids + ", " + i.next().getId();
					else
						ids = ids + i.next().getId();
				}
				
				return expl.getType().name() + " (" + ids + ")";
			} else if (element instanceof InfluenceSourceError) {
				IMarkerSet m = (IMarkerSet) expl.getExplanation();
				IAttributeValueMarker a = (IAttributeValueMarker) m.getElemList().get(0);
				
				return expl.getType().name() + " (" + a.getRel() + ", " + 
							a.getTid() + ", " + a.getAttrName() + ")";
			} else if (element instanceof SourceSkeletonMappingError) {
				Set<MappingType> set = (Set<MappingType>) expl.getExplanation();
				String ids = "";
				for (Iterator<MappingType> i = set.iterator(); i.hasNext();) {
					if (!ids.equals(""))
						ids = ids + ", " + i.next().getId();
					else
						ids = ids + i.next().getId();
				}
				
				return expl.getType().name() + " (" + ids + ")";
			} else if (element instanceof SuperflousMappingError) {
				Set<MappingType> set = (Set<MappingType>) expl.getExplanation();
				String ids = "";
				for (Iterator<MappingType> i = set.iterator(); i.hasNext();) {
					if (!ids.equals(""))
						ids = ids + ", " + i.next().getId();
					else
						ids = ids + i.next().getId();
				}
				
				return expl.getType().name() + " (" + ids + ")";
			} else if (element instanceof TargetSkeletonMappingError) {
				Set<MappingType> set = (Set<MappingType>) expl.getExplanation();
				String ids = "";
				for (Iterator<MappingType> i = set.iterator(); i.hasNext();) {
					if (!ids.equals(""))
						ids = ids + ", " + i.next().getId();
					else
						ids = ids + i.next().getId();
				}
				
				return expl.getType().name() + " (" + ids + ")";
			}
		} else if (element instanceof IMarkerSet) {
			MapScenarioHolder h = MapScenarioHolder.getInstance();
			IMarkerSet m = (IMarkerSet) element;
			List<String> source = new ArrayList<String>();
			List<String> relations = new ArrayList<String>();
			List<String> distinctRels = null;
			String relSE = "";
			boolean isSource = false;
			
			for (RelationType rel : h.getScenario().getSchemas().getSourceSchema().getRelationArray()) {
				source.add(rel.getName());
			}
			
			for (ISingleMarker s : m.getElemList()) {
				relations.add(s.getRel());
				if (source.contains(s.getRel()))
					isSource = true;
			}
			
			distinctRels = new ArrayList<String>(new HashSet<String>(relations));
			
			for (String s : distinctRels) {
				int n = Collections.frequency(relations, s);
				if (!relSE.equals(""))
					relSE = relSE + ", " + s + "("+n+")";
				else
					relSE = relSE + s + "("+n+")";
			}
			
			if (isSource)
				return "Source Relation Side Effects: " + relSE;
			else
				return "Target Relation Side Effects: " + relSE;
		} else if (element instanceof CorrespondenceType[]) {
			CorrespondenceType[] c = (CorrespondenceType[]) element;
			String ids = "";
			
			for (CorrespondenceType corr : c) {
				if (!ids.equals(""))
					ids = ids + ", " + corr.getId();
				else
					ids = ids + corr.getId();
			}
			
			return "Correspondence Side Effects: " + ids;
		} else if (element instanceof MappingType[]) {
			MappingType[] m = (MappingType[]) element;
			String ids = "";
			
			for (MappingType mapping : m) {
				if (!ids.equals(""))
					ids = ids + ", " + mapping.getId();
				else
					ids = ids + mapping.getId();
			}
			
			return "Mappings Side Effects: " + ids;
		} else if (element instanceof TransformationType[]) {
			TransformationType[] t = (TransformationType[]) element;
			String ids = "";
			
			for (TransformationType trans : t) {
				if (!ids.equals(""))
					ids = ids + ", " + trans.getId();
				else
					ids = ids + trans.getId();
			}
			
			return "Transformations Side Effects: " + ids;
		}
		
		return element.toString();
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof IBasicExplanation) {
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJS_WARN_TSK);
		}
		return PlatformUI.getWorkbench().getSharedImages()
		.getImage(ISharedImages.IMG_OBJ_FILE);
	}
}
