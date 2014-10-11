package org.vagabond.rcp.controller;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.explanation.marker.ScenarioDictionary;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.gui.views.CorrView;
import org.vagabond.rcp.gui.views.MappingsView;
import org.vagabond.rcp.gui.views.TransView;
import org.vagabond.rcp.mapview.controller.GraphEditPart;
import org.vagabond.rcp.mapview.view.MapGraphView;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.ResourceManager;
import org.vagabond.util.ConnectionManager;
import org.vagabond.util.LoggerUtil;
import org.vagabond.xmlmodel.RelationType;
import org.vagabond.xmlmodel.SchemaType;

import com.quantum.actions.BookmarkSelectionUtil;
import com.quantum.adapters.AdapterFactory;
import com.quantum.model.Bookmark;
import com.quantum.model.BookmarkCollection;
import com.quantum.model.JDBCDriver;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResults;

public class LoaderUtil {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			LoaderUtil.class);

	public enum ScenarioLoadState {
		None,
		ConnectedToDB,
		LoadedScenario,
		ViewsUpDated,
	}
	
	public static final String QUERY_TEMPLATE_DIR = "resource/queries";
	
	private static LoaderUtil instance = new LoaderUtil();
	
	private ScenarioLoadState state = ScenarioLoadState.None;
	
	private int entriesPerPage = 6;
	
	private LoaderUtil () {
		
	}
	
	public static LoaderUtil getInstance () {
		return instance;
	}
	
	// Connect to the database specified by the details in the preference pane
	public void connectToDB() throws Exception {
		int port;
		
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");
		String portString = Activator.getDefault().getPreferenceStore().getString("PORT");
		if (portString == null || portString.equals(""))
			port = 5432;
		else
			port = Integer.parseInt(portString);
		
		if (hostString == null || hostString.equals("") 
				|| databaseString == null || databaseString.equals("")) {
			throw new Exception ("Database options not set!");
		}
		
		LoaderUtil.getInstance().connectToDB(hostString, databaseString, 
				usernameString, passwordString, port);
	}

	
	// Connect to the database specified by the details in the preference pane
	public void connectToDB(String hostString, String databaseString,
			String usernameString, String passwordString, int port) throws Exception {
		log.debug("try to connect to <" + hostString + ":" + databaseString 
				+ "> as user <" + usernameString + ">");

		try {
			if (hostString == null || hostString.equals("") 
					|| databaseString == null || databaseString.equals("")) {
				throw new Exception ("Database parameters empty!");
			}

			Connection c = ConnectionManager.getInstance().getConnection(hostString, 
					databaseString, usernameString, passwordString, port);

			// Connect to QuantumDB's connection manager
			// Create a bookmark
			Bookmark bookmark = new Bookmark();
			bookmark.setName(databaseString);
			bookmark.setUsername(usernameString);
			bookmark.setPassword(passwordString);
			bookmark.setConnect("jdbc:postgresql://" + hostString + ":5432/" + databaseString);
			bookmark.setJDBCDriver(new JDBCDriver("org.postgresql.Driver", 
					new String[0], AdapterFactory.POSTGRES));
			bookmark.setConnection(c);
			BookmarkSelectionUtil.getInstance().setLastUsedBookmark(bookmark);
			BookmarkCollection.getInstance().addBookmark(bookmark);
		} catch (Exception e) {
			setState(ScenarioLoadState.None);
			throw (e);
		}
		
		setState(ScenarioLoadState.ConnectedToDB);
	}
	
	// Load the file and connect to DB if not done
	public void loadSchemaFile(String filePath) throws Exception {
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		loadSchemaFile(filePath, databaseString);
	}
	
	public void loadSchemaFile () throws Exception {
		String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		
		if (filePath == null || filePath.equals(""))
			throw new Exception ("No default scenario XML file set!");
		LoaderUtil.getInstance().loadSchemaFile(filePath, databaseString);
	}
	
	
	// Load the file specified in the preference pane and load the scenario to the database
	public void loadSchemaFile(String filePath, String databaseString) throws Exception {
		try {
			if (getState().equals(ScenarioLoadState.None))
				connectToDB();
			
			ModelLoader.getInstance().loadToInst(filePath);
			MapScenarioHolder h = MapScenarioHolder.getInstance();
//			ScenarioDictionary.getInstance().setSchemas(
//					h.getScenario().getSchemas().getSourceSchema(),
//					h.getScenario().getSchemas().getTargetSchema());

			Connection c = ConnectionManager.getInstance().getConnection();

			// Load scenario into db
			DatabaseScenarioLoader.getInstance().loadScenario(c);
			ScenarioDictionary.getInstance().initFromScenario();
			
			try {
				QueryHolder.getInstance().loadFromURLs(ResourceManager.getInstance()
						.getResourcesAsStreams("resource/queries", ".xml"));

			} 
			catch (Exception e) {
				throw new Exception ("Could not load query templates from " 
						+ QUERY_TEMPLATE_DIR, e);
			}

			Bookmark bookmark = BookmarkCollection.getInstance().find(databaseString);

			// Generate queries
			SchemaType source = h.getScenario().getSchemas().getSourceSchema();
			SchemaType target = h.getScenario().getSchemas().getTargetSchema();
			
			RelationType[] sourceRelArray = source.getRelationArray();
			
			for (int i = 0; i < sourceRelArray.length; i++) {
				
				String relationName = sourceRelArray[i].getName();
				
				String countSrcSQL = "select count(*) from source." + relationName;
				SQLResultSetResults countSrcResult = (SQLResultSetResults) MultiSQLServer.getInstance().execute(bookmark, c, countSrcSQL);
				int numberOfRows = Integer.parseInt(countSrcResult.getRows()[0].getAsStringArray()[0]);
				int totalPages = (int) Math.ceil((double)numberOfRows / (double)entriesPerPage);
				
				loadTableToView(databaseString, "Source", relationName, 1);
				TableNavHandler.getInstance().addNewRelation(TableNavHandler.SCHEMA_TYPE.SOURCE, relationName, TableViewManager.getInstance().getManagerId(), totalPages);
			}

			for (RelationType rel : target.getRelationArray()) {
				
				String relationName = rel.getName();
				
				String countTarSQL = "select count(*) from target." + relationName;
				SQLResultSetResults countTarResult = (SQLResultSetResults) MultiSQLServer.getInstance().execute(bookmark, c, countTarSQL);
				int numberOfRows = Integer.parseInt(countTarResult.getRows()[0].getAsStringArray()[0]);
				int totalPages = (int) Math.ceil((double)numberOfRows / (double)entriesPerPage);
				
				loadTableToView(databaseString, "Target", relationName, 1);
				TableNavHandler.getInstance().addNewRelation(TableNavHandler.SCHEMA_TYPE.TARGET, relationName, TableViewManager.getInstance().getManagerId(), totalPages);
			}
			setState(ScenarioLoadState.LoadedScenario);
			
		} catch (Exception e) {
			setState(ScenarioLoadState.None);
			throw e;
		}
	}

	public void updateViews () throws Exception {
		try {
			MapGraphView.getInstance().getViewer().setContents(
					ContentProvider.getInstance().generateGraph());
			GraphEditPart part = (GraphEditPart) MapGraphView.getInstance().getViewer()
					.getRootEditPart().getContents();
			MapGraphView.getInstance().getViewer().flush();
			part.setLayoutConstraints();
			part.refresh();
			
			TransView.getInstance().setTransformations(MapScenarioHolder
					.getInstance().getDocument().getMappingScenario()
					.getTransformations().getTransformationArray());
			CorrView.getInstance().setCorrespondences(MapScenarioHolder
					.getInstance().getDocument().getMappingScenario()
					.getCorrespondences().getCorrespondenceArray());
			MappingsView.getInstance().setMappings(MapScenarioHolder
					.getInstance().getDocument().getMappingScenario()
					.getMappings().getMappingArray());
			setState(ScenarioLoadState.ViewsUpDated);
		} catch (Exception e) {
			LoggerUtil.logException(e, log);
			setState(ScenarioLoadState.None);
			throw e;
		}
	}
	
	public void loadSchemaFileAndUpdate (String fileName) throws Exception {
		
		loadSchemaFile(fileName);
		updateViews();
		
	}
	
	public void setState(ScenarioLoadState state) {
		this.state = state;
	}

	public ScenarioLoadState getState() {
		return state;
	}
	
	
	public boolean loadTableToView(String databaseString, String sourceOrTarget, String relationName, int fromPage) throws Exception {
		Bookmark bookmark = BookmarkCollection.getInstance().find(databaseString);
		Connection c = ConnectionManager.getInstance().getConnection();
		
		String query;
		if (fromPage > 1)
			query = "select * from " + sourceOrTarget.toLowerCase() + "." + relationName + " order by tid limit " + entriesPerPage + " offset " + (fromPage - 1) * entriesPerPage;
		else
			query = "select * from " + sourceOrTarget.toLowerCase() + "." + relationName;
		bookmark.addQuery(query);
		SQLResults results = MultiSQLServer.getInstance().execute(bookmark, c, query);
		
		if (results.isResultSet()) {
			
			((SQLResultSetResults)results).setName(relationName);

			SQLResultSetCollection.getInstance()
					.addSQLResultSet(TableViewManager.getInstance().getManagerId(), 
					sourceOrTarget, (SQLResultSetResults) results);

		}
		
		// Automatically disable and enable the navigation buttons
		// Commented out because it cannot detect tab change
		/**
		TableNavHandler.SCHEMA_TYPE schemaType = sourceOrTarget.equalsIgnoreCase("Source") ? TableNavHandler.SCHEMA_TYPE.SOURCE : TableNavHandler.SCHEMA_TYPE.TARGET;
		GenericTableView tableView = (GenericTableView) TableViewManager.getInstance().getViewForId(sourceOrTarget);
		boolean[] buttonStatus = TableNavHandler.getNavButtonStatus(schemaType, relationName);
		tableView.setNavButtonStatus(buttonStatus[0], buttonStatus[1], buttonStatus[2], buttonStatus[3]);
		*/
		return true;
		
	}
	
	public boolean updateTableFromView(String databaseString, String sourceOrTarget, String relationName, int fromPage) throws Exception {
		
		SQLResultSetCollection.getInstance().removeSQLResultSetByName(TableViewManager.getInstance().getManagerId(), 
				sourceOrTarget, relationName);
		
		loadTableToView(databaseString, sourceOrTarget, relationName, fromPage);
	
		return true;
	}
	
}
