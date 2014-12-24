package prv.adt.ejree;

import java.text.DecimalFormat;
import java.util.List;

import prv.adt.ejree.data.Run;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RunListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Run> runs;
	
	public RunListAdapter(Context context, List<Run> runs) {
		this.runs = runs;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return runs.size();
	}

	@Override
	public Object getItem(int position) {
		return runs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			view = inflater.inflate(R.layout.run_item, parent, false);
		}
		
		Run run = runs.get(position);
		TextView tvDate = (TextView) view.findViewById(R.id.run_item_date);
		TextView tvDistance = (TextView) view.findViewById(R.id.run_item_distance);
		TextView tvTime = (TextView) view.findViewById(R.id.run_item_time);
		TextView tvSpeed = (TextView) view.findViewById(R.id.run_item_speed);
		
		tvDate.setText(DateFormat.format("dd/MM/yy", run.getDate()));
		tvDistance.setText(String.valueOf(run.getDistance()));
		DecimalFormat df = new DecimalFormat("00.00");
		tvTime.setText(String.valueOf(run.getTime().intValue()));
		tvSpeed.setText(String.valueOf(df.format(run.getSpeed())));
		
		return view;
	}

}
