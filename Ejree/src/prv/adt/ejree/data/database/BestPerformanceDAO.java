package prv.adt.ejree.data.database;

import java.util.ArrayList;
import java.util.List;

import prv.adt.ejree.data.Frequency;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BestPerformanceDAO {

	// DB Fields
		private SQLiteDatabase database;
		private DBHandler dbHandler;
		private String[] allColumns = {DBHandler.COLUMN_ID, 
			DBHandler.COLUMN_DATE, 
			DBHandler.COLUMN_DISTANCE,
			DBHandler.COLUMN_TIME,
			DBHandler.COLUMN_CALORIS};
		
		public BestPerformanceDAO(Context context) {
			// TODO Auto-generated constructor stub
			dbHandler = new DBHandler(context);
		}
		
		public void open() {
			database = dbHandler.getWritableDatabase();
		}
		
		public void close() {
			dbHandler.close();
		}
		
		public List<Frequency> retrieve(String where, String[] args) throws Exception {
			List<Frequency> frqs = new ArrayList<Frequency>();
			
			Cursor cursor = database.query(DBHandler.TABLE_RUN, allColumns, where, args, null, null, DBHandler.COLUMN_DATE + " Desc");
			cursor.moveToFirst();
			while (!cursor.isAfterLast() ) {
				Frequency frq = cursorToFrequency(cursor);
				frqs.add(frq);
				cursor.moveToNext();
			}
			
			cursor.close();
			return frqs;
		}
		
		public List<Frequency> retrieveSql(String query, String[] args) throws Exception{
			List<Frequency> frqs = new ArrayList<Frequency>();
			
			Cursor cursor = database.rawQuery(query, args);
			cursor.moveToFirst();
			while (!cursor.isAfterLast() ) {
				try {
					Frequency frq = cursorToFrequency(cursor);
					frqs.add(frq);
				} catch (Exception e) {
					
				}
				cursor.moveToNext();
			}
			
			cursor.close();
			return frqs;
		}
		
		public Frequency cursorToFrequency(Cursor cursor) throws Exception {
			
			Frequency frq;
			
			frq = new Frequency(null, null,
					cursor.getInt(cursor.getColumnIndex("RUNS_NUMBER")),
					cursor.getDouble(cursor.getColumnIndex("RUNS_DISTANCE")),
					cursor.getDouble(cursor.getColumnIndex("RUNS_TIME")),
					cursor.getDouble(cursor.getColumnIndex("RUNS_CALORIS")),
					cursor.getDouble(cursor.getColumnIndex("RUNS_SPEED")));
			return frq;
		}
}
