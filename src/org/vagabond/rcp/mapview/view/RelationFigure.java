package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public class RelationFigure extends Figure implements SelectableFigure {
	public static Color classColor = SWTResourceManager.getColor(
			new RGB(255,205,215));

	private Label label;
	private AttributesFigure attrs;
	private boolean selection = false;
	
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


	@Override
	public void setSelection(boolean selection) {
		if (this.selection == selection)
			return;
		LineBorder lineBorder = (LineBorder) getBorder();
		this.selection = selection;
		if (this.selection) {
			lineBorder.setWidth(2);
			lineBorder.setColor(GraphColors.selected);
		}
		else {
			lineBorder.setWidth(1);
			lineBorder.setColor(GraphColors.black);
		}		
	}


	@Override
	public void switchSelection() {
		setSelection(!this.selection);
	}

}
