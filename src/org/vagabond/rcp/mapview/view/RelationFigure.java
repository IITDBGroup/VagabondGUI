package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.swt.graphics.Color;

public class RelationFigure extends Figure {
	public static Color classColor = new Color(null,255,205,215);
	private Label label;
	private int MIN_WIDTH = 100;
	private int MIN_HEIGHT = 10;
	  
	public RelationFigure(Label name) {
	    ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
	    layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	    layout.setStretchMinorAxis(true);
	    layout.setStretchMajorAxis(true);
	    layout.setReversed(true);
	    setLayoutManager(layout);	
	    setBorder(new LineBorder(ColorConstants.black,1));
	    setBackgroundColor(classColor);
	    setOpaque(true);
		label = name;
		label.setSize(MIN_WIDTH, MIN_HEIGHT);
		
	    add(label);	
	}

	public Label getLabel() {
		return label;
	}
	
}
