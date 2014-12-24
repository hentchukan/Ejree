package prv.adt.ejree.data;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

public class Run implements Comparable<Run>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date;
	private Double distance;
	private Double time;
	private Double caloris;
	private Long id;
	
	public Run() {
		
	}
	
	public Run(Date date, Double distance, Double time, Double caloris) {
		this.date = date;
		this.distance = distance;
		this.time = time;
		this.caloris = caloris;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
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
		if (distance == null || time == null)
			return null;
		else {
			return time/distance;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Run))
			return false;
		Run other = (Run) o;
		
		return date.equals(other.getDate());
	}

	@Override
	public String toString() {
//		return "Run on "+ DateFormat.format("dd/MM/yyyy", date)+", distance "+distance+" KM, Time "+getTime()+" Mn, Speed "+speed+" Km/Hr";
		return DateFormat.format("dd/MM/yyyy", date)+":"+distance+":"+time+":"+caloris;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Run(new Date(date.getTime()), Double.valueOf(distance), Double.valueOf(time), Double.valueOf(caloris));
	}

	@Override
	public int compareTo(Run another) {
		if (another.getDate() == null)
			return -1;
		else return date.compareTo(another.getDate());
	}

	public String getDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format((this.date != null)?this.date:date);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Run parse(String item) {
		String[] attributes = item.split(":");
		
		java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = df.parse(attributes[0]);
		} catch (ParseException e) {
			return null;
		}
		
		return new Run(date, Double.valueOf(attributes[1]), Double.valueOf(attributes[2]), Double.valueOf(attributes[3]));
	}
	
}