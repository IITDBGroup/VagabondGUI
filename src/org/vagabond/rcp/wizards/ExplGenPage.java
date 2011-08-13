package org.vagabond.rcp.wizards;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.vagabond.rcp.gui.views.ExplView;

import com.quantum.Messages;
import com.quantum.model.Bookmark;
import com.quantum.sql.MultiSQLServer;
import com.quantum.sql.SQLResultSetResults;
import com.quantum.sql.SQLResultSetResults.Row;
import com.quantum.sql.SQLResults;
import com.quantum.ui.dialog.SQLExceptionDialog;

public class ExplGenPage extends WizardPage {
	protected IStructuredSelection selection;
	private SQLResultSetResults results;
	private TreeViewer viewer;
	
	public ExplGenPage(String pageName) {
		super(pageName);
		setTitle(Messages.getString("TableView.ExplGenTitle"));
		setDescription(Messages.getString("TableView.ExplGenDesc"));
	}
	
	public class MyContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List)
				return ((List) inputElement).toArray();
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof SQLResultSetResults.Row) {
				SQLResultSetResults.Row r = (SQLResultSetResults.Row) parentElement;
				return r.getAsStringArray();
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
	
	public class MyLabelProvider extends LabelProvider {
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
    	this.results = results;
		this.selection = selection;
    }

	@Override
	public void createControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		viewer.setContentProvider(new MyContentProvider());
		viewer.setLabelProvider(new MyLabelProvider());

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
    		ExplView.getInstance().generateErrorExpl(selection);
    		return true;
    	}
    	
    	return false;
    }

}
