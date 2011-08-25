package org.vagabond.rcp.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vagabond.rcp.Activator;

public class DBSettings extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public DBSettings() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new StringFieldEditor("HOST", "Host:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("DATABASE", "Database Name:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("USERNAME", "Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("PASSWORD", "Password",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Enter Database Connection Information");
	}
}
