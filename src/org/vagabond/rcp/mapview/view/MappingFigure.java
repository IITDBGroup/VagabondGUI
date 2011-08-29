package org.vagabond.rcp.mapview.view;



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.vagabond.rcp.util.SWTResourceManager;

public class MappingFigure extends Figure implements SelectableFigure {


	public static Color classColor = SWTResourceManager.
			getColor(new RGB(255,200,198));
	
	private Label label;
	private AttributesFigure attrs;
	private boolean selection = false;
	
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

	public void setSelected(boolean isSelected)
	{
	
	}

	@Override
	public void setSelection(boolean selection) {
		if (this.selection == selection)
			return;
		LineBorder lineBorder = (LineBorder) getBorder();
		this.selection = selection;
		if (this.selection)
			lineBorder.setWidth(2);
		else
			lineBorder.setWidth(1);
	}

	@Override
	public void switchSelection() {
		setSelection(!this.selection);
	}
	
}
