package org.vagabond.rcp.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.vagabond.explanation.model.IExplanationSet;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.TransformationType;

public class ExplGenContentProvider implements ITreeContentProvider {
	private IExplanationSet explSet;
	
	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.explSet = (IExplanationSet) newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return explSet.getExplanations().toArray();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IBasicExplanation) {
			IBasicExplanation b = (IBasicExplanation) parentElement;
			List<Object> children = new ArrayList<Object>();;
			
			if (b.getCorrSideEffectSize() > 0)
				children.add((CorrespondenceType[])b.getCorrespondenceSideEffects().toArray(new CorrespondenceType[0]));
			if (b.getMappingSideEffectSize() > 0)
				children.add((MappingType[])b.getMappingSideEffects().toArray(new MappingType[0]));
			if (b.getSourceSideEffectSize() > 0)
				children.add(b.getSourceSideEffects());
			if (b.getTargetSideEffectSize() > 0)
				children.add(b.getTargetSideEffects());
			if (b.getTransformationSideEffectSize() > 0)
				children.add((TransformationType[])b.getTransformationSideEffects().toArray(new TransformationType[0]));
			
			return children.toArray();
		}
		
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof IBasicExplanation) {
			return true;
		} else if (element instanceof Collection) {
			return false;
		}
		return false;
	}

}