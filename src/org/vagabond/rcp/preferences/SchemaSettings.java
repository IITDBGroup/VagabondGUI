package org.vagabond.rcp.preferences;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

import org.vagabond.rcp.Activator;

public class SchemaSettings extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public SchemaSettings() {
		super(GRID);
	}

	public void createFieldEditors() {
		addField(new FileFieldEditor("PATH", "&File preference:",
				getFieldEditorParent()));
		addField(new BooleanFieldEditor("BOOLEAN_VALUE",
				"&An example of a boolean preference", getFieldEditorParent()));

		addField(new RadioGroupFieldEditor("CHOICE",
				"An example of a multiple-choice preference", 1,
				new String[][] { { "&Choice 1", "choice1" },
						{ "&Choice 2", "choice2" } }, getFieldEditorParent()));
		addField(new StringFieldEditor("MySTRING1", "A &text preference:",
				getFieldEditorParent()));
		addField(new StringFieldEditor("MySTRING2", "A &text preference:",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Select Scenario File to load");
	}

}
