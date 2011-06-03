package org.vagabond.rcp.preferences;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.vagabond.rcp.Activator;

public class DBSettings extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public DBSettings() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new StringFieldEditor("host", "Host:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("database", "Database Name:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("username", "Username:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("password", "Password",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Enter Database Connection Information");
	}
}
