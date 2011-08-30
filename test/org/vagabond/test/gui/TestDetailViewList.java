package org.vagabond.test.gui;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
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
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		try {
			final DetailViewList<MappingType> details = new DetailViewList<MappingType>(shell, this); 

			PropertyConfigurator.configure("test/log4j.properties");
			ModelLoader.getInstance().loadToInst("../TrampExGen/resource/test/simpleTest.xml");
			MappingType[] maps  = MapScenarioHolder.getInstance().getDocument()
					.getMappingScenario().getMappings().getMappingArray();
			final MappingType[][] oMaps = new MappingType[2][]; 
			oMaps[0] = new MappingType[2];
			oMaps[0][0] = maps[0];
			oMaps[0][1] = maps[1];
			
			oMaps[1] = new MappingType[1];
			oMaps[1][0] = maps[1];
			
			final int[] pos = new int[1];
			pos[0] = 0;
			details.updateModel(oMaps[pos[0]]);
			
			Button b = new Button(shell, SWT.PUSH);
			b.addSelectionListener(new SelectionListener () {

				@Override
				public void widgetSelected(SelectionEvent e) {
					pos[0] = 1 - pos[0];
					details.updateModel(oMaps[pos[0]]);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			shell.setSize(500, 700);
			shell.open();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
