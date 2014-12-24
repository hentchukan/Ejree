package prv.adt.ejree;

import java.util.Calendar;
import java.util.Date;

import prv.adt.ejree.data.Run;
import prv.adt.ejree.data.database.RunDAO;
import prv.adt.ejree.util.DateHelper;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddRun extends Fragment {
	
	private Run run;
	private RunDAO dao;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.add_run, container, false);
		
		dao = new RunDAO(getActivity());
	    dao.open();
	    
		return rootView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		init();
		Button btnClear = (Button) getView().findViewById(R.id.clearButton);
		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clear();
			}
		});
		
		Button btnAdd = (Button) getView().findViewById(R.id.addButton);
		btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateValues();
				add();
				clear();
			}

		});
		
		((EditText) getView().findViewById(R.id.addRun_distance)).setSelectAllOnFocus(true);
		((EditText) getView().findViewById(R.id.addRun_time)).setSelectAllOnFocus(true);
		((EditText) getView().findViewById(R.id.addRun_caloris)).setSelectAllOnFocus(true);
		
		((TextView) getView().findViewById(R.id.addRun_date)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new  DatePickerDialogListener(v.getId()),calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
				
				datePicker.show();
			}
		});
		super.onViewCreated(view, savedInstanceState);
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}
	
	private void add() {
		try {
			run = dao.create(run);
			Toast.makeText(getActivity(), R.string.success_on_adding, Toast.LENGTH_SHORT).show();
		} catch (Throwable t) {
			init();
			Toast.makeText(getActivity(), R.string.error_on_adding, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void init() {
		run = new Run(new Date(), 0.0, 0.0, 0.0);
		bindFields();
	}
	
	private void clear() {
		init();
	}
	
	private void bindFields() {
		TextView tvDate = (TextView) getView().findViewById(R.id.addRun_date);
		tvDate.setText(DateFormat.format("dd/MM/yyyy", run.getDate()));
		
		EditText etDistance = (EditText) getView().findViewById(R.id.addRun_distance);
		etDistance.setText(String.valueOf(run.getDistance()));
		
		EditText etTime = (EditText) getView().findViewById(R.id.addRun_time);
		etTime.setText(String.valueOf(run.getTime()));
		
		EditText etCaloris = (EditText) getView().findViewById(R.id.addRun_caloris);
		etCaloris.setText(String.valueOf(run.getCaloris()));
	}
	
	private void updateValues() {
		EditText etDistance = (EditText) getView().findViewById(R.id.addRun_distance);
		run.setDistance(Double.valueOf(etDistance.getText().toString()));
		
		EditText etTime = (EditText) getView().findViewById(R.id.addRun_time);
		run.setTime(Double.valueOf(etTime.getText().toString()));

		EditText etCaloris = (EditText) getView().findViewById(R.id.addRun_caloris);
		run.setCaloris(Double.valueOf(etCaloris.getText().toString()));
	}

	private class DatePickerDialogListener implements OnDateSetListener {
		private int id ;
		
		public DatePickerDialogListener(int id) {
			this.id = id;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			TextView dateView = (TextView) getView().findViewById(id);
			Date date = new Date(year - 1900, monthOfYear, dayOfMonth);
			dateView.setText(DateHelper.getFormattedDate(date));
		}
		
	}
}
