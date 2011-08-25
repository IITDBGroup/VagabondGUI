package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.IFigure;

public interface SelectableFigure extends IFigure {

	public void setSelection (boolean selection);
	public void switchSelection ();
}
