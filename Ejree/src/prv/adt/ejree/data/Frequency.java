package prv.adt.ejree.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;

public class Frequency implements Comparable<Frequency>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date begin;
	private Date end;
	private String periode;
	
	private Integer runs;
	private Double distance;
	private Double time;
	private Double caloris;
	private Double speed;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
	
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

	public String getPeriode() {
		if (begin != null && end != null)
			this.periode = sdf.format(begin) + " to " + sdf.format(end);
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public Integer getRuns() {
		return runs;
	}

	public void setRuns(Integer runs) {
		this.runs = runs;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getTime() {
		return time;
	}

	public void setTime(Double time) {
		this.time = time;
	}

	public Double getCaloris() {
		return caloris;
	}

	public void setCaloris(Double caloris) {
		this.caloris = caloris;
	}
	
	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Frequency() {
		
	}
	
	public Frequency(Date begin, Date end, Integer runs, Double distance, Double time, Double caloris) {
		this.begin = begin;
		this.end = end;
		if (begin != null && end != null)
			this.periode = sdf.format(begin) + " to " + sdf.format(end);
		this.runs = runs;
		this.distance = distance;
		this.time = time;
		this.caloris = caloris;
	}

	public Frequency(Date begin, Date end, Integer runs, Double distance, Double time, Double caloris, Double speed) {
		this.begin = begin;
		this.end = end;
		if (begin != null && end != null)
			this.periode = sdf.format(begin) + " to " + sdf.format(end);
		this.runs = runs;
		this.distance = distance;
		this.time = time;
		this.caloris = caloris;
		this.speed = speed;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Frequency))
			return false;
		Frequency other = (Frequency) o;
		
		return begin.equals(other.getBegin()) && end.equals(other.getEnd());
	}

	@Override
	public String toString() {
//		return "Run on "+ DateFormat.format("dd/MM/yyyy", date)+", distance "+distance+" KM, Time "+getTime()+" Mn, Speed "+speed+" Km/Hr";
		return DateFormat.format("dd/MM/yyyy", begin)+":"+DateFormat.format("dd/MM/yyyy", end)+":"+runs+":"+distance+":"+time+":"+caloris;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Frequency(new Date(begin.getTime()), new Date(end.getTime()),Integer.valueOf(runs), Double.valueOf(distance), Double.valueOf(time), Double.valueOf(caloris));
	}
	
	@Override
	public int compareTo(Frequency frequency) {
		if (frequency.getBegin() == null)
			return -1;
		else return begin.compareTo(frequency.getBegin());
	}
	
	public static Frequency parse(String item) {
		String[] attributes = item.split(":");
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		Date begin, end;
		try {
			begin = df.parse(attributes[0]);
			end = df.parse(attributes[1]);
		} catch (ParseException e) {
			return null;
		}
		
		return new Frequency(begin, end, Integer.valueOf(attributes[2]), Double.valueOf(attributes[3]), Double.valueOf(attributes[4]), Double.valueOf(attributes[5]));
	}

}
