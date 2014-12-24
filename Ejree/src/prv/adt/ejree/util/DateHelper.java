package prv.adt.ejree.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
	
	public static Date getSpecificDate(int periodType, Date current) {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.setTime(current);
		
		c.set(periodType, 1);
		return c.getTime();
	}

	public static Date getBornInf(Date bornSup, int unit) {
		Calendar c = Calendar.getInstance(Locale.getDefault());
		c.setTime(bornSup);
		
		c.set(unit, 1);
		return c.getTime();
	}
	
	public static String getFormattedDate(Date date) {
		return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date);
	}

	public static Date stepForward(Date date, Integer step) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(step, c.get(step)+1);
		
		return c.getTime();
	}

	public static Date backADay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)-1);
		
		return c.getTime();
	}
}
