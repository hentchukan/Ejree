package prv.adt.ejree.util;

import java.util.Date;

public class Periode {

	private Date begin;
	private Date end;
	
	public Periode() {
		
	}
	
	public Periode(Date begin, Date end) {
		this.setBegin(begin);
		this.setEnd(end);
	}

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
}
