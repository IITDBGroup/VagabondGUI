package org.vagabond.rcp.gui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.marker.MarkerParser;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.IExplanationSet;

import com.quantum.sql.SQLResultSetResults;

public class ExplView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.explview";

	private Combo combo;
	private TableViewer viewer;
	private ExplanationSetGenerator gen = new ExplanationSetGenerator();

	public static ExplView getInstance() {
		return (ExplView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	/**
	 * The content provider class is responsible for providing objects to the
	 * view. It can wrap existing objects in adapters or simply return objects
	 * as-is. These objects may be sensitive to the current input of the view,
	 * or ignore it and always show the same content (like Task List, for
	 * example).
	 */
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		public void dispose() {
		}

		public Object[] getElements(Object parent) {
			if (parent instanceof Object[]) {
				return (Object[]) parent;
			}
	        return new Object[0];
		}
	}

	class ViewLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(
					ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		setLayout(parent);
		createViewer(parent);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, false);
		parent.setLayout(layout);
	}
	
	private void createViewer(Composite parent) {
		combo = new Combo(parent, SWT.READ_ONLY | SWT.DROP_DOWN);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		// Provide the input to the ContentProvider
		viewer.setInput(new String[] {"Source Copy Error", "Correspondence Error", "Superfluous Mapping Error"});
	
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	private void updateView(ExplanationCollection col) {
		int i = 0;
		for (IExplanationSet s : col.getExplSets()) {
			combo.add("E"+(i+1));
		}
	}
	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void generateErrorExpl(ITreeSelection selection) {
		IMarkerSet m;
		ExplanationCollection col;
		
		try {
			m = parseMarkers(selection);
			col = gen.findExplanations(m);
			updateView(col);
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