package org.vagabond.rcp.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.vagabond.rcp.controller.Filter;

import com.quantum.sql.SQLResultSetCollection;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;
import com.quantum.view.tableview.TableView;

public class GenericTableView extends TableView {
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
}
