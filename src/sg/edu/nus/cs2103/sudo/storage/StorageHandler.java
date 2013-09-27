package sg.edu.nus.cs2103.sudo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StorageHandler {
	/**
	 * This StorageHandler class is responsible for
	 * open and read file from the disk, and write
	 * changes to disl.(shall googleCal be included here in the future??)
	 * 
	 * @author Liu Dake
	 */
	private String fileName;
	private static final String TASK_LIST_BOUNDARY = "#boundary_here";

	public void StorageHandler(String fileName, ArrayList floatingTask, ArrayList deadLine, ArrayList timedTask){
		this.fileName = fileName;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}else{
				BufferedReader iptBuff = new BufferedReader(new FileReader(
						fileName));
				String temp = iptBuff.readLine();
				while (temp != null && temp!=TASK_LIST_BOUNDARY) {
					floatingTask.add(new FloatingTask(temp));
					temp = iptBuff.readLine();
				}
				while (temp != null && temp!=TASK_LIST_BOUNDARY) {
					deadLine.add(new DeadlineTask());
					temp = iptBuff.readLine();
				}
				while (temp != null && temp!=TASK_LIST_BOUNDARY) {
					timedTask.add(new TimedTask());
					temp = iptBuff.readLine();
				}
				iptBuff.close();
			}
		}catch(IOException e){
			feedBack(FEEDBACK_IO);
			System.exit(0);
		}
	}
	
	private void saveOneList(File file, ArrayList taskList) throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toString());
			output.newLine();
		}
		output.write(TASK_LIST_BOUNDARY);
		output.newLine();
		output.close();
	}
	
	public void save(ArrayList floatingTask, ArrayList deadLine, ArrayList timedTask) throws IOException{
		File file = new File(fileName);
		saveOneList(file, floatingTask);
		saveOneList(file, deadLine);
		saveOneList(file, timedTask);
	}
	
}
