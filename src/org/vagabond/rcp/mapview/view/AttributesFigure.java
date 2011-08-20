package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;


public class AttributesFigure extends Figure {

	public AttributesFigure () {
		FlowLayout layout = new FlowLayout();
		layout.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
		layout.setStretchMinorAxis(false);
		layout.setHorizontal(false);
		setLayoutManager(layout);
		setBorder(new ColumnFigureBorder());
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
