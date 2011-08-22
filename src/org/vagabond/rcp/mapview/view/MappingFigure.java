package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public class MappingFigure extends Figure {


	public static Color classColor = SWTResourceManager.
			getColor(new RGB(201,205,215));
	private Label label;
	private AttributesFigure attrs;
	
	public MappingFigure() {
		label = new Label();
		label.setFont(SWTResourceManager.getBoldSystemFont(12));
		label.setLabelAlignment(PositionConstants.CENTER);

		attrs = new AttributesFigure(classColor);
		
		ToolbarLayout layout = new ToolbarLayout();
		layout.setHorizontal(false);
		layout.setSpacing(0);
		layout.setStretchMinorAxis(true);
		setLayoutManager(layout);
		setBorder(new LineBorder(ColorConstants.black, 1));
		setBackgroundColor(classColor);
		setForegroundColor(ColorConstants.black);
		setOpaque(true);

		add(label);
		add(attrs);
		
//		ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
//		layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
//		layout.setStretchMinorAxis(true);
//		layout.setStretchMajorAxis(true);
//		layout.setReversed(true);
//
//		setLayoutManager(layout);	
//		setBorder(new LineBorder(ColorConstants.black,1));
//		setBackgroundColor(classColor);
//		setOpaque(true);
//		this.label = name;
//		//		label.setSize(MIN_WIDTH, MIN_HEIGHT);
//
//		add(label);	
	}

	public void setNameText (String text) {
		label.setText(text);
	}

	public Label getLabel() {
		return label;
	}

	public IFigure getAttrsFigure() {
		return attrs;
	}

}
