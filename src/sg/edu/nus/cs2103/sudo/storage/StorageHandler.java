package sg.edu.nus.cs2103.sudo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.joda.time.DateTime;
import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TimedTask;

public class StorageHandler {
	/**
	 * This StorageHandler class is responsible for:
	 * 1.Open and read file from the disk, and write changes to disk.
	 * 2.Keep track of the history, capture every change and save to the file.
	 * 3.Provide undo/redo functions
	 * 
	 * @author Liu Dake
	 */
	private String fileName;
	private static final String HISTORY_NAME = "sudoHistory.dat";
	private static final String MESSAGE_HISTORY_LOAD_ERROR = "Loading history file error: file can not be found.";
	private static final String MESSAGE_LAST_HISTORY = "No more histories. Can not undo.";
	private ArrayList<ArrayList<String>> history;
	private ArrayList<ArrayList<String>> history_redo;

	private static StorageHandler storageHandler;
	
//	This is a singleton class.
	private StorageHandler(String fileName, ArrayList<Task> tasks) {
		this.fileName = fileName;
		try {
			File file = new File(fileName);
			history_redo = new ArrayList<ArrayList<String>>();
			if (!file.exists()) {
				history = new ArrayList<ArrayList<String>>();
				ArrayList<String> nullTasks = new ArrayList<String>();
				history.add(nullTasks);
				XMLSerializer.write(history, HISTORY_NAME);
				file.createNewFile();
			} else {
				history = XMLSerializer.read(HISTORY_NAME);
				System.out.println(history);
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
			//displayError(MESSAGE_HISTORY_LOAD_ERROR);
			System.exit(0);

		} catch (IOException e) {
			//displayError(MESSAGE_IO_ERROR);
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static StorageHandler getStorageHandler(String fileName, ArrayList<Task> tasks) {
		if (storageHandler == null) {
			storageHandler = new StorageHandler(fileName, tasks);
		}
		return storageHandler;
	}
	
	

	//DO NOT delete
	private void display(String message) {
	
	}

	
	private Task stringToTask(String input){
		int bound = input.indexOf("#");
		String taskKind = input.substring(0, bound);
		String next = input.substring(bound+1);
		if(taskKind.equals("floating")){
			bound = next.indexOf("#");
			FloatingTask flt = new FloatingTask(next.substring(0, bound));
			if(next.substring(bound+1).equals("true")){
				flt.setComplete(true);
			}
			return flt;
		}else if(taskKind.equals("DEADLINE")){
			bound = next.indexOf("#");
			String descrbtion = next.substring(0, bound);
			next=next.substring(bound+1);
			bound = next.indexOf("#");
			String dateAndTime =  next.substring(0, bound);
			String finished = next.substring(bound+1);
			ArrayList<DateTime> dateTimes = InputParser.parseDateTime("fake fake "+dateAndTime);
			DeadlineTask ddt = new DeadlineTask(descrbtion, dateTimes);
			return ddt;
		}else if(taskKind.equals("TIMED")){
			bound = next.indexOf("#");
			String descrbtion = next.substring(0, bound);
			next=next.substring(bound+1);
			bound = next.indexOf("#");
			String dateAndTime =  next.substring(0, bound);
			String finished = next.substring(bound+1);
			ArrayList<DateTime> dateTimes = InputParser.parseDateTime("fake fake "+dateAndTime);
			TimedTask tmt = new TimedTask(descrbtion, dateTimes );
			return tmt;
		}else{
			//throw exception
			return null;
		}
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
	
	private void saveHistory() throws Exception{
		XMLSerializer.write(history, HISTORY_NAME);
	}
	
	public void save(ArrayList<Task> taskList, Boolean saveHistory)
			throws Exception {
		File file = new File(fileName);
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toStringForFile()+"\n");
			//output.newLine();
			//System.out.print("Saving:" + i+"th, "+(taskList.get(i)).toString() + "\n");
		}
		history_redo.clear();
		if(saveHistory){
		System.out.println("Recording new history: "+tasksToStrings(taskList));
		history.add(tasksToStrings(taskList));
		XMLSerializer.write(history, HISTORY_NAME);
		}
		output.close();
	}
	
	public ArrayList<Task> undo() throws Exception{
		System.out.println("Undoing!");
		if(history.size()>1){
			System.out.println("History size >1!");
			history_redo.add(history.get(history.size()-1));
			System.out.println("redo is good");
			System.out.println("passing to redo:"+history.get(history.size()-1));
			
			history.remove(history.size()-1);
			System.out.println("now the latest should be: "+history.get(history.size()-1));
			
		ArrayList<String> clone = (ArrayList<String>) history.get(history.size()-1);
		saveHistory();
		return stringsToTasks(clone);
		
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

	public ArrayList<Task> redo() throws Exception{
		System.out.println("Redoing!");
		if(history_redo.size()>0){
			System.out.println("Redo History size >0!");
			history.add(history_redo.get(history_redo.size()-1));
			System.out.println("passing to undo:"+history.get(history.size()-1));
			System.out.println("now the latest should be: "+history.get(history.size()-1));
			ArrayList<String> clone = (ArrayList<String>) history_redo.get(history_redo.size()-1);
			
			history_redo.remove(history_redo.size()-1);
			saveHistory();
		return stringsToTasks(clone);
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

}
