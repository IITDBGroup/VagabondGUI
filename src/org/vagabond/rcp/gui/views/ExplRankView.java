package org.vagabond.rcp.gui.views;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.IExplanationSet;
import org.vagabond.rcp.controller.ExplGenContentProvider;
import org.vagabond.rcp.controller.ExplGenLabelProvider;
import org.vagabond.rcp.controller.ExplViewActionGroup;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

import com.quantum.sql.SQLResultSetResults;

public class ExplRankView extends ViewPart {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			ExplRankView.class);
	
	public static final String ID = "org.vagabond.rcp.gui.views.explrankview";

	private TreeViewer viewer;
	private ExplanationSetGenerator gen = new ExplanationSetGenerator();
	protected ExplViewActionGroup actionGroup;
	private IExplanationSet curSet = null;
	

	public static ExplRankView getInstance() {
		return (ExplRankView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	public void createPartControl(Composite parent) {
		initActions();
		setLayout(parent);
		createViewer(parent);
	}
	
	public void initActions() {
        this.actionGroup = new ExplViewActionGroup(this);

        IActionBars actionBars = getViewSite().getActionBars();
        this.actionGroup.fillActionBars(actionBars);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, true);
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(new ExplGenContentProvider());
		viewer.setLabelProvider(new ExplGenLabelProvider());
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);		
	}

	public void updateView (IExplanationSet explSet) {
		this.curSet = explSet;
		viewer.setInput(explSet);
		this.actionGroup.updateActionBars();
	}
	
//	public void updateView() {
//		if (!col.hasNext())
//			col.resetIter();
//		
//		viewer.setInput(col.next());
//		this.actionGroup.updateActionBars();
//	}
	
//	public ExplanationCollection getExplCollection() {
//		return col;
//	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
//	public void generateErrorExpl(ITreeSelection selection) {
//		IMarkerSet m;
//
//		try {
//			m = parseMarkers(selection);
//			col = gen.findExplanations(m);
//			updateView();
//		} catch (Exception e) {
//			LoggerUtil.logException(e, log);
//		}
//	}
	
	private IMarkerSet parseMarkers(ITreeSelection selection) throws Exception {
		String relation, tid, attribute;
		SQLResultSetResults.Row r;
		IMarkerSet m = MarkerFactory.newMarkerSet();
		IAttributeValueMarker e;
		TreePath[] paths = selection.getPaths();
		int i = 0;
		
		for (TreePath p : paths) {
			i = 0;
			r = (SQLResultSetResults.Row)p.getFirstSegment();
			relation = r.getResultSet().getName();
			tid = r.getAsStringArray()[0];
			attribute = "";
			for (String s : r.getAsStringArray()) {
				i = i+1;
				if (s.equals((String)p.getLastSegment()))
					attribute = r.getResultSet().getColumnName(i);
			}
			e = (IAttributeValueMarker)MarkerFactory.newAttrMarker(relation,tid,attribute);
			m.add(e);
		}
		
		return m;
	}
}