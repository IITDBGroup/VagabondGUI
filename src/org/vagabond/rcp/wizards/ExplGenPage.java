package org.vagabond.rcp.wizards;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.ranking.DummyRanker;
import org.vagabond.explanation.ranking.AStarExplanationRanker;
import org.vagabond.rcp.gui.views.ExplRankView;
import org.vagabond.rcp.gui.views.ExplView;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

import com.quantum.Messages;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResultSetResults.Row;

public class ExplGenPage extends WizardPage {

	static Logger log = PluginLogProvider.getInstance().getLogger(ExplGenPage.class);
	
	protected IStructuredSelection selection;
	private TreeViewer viewer;
	private ExplanationSetGenerator gen = new ExplanationSetGenerator();
	
	public ExplGenPage(String pageName) {
		super(pageName);
		setTitle(Messages.getString("TableView.ExplGenTitle"));
		setDescription(Messages.getString("TableView.ExplGenDesc"));
	}
	
	public class ViewContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
				return ((List<?>) inputElement).toArray();
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof SQLResultSetResults.Row) {
				SQLResultSetResults.Row r = (SQLResultSetResults.Row) parentElement;
				return r.getAsStringArrayWithoutID();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof SQLResultSetResults.Row) {
				return true;
			}
			return false;
		}

	}
	
	public class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof SQLResultSetResults.Row) {
				SQLResultSetResults.Row r = (SQLResultSetResults.Row) element;
				return r.getAsStringArray()[0];
			}
			return ((String) element);
		}

		@Override
		public Image getImage(Object element) {
			if (element instanceof SQLResultSetResults.Row) {
				return PlatformUI.getWorkbench().getSharedImages()
						.getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			return PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE);
		}

	}
	
	public void init(SQLResultSetResults results, IStructuredSelection selection) {
//    	this.results = results;
		this.selection = selection;
    }

	@SuppressWarnings("unchecked")
	@Override
	public void createControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		List<SQLResultSetResults.Row> l = new ArrayList<Row>();
		
		for (Iterator<SQLResultSetResults.Row> i = selection.iterator(); i.hasNext();) {
			SQLResultSetResults.Row r = i.next();
			l.add(r);
		}
		
		viewer.setInput(l);
		viewer.expandAll();
		
		setControl(parent);
	}
	
    public boolean performFinish() {    	
    	ITreeSelection selection = (ITreeSelection) viewer.getSelection();
    	
    	if (selection != null) {
    		generateErrorExpl(selection);
    		ExplView.getInstance().updateView();
    		ContentProvider.getInstance().getExplCol().resetIter();
    		if (ContentProvider.getInstance().getExplCol().hasNext())
    			ExplRankView.getInstance().updateView(ContentProvider
    					.getInstance().getExplCol().next());
    		return true;
    	}
    	
    	return false;
    }
    
    
    
    public void generateErrorExpl(ITreeSelection selection) {
		IMarkerSet m;
    	ExplanationCollection col;
    	
    	try {
			m = parseMarkers(selection);
			col = gen.findExplanations(m);
			ContentProvider.getInstance().getExplModel().setCol(col);
			ContentProvider.getInstance().getExplModel().setMarkers(m);
			ContentProvider.getInstance().createRanker();
		} catch (Exception e) {
			LoggerUtil.logException(e, log);
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
