package org.vagabond.rcp.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.vagabond.rcp.controller.Filter;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;
import com.quantum.view.tableview.TableView;

public class GenericTableView extends TableView {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			GenericTableView.class);
	
	public static final String ID = null;
	public static final String VIEW_ID = null;
	
	protected List<Filter> filters = Collections.synchronizedList(new ArrayList<Filter>());
	
	/**
	 * Generic constructor
	 */
	public GenericTableView() {
		SQLResultSetCollection.getInstance().addPropertyChangeListener(this);
	}

	public void setSelection (String relId) {
		for(ResultSetViewer view: resultSetViewers) {
			if (view.getResultSet().getName().equals(relId))
			{
				getTabFolder().setSelection(view.getTabItem());
				break;
			}
		}
	}
	
	public void setFocus() {
	}

	public void dispose() {
		SQLResultSetCollection.getInstance().removePropertyChangeListener(this);
		super.dispose();
	}
	
	public Filter findFilterFor(SQLResultSetResults results) {
		Filter filter = null;
		for (Iterator<Filter> i = this.filters.iterator(); filter == null && i.hasNext();) {
			Filter temp = i.next();
			if (results != null && results.equals(temp.getResultSet())) {
				filter = temp;
			}
		}
		return filter;
	}
	
	protected void filterResultSets (VagaSelectionEvent e, boolean source) {
		clearResultSets();
		this.resultSetViewers.clear();
		for (Iterator<Filter> i = this.filters.iterator(); i.hasNext();) {
			Filter filter = i.next();
			try {
				SQLResultSetResults r = filter.filterByEvent(e, source);
				if (r != null)
					this.resultSetViewers.add(new ResultSetViewer(this, r));
			} catch (Exception e1) {
				LoggerUtil.logException(e1, log);
			}
		}
	}

	private void clearResultSets() {
		// Delete and recreate resultset viewers
		for (Iterator<ResultSetViewer> i = this.resultSetViewers.iterator(); i.hasNext();) {
			ResultSetViewer viewer = i.next();
			viewer.dispose();
		}
	}
}
