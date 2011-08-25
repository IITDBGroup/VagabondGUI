package org.vagabond.rcp.controller;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.rcp.Activator;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.ConnectionManager;
import org.vagabond.xmlmodel.CorrespondenceType;
import org.vagabond.xmlmodel.MappingType;
import org.vagabond.xmlmodel.RelAtomType;

import com.quantum.model.Bookmark;
import com.quantum.model.BookmarkCollection;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetResults;

public class Filter {
	static Logger log = PluginLogProvider.getInstance().getLogger(Filter.class);
	
	SQLResultSetResults result;
	SQLResultSetResults filterResult;
	Bookmark bookmark;
	String title;
	
	public Filter(SQLResultSetResults result) {
		String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
		
		this.result = result;
		this.bookmark = BookmarkCollection.getInstance().find(databaseString);

	}
	
	public SQLResultSetResults byMapping(Collection<String> maps, boolean source) throws Exception {
		if (source) {
			filterResult = null;
			for (String map : maps) {
				MappingType m = MapScenarioHolder.getInstance().getMapping(map);
				for (RelAtomType atom : m.getForeach().getAtomArray()) {
					String rel = atom.getTableref();
					if (result.getName().equals(rel)) {
						filterResult = result;
						break;
					}
				}
			}
		} else {
			StringBuffer mapList = new StringBuffer();;
			StringBuffer colList = new StringBuffer();;
			String query;
			Connection c = ConnectionManager.getInstance().getConnection();

			for(String mapName: maps) {
				mapList.append("('" + mapName + "'),");
			}
			mapList.append("('')");
			
			for (String colName: result.getColumnNames()) {
				colList.append(colName + ',');
			}
			colList.deleteCharAt(colList.length() - 1);
			
			query = QueryHolder.getQuery("MapAndTransProv.GetColsForMappings")
					.parameterize(colList.toString(), "target." + result.getName(), mapList.toString());
			
			this.filterResult = (SQLResultSetResults) MultiSQLServer.getInstance().
					execute(bookmark, c, query);
			this.filterResult.setName(result.getName());
		}
		return filterResult;
	}
	
	public SQLResultSetResults byCorrespondence(Collection<String> names, boolean source) throws Exception {
		Set<String> maps = new HashSet<String>();
		
		for (String name : names) {
			CorrespondenceType corr = MapScenarioHolder.getInstance().getCorr(name.toLowerCase());
			Collection<MappingType> mappings = MapScenarioHolder.getInstance().getMapsForCorr(corr);
	
			for (Iterator<MappingType> i = mappings.iterator(); i.hasNext();) {
				MappingType m = i.next();
				maps.add(m.getId());
			}
		}
		
		return byMapping(maps, source);
	}

	public SQLResultSetResults filterByEvent (VagaSelectionEvent e, boolean source) throws Exception {
		if (e.isEmpty())
			return result;
		if (e.getElementType().equals(ModelType.Mapping))
			return byMapping(e.getElementIds(), source);
		if (e.getElementType().equals(ModelType.Correspondence))
			return byCorrespondence(e.getElementIds(), source);
		throw new Exception("do not know how to filter on event " + e.toString());
	}
	
	
	public SQLResultSetResults getResultSet() {
		return this.result;
	}
	
}
