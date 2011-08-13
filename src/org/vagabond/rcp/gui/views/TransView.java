package org.vagabond.rcp.gui.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.vagabond.mapping.model.MapScenarioHolder;
import org.vagabond.xmlmodel.TransformationType;
import org.vagabond.xmlmodel.TransformationsType;

import com.quantum.PluginPreferences;
import com.quantum.QuantumPlugin;
import com.quantum.actions.BookmarkSelectionUtil;
import com.quantum.editors.ColorManager;
import com.quantum.model.Bookmark;
import com.quantum.sql.parser.SQLParserUtil;
import com.quantum.sql.parser.Token;
import com.quantum.util.sql.SQLGrammar;
import com.quantum.view.SQLQueryView;

public class TransView extends SQLQueryView {
	public static final String ID = "org.vagabond.rcp.gui.views.transview";
	private StyledText widget;
	
	private ColorManager colorManager = new ColorManager();
	private SyntaxHighlighter textUpdater = new SyntaxHighlighter(
			this.colorManager);
	
	public static TransView getInstance() {
		return (TransView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ID);
	}
	
	public void setTransformations() {
		widget.setText("");
		TransformationsType trans = MapScenarioHolder.getInstance().getDocument().getMappingScenario().getTransformations();
		
		for (TransformationType t : trans.getTransformationArray()) {
			widget.setText(widget.getText() + t.getId() + t.getCode() + "\n");
		}
	}

	public void createPartControl(Composite parent) {
		setLayout(parent);
		createEditor(parent);
	}
	
	private void setLayout(Composite parent) {
		GridLayout layout  = new GridLayout(1, false);
		parent.setLayout(layout);
	}
	
	private void createEditor(Composite parent) {
		this.widget = new StyledText(parent, SWT.V_SCROLL | SWT.BORDER);

		widget.setWordWrap(true);
		widget.setEditable(false);
		widget.addExtendedModifyListener(this.modifyListener);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		widget.setLayoutData(gridData);

		initializeColours(parent);

		this.widget.setDoubleClickEnabled(false);
	}
	
	private void initializeColours(Composite parent) {
		IPreferenceStore store = QuantumPlugin.getDefault()
				.getPreferenceStore();

		parent.setBackground(this.colorManager.getColor(PreferenceConverter
				.getColor(store, PluginPreferences.BACKGROUND_COLOR)));
		this.textUpdater.initializeColours();
	}
	
	/**
	 * Returns the query to be executed. The query is either 1) the text
	 * currently highlighted/selected in the editor or 2) all of the text in the
	 * editor.
	 * 
	 * @return query string to be executed
	 */
	public String getQuery() {
		String query;

		if (widget.getSelectionText().length() > 0)
			query = widget.getSelectionText();
		else
			query = widget.getText();

		return query;
	}
	
	public void setStyles(final StyleRange[] styles, final int start,
			final int length) {
		getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				try {
					for (int i = 0; i < styles.length; i++) {
						widget.setStyleRange(styles[i]);
					}
				} catch (Throwable t) {
					System.out
							.println("Error with styles: " + t.getClass().toString()); //$NON-NLS-1$
					// ignore any errors
				}
			}
		});
	}

	ExtendedModifyListener modifyListener = new ExtendedModifyListener() {
		public void modifyText(ExtendedModifyEvent event) {
			if (QuantumPlugin.getDefault().getPreferenceStore().getBoolean(
					"quantum.SQLEditorPreferences.enableSyntaxHighlighting") == false)
				return;
			textUpdater.updateText(getQuery(), event.start, event.length);
		}
	};
	
	private class UpdateRequest {
		public UpdateRequest(String text, int start, int length) {
			this.text = text;
			this.start = start;
			this.length = length;
		}
		public String text;
		public int start;
		public int length;
	}
	
	private class SyntaxHighlighter extends Thread {

		// store.setDefault("quantum.text.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.keyword.bold", true); //$NON-NLS-1$
		// store.setDefault("quantum.string.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.comment.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.numeric.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.table.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.view.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.column.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.procedure.bold", false); //$NON-NLS-1$
		// store.setDefault("quantum.variable.bold", false); //$NON-NLS-1$

		private Color STRING_LITERAL;
		private Color KEYWORD;
		private Color COMMENT;
		private Color NUMERIC;
		// private Color COLUMN;
		// private Color TABLE;
		// private Color VIEW;
		// private Color PROCEDURE;
		private Color DEFAULT;
		// private Color VARIABLE;

		// private boolean boldText= false;
		// private boolean boldKeyword = false;
		// private boolean boldString= false;
		// private boolean boldComment = false;
		// private boolean boldNumeric = false;
		// private boolean boldTable = false;
		// private boolean boldView = false;
		// private boolean boldColumn = false;
		// private boolean boldProcedure = false;
		// private boolean boldVariable = false;

		private boolean running = true;
		private LinkedList<UpdateRequest> requests = new LinkedList<UpdateRequest>();
		private final ColorManager colorManager;

		public SyntaxHighlighter(ColorManager colorManager) {
			super();
			this.colorManager = colorManager;

			setPriority(Thread.NORM_PRIORITY);
			start();
		}
		public void initializeColours() {
//			IPreferenceStore store = QuantumPlugin.getDefault()
//					.getPreferenceStore();

//			this.DEFAULT = this.colorManager.getColor(PreferenceConverter
//					.getColor(store, PluginPreferences.TEXT_COLOR));
//			this.KEYWORD = this.colorManager.getColor(PreferenceConverter
//					.getColor(store, PluginPreferences.KEYWORD_COLOR));
//			this.STRING_LITERAL = this.colorManager
//					.getColor(PreferenceConverter.getColor(store,
//							PluginPreferences.STRING_COLOR));
//			this.COMMENT = this.colorManager.getColor(PreferenceConverter
//					.getColor(store, PluginPreferences.COMMENT_COLOR));
//			this.NUMERIC = this.colorManager.getColor(PreferenceConverter
//					.getColor(store, PluginPreferences.NUMERIC_COLOR));
			// this.TABLE= this.colorManager.getColor(
			// PreferenceConverter.getColor(store,
			// PluginPreferences.TABLE_COLOR));
			// this.COLUMN = this.colorManager.getColor(
			// PreferenceConverter.getColor(store,
			// PluginPreferences.COLUMN_COLOR));
			// this.VIEW= this.colorManager.getColor(
			// PreferenceConverter.getColor(store,
			// PluginPreferences.VIEW_COLOR));
			// this.PROCEDURE = this.colorManager.getColor(
			// PreferenceConverter.getColor(store,
			// PluginPreferences.PROCEDURE_COLOR));
			// this.VARIABLE= this.colorManager.getColor(
			// PreferenceConverter.getColor(store,
			// PluginPreferences.VARIABLE_COLOR));
			// this.boldKeyword = store.getBoolean("quantum.keyword.bold");
			// this.boldColumn= store.getBoolean("quantum.column.bold");
			// this.boldComment = store.getBoolean("quantum.comment.bold");
			// this.boldNumeric = store.getBoolean("quantum.numeric.bold");
			// this.boldProcedure = store.getBoolean("quantum.procedure.bold");
			// this.boldString= store.getBoolean("quantum.string.bold");
			// this.boldTable = store.getBoolean("quantum.table.bold");
			// this.boldText= store.getBoolean("quantum.text.bold");
			// this.boldVariable = store.getBoolean("quantum.variable.bold");
			Display display = Display.getCurrent();
			
			this.DEFAULT = display.getSystemColor(SWT.COLOR_BLACK);
			this.KEYWORD = display.getSystemColor(SWT.COLOR_DARK_MAGENTA);
			this.STRING_LITERAL = display.getSystemColor(SWT.COLOR_DARK_BLUE);
			this.COMMENT = display.getSystemColor(SWT.COLOR_DARK_GREEN);
			this.NUMERIC = display.getSystemColor(SWT.COLOR_DARK_CYAN);
		}
		public synchronized void updateText(String text, int start, int length) {
			requests.add(new UpdateRequest(text, start, length));
			notify();
		}

		public void run() {
			while (running) {
				try {
					synchronized (this) {
						if (requests.size() <= 0) {
							wait();
						} else {
							Thread.sleep(10);
						}
					}
					UpdateRequest request = requests.removeFirst();
					String text = request.text.toUpperCase();
					// int dirtyStart = request.start;
					// int dirtyEnd = request.start + request.length;
					StyleRange styleRange;
					List<?> tokens = SQLParserUtil.getLexer().parse(text);
					List<StyleRange> styles = new ArrayList<StyleRange>();
					int min = Integer.MAX_VALUE;
					int max = 0;
					for (int i = 0; i < tokens.size(); i++) {
						Token t = (Token) tokens.get(i);
						String value = t.getValue();
						int start = t.getStart();
						int length = t.getEnd() - t.getStart();
						styleRange = new StyleRange();
						styleRange.start = start;
						styleRange.length = value.length();
						styleRange.fontStyle = SWT.NULL;
						styleRange.foreground = DEFAULT;
						// boolean upper = start <= dirtyEnd && start >=
						// dirtyStart;
						// boolean lower = ((start + length) >= dirtyStart &&
						// (start + length) <= dirtyEnd);
						// boolean both = (start <= dirtyStart && (start +
						// length) >= dirtyEnd);
						// if (upper || lower || both) {
						if (true) { // Let's update the whole text, as some
							// comment changes can alter everything
							min = Math.min(start, min);
							max = Math.max(max, start + length);
							if (t.getType() == Token.IDENTIFIER) {
								Bookmark bookmark = BookmarkSelectionUtil.getInstance().getLastUsedBookmark();
								boolean keyword = false;
								// We try to improve a bit the syntax
								// highlighting by using also
								// the database keywords if the bookmark is open
								if (bookmark != null && bookmark.isConnected()) {
									keyword = bookmark.isKeyword(value);
								} else {
									keyword = SQLGrammar.getInstance()
											.isKeyword(value);
								}
								if (keyword) {
									styleRange.fontStyle = SWT.BOLD;
									styleRange.foreground = KEYWORD;
								} else {
									styleRange.foreground = DEFAULT;
								}
								styles.add(styleRange);
							} else if (t.getType() == Token.COMMENT) {
								styleRange.foreground = COMMENT;
								styles.add(styleRange);
							} else if (t.getType() == Token.LITERAL) {
								styleRange.foreground = STRING_LITERAL;
								styles.add(styleRange);
							} else if (t.getType() == Token.NUMERIC) {
								styleRange.foreground = NUMERIC;
								styles.add(styleRange);
							} else {
								styles.add(styleRange);
							}
						}
					}
					StyleRange[] ranges = styles.toArray(new StyleRange[styles
							.size()]);
					if (max >= 0 && ranges.length > 0) {
						setStyles(ranges, min, max - min);
					}
				} catch (NoSuchElementException e) {
					// ignore a missing request
				} catch (InterruptedException e) {
					// ignore any interruptions
				}
			}
		}
	}

}

