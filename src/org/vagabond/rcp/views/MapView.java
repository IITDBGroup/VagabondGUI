package org.vagabond.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.ViewPart;

public class MapView extends ViewPart {
	public static final String ID = "org.vagabond.rcp.views.mapview";
	private Text text;
	
	@Override
	public void createPartControl(Composite parent) {
		this.text = new Text(parent, SWT.BORDER | SWT.MULTI);
		text.setText("Please connect to a database and load a schema file first.");
	}

	@Override
	public void setFocus() {
	}
	
	public void load(String[][] data) {
		text.setText("Source Schemas:\n");
		for (int i=0; i<data.length; i++) {
			for (int j=0; j<data.length; j++) {
				if (data[i][j] != null)
					text.append(data[i][j] + "\n");
			}
		}
	}

}