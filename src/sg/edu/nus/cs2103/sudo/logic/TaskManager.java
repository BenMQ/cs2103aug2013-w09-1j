package sg.edu.nus.cs2103.sudo.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTime.Property;
import org.joda.time.DateTimeComparator;
import org.joda.time.MutableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.exceptions.NoHistoryException;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;
import sg.edu.nus.cs2103.ui.UI;

/**
 * 
 * @author chenminqi
 * @author Ipsita Mohapatra A0101286N
 * 
 *         This is a singleton class responsible for handling the Task objects.
 *         The appropriate methods are called upon by the InputParser to execute
 *         user commands such as adding, editing, searching and deleting Task
 *         objects. It is also responsible for throwing exceptions when
 *         necessary.
 * 
 */

public class TaskManager {
	private static final int MAX_CHARACTER_LENGTH = 17;

	private static TaskManager taskManager;

	// A list of timed, deadline and floating tasks
	private ArrayList<Task> tasks;

	// The storage handler
	private StorageHandler storage;

	// is this the first time sudo is run?
	private boolean isReloaded = false;

	private TaskManager() throws Exception {
		tasks = new ArrayList<Task>();
		storage = StorageHandler.getStorageHandler(Constants.FILE_NAME);
		isReloaded = storage.prepareFile(tasks);
		updateAllIds();
	}

	public static TaskManager getTaskManager() {
		try {
			if (taskManager == null) {
				taskManager = new TaskManager();
			}
			return taskManager;
		} catch (Exception e) {

		}
		return taskManager;
	}

	/**
	 * Load an ArrayList of tasks into memory. This method should be called upon
	 * launch after the storage unit has read the stored item from disk. This
	 * action overrides any exiting tasks stored in memory
	 * 
	 * @param tasks
	 *            ArrayList of tasks that is provided by the storage unit
	 */
	public void preloadTasks(ArrayList<Task> tasks) {
		this.tasks = tasks;
	}

	public void relaunch() {
		StorageHandler.resetAll(Constants.FILE_NAME);
		tasks = new ArrayList<Task>();
		storage = StorageHandler.getStorageHandler(Constants.FILE_NAME);
		isReloaded = storage.prepareFile(tasks);
		updateAllIds();
		try {
			taskManager = new TaskManager();
			System.out.println("Files rebuilt.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Files rebuiling failed.");
			e.printStackTrace();
		}
	}
	/**
	 * Adds a new task into the list. Maintains a sorted list of items after
	 * each add.
	 * 
	 * @param newTask
	 * @return floatingTasks with new additions
	 * @throws Exception
	 */
	public ArrayList<Task> addTask(Task newTask) throws Exception {
		assert (newTask != null);

		newTask.setId(tasks.size() + 1);
		tasks.add(newTask);

		sortAndUpdateIds();
		saveToHistory();
		return tasks;
	}

	/**
	 * Replaces the task indicated by the displayId with the newTask Changes
	 * from one type of task to another if necessary.
	 * 
	 * 
	 * @param displayId
	 * @param newTask
	 * @return floatingTasks after editing
	 * @throws Exception
	 */
	public ArrayList<Task> editTask(int taskId, String taskDescription,
			ArrayList<DateTime> dates) throws IllegalStateException,
			IndexOutOfBoundsException, Exception {
		assert (dates.size() <= 2);
		
		checkEmptyList();

		int index = taskId - 1;
		checkValidityIndex(index);
		
		System.out.printf(Constants.MESSAGE_EDIT, taskId);
		editTaskWithIndex(taskDescription, dates, index);

		sortAndUpdateIds();
		saveToHistory();
		return tasks;

	}

	/**
	 * Helper method to edit the description in a given task
	 * 
	 * @param taskDescription
	 * @param task
	 * @return
	 */
	private Task editDescription(String taskDescription, Task task) {
		if (taskDescription != "" && taskDescription != null) {
			task.setDescription(taskDescription);
		}
		return task;
	}

	/**
	 * Helper method to edit the date and time in a given task
	 * 
	 * @param dates
	 * @param task
	 * @return
	 */
	private Task editDateTime(ArrayList<DateTime> dates, Task task) {
		if (dates.size() == 1) {
			if (!(task instanceof DeadlineTask)) {
				task = new DeadlineTask(task.getId(), task.getDescription(),
						task.isComplete(), dates.get(0));
			} else {
				task.setEndTime(dates.get(0));
			}
		} else if (dates.size() == 2) {
			checkValidityTimes(dates.get(0), dates.get(1));
			if (!(task instanceof TimedTask)) {
				task = new TimedTask(task.getId(), task.getDescription(),
						task.isComplete(), dates.get(0), dates.get(1));
			} else {
				task.setStartTime(dates.get(0));
				task.setEndTime(dates.get(1));
			}
		}

		return task;
	}

	/**
	 * Helper method to editTask given the index in the ArrayList<Task>
	 * 
	 * @param taskDescription
	 * @param dates
	 * @param index
	 */
	private void editTaskWithIndex(String taskDescription,
			ArrayList<DateTime> dates, int index) {

		Task oldTask = tasks.remove(index);
		Task newTask = editDescription(taskDescription, oldTask);
		newTask = editDateTime(dates, newTask);
		tasks.add(newTask);
	}
	
	/**
	 * Prints tasks to stdout. Incomplete tasks are always printed by default.
	 * If showAll is set to true, completed tasks are printed as well.
	 * 
	 * @param showAll
	 *            set to true to include completed tasks
	 */
	public void displayAllTasks(boolean showAll) throws IllegalStateException {
		checkEmptyList();
		int previousDay = 0;
		DateTime previousDate = null;
		boolean floatingStarted = false;
		boolean finishedStarted = false;
		
		if(showAll){
			System.out.print(Constants.MESSAGE_DISPLAY_ALL);
		} else {
			System.out.print(Constants.MESSAGE_DISPLAY);
		}
		
		for (int i = 0; i < tasks.size(); i++) {

			Task task = tasks.get(i);
			
			String completed = "";
			if (task.isComplete()) {
				completed = "Done!";
			}
			if (showAll || !task.isComplete) {	
				
				//Start of Day-level separators
				if(!task.isComplete() && (isTimedTask(task) || isDeadlineTask(task))){
					if(previousDay == 0 || task.getEndTime().getDayOfYear() != previousDay){
						previousDay = task.getEndTime().getDayOfYear();
						previousDate = task.getEndTime();
						
						//Todo: need this method to generate separators of constant size regardless of middle content
						UI.printDaySeparator(previousDate);
					}
				} else {
					if(!floatingStarted && isFloatingTask(task)){
						floatingStarted = true;
						System.out.println(Constants.FLOATING_TASK_SEPARATOR);
					}
					if(!finishedStarted && task.isComplete()){
						finishedStarted = true;
						System.out.println(Constants.FINISHED_TASK_SEPARATOR);
					}
				}
				//End of Day-level separators
				
//				System.out.println(task.toString() + " " + completed);
				System.out.println(UI.prettyPrint(task) + " " + completed);
			}

		}
	}

	/**
	 * Prints all incomplete tasks only
	 */
	public void displayAllTasks() throws IllegalStateException {
		displayAllTasks(false);
	}

	/**
	 * TODO: REFACTOR
	 * Displays the floating tasks only. To be shown in the side bar in the GUI
	 * Formatted to be 17 characters per line. 
	 */
	public String AllFloatingTasks(){
		
		checkEmptyList();
		String toReturn = "";
		ArrayList<FloatingTask> floatingTasks = this.getFloatingTasks();
		
		if (floatingTasks.size() == 0) {
			return (Constants.MESSAGE_NO_FLOATING_TASKS);
		}
		
		for (FloatingTask task: floatingTasks) {
			if (!task.isComplete()) {
				String str = task.toString();
				
				assert (!str.isEmpty());
				if (str.length() > MAX_CHARACTER_LENGTH) { 
					String[] tokens = str.split(" ");
					int currLength = 0;
					
					for (int j=0; j<tokens.length; j++) {
						String token = tokens[j];
						currLength += token.length();
						if (currLength > 17 && j > 1) {
							currLength = 3;
							currLength += token.length();
							toReturn += "\n   ";
						}
						toReturn += token + " ";
					}
				} else {
					toReturn += str;
				}
				toReturn += "\n";				
			}
		}
		
		return toReturn;
		
		
	}

	/**
	 * Mark an incomplete task as completed.
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	public ArrayList<Task> markAsComplete(int taskId) throws Exception {

		int index = taskId - 1;
		checkValidityIndex(index);

		Task currTask = tasks.get(index);
		if (currTask.isComplete()) {
			throw new UnsupportedOperationException(
					Constants.MESSAGE_ALREADY_COMPLETE);
		}

		currTask.setComplete(true);
		System.out.printf(Constants.MESSAGE_FINISH, currTask.description);
		sortAndUpdateIds();
		saveToHistory();
		return tasks;
	}

	/**
	 * Mark a completed task as incomplete.
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	public ArrayList<Task> markAsIncomplete(int taskId) throws Exception {
		int index = taskId - 1;
		checkValidityIndex(index);

		Task currTask = tasks.get(index);
		if (!currTask.isComplete()) {
			throw new UnsupportedOperationException(
					Constants.MESSAGE_ALREADY_INCOMPLETE);
		}

		currTask.setComplete(false);
		System.out.printf(Constants.MESSAGE_UNFINISH, currTask.description);
		sortAndUpdateIds();
		saveToHistory();
		return tasks;
	}

	/**
	 * Search for Task objects matching the input search string. By default,
	 * only incomplete tasks will be searched.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public ArrayList<Task> searchAndDisplay(String searchStr)
			throws NullPointerException, IllegalStateException {
		System.out.printf(Constants.MESSAGE_SEARCH, searchStr);
		ArrayList<Task> searchResults = search(searchStr, false);
		displaySearchResults(searchResults);
		return searchResults;
	}

	/**
	 * Search for Task objects matching the input search string. Searches all
	 * Task objects.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public ArrayList<Task> searchAllAndDisplay(String searchStr)
			throws NullPointerException, IllegalStateException {

		ArrayList<Task> searchResults = search(searchStr, true);
		displaySearchResults(searchResults);
		return searchResults;
	}

	/**
	 * Searches the floatingTasks for matches with the searchStr By default,
	 * only incomplete tasks will be searched
	 * 
	 * @param searchStr
	 * @return ArrayList of Task objects
	 */
	public ArrayList<Task> search(String searchStr, boolean searchAll)
			throws NullPointerException, IllegalStateException {

		if (searchStr == null || searchStr == "") {
			throw new NullPointerException(Constants.MESSAGE_INVALID_SEARCH);
		}
		
		if (tasks.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}

		ArrayList<Task> searchResults = new ArrayList<Task>();

		for (int i = 0; i < tasks.size(); i++) {
			Task currTask = tasks.get(i);
			String currTaskStr = currTask.toString();

			if (currTaskStr.toLowerCase().contains(searchStr.toLowerCase())) {
				if (searchAll || !currTask.isComplete) {
					searchResults.add(currTask);
				}
			}
		}
		return searchResults;
	}

	/**
	 * Prints out the list of search results containing Task objects.
	 */
	public void displaySearchResults(ArrayList<Task> searchResults)
			throws IllegalStateException {
		if (searchResults.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_NO_SEARCH_RESULTS);
		}

		System.out.println();
		System.out.println("Search Results");
		for (int i = 0; i < searchResults.size(); i++) {
			System.out.println(searchResults.get(i).toString());
		}
	}

	/**
	 * Search and prints out intervals that are free during the current day.
	 * Intervals shorter than 10 minutes are ignored.
	 * 
	 * @param dateTimes 0 to 2 DateTimes. If only one is specified, the date range will be that day.
	 * if none is specified, the date range will be the current day.
	 * 
	 * @author chenminqi
	 */
    public void searchForFreeIntervals(ArrayList<DateTime> dateTimes) {
    	if (dateTimes.size() > 2) {
            System.out.print(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
            return;
        }
    	
        assert(dateTimes.size() >= 0 && dateTimes.size() <= 2);
        ArrayList<DateTime> timeRange = getFlexibleTimeRange(dateTimes);
        ArrayList<MutableInterval> free = getFreeIntervals(timeRange);
        boolean noSlotsFound = true;
        
        for (int i = 0; i < free.size(); i++) {
            MutableInterval interval = free.get(i);
            if (interval.toDurationMillis() >= Constants.FREE_SLOT_MINIMUM_DURATION) {
                if (noSlotsFound) {
                    System.out.println(Constants.MESSAGE_FREE_SLOTS_PREFIX
                            + timeRange.get(0).toString("dd MMMM hh:mm a") + " to " + timeRange.get(1).toString("dd MMMM hh:mm a"));
                    noSlotsFound = false;
                }
                String output = interval.getStart().toString("dd MMMM hh:mm a") + " to " + interval.getEnd().toString("dd MMMM hh:mm a");
                System.out.println(output);
            }
        }
        if (noSlotsFound) {
            System.out.println(Constants.MESSAGE_NO_FREE_SLOTS);
        }
    }
    
    /**
     * Produces a start DateTime and an end DateTime based on the argument given.
     * If the input is an empty array, the range will be the current day.
     * If the input has one DateTime, the range will be that particular day.
     * If the input has two DateTimes, the range will be that.
     * @param dateTimes
     * @return range calculated
     */
    private ArrayList<DateTime> getFlexibleTimeRange(ArrayList<DateTime> dateTimes) {
        assert(dateTimes.size() >= 0 && dateTimes.size() <= 2);
        if (dateTimes.size() == 2) {
            if (dateTimes.get(0).isAfter(dateTimes.get(1))) {
                Collections.reverse(dateTimes);
            }
            return dateTimes;
        } else {
            DateTime day;
            if (dateTimes.size() == 1) {
                day = dateTimes.get(0);
            } else {
                day = DateTime.now();
            }
            DateTime startOfDay = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), 0, 0, 0);
            DateTime endOfDay = new DateTime(day.getYear(), day.getMonthOfYear(), day.getDayOfMonth(), 23, 59, 59);
            ArrayList<DateTime> range = new ArrayList<DateTime>(2);
            range.add(startOfDay);
            range.add(endOfDay);
            return range;
        }
    }
    
	/**
	 * Searches for all occupied time slots of today. If the actual slot of the day ends before 2359hrs,
	 * an interval [2359hrs, 2359hrs] which lasts for 0 seconds will be inserted at the end.
	 * @return intervals that are occupied today, the last item is guaranteed to end at 2359hrs, and hence
	 *         guaranteed to have at least 1 item returned.
	 * @author chenminqi
	 * @param timeRange 
	 */
	public ArrayList<MutableInterval> getOccupiedIntervals(ArrayList<DateTime> timeRange) {
	    sortTasks();
	    
	    DateTime start = timeRange.get(0);
	    DateTime end = timeRange.get(1);
	    
	    ArrayList<MutableInterval> occupied = new ArrayList<MutableInterval>();
	    MutableInterval last = new MutableInterval(end, end);
	    occupied.add(last);
	    
	    for (int i = tasks.size() - 1; i >= 0 ; i--) {
	        Task task = tasks.get(i);

            if (task.isComplete() || ! (task instanceof TimedTask)) {
                // we are only concerned with incomplete TimedTask
                continue;
            } else if (! task.endTime.isAfter(start)) {
	            // all unprocessed items ends before today, no more items needs processing
	            break;
	        } else if (! task.startTime.isBefore(last.getStart())) {
	            // we are only concerned with tasks that starts before the last occupied slot
	            continue;
	        } else if (! task.endTime.isBefore(last.getStart())) {
	            // overlap between task's end time and the last occupied slot's start time
	            last.setStart(task.startTime);
	        } else {
	            // there is a gap
	            last = new MutableInterval(task.startTime, task.endTime);
	            occupied.add(last);
	            if (! task.startTime.isAfter(start)) {
	                // reached the start of the day, no more processing needed.
	                last.setStart(start);
	                break;
	            }
	        }
	        
	    }
	    Collections.reverse(occupied);
	    return occupied;
	}
	
	/**
     * Searches for all free time slots of a specified time range
     * @param dateTimes 0 to 2 DateTimes. If only one is specified, the date range will be that day.
     * if none is specified, the date range will be the current day.
     * @return intervals that are free today
     * @author chenminqi
     */
	public ArrayList<MutableInterval> getFreeIntervals(ArrayList<DateTime> dateTimes) {
        DateTime start = dateTimes.get(0);
        ArrayList<MutableInterval> free = new ArrayList<MutableInterval>();
        
        ArrayList<MutableInterval> occupied = getOccupiedIntervals(dateTimes);

        if (occupied.get(0).getStart().isAfter(start)) {
            free.add(new MutableInterval(start, occupied.get(0).getStart()));
        }
        
        for (int i = 0; i < occupied.size() - 1; i ++) {
            free.add(new MutableInterval(occupied.get(i).getEnd(), occupied.get(i + 1).getStart()));
        }
        
        return free;
	}
	
	/**
	 * Schedules a task
	 * @param description
	 * @param timeRange
	 * @throws Exception 
	 */
	public void scheduleTask(String description, ArrayList<DateTime> dateTimes) throws Exception {
    	if (dateTimes.size() > 2) {
            System.out.print(Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
            return;
        }
		
	    ArrayList<DateTime> timeRange = getFlexibleTimeRange(dateTimes);
	    ArrayList<MutableInterval> free = getFreeIntervals(timeRange);
	    for (int i = 0; i < free.size(); i++) {
	        MutableInterval candidate = free.get(i);
	        DateTime start;
            DateTime startDay0800;
            DateTime startDay2300;
            DateTime nextDay0800;
            
            while (candidate.toDurationMillis() >= 2 * 60 * 60 * 1000) {
                start = candidate.getStart();
                startDay0800 = new DateTime(start.getYear(), start.getMonthOfYear(), start.getDayOfMonth(), 8, 0, 0);
                startDay2300 = new DateTime(start.getYear(), start.getMonthOfYear(), start.getDayOfMonth(), 23, 0, 0);
                nextDay0800 = startDay0800.plusDays(1);
                try {
                    if (start.isBefore(startDay0800)) {
                        candidate.setStart(startDay0800);
                        continue;
                    } else if (start.isAfter(startDay2300)) {
                        candidate.setEnd(nextDay0800);
                        continue;
                    }
                } catch (IllegalArgumentException e) {
                    // duration is too short for consideration, moving on
                    break;
                }
                
                if (!start.plusHours(2).isAfter(startDay2300)) {
                    ArrayList<DateTime> range = new ArrayList<DateTime>(2);
                    range.add(start);
                    range.add(start.plusHours(2));
                    TimedTask task = new TimedTask(description, range);
                    addTask(task);
                    System.out.printf(Constants.MESSAGE_ADD_TIMED, task.description,
                            UI.formatDate(task.startTime), UI.formatDate(task.endTime));
                    saveToHistory();
                    return;
                } else {
                    break;
                }
            }
	        
	    }
	    System.out.println(Constants.MESSAGE_NO_FREE_SLOTS);
	}
	/**
	 * Removes the task by first searching for the search string in the task
	 * description. If there is exactly one match, just delete it. If there are
	 * multiple matches, display all searchResults to user. By default,
	 * searchResults searches through all tasks. Wait for user input to delete
	 * again.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public int delete(String searchStr) throws IOException {

		if (searchStr == null || searchStr == "") {
			throw new NullPointerException(Constants.MESSAGE_INVALID_DELETE);
		}

		ArrayList<Task> searchResults = search(searchStr, true);
		int numResults = searchResults.size();

		if (numResults == 0) {
			throw new IllegalStateException(Constants.MESSAGE_NO_SEARCH_RESULTS);
		} else if (numResults == 1) {
			delete(searchResults.get(0).getId());
		} else {
			displaySearchResults(searchResults);
		}

		return numResults;
	}

	/**
	 * Given the id of the task, the task is deleted from floatingTasks
	 * 
	 * @throws IOException
	 */
	public void delete(int taskId) throws IOException {
		int index = taskId - 1;
		
		checkValidityIndex(index);

		System.out.printf(Constants.MESSAGE_DELETE,
				tasks.get(index).description);

		tasks.remove(index);
		sortAndUpdateIds();
		saveToHistory();
	}

	/**
	 * @author Liu Dake
	 * 
	 *         If history does not exist, throw Exception
	 * 
	 * @return
	 */
	public void undo() {
		try {
			tasks = (ArrayList<Task>) storage.undo().clone();
			System.out.println(Constants.MESSAGE_UNDO);
			//saveTasks();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			System.out.println(Constants.MESSAGE_HISTORY_LOAD_ERROR);
		} catch (NoHistoryException e) {
			System.out.println(Constants.MESSAGE_LAST_HISTORY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateAllIds();
		// return tasks;
	}

	/**
	 * @author Liu Dake
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Task> saveTasks() throws IOException {
		storage.save(false);
		return tasks;
	}

	/**
	 * @author Liu Dake
	 * 
	 *         If no redo provision exists in history, throw Exception
	 * 
	 * @return
	 */
	public void redo() {
		try {
			tasks = (ArrayList<Task>) storage.redo().clone();
			System.out.println(Constants.MESSAGE_REDO);
			//saveTasks();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			System.out.println(Constants.MESSAGE_HISTORY_LOAD_ERROR);
		} catch (NoHistoryException e) {
			System.out.println(Constants.MESSAGE_LAST_HISTORY);
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateAllIds();
		// return tasks;
	}

	/**
	 * To check if the task list is empty. If yes, throw exception.
	 */
	private void checkEmptyList() throws IllegalStateException {
		if (tasks.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}
	}

	/**
	 * Saves the task list to history and also saves the file. To be executed
	 * after each operation is completed.
	 * 
	 * @throws IOException
	 */
	private void saveToHistory() throws IOException {
		storage.save(true);
	}

	/**
	 * Sorts all the Task objects and updates all the ids. The ArrayList is now
	 * ready for saving to history and file.
	 */
	private void sortAndUpdateIds() {
		sortTasks();
		updateAllIds();
	}

	/**
	 * Sorts all the Task objects according to end time and incomplete tasks
	 * before completed tasks.
	 * 
	 * @throws Exception
	 */
	private ArrayList<Task> sortTasks() {
		Collections.sort(tasks, new SortTasksByEndTimeComparator());
		Collections.sort(tasks, new SortTasksByCompletedComparator());
		return tasks;
	}

	/**
	 * Updates the id of each of the Task objects. To be done after every
	 * operation.
	 */
	private void updateAllIds() {
		for (int i = 0; i < tasks.size(); i++) {
			tasks.get(i).setId(i + 1);
		}
	}

	/**
	 * Checks for the validity of the index used in operations. If invalid,
	 * throw exception.
	 * 
	 * @param index
	 * @throws IndexOutOfBoundsException
	 */
	private void checkValidityIndex(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= tasks.size()) {
			throw new IndexOutOfBoundsException(
					Constants.MESSAGE_INVALID_TASK_INDEX);
		}
	}

	/**
	 * Helper method Checks the validity of 2 DateTimes. To be valid, the first
	 * DateTime must occur chronologically before the second DateTime. If
	 * invalid, throw exception.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	private void checkValidityTimes(DateTime startTime, DateTime endTime) {
		checkStartAndEndTime(startTime, endTime);
	}

	/**
	 * Helper method
	 * 
	 * @param startTime
	 * @param endTime
	 */
	private void checkStartAndEndTime(DateTime startTime, DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();

		int check = dtComp.compare(endTime, startTime);

		// check == 0 if the startTime and endTime are the same (Invalid
		// TimedTask)
		// check == -1 if endTime occurs before startTime (Invalid TimedTask)
		// check == 1 if endTime occurs after startTime (Valid TimedTask)
		boolean sameStartAndEnd = check == 0;
		if (sameStartAndEnd) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_SAME_START_END_TIME);
		} else {
			boolean invalidStartAndEnd = check == -1;
			if (invalidStartAndEnd) {
				throw new IllegalArgumentException(
						Constants.MESSAGE_END_BEFORE_START_TIME);
			}
		}
	}

	/**
	 * Helps the user get started with using sudo
	 */
	public void help(String topic) {
		if (topic == null){
			System.out.println(Constants.MESSAGE_WELCOME_HELP_PAGE);
		} else if(topic.toUpperCase().equals("LIST")){
			System.out.println(Constants.HELP_LIST);
		} else {
			String helpMessage = Constants.helpTopics.get(topic.toUpperCase());
			if(helpMessage == null){
				System.out.printf(Constants.HELP_NOT_FOUND, topic);
			} else {
				System.out.println(helpMessage);
			}
		}
	}


	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	public ArrayList<FloatingTask> getFloatingTasks() {
		ArrayList<FloatingTask> toReturn = new ArrayList<FloatingTask>();
		for (Task task : this.tasks) {
			if ((task instanceof FloatingTask)) {
				toReturn.add((FloatingTask) task);
			}
		}
		return toReturn;
	}

	public void clearTasks() {
		this.tasks.clear();
	}

	public boolean isReloaded() {
		return isReloaded;
	}

	public int getTaskNumber() {
		return this.tasks.size();
	}

	public int getCompletedPercentage() {
		int completed = 0;
		for (Task t : tasks) {
			if (t.isComplete) {
				completed++;
			}
		}
		if (this.tasks.size() == 0) {
			return 0;
		}
		int toReturn = 100 * completed / this.tasks.size();
		if (toReturn == 100) {
			// System.out.println("You have finished all tasks!");
		}
		;
		return toReturn;
	}

	public static boolean isFloatingTask(Task task) {
		return task.startTime == null && task.endTime == null;
	}

	public static boolean isDeadlineTask(Task task) {
		return task.getEndTime() != null;
	}

	public static boolean isTimedTask(Task task) {
		return task.getStartTime() != null && isDeadlineTask(task);
	}
	
}
