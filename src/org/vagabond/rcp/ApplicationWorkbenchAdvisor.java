package org.vagabond.rcp;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "VagabondRCP.perspective";

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	public void postStartup() {
		try {
			String hostString = Activator.getDefault().getPreferenceStore().getString("HOST");
			String databaseString = Activator.getDefault().getPreferenceStore().getString("DATABASE");
			String usernameString = Activator.getDefault().getPreferenceStore().getString("USERNAME");
			String filePath = Activator.getDefault().getPreferenceStore().getString("PATH");
			
			if (!hostString.equals("") && !databaseString.equals("")
					&& !usernameString.equals("") && !filePath.equals("")) {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				//IViewPart view = window.getActivePage().findView("org.vagabond.rcp.gui.views.sdbview");
				IHandlerService service = (IHandlerService) window.getService(IHandlerService.class);
				service.executeCommand("org.vagabond.rcp.util.start", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
