package org.vagabond.rcp.gui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.vagabond.rcp.controller.Filter;
import org.vagabond.rcp.controller.TableNavHandler;
import org.vagabond.rcp.model.TableViewManager;
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
	
	public static String ID = null;
	public static String VIEW_ID = null;
	protected String NavViewType;
	
	protected List<Filter> filters = Collections.synchronizedList(new ArrayList<Filter>());

	protected Button fpButton;
	protected Button ppButton;
	protected Button npButton;
	protected Button lpButton;
	
	public enum NAV_ACTION {FIRST_PAGE(1), PREVIOUS_PAGE(2), NEXT_PAGE(3), LAST_PAGE(4);
		private int value;
		
		private NAV_ACTION(int value) {
			this.value = value;
		}
	};
	
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
	
	public void createPartControl(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		parent.setLayout(gridLayout);
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.horizontalSpan = 4;
		gridData.verticalAlignment = GridData.FILL;
		
		this.fpButton = new Button(parent, SWT.PUSH);
		this.fpButton.setText("|<");
		this.fpButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                navButtonAction(NAV_ACTION.FIRST_PAGE);
            }
        });
		
		this.ppButton = new Button(parent, SWT.PUSH);
		this.ppButton.setText("<");
		this.ppButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                navButtonAction(NAV_ACTION.PREVIOUS_PAGE);
            }
        });
		
		this.npButton = new Button(parent, SWT.PUSH);
		this.npButton.setText(">");
		this.npButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                navButtonAction(NAV_ACTION.NEXT_PAGE);
            }
        });
		
		this.lpButton = new Button(parent, SWT.PUSH);
		this.lpButton.setText(">|");
		this.lpButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                navButtonAction(NAV_ACTION.LAST_PAGE);
            }
        });
		
        this.tabs = new TabFolder(parent, SWT.PUSH);
		this.tabs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				fireSelectionChangedEvent();
			}
		});
		
		this.tabs.setLayoutData(gridData);
		
        initActions();

		SQLResultSetResults[] resultSets = TableViewManager.getInstance().getResultSets(VIEW_ID);
		for (int i = 0, length = resultSets == null ? 0 : resultSets.length; i < length; i++) {
			this.resultSetViewers.add(new ResultSetViewer(this, resultSets[i]));
			this.filters.add(new Filter(resultSets[i]));
		}
	}
	
	public void navButtonAction(NAV_ACTION action) {
		String relationName = this.tabs.getItem(this.tabs.getSelectionIndex()).getText();
		TableNavHandler.SCHEMA_TYPE sourceOrTarget = NavViewType.equals("Source") ? TableNavHandler.SCHEMA_TYPE.SOURCE : TableNavHandler.SCHEMA_TYPE.TARGET;
		try {
			switch ((int) action.value) {
				case 1:
					TableNavHandler.getInstance().navigatetoFirstPage(sourceOrTarget, relationName);
					break;
				case 2:
					TableNavHandler.getInstance().navigateToPreviousPage(sourceOrTarget, relationName);
					break;
				case 3:
					TableNavHandler.getInstance().navigateToNextPage(sourceOrTarget, relationName);
					break;
				case 4:
					TableNavHandler.getInstance().navigateToLastPage(sourceOrTarget, relationName);
					break;
				default:
					log.debug("TableViews navigation buttons switch loop fails.");
					break;
			}
		} catch (Exception e) {
			log.debug("TableViews navigation button actions has caught an exception.");
			e.printStackTrace();
		}
	}
	
	public void setNavButtonStatus(boolean first, boolean previous, boolean next, boolean last) {
		fpButton.setEnabled(first);
		ppButton.setEnabled(previous);
		npButton.setEnabled(next);
		lpButton.setEnabled(last);
	}
	
	public class NavButtonListener implements SelectionListener {

	    @Override
	    public void widgetDefaultSelected(SelectionEvent arg0) {}

	    @Override
	    public void widgetSelected(SelectionEvent arg0) {}

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
