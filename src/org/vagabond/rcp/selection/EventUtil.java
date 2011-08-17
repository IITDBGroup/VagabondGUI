package org.vagabond.rcp.selection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.TransformationType;

public class EventUtil {

	private static EventUtil instance = new EventUtil ();
	
	private EventUtil () {
		
	}
	
	public static EventUtil getInstance () {
		return instance;
	}

	
	public String[] toUpper (String[] lower) {
		String[] result = new String[lower.length];
		
		for(int i=0; i < result.length; i++)
			result[i] = lower[i].toUpperCase();
		
		return result;
	}
	
	public CorrespondenceType[] getCorrespondencesForEvent (VagaSelectionEvent e) 
			throws Exception {
		assert(e.getElementType().equals(ModelType.Correspondence));
		return getCorrespondencesForIds(e.getElementIds());
	}

	public CorrespondenceType[] getCorrespondencesForIds (Collection<String> ids) 
			throws Exception {
		List<CorrespondenceType> result;

		result = new ArrayList<CorrespondenceType> ();
		for(String id: ids)
			result.add(MapScenarioHolder.getInstance().getCorr(id));

		return result.toArray(new CorrespondenceType[] {});
	}

	
	public MappingType[] getMappingsForEvent (VagaSelectionEvent e) throws Exception {
		assert(e.getElementType().equals(ModelType.Mapping));
		return getMappingsForIds(e.getElementIds());
	}
	
	public MappingType[] getMappingsForIds (Collection<String> ids) throws Exception {
		List<MappingType> result;
		
		result = new ArrayList<MappingType> ();
		for(String id: ids)
			result.add(MapScenarioHolder.getInstance().getMapping(id));
		
		return result.toArray(new MappingType[] {});
	}
	
	public TransformationType[] getTransformationsForEvent (VagaSelectionEvent e) throws Exception {
		assert(e.getElementType().equals(ModelType.Transformation));
		return getTransformationsForIds(e.getElementIds());
	}
	
	public TransformationType[] getTransformationsForIds (Collection<String> ids) throws Exception {
		List<TransformationType> result;
		
		result = new ArrayList<TransformationType> ();
		for(String id: ids)
			result.add(MapScenarioHolder.getInstance().getTransformation(id));
		
		return result.toArray(new TransformationType[] {});
	}
}
