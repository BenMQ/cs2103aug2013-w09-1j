//@author A0105656E
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

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.exceptions.NoHistoryException;
import sg.edu.nus.cs2103.sudo.exceptions.WrongTaskDescriptionStringException;
import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TimedTask;
import sg.edu.nus.cs2103.ui.GUI;

public class StorageHandler {
	/**
	 * This StorageHandler class is responsible for:
	 * 1.Open and read file from the disk, and write changes to disk.
	 * 2.Keep track of the history, capture every change and save the undoable history records into the file.
	 * 3.Provide undo/redo functions when running
	 */
	private static StorageHandler storageHandler;
	private String fileName;
	public ArrayList<ArrayList<String>> history;
	public ArrayList<ArrayList<String>> history_redo;
	private ArrayList<Task> tasks;
	private String historyName;

	/**
	 * (This is a singleton class)
	 * Build a StorageHandler.
	 * @param  fileName
	 */
	private StorageHandler(String fileName, String historyName) {
		this.fileName = fileName;
		this.historyName = historyName;
		initializeHistory();
	}
	
	/**
	 * Give a StorageHandler.
	 * @param fileName
	 */
	public static StorageHandler getStorageHandler(String fileName, String historyName) {
		if (storageHandler == null) {
			storageHandler = new StorageHandler(fileName, historyName);
		}
		return storageHandler;
	}
	
	/**
	 * reset the StorageHandler but keep the files
	 */
	public static void resetStorageHandler() {
			storageHandler.history.clear();
			storageHandler.history_redo.clear();
			storageHandler = null;
	}
	
	/**
	 * reset the StorageHandler and clear all files.
	 */
	public static void resetAll(String fileName, String historyName) {
		File file = new File(fileName);
		File historyFile = new File(historyName);
		if (file.exists()) {
			file.delete();
			historyFile.delete();
		}
		storageHandler = null;
}
	
	/**
	 * initialize history files and ArrayLists but keep the history file on disk
	 */
	private void initializeHistory(){
		history_redo = new ArrayList<ArrayList<String>>();
		history = new ArrayList<ArrayList<String>>();
		//Add a null task list to the bottom if it is first time started
		ArrayList<String> nullTasks = new ArrayList<String>();
		history.add(nullTasks);
	}
	
	/**
	 * clear history files and overwrite the history record on disk
	 */
	public void rebuildHistory(){
		initializeHistory();
		saveHistory();
	}
	/**
	 * read history file from the disk
	 * @throws FileNotFoundException
	 */
	private void readHistory() throws FileNotFoundException{
		history = XMLSerializer.read(historyName);
	}
	
	/**
	 * 
	 * Read history file and task file from the disk to task list.
	 * @param taskIn the ArrayList of the whole task. Must be related to the task ArrayList in taskManager.
	 * @return isReloaded shows whether this is first time run or reload from previous record
	 */
	
	public boolean prepareFile(ArrayList<Task> taskIn) throws WrongTaskDescriptionStringException {
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
			GUI.print_add(Constants.MESSAGE_NO_HISTORY, 4);
			saveHistory();
			e.printStackTrace();
		} catch (IOException e) {
			GUI.print_add(Constants.MESSAGE_IO_ERROR, 4);
			e.printStackTrace();
		}
		return isReloaded;
	}
		
	/**
	 * Build a FloatingTask
	 * @param input the task description string
	 * @return flt the FloatingTask
	 */
	private FloatingTask buildFloatingTask(String input){
		int bound = input.indexOf("#");
		FloatingTask flt = new FloatingTask(input.substring(0, bound));
		if(input.substring(bound+1).equals("true")){
			flt.setComplete(true);
		}
		return flt;
	}
	
	/**
	 * Build a Deadline Task
	 * @param input the task description string
	 * @return ddt the DeadlineTask
	 */
	private DeadlineTask buildDeadlineTask(String input){
		int bound = input.indexOf("#");
		String descrbtion = input.substring(0, bound);
		input=input.substring(bound+1);
		bound = input.indexOf("#");
		String dateAndTime =  input.substring(0, bound);
		String finished = input.substring(bound+1);
		ArrayList<DateTime> dateTimes = 
		        InputParser.parseDateTime("fake 'fake' " + dateAndTime,
		                                  COMMAND_TYPE.INVALID);
		DeadlineTask ddt = new DeadlineTask(descrbtion, dateTimes);
		if(finished.equals("true")){
			ddt.setComplete(true);
		}
		return ddt;
	}
	
	/**
	 * Build a TimedTask
	 * @param input the task description string
	 * @return tmt the TimedTask
	 */
	private TimedTask buildTimedTask(String input){
		int bound = input.indexOf("#");
		String descrbtion = input.substring(0, bound);
		input=input.substring(bound+1);
		bound = input.indexOf("#");
		String dateAndTime =  input.substring(0, bound);
		String finished = input.substring(bound+1);
		ArrayList<DateTime> dateTimes =
		        InputParser.parseDateTime("fake 'fake' "+ dateAndTime,
		                                  COMMAND_TYPE.INVALID);
		TimedTask tmt = new TimedTask(descrbtion, dateTimes );
		if(finished.equals("true")){
			tmt.setComplete(true);
		}
		return tmt;
	}
	
	/**
	 * Convert one String into one Task
	 * @param input String which describes the task
	 * @return Task described by the  String
	 * @throws WrongTaskDescriptionStringException 
	 */	
	private Task stringToTask(String input) throws WrongTaskDescriptionStringException{
		int bound = input.indexOf("#");
		String taskKind = input.substring(0, bound);
		String next = input.substring(bound+1);
		if(taskKind.equals("floating")){
			return buildFloatingTask(next);
		}else if(taskKind.equals("DEADLINE")){
			return buildDeadlineTask(next);
		}else if(taskKind.equals("TIMED")){
			return buildTimedTask(next);
		}else{
			throw(new WrongTaskDescriptionStringException("Task description string error: no such kind of task."));
		}
	}
	
	/**
	 * Convert a list of Strings into a list of Tasks
	 * @param str ArrayList<String> which describes the tasks
	 * @return tasks ArrayList<Task> described by the  Strings
	 * @throws WrongTaskDescriptionStringException 
	 */	
	private ArrayList<Task> stringsToTasks(ArrayList<String> str) throws WrongTaskDescriptionStringException{
		ArrayList<Task> tasks = new ArrayList<Task>();
		for(String i: str){
			tasks.add(stringToTask(i));
		}
		return tasks;
	}
	
	/**
	 * Convert a list of Tasks into a list of Strings
	 * @param tasks ArrayList<Task> described by the  Strings
	 * @return toReturn ArrayList<String> which describes the tasks
	 */	
	private ArrayList<String> tasksToStrings(ArrayList<Task> tasks){
		ArrayList<String> toReturn = new ArrayList<String>();
		
		for(int i=0;i<tasks.size();i++){
			toReturn.add(tasks.get(i).toStringForFile());
		}
		return toReturn;
	}
	
	/**
	 * Write history records (undo) to the disk.
	 * @throws FileNotFoundException
	 */	
	private void saveHistory(){
			try {
				XMLSerializer.write(history, historyName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	/**
	 * Save all the tasks and undo-history(optional) into the disc
	 * 
	 * @param  saveHistory Boolean save the history or not
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
	 * @return tasks ArrayList<Task> the result of undo
	 * @throws NoHistoryException
	 * @throws FileNotFoundException 
	 * @throws WrongTaskDescriptionStringException 
	 */	
	public ArrayList<Task> undo() throws NoHistoryException, FileNotFoundException, WrongTaskDescriptionStringException{

		if(history.size()>1){
			history_redo.add(tasksToStrings(tasks));
			history.remove(history.size()-1);
			tasks = stringsToTasks(history.get(history.size()-1));
			saveHistory();
		return tasks;
		}else{
			throw new NoHistoryException("Can not undo anymore.");
		}
	}
	/**
	 * Redo returns the change before undo made by the user.
	 * It will not be saved after user exit
	 * @return tasks ArrayList<Task> the result of redo
	 * @throws NoHistoryException
	 * @throws FileNotFoundException 
	 * @throws WrongTaskDescriptionStringException 
	 */	
	public ArrayList<Task> redo() throws NoHistoryException, FileNotFoundException, WrongTaskDescriptionStringException{
		if(history_redo.size()>0){
			tasks = stringsToTasks(history_redo.get(history_redo.size()-1));
			history.add(tasksToStrings(tasks));
			history_redo.remove(history_redo.size()-1);
			saveHistory();
		return tasks;
		}else{
			throw new NoHistoryException("Can not redo anymore.");
		}
	}
}
