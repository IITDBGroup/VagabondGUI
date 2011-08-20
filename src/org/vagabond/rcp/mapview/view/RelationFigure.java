package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public class RelationFigure extends Figure {
	public static Color classColor = SWTResourceManager.getColor(
			new RGB(255,205,215));
	
	private Label label;
	private AttributesFigure attrs = new AttributesFigure();
//	private int MIN_WIDTH = 100;
//	private int MIN_HEIGHT = 10;
	  
	public RelationFigure() {
	    label = new Label();
	    label.setFont(SWTResourceManager.getBoldSystemFont(12));
	    label.setLabelAlignment(PositionConstants.CENTER);
		ToolbarLayout layout = new ToolbarLayout();
		layout.setHorizontal(false);
		layout.setSpacing(2);
		layout.setStretchMinorAxis(true);
		
		
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(ColorConstants.tooltipBackground);
		setForegroundColor(ColorConstants.black);
		setOpaque(true);
		
		add(label);
		add(attrs);
//	    setBorder(new LineBorder(ColorConstants.black,1));
//	    setBackgroundColor(classColor);
//	    setOpaque(true);
//		setPreferredSize(MIN_WIDTH, SWT.DEFAULT);
//	    label = name;
//		label.setFont(SWTResourceManager.getBoldSystemFont(13));
//		label.setSize(MIN_WIDTH, MIN_HEIGHT);
//	    ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
//	    layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
//	    layout.setStretchMinorAxis(true);
//	    layout.setStretchMajorAxis(true);
//	    layout.setReversed(true);
//	    setLayoutManager(layout);
//	    add(label);	
	}
	
	public void revalidatePrefferedSize ()  {
//		int wHint = MIN_WIDTH;
//		int hHint = MIN_HEIGHT;
//		
//		wHint = Math.max(wHint, label.getPreferredSize().width);
//		
//		for(Object o: getChildren()) {
//			if (o instanceof AttributeFigure) {
//				AttributeFigure attr = (AttributeFigure) o;
//				wHint = Math.max(attr.getPreferredSize().width, wHint);
//				hHint = hHint + attr.getPreferredSize().height;
//			}
//		}
//		
//		setPreferredSize(wHint, hHint);
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
	
}
