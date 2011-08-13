package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;

public class AttributeFigure extends Figure {
	private Label label;
	private int MIN_WIDTH = 100;
	private int MIN_HEIGHT = 10;
	
	  public AttributeFigure() {
	    ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
	    layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
	    layout.setStretchMinorAxis(false);
	    layout.setSpacing(2);
	    setLayoutManager(layout);
	    setBorder(new AttributeFigureBorder());
	    
	    label = new Label();
	    label.setSize(MIN_WIDTH, MIN_HEIGHT);
	    add(label);
	  }
	    
	  public class AttributeFigureBorder extends AbstractBorder {
	    public Insets getInsets(IFigure figure) {
	      return new Insets(0,0,1,0);
	    }
	    public void paint(IFigure figure, Graphics graphics, Insets insets) {
	      graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
	    		  tempRect.getTopRight());
	    }
	  }

	public Label getLabel() {
		return label;
	}
}