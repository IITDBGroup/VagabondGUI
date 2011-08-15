package org.vagabond.rcp.gui.views;

import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.rcp.controller.ExplGenContentProvider;
import org.vagabond.rcp.controller.ExplGenLabelProvider;

import com.quantum.sql.SQLResultSetResults;

public class ExplView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.explview";

	private Combo combo;
	private TreeViewer viewer;
	private ExplanationSetGenerator gen = new ExplanationSetGenerator();
	private ExplanationCollection col;

	public static ExplView getInstance() {
		return (ExplView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}

	public void createPartControl(Composite parent) {
		setLayout(parent);
		createViewer(parent);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, true);
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(new ExplGenContentProvider());
		viewer.setLabelProvider(new ExplGenLabelProvider());
		// Provide the input to the ContentProvider
		//viewer.setInput(new String[] {"Source Copy Error", "Correspondence Error", "Superfluous Mapping Error"});
	
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		combo.setLayoutData(gridData);
		
		combo.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		    	  int selection = combo.getSelectionIndex();
		    	  IAttributeValueMarker marker = (IAttributeValueMarker)col.getErrorIdMap().get(selection);
		          viewer.setInput(col.getErrorExplMap().get(marker));
		          
		        }
			});
		
	}

	private void updateView() {
		IAttributeValueMarker marker;
		
		combo.removeAll();
		
		int i = 1;
		for (int j=0; j<col.getErrorIdMap().getSize(); j++) {
			marker = (IAttributeValueMarker)col.getErrorIdMap().get(j);
			
			combo.add("E"+Integer.toString(i)+" ("+marker.getRel()+", "+
					marker.getTid()+", "+marker.getAttrName()+")", j);
			i++;
		}

		combo.select(0);
		marker = (IAttributeValueMarker)col.getErrorIdMap().get(0);
     	viewer.setInput(col.getErrorExplMap().get(marker));
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void generateErrorExpl(ITreeSelection selection) {
		IMarkerSet m;

		try {
			m = parseMarkers(selection);
			col = gen.findExplanations(m);
			updateView();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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