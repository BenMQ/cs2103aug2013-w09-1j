package sg.edu.nus.cs2103.sudo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.TimedTask;

public class StorageHandler {
	/**
	 * This StorageHandler class is responsible for
	 * open and read file from the disk, and write
	 * changes to disl.(shall googleCal be included here in the future??)
	 * 
	 * @author Liu Dake
	 */
	private String fileName;
	private static final String MESSAGE_IO_ERROR = null;

	public void StorageHandler(String fileName, ArrayList tasks){
		this.fileName = fileName;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}else{
				BufferedReader iptBuff = new BufferedReader(new FileReader(
						fileName));
				String temp = iptBuff.readLine();
				while (temp != null) {
					tasks.add(new FloatingTask(temp));
					temp = iptBuff.readLine();
				}
				iptBuff.close();
			}
		}catch(IOException e){
			displayError(MESSAGE_IO_ERROR);
			System.exit(0);
		}
	}
	
	private void displayError(String messageIoError) {
		// TODO Auto-generated method stub
		
	}

	private void saveOneList(File file, ArrayList taskList) throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toStringForFile());
			output.newLine();
		}
		output.newLine();
		output.close();
	}
	
	public void save(ArrayList tasks) throws IOException{
		File file = new File(fileName);
		saveOneList(file, tasks);
	}
	
}
