package org.vagabond.rcp.mapview.view;

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
	private boolean boldFont;
	private boolean selection = false;
	
	public AttributeFigure(boolean bold) {
		this.boldFont = bold;
		
		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
		layout.setStretchMinorAxis(false);
		layout.setSpacing(0);
		setLayoutManager(layout);

		try {
			label = new Label("", SWTResourceManager.getImage("icons/attribute.gif"));
		} catch (Exception e) {
			LoggerUtil.logException(e, log);
			label = new Label();
		}
		label.setFont(SWTResourceManager.getSystemFont(10, bold));
		
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
	
	public void setBoldFont (boolean boldFont) {
		if (this.boldFont != boldFont) {
			label.setFont(SWTResourceManager.getSystemFont(10, boldFont));
			this.boldFont = boldFont;
		}
	}
	
	public void setSelected (boolean selection) {
		if (this.selection != selection) {
			log.debug("selection changed to " + selection);
			this.selection = selection;
		}
	}
}