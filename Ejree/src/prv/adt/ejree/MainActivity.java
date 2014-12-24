package prv.adt.ejree;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import prv.adt.ejree.data.Run;
import prv.adt.ejree.data.RunOnFiles;
import prv.adt.ejree.data.database.RunDAO;
import prv.adt.ejree.util.DateHelper;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private Date begin;
	private Date end;
	protected String m_chosen;
	
	public Date getBegin() {
		return begin;
	}

	public void setBegin(Date begin) {
		this.begin = begin;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, getFunctions()), this);
	}

	private String[] getFunctions() {
		return new String[] {
				getString(R.string.menu_add_run),
				getString(R.string.menu_show_history),
				getString(R.string.menu_frequence), 
				getString(R.string.menu_best_performance)};
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.action_import) {
			Toast.makeText(getApplicationContext(), "Import thing", Toast.LENGTH_LONG).show();
			
			///////////////////////////////////////////////////////////////////////////////////////////////// 
			//Create FileOpenDialog and register a callback ////////////////////////////////////////////////
			///////////////////////////////////////////////// 
			SimpleFileDialog fileOpenDialog = new SimpleFileDialog(MainActivity.this, SimpleFileDialog.FILE_OPEN, new SimpleFileDialog.SimpleFileDialogListener() { 
				@Override 
				public void onChosenDir(String chosenDir) { 
					// The code in this function will be executed when the dialog OK button is pushed 
					m_chosen = chosenDir;
					
					File file = new File(m_chosen);
					RunOnFiles rof = new RunOnFiles();
					rof.setPath(file.getParentFile());
					rof.setFilename(file.getName());
					
					loadSaveFromFile();
				}
			}); //You can change the default filename using the public variable "Default_File_Name" 

			fileOpenDialog.Default_File_Name = "";
			fileOpenDialog.chooseFile_or_Dir();
			Toast.makeText(getApplicationContext(), "Import done", Toast.LENGTH_LONG).show();
		} else if (item.getItemId() == R.id.action_export) {
			Toast.makeText(getApplicationContext(), "Export thing", Toast.LENGTH_LONG).show();

			// ////////////////////////////////////////////////
			// Create FileOpenDialog and register a callback
			// ////////////////////////////////////////////////
			SimpleFileDialog fileOpenDialog = new SimpleFileDialog(MainActivity.this, SimpleFileDialog.FILE_SAVE
					, new SimpleFileDialog.SimpleFileDialogListener() {
						@Override
						public void onChosenDir(String chosenDir) {
							// The code in this function will be executed when
							// the dialog OK button is pushed
							m_chosen = chosenDir;

							File file = new File(m_chosen);
							writeOnFile(file);
						}
					}); // You can change the default filename using the public
						// variable "Default_File_Name"

			fileOpenDialog.Default_File_Name = "";
			fileOpenDialog.chooseFile_or_Dir();
			Toast.makeText(getApplicationContext(), "Export done", Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void writeOnFile(File file) {
		RunDAO dao = new RunDAO(getApplicationContext());
	    dao.open();
	    
	    List<Run> runs = dao.retrieve();
	    RunOnFiles rof = new RunOnFiles();
	    rof.setPath(file.getParentFile());
	    rof.setFilename(file.getName());
	    
	    for (Run run : runs) {
	    	try {
				RunOnFiles.writeRun(run, getApplicationContext());
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	}

	private void loadSaveFromFile() {
		List<String> runString = null;
		RunDAO dao = new RunDAO(getApplicationContext());
	    dao.open();
	    
		try {
			runString = RunOnFiles.readRuns(getApplicationContext());
			for (String item : runString) {
				Run run = Run.parse(item);
				try {
					dao.create(run);
				} catch (Throwable t) {
					continue;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = null;
		
		switch (position) {
		case 0 : 
			fragment = new AddRun();
			break;
		case 1 :
			fragment = new ShowHistory();
			break;
		case 2 :
			fragment = new ShowFrequency();
			break;
		case 3 :
			fragment = new ShowBestPerformance();
			break;
		default :
			break;
				
		}
		
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
	    AlertDialog.Builder builder = new Builder(this);
	    switch(id) {
	    	
	    }
	    dialog = builder.show();
	    return dialog;
	}
	
	private void init() {
		begin = DateHelper.getSpecificDate(Calendar.DAY_OF_MONTH, new Date());
		end = new Date();
	}
	
}
