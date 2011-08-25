package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public interface GraphColors extends ColorConstants {

	public static final Color selected = SWTResourceManager.getColor(new RGB(100,0,0));
}
