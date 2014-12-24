package prv.adt.ejree;

import java.text.DecimalFormat;
import java.util.List;

import prv.adt.ejree.data.Frequency;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FrequencyListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Frequency> frequencies;
	
	public FrequencyListAdapter(Context context, List<Frequency> frequencies) {
		this.frequencies = frequencies;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return frequencies.size();
	}

	@Override
	public Object getItem(int position) {
		return frequencies.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.frequency_item, parent, false);
		}
		
		Frequency frequency = frequencies.get(position);
		TextView textView = (TextView) view.findViewById(R.id.frequency_item_periode);
		textView.setText(frequency.getPeriode());
		textView = (TextView) view.findViewById(R.id.frequency_item_runs);
		textView.setText(String.valueOf(frequency.getRuns()));
		textView = (TextView) view.findViewById(R.id.frequency_item_distance);
		textView.setText(String.valueOf(frequency.getDistance()));
		textView = (TextView) view.findViewById(R.id.frequency_item_time);
		textView.setText(String.valueOf(frequency.getTime().intValue()));
		DecimalFormat df = new DecimalFormat("00.00");
		textView = (TextView) view.findViewById(R.id.frequency_item_speed);
		textView.setText(String.valueOf(df.format((frequency.getDistance() / frequency.getTime()))));
		
		return view;
		
	}

}
