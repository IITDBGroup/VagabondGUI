package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

public class NodeFigure extends Figure {
	private Label label;
	private RectangleFigure rectangle;
	
	public NodeFigure() {
		ToolbarLayout layout = new ToolbarLayout();
		setLayoutManager(layout);
		setBorder(new LineBorder());
		rectangle = new RectangleFigure();
		add(rectangle);
		label = new Label();
		add(label);
	}
	
	public Label getLabel() {
		return label;
	}
	
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		setConstraint(rectangle, new Rectangle(0,0,r.width, r.height));
		setConstraint(label, new Rectangle(0,0,r.width,r.height));
		rectangle.invalidate();
		label.invalidate();
	}
}
