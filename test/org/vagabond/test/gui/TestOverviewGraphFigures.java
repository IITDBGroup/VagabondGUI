package org.vagabond.test.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.vagabond.rcp.mapview.view.AttributeFigure;
import org.vagabond.rcp.mapview.view.LeftRightParentBoxFigureAnchor;
import org.vagabond.rcp.mapview.view.MappingFigure;
import org.vagabond.rcp.mapview.view.MyConnectionRouter;
import org.vagabond.rcp.mapview.view.RelationFigure;


public class TestOverviewGraphFigures {

	public static void main(String args[]) throws FileNotFoundException{
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setSize(400, 400);
		shell.setText("UMLClassFigure Test");
		LightweightSystem lws = new LightweightSystem(shell);
		Figure contents = new Figure();
		XYLayout contentsLayout = new XYLayout();
		contents.setLayoutManager(contentsLayout);

//		Font classFont = new Font(null, "Arial", 12, SWT.BOLD);
//		Label nameLabel1 = new Label("Relation one");
//		nameLabel1.setFont(classFont);
//
//		Label nameLabel2 = new Label("Relation two");
//		nameLabel1.setFont(classFont);

		final RelationFigure relFigure1 = new RelationFigure();
		relFigure1.setNameText("Relation One");
		final RelationFigure relFigure2 = new RelationFigure();
		relFigure2.setNameText("Relation Two");

		final MappingFigure mapFig = new MappingFigure();
		mapFig.setNameText("M1");
		
//		Label aLabel1 = new Label("One Name(1)");
//		Label aLabel2 = new Label("One Attr(2)");
//		Label aLabel3 = new Label("One Attr(3) long long long");
//		Label aLabel4 = new Label("Two Name(1)");
//		Label aLabel5 = new Label("Two Attr(2)");

		Image image = new Image(d, new FileInputStream("icons/attribute.gif"));
		
		AttributeFigure aLabel1 = new AttributeFigure ();
		aLabel1.setAttrName("One Name(1)");
		aLabel1.getLabel().setIcon(image);
		AttributeFigure aLabel2 = new AttributeFigure ();
		aLabel2.setAttrName("One Name(2)");
		aLabel2.getLabel().setIcon(image);
		AttributeFigure aLabel3 = new AttributeFigure ();
		aLabel3.setAttrName("One Name(3) long long long");
		aLabel3.getLabel().setIcon(image);
		
		AttributeFigure aLabel4 = new AttributeFigure ();
		aLabel4.setAttrName("Two Name(1)");
		aLabel4.getLabel().setIcon(image);
		AttributeFigure aLabel5 = new AttributeFigure ();
		aLabel5.setAttrName("Two Name(2)");
		aLabel5.getLabel().setIcon(image);
		
		AttributeFigure mLabel1 = new AttributeFigure();
		mLabel1.setAttrName("a");
		AttributeFigure mLabel2 = new AttributeFigure();
		mLabel2.setAttrName("b");
		
		
		relFigure1.getAttrsFigure().add(aLabel1);
		relFigure1.getAttrsFigure().add(aLabel2);
		relFigure1.getAttrsFigure().add(aLabel3);

		relFigure2.getAttrsFigure().add(aLabel4);
		relFigure2.getAttrsFigure().add(aLabel5);

		mapFig.getAttrsFigure().add(mLabel1);
		mapFig.getAttrsFigure().add(mLabel2);
		
		contentsLayout.setConstraint(relFigure1, new Rectangle(10,10,-1,-1));
		contentsLayout.setConstraint(relFigure2, new Rectangle(400, 50, -1, -1));
		contentsLayout.setConstraint(mapFig, new Rectangle(250, 30, -1, -1));
		contents.add(relFigure1);
		contents.add(relFigure2);
		contents.add(mapFig);

		PolylineConnection c = new PolylineConnection();
		LeftRightParentBoxFigureAnchor sourceAnchor = new LeftRightParentBoxFigureAnchor(aLabel2, false);
		LeftRightParentBoxFigureAnchor targetAnchor = new LeftRightParentBoxFigureAnchor(aLabel4, true);
		c.setSourceAnchor(sourceAnchor);
		c.setTargetAnchor(targetAnchor);
		contents.add(c);
//		c.setConnectionRouter(new MyConnectionRouter(contents));

		PolylineConnection c2 = new PolylineConnection();
		sourceAnchor = new LeftRightParentBoxFigureAnchor(aLabel2, false);
		targetAnchor = new LeftRightParentBoxFigureAnchor(mLabel1, true);
		c2.setSourceAnchor(sourceAnchor);
		c2.setTargetAnchor(targetAnchor);
		contents.add(c2);
//		c.setConnectionRouter(new MyConnectionRouter(contents));

		PolylineConnection c3 = new PolylineConnection();
		sourceAnchor = new LeftRightParentBoxFigureAnchor(mLabel1, false);
		targetAnchor = new LeftRightParentBoxFigureAnchor(aLabel5, true);
		c3.setSourceAnchor(sourceAnchor);
		c3.setTargetAnchor(targetAnchor);
		contents.add(c3);		
		
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}


