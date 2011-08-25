package org.vagabond.test.gui;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.generation.QueryHolder;
import org.vagabond.explanation.marker.MarkerParser;
import org.vagabond.explanation.marker.SchemaResolver;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.explanation.model.IExplanationSet;
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
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		ExplanationSetGenerator gen = new ExplanationSetGenerator();
		PropertyConfigurator.configure("test/log4j.properties");
		ModelLoader.getInstance().loadToInst("../TrampExGen/resource/test/simpleTest.xml");
		SchemaResolver.getInstance().setSchemas();
		QueryHolder.getInstance().loadFromDir(new File("resource/queries"));
		ConnectionManager.getInstance().getConnection(
				"localhost", "tramptest", "postgres","");
		DatabaseScenarioLoader.getInstance().loadScenario(
				ConnectionManager.getInstance().getConnection());
		
		final ExplanationCollection col = gen.findExplanations(MarkerParser.getInstance()
				.parseSet("{A(employee,2|2,city)}"));
		log.debug(col.getExplSets().iterator().next().toString());

		IExplanationSet e = col.next();
		
		final DetailViewList<IBasicExplanation> details = new DetailViewList<IBasicExplanation>(shell, 
				ExplainDetailViewFactory.withExplains);
		details.updateModel(e);details.updateModel(e);
		
		Button b = new Button(shell, SWT.PUSH);
		b.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (col.hasNext()) {
					details.updateModel(col.next());
					shell.layout();
					//shell.layout(true,true);
				}
				else {
					col.resetIter();
					details.updateModel(col.next());
					shell.layout();
				}
			}
			
		});
		
		shell.setSize(500, 700);
		shell.open();
		
		e = col.next();
		details.updateModel(e);
		
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
