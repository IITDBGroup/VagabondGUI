package org.vagabond.rcp.gui.views;

import com.quantum.sql.SQLResultSetCollection;
import com.quantum.view.tableview.TableView;

public class GenericTableView extends TableView {
	public static final String ID = null;
	public static final String VIEW_ID = null;
	
	/**
	 * Generic constructor
	 */
	public GenericTableView() {
		SQLResultSetCollection.getInstance().addPropertyChangeListener(this);
	}

	public void setFocus() {
	}

	public void dispose() {
		SQLResultSetCollection.getInstance().removePropertyChangeListener(this);
		super.dispose();
	}
}
