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
		shell.setSize(700, 700);
		shell.setText("UMLClassFigure Test");
		LightweightSystem lws = new LightweightSystem(shell);
		
		Figure contents =TopLevelContents.createContents(); 
			
		lws.setContents(contents);
		shell.open();
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
}


