package org.vagabond.rcp.model;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

import org.vagabond.rcp.gui.views.SourceDBView;
import org.vagabond.rcp.gui.views.TargetDBView;

import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ITableViewManager;
import com.quantum.view.tableview.TableView;

public class TableViewManager implements ITableViewManager {
	public static final String MANAGER_ID = "StandardManager";
	
	private SourceDBView sourceView;
	private TargetDBView targetView;
	private Map<String,TableView> viewIdMap;
	
	private static TableViewManager instance;
	
	private TableViewManager() {
		SQLResultSetCollection.getInstance().addTableManager(this);
		viewIdMap = new HashMap<String, TableView> ();
	}
	
	public static TableViewManager getInstance () {
		if (instance == null)
			instance = new TableViewManager();
		return instance;
	}
	
	public void setSourceView(SourceDBView view) {
		this.sourceView = view;
	}
	
	public void setTargetView(TargetDBView view) {
		this.targetView = view;
	}

	public SQLResultSetResults[] getResultSets(String viewId) {
		return SQLResultSetCollection.getInstance().getResultSets(MANAGER_ID, viewId);
	}
	
	public TableView getViewForId (String id) {
		if (id.equals("Source"))
			return sourceView;
		else
			return targetView;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		PropertyChangeEvent viewEvent;
		viewEvent = new PropertyChangeEvent(arg0.getSource(), "resultSets", arg0.getOldValue(), arg0.getNewValue());
		
		if(arg0.getPropertyName().startsWith("Source.")) {
			sourceView.propertyChange(viewEvent);
		}
		else {
			targetView.propertyChange(viewEvent);
		}
	}

	@Override
	public String getManagerId() {
		return MANAGER_ID;
	}
}
