package org.vagabond.rcp.mapview.view;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class LeftRightParentBoxFigureAnchor extends AbstractConnectionAnchor {

	private boolean left;
	
	public LeftRightParentBoxFigureAnchor (IFigure owner, boolean left) {
		super(owner);
		this.left = left;
	}
	

	/**
	 * Place anchor on right or left box of relation node using the y coordinates 
	 * of the attr node
	 */
	@Override
	public Point getLocation(Point reference) {
		Rectangle parentBounds;
		Point attrBounds;
		Point location;
		IFigure parent = getOwner().getParent();
		IFigure attr = getOwner();
		
		if (parent instanceof AttributesFigure)
			parent = parent.getParent();
		
		parentBounds = parent.getBounds();
		parent.translateToAbsolute(parentBounds);
		attrBounds = attr.getBounds().getCenter();
		attr.translateToAbsolute(attrBounds);
		
		location = new Point(parentBounds.x + (left ? 0 : parentBounds.width), 
				attrBounds.y);
		
		return location;
	}

}
