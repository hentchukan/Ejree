package prv.adt.ejree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import prv.adt.ejree.R.drawable;
import prv.adt.ejree.data.Run;
import prv.adt.ejree.data.database.RunDAO;
import prv.adt.ejree.util.DateHelper;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShowHistory extends Fragment implements OnClickListener, OnTouchListener {

	private List<Run> runs = new ArrayList<Run>();
	private ListView listView;
	private Date bornInf, bornSup;
	private RunDAO dao;
	private int[] heads = {R.id.show_history_date, R.id.show_history_distance, R.id.show_history_speed, R.id.show_history_time};
	
	private float historicX = Float.NaN, historicY = Float.NaN;
	private static final int DELTA = 50;
	
	public ShowHistory() {
		super();
		
		bornSup = new Date();
		bornInf = DateHelper.getBornInf(bornSup, Calendar.DAY_OF_YEAR);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.show_history, container, false);
		
		for (Integer i : heads) {
			TextView tv = (TextView) rootView.findViewById(i);
			tv.setOnClickListener(this);
		}
		
		Button born = (Button) rootView.findViewById(R.id.show_history_bornInf);
		born.setText(DateHelper.getFormattedDate(bornInf));
		born.setOnClickListener(new DatePickerListener());
		
		born = (Button) rootView.findViewById(R.id.show_history_bornSup);
		born.setText(DateHelper.getFormattedDate(bornSup));
		born.setOnClickListener(new DatePickerListener());
		
		ImageView search = (ImageView) rootView.findViewById(R.id.show_history_search);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
				
				listView.setAdapter(new RunListAdapter(getActivity(), runs));
				listView.invalidateViews();
			}
		});
		
		listView = (ListView) rootView.findViewById(R.id.listRuns);
		RunListAdapter adapter = new RunListAdapter(getActivity(), runs);
		listView.setAdapter(adapter);
		listView.setOnTouchListener(this);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> listAdapter, View view, int index, long arg3) {
				Run run = (Run) listView.getAdapter().getItem(index);
				dao.delete(run);
				search();
				listView.setAdapter(new RunListAdapter(getActivity(), runs));
				listView.invalidateViews();
			}
		});
		dao = new RunDAO(getActivity());
	    dao.open();
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
//		listView = (ListView) getActivity().findViewById(R.id.listRuns);
//		
//		runs = retrieveHistory();
//		RunListAdapter adapter = new RunListAdapter(getActivity(), runs);
//		listView.setAdapter(adapter);
		super.onViewCreated(view, savedInstanceState);
	}
	
	protected void search() {
		runs = retrieveHistory();
	}
	
	private List<Run> retrieveHistory() {
		return dao.retrieve();
	}

	public List<Run> getRuns() {
		return runs;
	}

	public void setRuns(List<Run> runs) {
		this.runs = runs;
	}
	
	public void sortDistance() {
		
	}
	
	private class DatePickerListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Calendar calendar = Calendar.getInstance();
			if (v.getId() == R.id.show_frequence_bornInf)
				calendar.setTime(bornInf);
			else calendar.setTime(bornSup);
			DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new  DatePickerDialogListener(v.getId()),calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			
			datePicker.show();
		}
		
	}
	
	private class DatePickerDialogListener implements OnDateSetListener {
		private int id ;
		
		public DatePickerDialogListener(int id) {
			this.id = id;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			Button button = (Button) getView().findViewById(id);
			Date date = null;
			
			if (id == R.id.show_frequence_bornInf) {
				bornInf = new Date(year-1900, monthOfYear, dayOfMonth);
				date = bornInf;
			} else {
				bornSup = new Date(year-1900, monthOfYear, dayOfMonth);
				date = bornSup;
			}
			
			button.setText(DateHelper.getFormattedDate(date));
		}
	}

	@Override
	public void onClick(View v) {
		for (Integer i : heads) {
			TextView tv = (TextView) getView().findViewById(i);
			if (v.getId() == tv.getId())
				tv.setBackgroundColor(drawable.red_square);
			else tv.setBackgroundColor(drawable.blue_square);
		}
		
		if (v.getId() == R.id.show_history_date) 
			Collections.sort(runs, new Comparator<Run>() {
				@Override
				public int compare(Run lhs, Run rhs) {
					return rhs.getDate().compareTo(lhs.getDate());
				}
			});
		else if (v.getId() == R.id.show_history_distance)
			Collections.sort(runs, new Comparator<Run>() {
				@Override
				public int compare(Run lhs, Run rhs) {
					return rhs.getDistance().compareTo(lhs.getDistance());
				}
			});
		else if (v.getId() == R.id.show_history_time)
			Collections.sort(runs, new Comparator<Run>() {
				@Override
				public int compare(Run lhs, Run rhs) {
					return rhs.getTime().compareTo(lhs.getTime());
				}
			});
		else if (v.getId() == R.id.show_history_speed)
			Collections.sort(runs, new Comparator<Run>() {
				@Override
				public int compare(Run lhs, Run rhs) {
					return rhs.getSpeed().compareTo(lhs.getSpeed());
				}
			});
		
		listView.invalidateViews();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            historicX = event.getX();
            historicY = event.getY();
            break;

            case MotionEvent.ACTION_UP:
            if (event.getX() - historicX < -DELTA) {
            	 int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                 if(position!=ListView.INVALID_POSITION){
                	 listView.performItemClick(listView.getChildAt(position-listView.getFirstVisiblePosition()), position, listView.getItemIdAtPosition(position));
                 }
                return true;
            } else if (event.getX() - historicX > DELTA) {
            	int position = listView.pointToPosition((int) event.getX(), (int) event.getY());
                if(position!=ListView.INVALID_POSITION){
               	 	listView.performItemClick(listView.getChildAt(position-listView.getFirstVisiblePosition()), position, listView.getItemIdAtPosition(position));
                }
                return true;
            } break;
            default: return false;
        }
        return false;
	}
	
}
