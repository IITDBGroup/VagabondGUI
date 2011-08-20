package org.vagabond.rcp.gui.views.modelWidgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerSetUtil;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.RelationSideEffectEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.util.Pair;

public class RelationSideEffectViewer {

	protected Composite comp;
	protected Label name;
	protected Label dataLabel;
	protected Vector<Label> relSE;
	protected final boolean source;
	
	
	public RelationSideEffectViewer (Composite parent, int flags, 
			boolean source) {
		comp = new Composite(parent, flags);
		RowLayout layout = new RowLayout();
		layout.marginWidth = 0;
		comp.setLayout(layout);
		
		this.source = source;
		
		createGui();
	}
	
	protected void createGui () {
		name = new Label(comp, SWT.NONE);
		dataLabel = new Label(comp, SWT.NONE);
		name.setText(source ? "Source:" : "Target:");
		
		relSE = new Vector<Label> ();
	}
	
	
	
	protected void updateModel (IBasicExplanation expl) {
		Map<String, IMarkerSet> sideEff;
		List<String> sortRels;
		IMarkerSet sideEffects;
		int i = 0;
		
		sideEffects = source ? expl.getSourceSideEffects() : 
				expl.getTargetSideEffects();
		sideEff = MarkerSetUtil.partitionOnRelation(sideEffects);
		sortRels = new ArrayList<String> (sideEff.keySet());
		Collections.sort(sortRels);
		
		adaptSize(sideEff.keySet().size());
		
		for(String rel: sortRels) {
			updateLabel (relSE.get(i++), rel, sideEff.get(rel));
		}
	}
	
	private void updateLabel (Label lab, String rel, IMarkerSet markers) {
		lab.setText(rel + "(" + markers.getSize() + ")");
		lab.setData(new Pair<String, IMarkerSet> (rel, markers));
		lab.addMouseListener(new MouseAdapter () {
			@Override
			public void mouseDown (MouseEvent e) {
				Label lab = (Label) e.getSource();
				Pair<String, IMarkerSet> data = (Pair<String, IMarkerSet>) 
						lab.getData(); 
				
				GlobalSelectionController.fireModelSelection(
						new RelationSideEffectEvent(false, source, 
								data.getKey(), data.getValue()));
			}
		});
	}
	
	private void adaptSize (int size) {
		int curSize = relSE.size();
		while (curSize > size) {
			relSE.get(curSize - 1).dispose();
			relSE.remove(--curSize);
		}
		while (curSize++ < size)
			relSE.add(new Label(comp, SWT.NONE));
 	}
	
	public void dispose () {
		comp.dispose();
	}
	
}
