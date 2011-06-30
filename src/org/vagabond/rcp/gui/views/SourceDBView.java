package org.vagabond.rcp.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.model.TableViewManager;

import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;


public class SourceDBView extends GenericTableView {
	public static final String ID = "org.vagabond.rcp.gui.views.sdbview";
	public static final String VIEW_ID = "Source";
	
	public SourceDBView() {
		TableViewManager.getInstance().setSourceView(this);
	}
	
	public static SourceDBView getInstance() {
		return (SourceDBView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	public void createPartControl(Composite parent) {
        this.tabs = new TabFolder(parent, SWT.NONE);
		this.tabs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				fireSelectionChangedEvent();
			}
		});

        initActions();

		SQLResultSetResults[] resultSets = TableViewManager.getInstance().getResultSets(VIEW_ID);
		for (int i = 0, length = resultSets == null ? 0 : resultSets.length; i < length; i++) {
			this.resultSetViewers.add(new ResultSetViewer(this, resultSets[i]));
		}
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if ("resultSets".equals(event.getPropertyName())) {
			SQLResultSetResults selection = getSelectedResultSet();

			Collection<SQLResultSetResults> additions = getAddedResultSets();
			for (SQLResultSetResults results : additions) {
				this.resultSetViewers.add(new ResultSetViewer(this, results));
			}

			Collection<SQLResultSetResults> deletions = getRemovedResultSets();
			for (SQLResultSetResults results : deletions) {
				ResultSetViewer viewer = findViewerFor(results);
				this.resultSetViewers.remove(viewer);
				viewer.dispose();
			}

			SQLResultSetResults newSelection = getSelectedResultSet();
			if (selection != null && newSelection == null) {
				fireSelectionChangedEvent();
			} else if (selection == null && newSelection != null) {
				fireSelectionChangedEvent();
			} else if (selection != null && !selection.equals(newSelection)) {
				fireSelectionChangedEvent();
			}
		}
	}
	
	private Collection<SQLResultSetResults> getRemovedResultSets() {
		SQLResultSetResults[] results = TableViewManager.getInstance().getResultSets(this.VIEW_ID);
		Collection<SQLResultSetResults> collection = (results == null)
				? new ArrayList<SQLResultSetResults>()
				: new ArrayList<SQLResultSetResults>(Arrays.asList(results));
		Collection<SQLResultSetResults> visible = getResultSets();
		visible.removeAll(collection);
		return visible;
	}

	private Collection<SQLResultSetResults> getAddedResultSets() {
		SQLResultSetResults[] results = TableViewManager.getInstance().getResultSets(this.VIEW_ID);
		Collection<SQLResultSetResults> collection = (results == null)
				? new ArrayList<SQLResultSetResults>()
				: new ArrayList<SQLResultSetResults>(Arrays.asList(results));
		collection.removeAll(getResultSets());
		return collection;
	}
	

	private Collection<SQLResultSetResults> getResultSets() {
		List<SQLResultSetResults> list = new ArrayList<SQLResultSetResults>();
		for (Iterator<ResultSetViewer> i = this.resultSetViewers.iterator(); i.hasNext();) {
			ResultSetViewer viewer = i.next();
			list.add(viewer.getResultSet());
		}
		return list;
	}
}
