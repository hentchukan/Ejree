package prv.adt.ejree.data.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import prv.adt.ejree.data.Run;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class RunDAO {
	
	// DB Fields
	private SQLiteDatabase database;
	private DBHandler dbHandler;
	private String[] allColumns = {DBHandler.COLUMN_ID, 
		DBHandler.COLUMN_DATE, 
		DBHandler.COLUMN_DISTANCE,
		DBHandler.COLUMN_TIME,
		DBHandler.COLUMN_CALORIS};
	
	public RunDAO(Context context) {
		dbHandler = new DBHandler(context);
	}
	
	public void open() {
		database = dbHandler.getWritableDatabase();
	}
	
	public void close() {
		dbHandler.close();
	}
	
	public Run create(Run run) throws Throwable {
		ContentValues values = new ContentValues();
		values.put(DBHandler.COLUMN_DATE, run.getDateTime());
		values.put(DBHandler.COLUMN_DISTANCE, String.valueOf(run.getDistance()));
		values.put(DBHandler.COLUMN_TIME, String.valueOf(run.getTime()));
		values.put(DBHandler.COLUMN_CALORIS, String.valueOf(run.getCaloris()));
		
		long insertId = database.insertOrThrow(DBHandler.TABLE_RUN, null, values);
		
		Cursor cursor = database.query(DBHandler.TABLE_RUN,
		        allColumns, DBHandler.COLUMN_ID + " = " + insertId, null,
		        null, null, null);
		
		cursor.moveToFirst();
		Run runWithId = null;
		
		try {
			cursorToRun(cursor);
		} catch (Exception e) {
			throw e;
		}
		cursor.close();
		
		return runWithId;
	}
	
	public List<Run> retrieve() {
		List<Run> runs = new ArrayList<Run>();
		
		Cursor cursor = database.query(DBHandler.TABLE_RUN, allColumns, null, null, null, null, DBHandler.COLUMN_DATE + " Desc");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() ) {
			Run run = null;
			try {
				run = cursorToRun(cursor);
			} catch (Exception e) {
				continue;
			}
			runs.add(run);
			cursor.moveToNext();
		}
		
		cursor.close();
		return runs;
	}
	
	public List<Run> retrieve(String where, String[] args) {
		List<Run> runs = new ArrayList<Run>();
		
		Cursor cursor = database.query(DBHandler.TABLE_RUN, allColumns, where, args, null, null, DBHandler.COLUMN_DATE + " Desc");
		cursor.moveToFirst();
		while (!cursor.isAfterLast() ) {
			Run run = null;
			try {
				cursorToRun(cursor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			runs.add(run);
			cursor.moveToNext();
		}
		
		cursor.close();
		return runs;
	}
	
	public List<Run> retrieveSql(String query, String[] args) {
		List<Run> runs = new ArrayList<Run>();
		
		Cursor cursor = database.rawQuery(query, args);
		cursor.moveToFirst();
		while (!cursor.isAfterLast() ) {
			Run run = null;
			try {
				cursorToRun(cursor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			runs.add(run);
			cursor.moveToNext();
		}
		
		cursor.close();
		return runs;
	}
	
	public void delete(Run run) {
		if (run.getId() == null)
			return ;
		
	    database.delete(DBHandler.TABLE_RUN, DBHandler.COLUMN_ID + " = " + run.getId(), null);
	}
	
	public Run cursorToRun(Cursor cursor) throws Exception {
		
		Run run;
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault()).parse(cursor.getString(cursor.getColumnIndex(DBHandler.COLUMN_DATE)));
		} catch (ParseException e) {
			date = new Date();
		}
		run = new Run(date,
				cursor.getDouble(cursor.getColumnIndex(DBHandler.COLUMN_DISTANCE)),
				cursor.getDouble(cursor.getColumnIndex(DBHandler.COLUMN_TIME)),
				cursor.getDouble(cursor.getColumnIndex(DBHandler.COLUMN_CALORIS)));
		run.setId(cursor.getLong(0));
		
		return run;
	}
}
