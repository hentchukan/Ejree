package prv.adt.ejree.data.database;


public class Stats {

	// Number of runs per (all, month, week)
	// Distance of runs per (all, month, week)
	// Time of runs per (all, month, week)
	// 
	public static String frequency = "Select count(*) as RUNS_NUMBER, sum(RUN_DISTANCE) as RUNS_DISTANCE, sum(RUN_TIME) as RUNS_TIME, sum(RUN_CALORIS) as RUNS_CALORIS from RUN where strftime('%d.%m.%Y', RUN_DATE) BETWEEN DATE(?) AND DATE(?)";
	
	
	// Best % performance per (all, month, week)
	public static String sortByFromClause = "% LIMIT ?";
	// - Distance
	public static String sortByDistance = "Select * from RUN where DATE(RUN_DATE) BETWEEN ? AND ? Order By RUN_DISTANCE Desc";
	// - Time
	public static String sortByTime = "Select * from RUN where DATE(RUN_DATE) BETWEEN ? AND ? Order By (RUN_DISTANCE/RUN_SPEED) Desc";
	// - Speed
	public static String sortBySpeed = "Select * from RUN where DATE(RUN_DATE) BETWEEN ? AND ? Order By RUN_SPEED Desc";
	
}