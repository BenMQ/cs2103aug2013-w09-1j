package sg.edu.nus.cs2103.sudo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.Task;

public class StorageHandler {
	/**
	 * This StorageHandler class is responsible for:
	 * 1.Open and read file from the disk, and write changes to disk.
	 * 2.Keep track of the history, capture every change and save to the file.
	 * 
	 * @author Liu Dake
	 */
	private String fileName;
	private static final String MESSAGE_IO_ERROR = null;
	private static final String HISTORY_NAME = "sudoHistory.dat";
	private static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading history file error: file can not be found.";
	private static final String MESSAGE_LAST_HISTORY = "No more histories. Can not undo.";

	private History history;

	public StorageHandler(String fileName, ArrayList tasks) {
		this.fileName = fileName;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				
				history = new History(HISTORY_NAME);
				history.addNull();
				XMLSerializer.write(history);
				file.createNewFile();
			} else {
				history = XMLSerializer.read(HISTORY_NAME);
				BufferedReader iptBuff = new BufferedReader(new FileReader(
						fileName));
				String temp = iptBuff.readLine();
				while (temp != null) {
					tasks.add(new FloatingTask(temp));
					temp = iptBuff.readLine();
				}
				iptBuff.close();
			}
		} catch (ClassNotFoundException e) {
			displayError(MESSAGE_HISTORY_LOAD_ERROR);
			System.exit(0);

		} catch (IOException e) {
			displayError(MESSAGE_IO_ERROR);
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayError(String messageIoError) {
		// TODO Auto-generated method stub

	}

	private void saveOneList(File file, ArrayList<Task> taskList)
			throws Exception {
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toStringForFile());
			output.newLine();
			System.out.print("Saving:" + (taskList.get(i)).toString() + "\n");
		}
		HistorySlice slice = new HistorySlice(taskList);
		history.add(slice);
		XMLSerializer.write(history);
		output.newLine();
		output.close();
	}

	public void save(ArrayList<Task> tasks) throws Exception {
		File file = new File(fileName);
		saveOneList(file, tasks);
	}
	
	public boolean getLastHistory(ArrayList<Task> tasks){
		if(history.undoable()){
		ArrayList<Task> clone = (ArrayList<Task>) history.undo().getTasks().clone();
		tasks = clone;
		return true;
		}else{
			System.out.print(MESSAGE_LAST_HISTORY);
			return false;
		}
	}

}
