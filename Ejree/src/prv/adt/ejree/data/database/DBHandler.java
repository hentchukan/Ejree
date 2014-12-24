package prv.adt.ejree.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

	public static final String TABLE_RUN = "RUN";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DATE = "RUN_DATE";
	public static final String COLUMN_DISTANCE = "RUN_DISTANCE";
	public static final String COLUMN_TIME = "RUN_TIME";
	public static final String COLUMN_CALORIS = "RUN_CALORIS";
	
	public static final String DATABASE_NAME = "RUN.DB";
	public static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_RUN + "("  
	      + COLUMN_ID + " integer primary key autoincrement, "
	      + COLUMN_DATE + " DATETIME not null unique, "
	      + COLUMN_DISTANCE + " number(10), "
	      + COLUMN_TIME + " number(10), "
	      + COLUMN_CALORIS + " number(10) "
	      + " );";
	
	public DBHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(DBHandler.class.getName(),
		        "Upgrading database from version " + oldVersion + " to "
		            + newVersion + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_RUN);
		    onCreate(database);
	}

}
