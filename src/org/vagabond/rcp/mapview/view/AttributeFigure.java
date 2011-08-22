package org.vagabond.rcp.mapview.view;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.SWTResourceManager;
import org.vagabond.util.LoggerUtil;

public class AttributeFigure extends Figure {
	
	static Logger log = PluginLogProvider.getInstance().getLogger(AttributeFigure.class);
	
	private Label label;
//	private int MIN_WIDTH = 100;
//	private int MIN_HEIGHT = 10;

	public AttributeFigure() {
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(0);
		setBorder(new AttributeFigureBorder());
		setLayoutManager(layout);
		//	    setBorder(new AttributeFigureBorder());

		try {
			label = new Label("", SWTResourceManager.getImage("icons/attribute.gif"));
		} catch (Exception e) {
			LoggerUtil.logException(e, log);
			label = new Label();
		}
		label.setFont(SWTResourceManager.getSystemFont(10, false));
		
		//	    label.setSize(MIN_WIDTH, MIN_HEIGHT);
		add(label);
	}

	public class AttributeFigureBorder extends AbstractBorder {
		public Insets getInsets(IFigure figure) {
			return new Insets(1,1,1,1);
		}
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
					tempRect.getBottomRight());
		}
	}

	public Label getLabel() {
		return label;
	}
	
	public void setAttrName (String name) {
		label.setText(name);
	}
}