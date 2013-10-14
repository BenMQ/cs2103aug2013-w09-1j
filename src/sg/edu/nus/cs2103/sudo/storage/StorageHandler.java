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
	private ArrayList<ArrayList<String>> history;
	private ArrayList<ArrayList<String>> history_redo;

	private static StorageHandler storageHandler;
	/**
	 * (This is a singleton class)
	 * Build a StorageHandler.
	 * @param String of the file name, ArrayList of all Task objects
	 */
	private StorageHandler(String fileName, ArrayList<Task> tasks) {
		this.fileName = fileName;
		initializeHistory();
		try {
			File file = new File(fileName);
			if (!file.exists()) {
			saveHistory();
				file.createNewFile();
			} else {
				readHistory();
				prepareFile(tasks);
			}
		} catch (ClassNotFoundException e) {
			System.exit(0);

		} catch (IOException e) {
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			//Exception part will be finished after we create the Exception package
		}
	}
	
	public static StorageHandler getStorageHandler(String fileName, ArrayList<Task> tasks) {
		if (storageHandler == null) {
			storageHandler = new StorageHandler(fileName, tasks);
		}
		return storageHandler;
	}
	
	
	private void initializeHistory(){
		history_redo = new ArrayList<ArrayList<String>>();
		history = new ArrayList<ArrayList<String>>();
		//Add a null task list to the bottom
		ArrayList<String> nullTasks = new ArrayList<String>();
		history.add(nullTasks);
	}
	
	private void readHistory() throws Exception{
		history = XMLSerializer.read(Constants.HISTORY_NAME);
	}
	
	private void prepareFile(ArrayList<Task> tasks) throws IOException{
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
			System.out.println(dateAndTime);
			String finished = next.substring(bound+1);
			ArrayList<DateTime> dateTimes = InputParser.parseDateTime("fake 'fake' "+dateAndTime);
			System.out.println(dateTimes);
			
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
	private void saveHistory() throws Exception{
		XMLSerializer.write(history, Constants.HISTORY_NAME);
	}
	
	/**
	 * Save all the tasks and undo-history(optional) into the disc
	 * 
	 * @param ArrayList<Task>	The ArrayList of the tasks.
	 * 	      Boolean			save the history or not
	 */	
	public void save(ArrayList<Task> taskList, Boolean saveHistory)
			throws Exception {
		File file = new File(fileName);
		
		BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
		//Save line by line
		
		for (int i = 0; i < taskList.size(); i++) {
			output.write((taskList.get(i)).toStringForFile()+"\n");
		}
		history_redo.clear();
		if(saveHistory){
		history.add(tasksToStrings(taskList));
		saveHistory();
		}
		output.close();
	}
	
	/**
	 * Undo returns the last change made by the user.
	 * It will be saved after user exit sudo
	 * @return ArrayList<Task> the result of undo
	 */	
	public ArrayList<Task> undo() throws Exception{

		if(history.size()>1){
			history_redo.add(history.get(history.size()-1));
			history.remove(history.size()-1);
			saveHistory();
		return stringsToTasks(history.get(history.size()-1));
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

	public ArrayList<Task> redo() throws Exception{
		if(history_redo.size()>0){
			history.add(history_redo.get(history_redo.size()-1));
			ArrayList<String> toReturn = history_redo.get(history_redo.size()-1);
			history_redo.remove(history_redo.size()-1);
			saveHistory();
		return stringsToTasks(toReturn);
		}else{
			throw new NoHistoryException("Cant do this");
		}
	}

}
