package org.vagabond.test.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.rcp.mapview.controller.GraphEditPart;
import org.vagabond.rcp.mapview.controller.VagabondEditPartFactory;
import org.vagabond.rcp.mapview.model.ContentProvider;
import org.vagabond.rcp.mapview.view.AttributeFigure;
import org.vagabond.rcp.mapview.view.LeftRightParentBoxFigureAnchor;
import org.vagabond.rcp.mapview.view.MappingFigure;
import org.vagabond.rcp.mapview.view.RelationFigure;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.LoggerUtil;

public class TestGEFComponents {

	static Logger log = PluginLogProvider.getInstance().getLogger(TestGEFComponents.class);
	
	public static void main(String args[]) throws Exception{
		PropertyConfigurator.configure("test/log4j.properties");
		
		Display d = new Display();
		final Shell shell = new Shell(d);
		shell.setLayout(new FillLayout());
		shell.setSize(700, 600);
		shell.setText("UMLClassFigure Test");
//		LightweightSystem lws = new LightweightSystem(shell);
		
		final ScrollingGraphicalViewer viewer;
		
//		Figure contents =TopLevelContents.createContents(); 
		
		ModelLoader.getInstance().loadToInst("../TrampExGen/resource/exampleScenarios/homelessDebugged.xml");
		log.debug(MapScenarioHolder.getInstance().getDocument().toString());
		
		
		viewer = new ScrollingGraphicalViewer();
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		VagabondEditPartFactory editPartFactory = new VagabondEditPartFactory();
		// Initialize the viewer, 'parent' is the
		// enclosing RCP windowframe
		viewer.createControl(shell);
		viewer.setRootEditPart(rootEditPart);
		viewer.setEditPartFactory(editPartFactory);
		
		viewer.getControl().addListener (SWT.Resize,  new Listener () {
		    public void handleEvent (Event e) {
		    	try {
		    		log.debug("resize on map graph");
		    		GraphEditPart part = (GraphEditPart) viewer.getRootEditPart().getContents();
		    		part.setLayoutConstraints();
		    		
		    		// refresh
		    		viewer.getRootEditPart().getContents().refresh();
				} catch (Exception e1) {
					LoggerUtil.logException(e1, log);
				}
//		    	// resize map
//		    	GraphEditPart graph = (GraphEditPart) viewer.getRootEditPart().getChildren().get(0);
//		    	graph.setLayoutConstraints();
		    }
		});
		
		ContentProvider.getInstance().generateGraph();
		viewer.setContents(ContentProvider.getInstance().getGraph());
		// Set the view's background to white
		viewer.getControl().setBackground(new Color(null, 255, 255, 255));
		
		shell.open();
		// refresh
		viewer.getRootEditPart().getContents().refresh();
		
		// manually set things
		GraphEditPart part = (GraphEditPart) viewer.getRootEditPart().getContents();
		part.setLayoutConstraints();
		
		// refresh
		viewer.getRootEditPart().getContents().refresh();
		
		
		while (!shell.isDisposed())
			while (!d.readAndDispatch())
				d.sleep();
	}
	
}
