package org.vagabond.test.gui;

import java.io.FileNotFoundException;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


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


