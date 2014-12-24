package prv.adt.ejree.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;

public class RunOnFiles {

	private static String filename = "runs.rns";
	private static File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
	
	public static void writeRun(Run run, Context context) throws IOException {
		File file = new File(path, filename);
		FileWriter fw = new FileWriter(file, true);
		fw.append(run.toString()+"\n");
		fw.flush();
		fw.close();
	}
	
	public static List<String> readRuns(Context context) throws IOException {
		List<String> runsList = new ArrayList<String>();
		File file = new File(path, filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
		   runsList.add(line);
		}
		br.close();
		
		return runsList;
	}
	
	public void setFilename(String filename) {
		RunOnFiles.filename = filename;
	}
	
	public void setPath(File dir) {
		RunOnFiles.path = dir;
	}
}
