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

import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
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
	private static final String HISTORY_REDO_NAME = "sudoHistoryRedo.dat";
	private static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading history file error: file can not be found.";
	private static final String MESSAGE_LAST_HISTORY = "No more histories. Can not undo.";
	private ArrayList<ArrayList<String>> history;
	private ArrayList<ArrayList<String>> history_redo;

	public StorageHandler(String fileName, ArrayList<Task> tasks) {
		this.fileName = fileName;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				history = new ArrayList<ArrayList<String>>();
				ArrayList<String> nullTasks = new ArrayList<String>();
				history.add(nullTasks);
				history_redo = new ArrayList<ArrayList<String>>();
				XMLSerializer.write(history, HISTORY_NAME);
				XMLSerializer.write(history_redo, HISTORY_REDO_NAME);
				file.createNewFile();
			} else {
				history = XMLSerializer.read(HISTORY_NAME);
				history_redo = XMLSerializer.read(HISTORY_REDO_NAME);
			//	System.out.print("ahhhhhhhhh");
				BufferedReader iptBuff = new BufferedReader(new FileReader(
						fileName));
				String temp = iptBuff.readLine();
				while (temp != null) {
					Task nextTask = stringToTask(temp);
					tasks.add(nextTask);
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

	
	private Task stringToTask(String input){
		int bound = input.indexOf("#");
		String taskKind = input.substring(0, bound);
		String next = input.substring(bound+1);
		//if(taskKind.equals("floating")){
			bound = next.indexOf("#");
			FloatingTask flt = new FloatingTask(next.substring(0, bound));
			if(next.substring(bound+1).equals("true")){
				flt.setComplete(true);
			}
			return flt;
		//}else if(taskKind.equals("DEADLINE")){
			
		//}else if(taskKind.equals("TIMED")){
			
		//}else{
			
		//}
	
	}
	
	private ArrayList<Task> stringsToTasks(ArrayList<String> str){
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(String i: str){
			tasks.add(stringToTask(i));
		}
		return tasks;
	}
	
		
	private ArrayList<String> tasksToStrings(ArrayList<Task> tasks){
		ArrayList<String> toReturn = new ArrayList<String>();
		
		for(int i=0;i<tasks.size();i++){
			toReturn.add(tasks.get(i).toStringForFile());
		}
		return toReturn;
	}
	
	
	public void save(ArrayList<Task> taskList)
			throws Exception {
		File file = new File(fileName);
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toStringForFile()+"\n");
			//output.newLine();
			System.out.print("Saving:" + i+"th, "+(taskList.get(i)).toString() + "\n");
		}
		history.add(tasksToStrings(taskList));
		history_redo.clear();
		XMLSerializer.write(history, HISTORY_NAME);
		XMLSerializer.write(history_redo, HISTORY_REDO_NAME);
		//output.newLine();
		output.close();
	}
	
	public ArrayList<Task> undo() throws NoHistoryException{
		if(history.size()>1){
			history_redo.add(history.get(history.size()-1));
			history.remove(history.size()-1);
			
		ArrayList<String> clone = (ArrayList<String>) history.get(history.size()-1);
		return stringsToTasks(clone);
		
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

	public ArrayList<Task> redo() throws NoHistoryException{
		if(history_redo.size()>0){
			history.add(history_redo.get(history_redo.size()-1));
			ArrayList<String> clone = (ArrayList<String>) history_redo.get(history_redo.size()-1);
			history_redo.remove(history_redo.size()-1);
		return stringsToTasks(clone);
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

}
