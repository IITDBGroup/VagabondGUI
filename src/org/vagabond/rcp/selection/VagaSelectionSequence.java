package org.vagabond.rcp.selection;


import java.util.Vector;

import org.apache.log4j.Logger;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

public class VagaSelectionSequence implements Cloneable {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			VagaSelectionSequence.class);
	
	private Vector<VagaSelectionEvent> eventSeq;
	private String toString = null;
	
	public static VagaSelectionSequence EMPTY_SELECTION_SEQ = new VagaSelectionSequence();
	
	// create empty sequence
	public VagaSelectionSequence () {
		eventSeq = new Vector<VagaSelectionEvent> ();
	}
	
	public VagaSelectionSequence (VagaSelectionSequence seq) {
		doClone(this, seq);
	}
	
	public VagaSelectionSequence (VagaSelectionEvent ... events) {
		eventSeq = new Vector<VagaSelectionEvent> ();
		for(VagaSelectionEvent e: events)
			eventSeq.add(e);
	}
	
	public int getLength () {
		return eventSeq.size();
	}
	
	public boolean isEmpty () {
		return eventSeq.isEmpty();
	}
	
	public void appendEvent (VagaSelectionEvent e) {
		eventSeq.add(e);
		toString = null; //TODO optimize
	}
	
	public void clear () {
		eventSeq.clear();
		toString = null;
	}
	
	public void makeSingleton (VagaSelectionEvent e) {
		eventSeq.clear();
		eventSeq.add(e);
		toString = null;
	}
	
	public boolean isLimitScope () {
		return !eventSeq.isEmpty() && eventSeq.get(0).isLimitScope();
	}
	
	@Override
	public String toString () {
		StringBuilder result;
		
		if (toString == null) {
			result = new StringBuilder ();
			
			result.append("SEQ <<");
			for(VagaSelectionEvent e: eventSeq) {
				result.append(e.toString() + " -> ");
			}
			result.delete(result.length() - 2, result.length());
			result.append(">>");
			
			toString = result.toString();
		}
		
		return toString;
	}
	
	@Override
	public Object clone() {
		Object result = null;
		
		try {
			result = super.clone();
		} catch (CloneNotSupportedException e) {
			LoggerUtil.logException(e, log);
		}
		
		doClone((VagaSelectionSequence) result, this);
		
		return result;
	}
	
	private void doClone (VagaSelectionSequence newSeq, Object obj) {
		VagaSelectionSequence seq = (VagaSelectionSequence) obj;
		
		newSeq.eventSeq = new Vector<VagaSelectionEvent> ();		
		for(VagaSelectionEvent e: seq.eventSeq) {
			newSeq.eventSeq.add(new VagaSelectionEvent(e));
		}
	}
	
	public String toUserString () {
		StringBuilder result = new StringBuilder();
		
		result.append(isLimitScope() ? "Drilldown Navigation: " 
				: "Selected Element(s): ");
		
		for(VagaSelectionEvent e: eventSeq)
			result.append(e.toUserString() + " -> ");
		result.delete(result.length() - 4, result.length());
		return result.toString(); //TODO
	}
 }
