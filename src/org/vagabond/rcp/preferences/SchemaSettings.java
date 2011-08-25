package org.vagabond.rcp.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.vagabond.rcp.Activator;

public class SchemaSettings extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public SchemaSettings() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new FileFieldEditor("PATH", "&File preference:",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Select Scenario File to load");
	}

}
