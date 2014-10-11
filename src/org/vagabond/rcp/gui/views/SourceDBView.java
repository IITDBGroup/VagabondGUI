package org.vagabond.rcp.gui.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.vagabond.explanation.generation.prov.SourceProvParser;
import org.vagabond.explanation.marker.TupleMarker;
import org.vagabond.rcp.controller.DBViewActionGroup;
import org.vagabond.rcp.controller.Filter;
import org.vagabond.rcp.model.TableViewManager;
import org.vagabond.rcp.selection.GlobalSelectionController;
import org.vagabond.rcp.selection.VagaSelectionEvent;
import org.vagabond.rcp.selection.VagaSelectionEvent.ModelType;
import org.vagabond.rcp.selection.VagaSelectionListener;
import org.vagabond.rcp.util.PluginLogProvider;

import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResultSetResults.Row;
import com.quantum.view.tableview.ResultSetViewer;

public class SourceDBView extends GenericTableView implements VagaSelectionListener {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			SourceDBView.class);
	
	public static final String ID = "org.vagabond.rcp.gui.views.sdbview";
	public static final String VIEW_ID = "Source";

	public static final Set<ModelType> interest;
	
	static {
		interest = new HashSet<ModelType> ();
		interest.add(ModelType.None);
		interest.add(ModelType.SourceRelation);
		interest.add(ModelType.Mapping);
		interest.add(ModelType.Correspondence);
	}

	public SourceDBView() {
		// Cannot use VIEW_ID because it's set to null in GenericTableView
		// and cannot change the null to something else (it will break something else)
		NavViewType = "Source";
		TableViewManager.getInstance().setSourceView(this);
	}
	
	public static SourceDBView getInstance() {
		return (SourceDBView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	public void initActions() {
		GlobalSelectionController.addSelectionListener(this);
        this.actionGroup = new DBViewActionGroup(this);

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
		SQLResultSetResults[] results = TableViewManager.getInstance().getResultSets(VIEW_ID);
		Collection<SQLResultSetResults> collection = (results == null)
				? new ArrayList<SQLResultSetResults>()
				: new ArrayList<SQLResultSetResults>(Arrays.asList(results));
		Collection<SQLResultSetResults> visible = getResultSets();
		visible.removeAll(collection);
		return visible;
	}

	private Collection<SQLResultSetResults> getAddedResultSets() {
		SQLResultSetResults[] results = TableViewManager.getInstance().getResultSets(VIEW_ID);
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
	
	public void highlightProvenance(SourceProvParser parser) {
		Iterator<?> iterator = parser.getAllProv().getTuplesInProv().iterator();
		List<SQLResultSetResults.Row> rows;
		ResultSetViewer viewer;
		
		for (Iterator<ResultSetViewer> i = this.resultSetViewers.iterator(); i.hasNext();) {
    		rows = new ArrayList<Row>();
			viewer = i.next();
			iterator = parser.getAllProv().getTuplesInProv().iterator();
			
		    while (iterator.hasNext()) {
		    	TupleMarker tuple = (TupleMarker)iterator.next();
		    	String tableName = tuple.getRel();
			
				if (tableName.equals(viewer.getTabItem().getText())) {
					int tid = Integer.parseInt(tuple.getTid());
					
					for (Iterator<SQLResultSetResults.Row> j = viewer.getResultSet().iterator();
							j.hasNext();) {
						SQLResultSetResults.Row r = j.next();
						if (tid == ((Number)r.get(1)).intValue()) {
							rows.add(r);
						}
					}					
				}
		    }
		    
			if (rows.size() > 0) {
				viewer.setSelection(new StructuredSelection(rows));
				viewer.getTabItem().setText("*"+viewer.getResultSet().getName());
				// when target schema gets unselected, the name should change back
				//  do with global selection controller?
			}
		}
	}
	
	public void resetSelections() {
    	for (Iterator<ResultSetViewer> i = this.resultSetViewers.iterator(); i.hasNext();) {
			ResultSetViewer viewer = i.next();
			viewer.resetSelection();
			viewer.getTabItem().setText(viewer.getResultSet().getName());
		}
	}

	@Override
	public void event(VagaSelectionEvent e) {
		if (e.isEmpty()) {
			resetSelections();
			filterResultSets(e, true);
			return;
		} else if (e.isReset()) {
			resetSelections();
			filterResultSets(e, true);
			return;
		}
		
		if (e.isLimitScope()) {
			if (e.getElementType().equals(ModelType.Correspondence) 
					|| e.getElementType().equals(ModelType.Mapping)) {
				filterResultSets(e, true);
			}
		}
		// normal navigation, just listen on SourceRelationEvents
		else {
			if (e.getElementType().equals(ModelType.SourceRelation))
				setSelection(e.getElementIds().iterator().next());
		}
	}
	
	@Override
	public Set<ModelType> interestedIn() {
		return interest;
	}
}

