package sg.edu.nus.cs2103.sudo.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.exceptions.MissingFileException;
import sg.edu.nus.cs2103.sudo.exceptions.NoHistoryException;
import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TimedTask;
import sg.edu.nus.cs2103.ui.UI;

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
	public ArrayList<ArrayList<String>> history;
	public ArrayList<ArrayList<String>> history_redo;
	
	private ArrayList<Task> tasks;
	
	private static StorageHandler storageHandler;
	/**
	 * (This is a singleton class)
	 * Build a StorageHandler.
	 * @param String of the file name, ArrayList of all Task objects
	 */
	private StorageHandler(String fileName) {
		this.fileName = fileName;
		initializeHistory();
	}
	
	public static StorageHandler getStorageHandler(String fileName) {
		if (storageHandler == null) {
			storageHandler = new StorageHandler(fileName);
		}
		return storageHandler;
	}
	
	public static void resetStorageHandler() {
			storageHandler.history.clear();
			storageHandler.history_redo.clear();
			storageHandler = null;
	}
	
	public static void resetAll(String fileName) {
		File file = new File(fileName);
		File historyFile = new File(Constants.HISTORY_NAME);
		if (file.exists()) {
			file.delete();
			historyFile.delete();
		}
		storageHandler = null;
}
	
	
	private void initializeHistory(){
		history_redo = new ArrayList<ArrayList<String>>();
		history = new ArrayList<ArrayList<String>>();
		//Add a null task list to the bottom if it is first time started
		ArrayList<String> nullTasks = new ArrayList<String>();
		history.add(nullTasks);
	}
	
	public void rebuildHistory(){
		initializeHistory();
		saveHistory();
	}
	
	private void readHistory() throws FileNotFoundException{
		history = XMLSerializer.read(Constants.HISTORY_NAME);
	}
	
	public boolean prepareFile(ArrayList<Task> taskIn) {
		tasks = taskIn;
		boolean isReloaded = true;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				saveHistory();
				file.createNewFile();
				isReloaded=false;
			} else {
				BufferedReader iptBuff = new BufferedReader(new FileReader(
						fileName));
				String temp = iptBuff.readLine();
				while (temp != null) {
					//read line by line
					Task nextTask = stringToTask(temp);
					tasks.add(nextTask);
					temp = iptBuff.readLine();
				}
				iptBuff.close();
				readHistory();
				
			}
			return isReloaded;
		} catch (FileNotFoundException e) {
			UI.forcePrint("History file was removed or deleted.");
			saveHistory();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isReloaded;
	}
	
	/**
	 * Convert one String into one Task
	 * @param String which describes the task
	 * @return Task described by the  String
	 */	
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
			ArrayList<DateTime> dateTimes = InputParser.parseDateTime("fake 'fake' "+dateAndTime);
			DeadlineTask ddt = new DeadlineTask(descrbtion, dateTimes);
			if(finished.equals("true")){
				ddt.setComplete(true);
			}
			return ddt;
		}else if(taskKind.equals("TIMED")){
			bound = next.indexOf("#");
			String descrbtion = next.substring(0, bound);
			next=next.substring(bound+1);
			bound = next.indexOf("#");
			String dateAndTime =  next.substring(0, bound);
			String finished = next.substring(bound+1);
			ArrayList<DateTime> dateTimes = InputParser.parseDateTime("fake 'fake' "+dateAndTime);
			TimedTask tmt = new TimedTask(descrbtion, dateTimes );
			if(finished.equals("true")){
				tmt.setComplete(true);
			}
			return tmt;
		}else{
			//should throw exception instead of return null...
			return null;
		}
	}
	
	/**
	 * Convert a list of Strings into a list of Tasks
	 * @param ArrayList<String> which describes the tasks
	 * @return ArrayList<Task> described by the  Strings
	 */	
	private ArrayList<Task> stringsToTasks(ArrayList<String> str){
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(String i: str){
			tasks.add(stringToTask(i));
		}
		return tasks;
	}
	
	/**
	 * Convert a list of Tasks into a list of Strings
	 * @param ArrayList<Task> described by the  Strings
	 * @return ArrayList<String> which describes the tasks
	 */	
	private ArrayList<String> tasksToStrings(ArrayList<Task> tasks){
		ArrayList<String> toReturn = new ArrayList<String>();
		
		for(int i=0;i<tasks.size();i++){
			toReturn.add(tasks.get(i).toStringForFile());
		}
		return toReturn;
	}
	
	//Save the history
	private void saveHistory(){
			try {
				XMLSerializer.write(history, Constants.HISTORY_NAME);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * Save all the tasks and undo-history(optional) into the disc
	 * 
	 * @param ArrayList<Task>	The ArrayList of the tasks.
	 * 	      Boolean			save the history or not
	 * @throws IOException 
	 */	
	public void save(Boolean saveHistory) throws IOException {
		File file = new File(fileName);
		
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		//Save line by line
		
		for (int i = 0; i < tasks.size(); i++) {
			output.write((tasks.get(i)).toStringForFile()+"\n");
		}
		history_redo.clear();
		if(saveHistory){
		history.add(tasksToStrings(tasks));
		saveHistory();
		}
		output.close();
	}
	
	/**
	 * Undo returns the last change made by the user.
	 * It will be saved after user exit sudo
	 * @return ArrayList<Task> the result of undo
	 * @throws NoHistoryException
	 * @throws FileNotFoundException 
	 */	
	public ArrayList<Task> undo() throws NoHistoryException, FileNotFoundException{

		if(history.size()>1){
			history_redo.add(history.get(history.size()-1));
			history.remove(history.size()-1);
			saveHistory();
			tasks = stringsToTasks(history.get(history.size()-1));
		return stringsToTasks(history.get(history.size()-1));
		}else{
			throw new NoHistoryException("Can not undo anymore.");
		}
	}

	public ArrayList<Task> redo() throws NoHistoryException, FileNotFoundException{
		if(history_redo.size()>0){
			history.add(history_redo.get(history_redo.size()-1));
			ArrayList<String> toReturn = history_redo.get(history_redo.size()-1);
			history_redo.remove(history_redo.size()-1);
				saveHistory();
			tasks = stringsToTasks(toReturn);
		return stringsToTasks(toReturn);
		}else{
			throw new NoHistoryException("Can not redo anymore.");
		}
	}
}
