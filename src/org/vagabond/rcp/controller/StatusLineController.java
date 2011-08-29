package org.vagabond.rcp.controller;

import org.eclipse.jface.action.IStatusLineManager;

public class StatusLineController {

	private static StatusLineController inst = new StatusLineController();
	
	private IStatusLineManager statusLine;
	
	private StatusLineController() {
		
	}

	public static void setStatus (String message) {
		inst.statusLine.setMessage(message);
	}
	
	public static StatusLineController getInstance() {
		return inst;
	}
	
	public IStatusLineManager getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(IStatusLineManager statusLine) {
		this.statusLine = statusLine;
	}
	
	
}
