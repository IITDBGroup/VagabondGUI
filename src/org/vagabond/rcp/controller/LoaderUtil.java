package org.vagabond.rcp.controller;

import java.sql.Connection;

import org.apache.log4j.Logger;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.explanation.marker.SchemaResolver;
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
	
	private LoaderUtil () {
		
	}
	
	public static LoaderUtil getInstance () {
		return instance;
	}
	
	// Connect to the database specified by the details in the preference pane
	public void connectToDB() throws Exception {
		String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
		String passwordString = Activator.getDefault().getPreferenceStore().getString("PASSWORD");

		if (hostString == null || hostString.equals("") 
				|| databaseString == null || databaseString.equals("")) {
			throw new Exception ("Database options not set!");
		}
		
		LoaderUtil.getInstance().connectToDB(hostString, databaseString, 
				usernameString, passwordString);
	}

	
	// Connect to the database specified by the details in the preference pane
	public void connectToDB(String hostString, String databaseString,
			String usernameString, String passwordString) throws Exception {
		log.debug("try to connect to <" + hostString + ":" + databaseString 
				+ "> as user <" + usernameString + ">");

		try {
			if (hostString == null || hostString.equals("") 
					|| databaseString == null || databaseString.equals("")) {
				throw new Exception ("Database parameters empty!");
			}

			Connection c = ConnectionManager.getInstance().getConnection(hostString, 
					databaseString, usernameString, passwordString);

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
			SchemaResolver.getInstance().setSchemas(
					h.getScenario().getSchemas().getSourceSchema(),
					h.getScenario().getSchemas().getTargetSchema());

			Connection c = ConnectionManager.getInstance().getConnection();

			// Load scenario into db
			DatabaseScenarioLoader.getInstance().loadScenario(c);

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
			String query;
			SQLResults results = null;

			for (RelationType rel : source.getRelationArray()) {
				query = "select * from source." + rel.getName();
				bookmark.addQuery(query);
				results = MultiSQLServer.getInstance().execute(bookmark, c, query);
				if (results.isResultSet()) {
					((SQLResultSetResults)results).setName(rel.getName());

					SQLResultSetCollection.getInstance()
							.addSQLResultSet(TableViewManager.getInstance().getManagerId(), 
							"Source", (SQLResultSetResults) results);
				}
			}

			for (RelationType rel : target.getRelationArray()) {
				query = "select * from target." + rel.getName();
				bookmark.addQuery(query);
				results = MultiSQLServer.getInstance().execute(bookmark, c, query);
				if (results.isResultSet()) {
					((SQLResultSetResults)results).setName(rel.getName());

					SQLResultSetCollection.getInstance()
							.addSQLResultSet(TableViewManager.getInstance().getManagerId(), 
							"Target", (SQLResultSetResults) results);
				}
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
	
}
