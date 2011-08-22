package org.vagabond.test.gui;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.mapping.model.ModelLoader;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewFactory;
import org.vagabond.rcp.gui.views.detailWidgets.DetailViewList;
import org.vagabond.rcp.gui.views.detailWidgets.MappingDetailView;
import org.vagabond.rcp.gui.views.detailWidgets.ModelElementDetailView;
import org.vagabond.xmlmodel.MappingType;

public class TestDetailViewList implements DetailViewFactory {

	public TestDetailViewList () throws Exception {

	}
	
	public void run () throws Exception {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		DetailViewList<MappingType> details = new DetailViewList<MappingType>(shell, this); 
	
		PropertyConfigurator.configure("test/log4j.properties");
		ModelLoader.getInstance().loadToInst("../TrampExGen/resource/test/simpleTest.xml");
		MappingType[] maps  = MapScenarioHolder.getInstance().getDocument()
				.getMappingScenario().getMappings().getMappingArray();
		details.updateModel(maps);
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
		TestDetailViewList thing = new TestDetailViewList();
		thing.run();
	}

	@Override
	public ModelElementDetailView createView(Composite parent) {
		return new MappingDetailView(parent, SWT.NONE);
	}
	
}
