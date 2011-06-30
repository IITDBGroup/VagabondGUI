package org.vagabond.rcp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class MapView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.gui.views.mapview";
	private Text text;
	
	public static MapView getInstance() {
		return (MapView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		this.text = new Text(parent, SWT.BORDER | SWT.MULTI);
		text.setText("Please connect to a database and load a schema file first.");
	}

	@Override
	public void setFocus() {
	}
	
	public void load(String[][] data, String[][] data2) {
		text.setText("Source Schemas:\n");
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data[i].length; j++) {
				if (data[i][j] != null) {
					text.append(data[i][j] + "\n");
				}
			}
		}
		text.append("\nTarget Schemas:\n");
		for (int i=0; i<data2.length; i++) {
			for (int j=0; j<data2[i].length; j++) {
				if (data2[i][j] != null) {
					text.append(data2[i][j] + "\n");
				}
			}
		}
	}

}