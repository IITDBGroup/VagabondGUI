package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;


public class AttributesFigure extends Figure {

	private Color background;
	
	public AttributesFigure (Color background) {
		ToolbarLayout layout = new ToolbarLayout();
		layout.setHorizontal(false);
		layout.setSpacing(0);
		layout.setStretchMinorAxis(true);
		
		setLayoutManager(layout);
		
		setBorder(new ColumnFigureBorder());
		if (background != null) {
			this.background = background;
			setBackgroundColor(background);
		}
		else
			setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.black);
		setOpaque(true);
	}
	
	class ColumnFigureBorder extends AbstractBorder
	{
	
		public Insets getInsets(IFigure figure)
		{
			return new Insets(2, 3, 3, 4);
		}
	
		public void paint(IFigure figure, Graphics graphics, Insets insets)
		{
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), 
					tempRect.getTopRight());
		}
	}
	
}
