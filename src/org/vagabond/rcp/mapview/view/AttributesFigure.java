package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;


public class AttributesFigure extends Figure {

	private Color background;
	
	public AttributesFigure (Color background) {
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(false);
		setLayoutManager(layout);
		
//		setBorder(new ColumnFigureBorder());
		setBorder(new LineBorder(ColorConstants.black, 1));
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
			return new Insets(5, 3, 3, 1);
		}
	
		public void paint(IFigure figure, Graphics graphics, Insets insets)
		{
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());
		}
	}
	
}
