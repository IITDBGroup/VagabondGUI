package org.vagabond.rcp.DetailSelection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.vagabond.explanation.generation.ExplanationSetGenerator;
import org.vagabond.explanation.marker.IAttributeValueMarker;
import org.vagabond.explanation.marker.IMarkerSet;
import org.vagabond.explanation.marker.MarkerFactory;
import org.vagabond.explanation.model.ExplanationCollection;
import org.vagabond.rcp.gui.views.ExplRankView;
import org.vagabond.rcp.gui.views.ExplView;
import org.vagabond.rcp.model.ContentProvider;
import org.vagabond.rcp.util.PluginLogProvider;

import com.quantum.sql.SQLResultSetResults;

import org.vagabond.util.ConnectionManager;

public class DetailSelectionPage extends WizardPage{
	static Logger log = PluginLogProvider.getInstance().getLogger(DetailSelectionPage.class);
	//variables
	protected IStructuredSelection selection;
	final Text text[]=new Text[10];
	final Button button[]=new Button[10];
	final String data[]=new String[10];
	final Combo combo[]=new Combo[10];
	private String[] comparisonInt={"=","<",">","between"}; 
	private String[] comparisonString={"=","contains","start with","end with"};
	private String[] comparisonDate={"before","after","between"};
	protected SQLResultSetResults results;
	private String relname;
	private int length;
	private String query;
	private String tempQuery="";
	private String queryForTable;
	private ResultSet rs;
	private ResultSet rsTable;
	private List<String> type=new ArrayList<String>();
	private List<String> tid=new ArrayList<String>();
	private ExplanationSetGenerator gen = new ExplanationSetGenerator();
	private StringTokenizer st;
	//constructor
	public DetailSelectionPage(String pageName,SQLResultSetResults results,String relname){
		super(pageName);
		setTitle("Detailed Selection");
		setMessage("Please define your selection:");
		this.results=results;
		length=0;
		this.relname=relname;
	}
	
	public void createControl(Composite parent){
		//get attribute type
		try {
			initial();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Composite composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout(3,false));
		for(int count=0;count<results.getColumnCount()-1;count++){
			//set conditions
			button[count]=new Button(composite,SWT.CHECK);
			button[count].setText(results.getColumnName(count+2));
			
			if(type.get(count+1).equals("text")){
				combo[count]=new Combo(composite,SWT.DROP_DOWN);
				combo[count].setItems(comparisonString);
				combo[count].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
			else if(type.get(count+1).equals("int8")){
				combo[count]=new Combo(composite,SWT.DROP_DOWN);
				combo[count].setItems(comparisonInt);
				combo[count].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
			else if(type.get(count+1).equals("date")){
				combo[count]=new Combo(composite,SWT.DROP_DOWN);
				combo[count].setItems(comparisonDate);
				combo[count].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
			
			text[count]=new Text(composite,SWT.BORDER);
			text[count].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			length=count;
		}

		setControl(composite);	
	}
	
	public boolean performFinish() throws Exception { 
			IMarkerSet m = MarkerFactory.newMarkerSet();
			IAttributeValueMarker e;
			ExplanationCollection col;
			//different queries according to different conditions
			for(int count=0;count<length+1;count++){
				if(button[count].getSelection()){
					data[count]=text[count].getText();
					//int
					if(type.get(count+1).equals("int8")){
						//between, separated by space
						if(combo[count].getText().equals("between")){
							String bound[]=new String[2];
							st=new StringTokenizer(data[count]);
							int counter=0;
							while(st.hasMoreTokens()){
								bound[counter]=st.nextToken();
								counter++;
							}
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+" BETWEEN "+bound[0]+" AND "+bound[1]+" ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+" BETWEEN "+bound[0]+" AND "+bound[1]+" ";
							}
						}
						else{
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+combo[count].getText()+data[count]+" ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+combo[count].getText()+data[count]+" ";
							}
						}
					}
					//text
					else if(type.get(count+1).equals("text")){
						//contains
						if(combo[count].getText().equals("contains")){
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+" LIKE '%"+data[count]+"%' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+" LIKE '%"+data[count]+"%' ";
							}
						}
						//start with
						else if(combo[count].getText().equals("start with")){
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+" LIKE '"+data[count]+"%' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+" LIKE '"+data[count]+"%' ";
							}
						}
						//end with
						else if(combo[count].getText().equals("end with")){
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+" LIKE '%"+data[count]+"' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+" LIKE '%"+data[count]+"' ";
							}
						}
						else{
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+combo[count].getText()+"'"+data[count]+"' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+combo[count].getText()+"'"+data[count]+"' ";
							}
						}
					}
					//date
					else if(type.get(count+1).equals("date")){
						//before
						if(combo[count].getText().equals("before")){
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+"<"+"'"+data[count]+"' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+"<"+"'"+data[count]+"' ";
							}
						}
						//after
						else if(combo[count].getText().equals("after")){
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+">"+"'"+data[count]+"' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+">"+"'"+data[count]+"' ";
							}
						}
						//between, separated by space
						else if(combo[count].getText().equals("between")){
							String bound[]=new String[2];
							st=new StringTokenizer(data[count]);
							int counter=0;
							while(st.hasMoreTokens()){
								bound[counter]=st.nextToken();
								counter++;
							}
							if(tempQuery.equals("")){
								tempQuery=tempQuery+results.getColumnName(count+2)+" BETWEEN '"+bound[0]+"' AND '"+bound[1]+"' ";
							}
							else{
								tempQuery=tempQuery+"AND "+results.getColumnName(count+2)+" BETWEEN '"+bound[0]+"' AND '"+bound[1]+"' ";
							}
						}
					}
				}			
			}

		query="SELECT tid FROM target."+relname+" WHERE "+tempQuery+";";
		//execute the query
		rs=ConnectionManager.getInstance().execQuery(query);
		//get tid
		while(rs.next()){
			tid.add(rs.getString(1));
		}
		//explanation generation
		for(int count=0;count<length+1;count++){
			if(button[count].getSelection()){
				for(int i=0;i<tid.size();i++){
					e=(IAttributeValueMarker)MarkerFactory.newAttrMarker(relname,tid.get(i),results.getColumnName(count+2));
					m.add(e);
				}
			}
		}
		col = gen.findExplanations(m);
		ContentProvider.getInstance().getExplModel().setCol(col);
		ContentProvider.getInstance().getExplModel().setMarkers(m);
		ContentProvider.getInstance().createRanker();
		ExplView.getInstance().updateView();
		ContentProvider.getInstance().getExplCol().resetIter();
		if (ContentProvider.getInstance().getExplCol().hasNext())
			ExplRankView.getInstance().updateView(ContentProvider
					.getInstance().getExplCol().next());
		
		return true;
	}
	//get attribute type
	public void initial() throws SQLException, ClassNotFoundException{
		queryForTable="SELECT a.attnum, a.attname, p.typname FROM pg_attribute a, pg_class c, pg_type p "
				+ "WHERE c.oid = a.attrelid AND relname = '"
				+ relname + 
				"' AND relkind = 'v' AND a.attnum > 0 AND atttypid = p.oid ORDER BY attnum ASC;";
		
		rsTable=ConnectionManager.getInstance().execQuery(queryForTable);
		while(rsTable.next()){
			type.add(rsTable.getString(3));
		}
	}
}

