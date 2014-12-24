package prv.adt.ejree;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import prv.adt.ejree.data.Frequency;
import prv.adt.ejree.data.database.FrequencyDAO;
import prv.adt.ejree.util.DateHelper;
import prv.adt.ejree.util.Periode;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFrequency extends Fragment {

	private List<Frequency> frequencies = new ArrayList<Frequency>();
	private ListView listView;
	private Date bornInf, bornSup;
	private int unit;
	
	private FrequencyDAO dao;
	private int[] heads = {R.id.show_frequency_periode, R.id.show_frequency_distance, R.id.show_frequency_speed, R.id.show_frequency_time};
	private SparseIntArray step = null;
	
	public List<Frequency> getFrequencies() {
		return frequencies;
	}

	public void setFrequencies(List<Frequency> frequencies) {
		this.frequencies = frequencies;
	}

	public ShowFrequency() {
		super();
		
		unit = Calendar.DAY_OF_WEEK;
				
		bornSup = new Date();
		bornInf = DateHelper.getBornInf(bornSup, unit);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.show_frequency, container, false);
		
		for (Integer i : heads) {
			TextView tv = (TextView) rootView.findViewById(i);
			tv.setOnClickListener(new SortListener());
		}
		
		RadioButton rb = (RadioButton) rootView.findViewById(R.id.show_frequence_month);
		rb.setSelected(unit == Calendar.DAY_OF_WEEK);
		rb.setOnClickListener(new CheckRadioListener());
		rb = (RadioButton) rootView.findViewById(R.id.show_frequence_week);
		rb.setSelected(unit == Calendar.DAY_OF_MONTH);
		rb.setOnClickListener(new CheckRadioListener());
		rb = (RadioButton) rootView.findViewById(R.id.show_frequence_year);
		rb.setSelected(unit == Calendar.DAY_OF_YEAR);
		rb.setOnClickListener(new CheckRadioListener());
		
		Button born = (Button) rootView.findViewById(R.id.show_frequence_bornInf);
		born.setText(DateHelper.getFormattedDate(bornInf));
		born.setOnClickListener(new DatePickerListener());
		
		born = (Button) rootView.findViewById(R.id.show_frequence_bornSup);
		born.setText(DateHelper.getFormattedDate(bornSup));
		born.setOnClickListener(new DatePickerListener());
		
		ImageView search = (ImageView) rootView.findViewById(R.id.show_frequency_search);
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				search();
				
				listView.setAdapter(new FrequencyListAdapter(getActivity(), frequencies));
				listView.invalidateViews();
			}
		});
		
		listView = (ListView) rootView.findViewById(R.id.show_frequency_list);
		FrequencyListAdapter adapter = new FrequencyListAdapter(getActivity(), frequencies);
		listView.setAdapter(adapter);
		dao = new FrequencyDAO(getActivity());
	    dao.open();
	    
		return rootView;
	}
	
	protected void search() {
		List<Periode> periodes = createPeriodes(bornInf, bornSup, unit);
		frequencies = new ArrayList<Frequency>();
		
		for (Periode periode : periodes) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			try {
				List<Frequency> results = dao.retrieveSql("Select count(*) as RUNS_NUMBER, sum(RUN_DISTANCE) as RUNS_DISTANCE, sum(RUN_TIME) as RUNS_TIME, sum(RUN_CALORIS) as RUNS_CALORIS from RUN Where strftime('%Y-%m-%d', RUN_DATE) BETWEEN '"+sdf.format(periode.getBegin())+"' AND '"+sdf.format(periode.getEnd())+"'", null);
				if (results != null && results.size() > 0) {
					for (Frequency frq : results) {
						frq.setBegin(periode.getBegin());
						frq.setEnd(periode.getEnd());
						frequencies.add(frq);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private List<Periode> createPeriodes(Date bornInf, Date bornSup, int unit) {
		List<Periode> periodes = new ArrayList<Periode>();
		// Make bornInf be the first day of the unit
		// init begin as bornInf, end as bornInf
		// While (end.before bornSup) end is Add a step to the unit in question then minus a day
		// construct new period, before is Add a step to the unit in question.
		
		bornInf = DateHelper.getBornInf(bornInf, unit);
		Date begin = bornInf;
		Date end   = bornInf;
		while (begin.before(bornSup)) {
			end = DateHelper.stepForward(begin, getStep().get(unit));
			end = DateHelper.backADay(end);
			periodes.add(new Periode(begin, end));
			begin = DateHelper.stepForward(begin, getStep().get(unit));
		}
		return periodes;
	}

	public SparseIntArray getStep() {
		if (step == null) {
			step = new SparseIntArray();
			step.put(Calendar.DAY_OF_WEEK, Calendar.WEEK_OF_YEAR);
			step.put(Calendar.DAY_OF_MONTH, Calendar.MONTH);
			step.put(Calendar.DAY_OF_YEAR, Calendar.YEAR);
		}
		return step;
	}

	public void setStep(SparseIntArray step) {
		this.step = step;
	}

	private class SortListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			// TODO for clicks
			Toast.makeText(getActivity(), "showing toast of a click ", Toast.LENGTH_SHORT).show();
		}
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
	
	private class CheckRadioListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.show_frequence_week )
				unit = Calendar.DAY_OF_WEEK;
			if (v.getId() == R.id.show_frequence_month)
				unit = Calendar.DAY_OF_MONTH;
			if (v.getId() == R.id.show_frequence_year)
				unit = Calendar.DAY_OF_YEAR;
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
}
