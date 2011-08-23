package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.OneLineBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public class RelationFigure extends Figure {
	public static Color classColor = SWTResourceManager.getColor(
			new RGB(255,205,215));

	private Label label;
	private AttributesFigure attrs;

	public RelationFigure() {
		label = new Label();
		label.setFont(SWTResourceManager.getBoldSystemFont(12));
		label.setLabelAlignment(PositionConstants.CENTER);
		label.setBorder(new MarginBorder(new Insets(3,5,0,5)));
		attrs = new AttributesFigure(ColorConstants.tooltipBackground);
		
		ToolbarLayout layout = new ToolbarLayout();
		layout.setHorizontal(false);
		layout.setSpacing(0);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.black);
		setOpaque(true);

		add(label);
		add(attrs);
	}


	public void setNameText (String text) {
		label.setText(text);
	}

	public Label getLabel() {
		return label;
	}

	public AttributesFigure getAttrsFigure () {
		return attrs;
	}

	public void setSelected(boolean isSelected)
	{
		LineBorder lineBorder = (LineBorder) getBorder();
		if (isSelected)
		{
			lineBorder.setWidth(2);
		}
		else
		{
			lineBorder.setWidth(1);
		}
	}
	
	public class RelationLineBorder extends LineBorder  {
		
		public RelationLineBorder(Color lineColor, int width) {
			super(lineColor, width);
		}
		
		@Override
		public Insets getInsets(IFigure figure) {
			return new Insets(getWidth() + 3);
		}
	}


}
