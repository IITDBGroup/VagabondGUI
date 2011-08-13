package org.vagabond.rcp.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

import com.quantum.sql.SQLResultSetResults;

public class ExplGenWizard extends Wizard {
	
	protected ExplGenPage page;
	protected IStructuredSelection selection;
	private SQLResultSetResults results;
	
	public void init(String title, ExplGenPage page, 
			SQLResultSetResults results, IStructuredSelection selection) {
		this.selection = selection;
		this.page = page;
		this.results = results;
		setWindowTitle(title);
	}
	
	@Override
	public boolean performFinish() {
		return page.performFinish();
	}
	
	public void addPages() {
		page.init(this.results, this.selection);
		addPage(page);
	}

}
