package org.vagabond.rcp.controller;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.gui.views.GenericTableView;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.ConnectionManager;

import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLStandardResultSetResults;

/**
 * Provide navigation functionalities for the buttons on Source and Target DB views
 * @author ken
 *
 */

public class TableNavHandler {

	public enum NAV_ACTION {FIRST_PAGE, PREVIOUS_PAGE, NEXT_PAGE, LAST_PAGE};
	public enum SCHEMA_TYPE {SOURCE("Source"), TARGET("Target");
		private String value;

		private SCHEMA_TYPE(String value) {
			this.value = value;
		}

		public String toString() {
			return this.value;
		}
	};
	
	private String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
	
	private static final TableNavHandler instance = new TableNavHandler();
	static Logger log = PluginLogProvider.getInstance().getLogger(
			TableNavHandler.class);
	
	// HashMap that stores the total pages of each relation
	private HashMap<String, Integer> sourceMap = new HashMap<String, Integer>();
	private HashMap<String, Integer> targetMap = new HashMap<String, Integer>();
	
	private TableNavHandler() {}
	
	public static TableNavHandler getInstance() {
		return instance;
	}
	
	public void addNewRelation(SCHEMA_TYPE type, String relationName, String managerID, int totalPages) {
		if (type == SCHEMA_TYPE.SOURCE)
			sourceMap.put(relationName, totalPages);
		else
			targetMap.put(relationName, totalPages);
		log.debug("TableNavHandler: Adding new relation: " + relationName + ", " + managerID + ", " + totalPages);
	}
	
	public void navigateToPage(SCHEMA_TYPE type, String relationName, int page) throws Exception {
		int totalPages = getTotalPages(type, relationName);

		page = Math.max(page, 1);
		page = Math.min(page, totalPages);
		
		LoaderUtil.getInstance().updateTableFromView(databaseString, type.toString(), relationName, page);
	}
	
	public void navigateToPreviousPage(SCHEMA_TYPE type, String relationName) throws Exception {
		SQLResultSetResults result = getResultSet(type, relationName);
		((SQLStandardResultSetResults)result).previousPage(ConnectionManager.getInstance().getConnection());
		// Do not update button status due to tab bug
		//updateButtonStatus(type, relationName);
	}
	
	public void navigateToNextPage(SCHEMA_TYPE type, String relationName) throws Exception {
		SQLResultSetResults result = getResultSet(type, relationName);
		((SQLStandardResultSetResults)result).nextPage(ConnectionManager.getInstance().getConnection());
		// Do not update button status due to tab bug
		//updateButtonStatus(type, relationName);
	}

	public void navigatetoFirstPage(SCHEMA_TYPE type, String relationName) throws Exception {
		navigateToPage(type, relationName, -1);
	}

	public void navigateToLastPage(SCHEMA_TYPE type, String relationName) throws Exception {
		navigateToPage(type, relationName, Integer.MAX_VALUE);
	}
	
	private int getTotalPages(SCHEMA_TYPE type, String relationName) {
		HashMap<String, Integer> schemaMap;
		if (type == SCHEMA_TYPE.SOURCE)
			schemaMap = sourceMap;
		else
			schemaMap = targetMap;
		int total = schemaMap.get(relationName);
		return total;
	}
	
	public static SQLResultSetResults getResultSet(SCHEMA_TYPE type, String relationName) {

		SQLResultSetResults[] allSets = SQLResultSetCollection.getInstance().getResultSets(TableViewManager.getInstance().getManagerId(), type.toString());
		
		for (SQLResultSetResults result : allSets) {
			if (result.getName().equals(relationName)) {
				return result;
			}
		}
		
		return null;
	}
	
	public static boolean[] getNavButtonStatus(SCHEMA_TYPE type, String relationName) {
		boolean[] toReturn = {false, false, false, false};
		
		SQLStandardResultSetResults result = (SQLStandardResultSetResults)getResultSet(type, relationName);
		
		if (result.hasNextPage()) {
			toReturn[2] = true;
			toReturn[3] = true;
		}
		
		if (result.hasPreviousPage()) {
			toReturn[0] = true;
			toReturn[1] = true;
		}
		
		return toReturn;
	}
	
	public void updateButtonStatus(SCHEMA_TYPE type, String relationName) {
		GenericTableView tableView = (GenericTableView) TableViewManager.getInstance().getViewForId(type.toString());
		
		boolean[] buttonStatus = getNavButtonStatus(type, relationName);
		
		tableView.setNavButtonStatus(buttonStatus[0], buttonStatus[1], buttonStatus[2], buttonStatus[3]);
	}
}
