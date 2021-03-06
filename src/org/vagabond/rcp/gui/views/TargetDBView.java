package org.vagabond.rcp.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.controller.Filter;
import org.vagabond.rcp.controller.TargetDBViewActionGroup;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.selection.VagaSelectionListener;

import com.quantum.sql.SQLResultSetResults;
import com.quantum.view.tableview.ResultSetViewer;


public class TargetDBView extends GenericTableView implements VagaSelectionListener {
	public static final String ID = "org.vagabond.rcp.gui.views.tdbview";
	public static final String VIEW_ID = "Target";
	
	public static final Set<ModelType> interest;
	
	static {
		interest = new HashSet<ModelType> ();
		interest.add(ModelType.None);
		interest.add(ModelType.SourceRelation);
		interest.add(ModelType.Mapping);
		interest.add(ModelType.Correspondence);
	}

	public TargetDBView() {
		// Cannot use VIEW_ID because it's set to null in GenericTableView
		// and cannot change the null to something else (it will break something else)
		NavViewType = "Target";
		TableViewManager.getInstance().setTargetView(this);
	}
	
	public static TargetDBView getInstance() {
		return (TargetDBView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	public void initActions() {
		GlobalSelectionController.addSelectionListener(this);
        this.actionGroup = new TargetDBViewActionGroup(this);

//        IActionBars actionBars = getViewSite().getActionBars();
//        this.actionGroup.fillActionBars(actionBars);
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		if ("resultSets".equals(event.getPropertyName())) {
			SQLResultSetResults selection = getSelectedResultSet();

			Collection<SQLResultSetResults> additions = getAddedResultSets();
			for (SQLResultSetResults results : additions) {
				this.resultSetViewers.add(new ResultSetViewer(this, results));
				this.filters.add(new Filter(results));
			}

			Collection<SQLResultSetResults> deletions = getRemovedResultSets();
			for (SQLResultSetResults results : deletions) {
				ResultSetViewer viewer = findViewerFor(results);
				Filter filter = findFilterFor(results);
				this.resultSetViewers.remove(viewer);
				this.filters.remove(filter);
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
		SQLResultSetResults[] results = TableViewManager.getInstance()
				.getResultSets(VIEW_ID);
		Collection<SQLResultSetResults> collection = (results == null)
				? new ArrayList<SQLResultSetResults>()
				: new ArrayList<SQLResultSetResults>(Arrays.asList(results));
		Collection<SQLResultSetResults> visible = getResultSets();
		visible.removeAll(collection);
		return visible;
	}

	private Collection<SQLResultSetResults> getAddedResultSets() {
		SQLResultSetResults[] results = TableViewManager.getInstance()
				.getResultSets(VIEW_ID);
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

	@Override
	public void event(VagaSelectionEvent e) {
		if (e.isEmpty()) {
			filterResultSets(e, false);
			return;
		}  else if (e.isReset()) {
			filterResultSets(e, false);
			return;
		}
		
		if (e.isLimitScope()) {
			if (e.getElementType().equals(ModelType.Correspondence) 
					|| e.getElementType().equals(ModelType.Mapping)) {
				filterResultSets(e, false);
			}
		}
		// normal navigation, just listen on SourceRelationEvents
		else {
			if (e.getElementType().equals(ModelType.TargetRelation))
				setSelection(e.getElementIds().iterator().next());
		}
	}

	@Override
	public Set<ModelType> interestedIn() {
		return interest;
	}
	
}
