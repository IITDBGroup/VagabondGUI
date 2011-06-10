package org.vagabond.rcp.gui.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class DBView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.dbview";

	private TableViewer sourceViewer;
	private TableViewer targetViewer;

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
		Group source = new Group(parent, SWT.SHADOW_NONE);
		source.setText("Source Schema");
		source.setLayout(new GridLayout(1, false));
		Group target = new Group(parent, SWT.SHADOW_NONE);
		target.setText("Target Schema");
		target.setLayout(new GridLayout(1, false));
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		Combo sourceCombo = new Combo(source, SWT.DROP_DOWN | SWT.READ_ONLY);
		sourceCombo.setLayoutData(gridData);
		Combo targetCombo = new Combo(target, SWT.DROP_DOWN | SWT.READ_ONLY);
		targetCombo.setLayoutData(gridData);
		
		gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		sourceViewer = new TableViewer(source, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		sourceViewer.setContentProvider(new ViewContentProvider());
		sourceViewer.setLabelProvider(new ViewLabelProvider());
		sourceViewer.getControl().setLayoutData(gridData);
		
		targetViewer = new TableViewer(target, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		targetViewer.setContentProvider(new ViewContentProvider());
		targetViewer.setLabelProvider(new ViewLabelProvider());
		targetViewer.getControl().setLayoutData(gridData);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		sourceViewer.getControl().setFocus();
	}
	
	public TableViewer getSourceViewer() {
		return sourceViewer;
	}
}