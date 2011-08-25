package org.vagabond.test.gui;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.explanation.marker.MarkerParser;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.basic.IBasicExplanation;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.mapping.scenarioToDB.DatabaseScenarioLoader;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.ExplainDetailViewFactory;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.util.ConnectionManager;

public class TestExplanationViewList {

	static Logger log = PluginLogProvider.getInstance().getLogger(
			TestExplanationViewList.class);
	
	public void run () throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		DetailViewList<IBasicExplanation> details = new DetailViewList<IBasicExplanation>(shell, 
				ExplainDetailViewFactory.withExplains); 
	
		ExplanationSetGenerator gen = new ExplanationSetGenerator();
		
		PropertyConfigurator.configure("test/log4j.properties");
		ModelLoader.getInstance().loadToInst("../TrampExGen/resource/test/simpleTest.xml");
		SchemaResolver.getInstance().setSchemas();
		QueryHolder.getInstance().loadFromDir(new File("resource/queries"));
		ConnectionManager.getInstance().getConnection(
				"localhost", "tramptest", "postgres","");
		DatabaseScenarioLoader.getInstance().loadScenario(
				ConnectionManager.getInstance().getConnection());
		
		ExplanationCollection col = gen.findExplanations(MarkerParser.getInstance()
				.parseSet("{A(employee,2|2,city)}"));
		log.debug(col.getExplSets().iterator().next().toString());
		details.updateModel(col.getExplSets().iterator().next());
		
		shell.setSize(500, 700);
		shell.open();
		
		while(!display.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
	
	public static void main (String[] args) throws Exception {
		TestExplanationViewList thing = new TestExplanationViewList();
		thing.run();
	}

	
}
