package sg.edu.nus.cs2103.sudo.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.MutableInterval;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.HelpConstants;
import sg.edu.nus.cs2103.sudo.exceptions.NoHistoryException;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;
import sg.edu.nus.cs2103.ui.DisplayUtils;
import sg.edu.nus.cs2103.ui.GUI;

/**
 * @author chenminqi
 * @author Ipsita Mohapatra A0101286N
 * @author Yos Riady A0099317
 * 
 *         This is a singleton class responsible for handling the Task objects.
 *         The appropriate methods are called upon by the InputParser to execute
 *         user commands such as adding, editing, searching and deleting Task
 *         objects. It is also responsible for throwing exceptions when
 *         necessary.
 */

public class TaskManager {

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
		TaskManagerUtils.updateAllIds(tasks);
	}

	public static TaskManager getTaskManager() {
		if (taskManager == null) {
			try {
				taskManager = new TaskManager();
			} catch (Exception e) {

			}
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
		TaskManagerUtils.updateAllIds(tasks);
		try {
			taskManager = new TaskManager();
			GUI.print_add("Files rebuilt.",2);

		} catch (Exception e) {
			GUI.print_add("Files rebuiling failed.",3);

			e.printStackTrace();
		}
	}

	/**
	 * Adds a new task into the list. Maintains a sorted list of items after
	 * each add.
	 * 
	 * @param newTask
	 *            Task to be added into task list
	 * @return modified task list
	 * @throws Exception
	 */
	public ArrayList<Task> addTask(Task newTask) throws Exception {
		assert (newTask != null);

		newTask.setId(tasks.size() + 1);
		tasks.add(newTask);

		TaskManagerUtils.sortAndUpdateIds(tasks);
		//TaskManagerUtils.saveToHistory(storage);
		storage.save(true);
		return tasks;
	}
	
	/**
	 * Adds a task based on the number of date
	 * arguments.
	 * 
	 * @param taskDescription
	 *            The task description
	 * @param dateTimes
	 *            A list of DateTimes
	 * @throws Exception
	 */
	public void add(String taskDescription,
			ArrayList<DateTime> dateTimes) throws Exception {

		if (taskDescription == null) {
			GUI.print_add(Constants.MESSAGE_MISSING_DESCRIPTION,2);

			return;
		}

		assert taskDescription != null;
		int numOfDates = dateTimes.size();
		if (numOfDates == 0) {
			Task task = new FloatingTask(taskDescription);
			this.addTask(task);
			GUI.print_add(task.getAddMessage(),2);

		} else if (numOfDates == 1) {
			Task task = new DeadlineTask(taskDescription, dateTimes);
			this.addTask(task);
			GUI.print_add(task.getAddMessage(),2);
			
		} else if (numOfDates == 2) {
			Task task = new TimedTask(taskDescription, dateTimes);
			this.addTask(task);
			GUI.print_add(task.getAddMessage(), 2);
			
		} else {
			GUI.print_add(Constants.MESSAGE_INVALID_NUMBER_OF_DATES, 2);
			
		}
	}

	/**
	 * Replaces the task indicated by the displayId with the newTask Changes
	 * from one type of task to another if necessary.
	 * 
	 * @param taskId
	 *            id of task to be edited
	 * @param taskDescription
	 *            new task description (if any)
	 * @param dates
	 *            ArrayList of DateTimes containing new DateTimes (if any)
	 * @return modified task list
	 * @throws IllegalStateException
	 * @throws IndexOutOfBoundsException
	 * @throws Exception
	 */
	public ArrayList<Task> editTask(int taskId, String taskDescription,
			ArrayList<DateTime> dates) throws IllegalStateException,
			IndexOutOfBoundsException, Exception {
		assert (dates.size() <= 2);

		TaskManagerUtils.checkEmptyList(tasks);

		int index = taskId - 1;
		TaskManagerUtils.checkValidityIndex(index, tasks);

		GUI.print_add(String.format(Constants.MESSAGE_EDIT, taskId),2);
	
		TaskManagerUtils.editTaskHelper(taskDescription, dates, index, tasks);

		TaskManagerUtils.sortAndUpdateIds(tasks);
		storage.save(true);
		//TaskManagerUtils.saveToHistory(storage);
		return tasks;
	}

	/**
	 * Prints out finished tasks
	 */
	public void displayFinishedTasks() {
		ArrayList<Task> tasks = TaskManagerUtils.getFinishedTasks(this.tasks);
		TaskManagerUtils.showDisplayMessage();

		for (Task task : tasks) {
			String completed = "";
			if (task.isComplete()) {
				completed = Constants.TASK_COMPLETED_FLAG;
			}
			GUI.print_add("\n",1);
			DisplayUtils.prettyPrint(task);
			GUI.print_add(" "+ completed, 1);
			
		}
	}

	/**
	 * Prints tasks to stdout. Incomplete tasks are always printed by default.
	 * If showAll is set to true, completed tasks are printed as well.
	 * 
	 * @param showAll
	 *            set to true to include completed tasks
	 */
	public void displayAllTasks(boolean showAll) throws IllegalStateException {
		TaskManagerUtils.checkEmptyList(tasks);
		DateTime previousDate = null;
		boolean floatingStarted = false;
		boolean finishedStarted = false;

		TaskManagerUtils.showDisplayMessage(showAll);

		for (int i = 0; i < tasks.size(); i++) {

			Task task = tasks.get(i);

			String completed = "";
			if (task.isComplete()) {
				completed = Constants.TASK_COMPLETED_FLAG;
			}
			if (showAll || !task.isComplete) {

				if (!task.isComplete() && !task.isFloatingTask()) {
					previousDate = DisplayUtils.
							insertDateSeparators(previousDate, task);
				} else {
					floatingStarted = DisplayUtils.
							insertFloatingSeparator(floatingStarted, task);
					finishedStarted = DisplayUtils.
							insertFinishedSeparator(finishedStarted, task);
				}

				GUI.print_add("\n",0);
				DisplayUtils.prettyPrint(task);
				GUI.print_add(" " + completed, 1);
				
			}

		}
	}

	/**
	 * Prints all incomplete tasks only.
	 * 
	 * @throws IllegalStateException
	 */
	public void displayAllTasks() throws IllegalStateException {
		displayAllTasks(false);
	}

	/**
	 * Displays floating tasks only. To be shown in the side bar in the GUI
	 * Formatted to be 17 characters per line.
	 * 
	 * @return String of floating tasks.
	 */
	public String displayFloatingTasks() {
		TaskManagerUtils.checkEmptyList(tasks);
		ArrayList<FloatingTask> floatingTasks = TaskManagerUtils
				.getFloatingTasks(tasks);

		if (floatingTasks.size() == 0) {
			return (Constants.MESSAGE_NO_FLOATING_TASKS);
		}

		String toReturn = TaskManagerUtils.formatFloatingTasks(floatingTasks);
		return toReturn;
	}

	/**
	 * Mark an incomplete task as completed.
	 * 
	 * @param taskId
	 *            id of task to be marked as completed
	 * @return modified task list
	 * @throws IOException 
	 * @throws Exception
	 */
	public ArrayList<Task> markAsComplete(int taskId)
			throws UnsupportedOperationException, IOException {

		int index = taskId - 1;
		TaskManagerUtils.checkValidityIndex(index, tasks);

		Task currTask = tasks.get(index);
		if (currTask.isComplete()) {
			throw new UnsupportedOperationException(
					Constants.MESSAGE_ALREADY_COMPLETE);
		}

		currTask.setComplete(true);
		GUI.print_add(String.format(Constants.MESSAGE_FINISH, currTask.description),2);
		
		TaskManagerUtils.sortAndUpdateIds(tasks);
		storage.save(true);
		//TaskManagerUtils.saveToHistory(storage);

		return tasks;
	}

	/**
	 * Mark a completed task as incomplete.
	 * 
	 * @param taskId
	 *            id of task to be marked as incomplete
	 * @return modified task list
	 * @throws Exception
	 */
	public ArrayList<Task> markAsIncomplete(int taskId) throws Exception {

		int index = taskId - 1;
		TaskManagerUtils.checkValidityIndex(index, tasks);

		Task currTask = tasks.get(index);
		if (!currTask.isComplete()) {
			throw new UnsupportedOperationException(
					Constants.MESSAGE_ALREADY_INCOMPLETE);
		}

		currTask.setComplete(false);
		GUI.print_add(String.format(Constants.MESSAGE_UNFINISH, currTask.description),2);
	
		TaskManagerUtils.sortAndUpdateIds(tasks);
		storage.save(true);
		//TaskManagerUtils.saveToHistory(storage);

		return tasks;
	}

	/**
	 * Search for Task objects matching the input search string. By default,
	 * only incomplete tasks will be searched. Prints out the list of searched
	 * Task objects.
	 * 
	 * @param searchStr
	 *            string to be searched for
	 * @return ArrayList<Task> list of search results
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 */
	public ArrayList<Task> searchAndDisplay(String searchStr)
			throws NullPointerException, IllegalStateException {

		GUI.print_add(String.format(Constants.MESSAGE_SEARCH, searchStr),2);
		
		ArrayList<Task> searchResults = search(searchStr, false);
		TaskManagerUtils.displaySearchResults(searchResults);

		return searchResults;
	}

	/**
	 * Search for Task objects matching the input search string. Searches all
	 * Task objects. Prints out the list of searched Task objects.
	 * 
	 * @param searchStr
	 *            string to be searched for
	 * @return ArrayList<Task> list of search results
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 */
	public ArrayList<Task> searchAllAndDisplay(String searchStr)
			throws NullPointerException, IllegalStateException {

		ArrayList<Task> searchResults = search(searchStr, true);
		TaskManagerUtils.displaySearchResults(searchResults);

		return searchResults;
	}

	/**
	 * Searches for matches with the searchStr. By default, only incomplete
	 * tasks will be searched.
	 * 
	 * @param searchStr
	 *            string to be search for
	 * @param searchAll
	 *            set to true if search for incomplete and completed tasks
	 * @returns ArrayList<Task> list of search results
	 * @throws NullPointerException
	 * @throws IllegalStateException
	 */
	public ArrayList<Task> search(String searchStr, boolean searchAll)
			throws NullPointerException, IllegalStateException {

		boolean isInvalidString = searchStr == null || searchStr == "";
		if (isInvalidString) {
			throw new NullPointerException(Constants.MESSAGE_INVALID_SEARCH);
		}

		TaskManagerUtils.checkEmptyList(tasks);

		ArrayList<Task> searchResults = TaskManagerUtils.searchHelper(tasks,
				searchStr, searchAll);

		return searchResults;
	}

	/**
	 * Search and prints out intervals that are free during the current day.
	 * Intervals shorter than 10 minutes are ignored.
	 * 
	 * @param dateTimes
	 *            0 to 2 DateTimes. If only one is specified, the date range
	 *            will be that day. if none is specified, the date range will be
	 *            the current day.
	 * 
	 * @author chenminqi
	 */
	public void searchForFreeIntervals(ArrayList<DateTime> dateTimes) {
		if (dateTimes.size() > 1) {
			GUI.print_add(Constants.MESSAGE_INVALID_NUMBER_OF_DATES, 3);
			
			return;
		}

		assert (dateTimes.size() >= 0 && dateTimes.size() <= 2);
		ArrayList<DateTime> timeRange = TaskManagerUtils.getFlexibleTimeRange(dateTimes);
		if (timeRange.get(0).isBefore(DateTime.now())) {
            timeRange.set(0, DateTime.now());
        }
		ArrayList<MutableInterval> free = getFreeIntervals(timeRange);
		boolean noSlotsFound = true;

		for (int i = 0; i < free.size(); i++) {
			MutableInterval interval = free.get(i);
			if (interval.toDurationMillis() >= Constants.FREE_SLOT_MINIMUM_DURATION) {
				if (noSlotsFound) {
					GUI.print_add(String.format(Constants.MESSAGE_FREE_SLOTS_PREFIX,
					        timeRange.get(0).toString("dd MMMM")),1);
					
					noSlotsFound = false;
				}
				String output = interval.getStart().toString("hh:mm a")
						+ " to "
						+ interval.getEnd().toString("hh:mm a");
				GUI.print_add(output,1);
				
			}
		}
		if (noSlotsFound) {
			GUI.print_add(Constants.MESSAGE_NO_FREE_SLOTS,3);
		
		}
	}

	/**
	 * Searches for all occupied time slots of today. If the actual slot of the
	 * day ends before 2359hrs, an interval [2359hrs, 2359hrs] which lasts for 0
	 * seconds will be inserted at the end.
	 * 
	 * @return intervals that are occupied today, the last item is guaranteed to
	 *         end at 2359hrs, and hence guaranteed to have at least 1 item
	 *         returned.
	 * @author chenminqi
	 * @param timeRange
	 */
	public ArrayList<MutableInterval> getOccupiedIntervals(
			ArrayList<DateTime> timeRange) {
		TaskManagerUtils.sortTasks(tasks);

		DateTime start = timeRange.get(0);
		DateTime end = timeRange.get(1);

		ArrayList<MutableInterval> occupied = new ArrayList<MutableInterval>();
		MutableInterval last = new MutableInterval(end, end);
		occupied.add(last);

		for (int i = tasks.size() - 1; i >= 0; i--) {
			Task task = tasks.get(i);

			if (task.isComplete() || !(task instanceof TimedTask)) {
				// we are only concerned with incomplete TimedTask
				continue;
			} else if (!task.endTime.isAfter(start)) {
				// all unprocessed items ends before today, no more items needs
				// processing
				break;
			} else if (!task.startTime.isBefore(last.getStart())) {
				// we are only concerned with tasks that starts before the last
				// occupied slot
				continue;
			} else if (!task.endTime.isBefore(last.getStart())) {
				// overlap between task's end time and the last occupied slot's
				// start time
				last.setStart(task.startTime);
			} else {
				// there is a gap
				last = new MutableInterval(task.startTime, task.endTime);
				occupied.add(last);
				if (!task.startTime.isAfter(start)) {
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
	 * 
	 * @param dateTimes
	 *            0 to 2 DateTimes. If only one is specified, the date range
	 *            will be that day. if none is specified, the date range will be
	 *            the current day.
	 * @return intervals that are free today
	 * @author chenminqi
	 */
	public ArrayList<MutableInterval> getFreeIntervals(
			ArrayList<DateTime> dateTimes) {
		DateTime start = dateTimes.get(0);
		ArrayList<MutableInterval> free = new ArrayList<MutableInterval>();

		ArrayList<MutableInterval> occupied = getOccupiedIntervals(dateTimes);

		if (occupied.get(0).getStart().isAfter(start)) {
			free.add(new MutableInterval(start, occupied.get(0).getStart()));
		}

		for (int i = 0; i < occupied.size() - 1; i++) {
			free.add(new MutableInterval(occupied.get(i).getEnd(), occupied
					.get(i + 1).getStart()));
		}

		return free;
	}

	/**
	 * Schedules a task
	 * 
	 * @param description
	 * @param timeRange
	 * @throws Exception
	 */
	public void scheduleTask(int taskId, long duration, ArrayList<DateTime> dateTimes)
			throws Exception {
        int index = taskId - 1;
        try {
            TaskManagerUtils.checkValidityIndex(index, tasks);  
        } catch (IndexOutOfBoundsException e) {
            GUI.print_add(Constants.MESSAGE_INVALID_TASK_INDEX, 3);
            
            return;
        }
        if (dateTimes.size() > 1) {
            GUI.print_add(Constants.MESSAGE_INVALID_NUMBER_OF_DATES, 3);
            
            return;
        } else if (duration <= 0) {
            GUI.print_add(Constants.MESSAGE_INCOMPLETE_COMMAND, 3);
            return;
        }
        
        if (tasks.get(index).isComplete()) {
            GUI.print_add(Constants.MESSAGE_ALREADY_COMPLETE, 3);
            return;
        }
        String description = tasks.get(index).getDescription();
		ArrayList<DateTime> timeRange = TaskManagerUtils.getFlexibleTimeRange(dateTimes);
		if (timeRange.get(0).isBefore(DateTime.now())) {
		    timeRange.set(0, DateTime.now());
		}
		ArrayList<MutableInterval> free = getFreeIntervals(timeRange);
		for (int i = 0; i < free.size(); i++) {
			MutableInterval candidate = free.get(i);
			DateTime start;
			DateTime startDay0800;
			DateTime startDay2300;
			DateTime nextDay0800;

			while (candidate.toDurationMillis() >= 2 * 60 * 60 * 1000) {
				start = candidate.getStart();
				startDay0800 = new DateTime(start.getYear(),
						start.getMonthOfYear(), start.getDayOfMonth(), 8, 0, 0);
				startDay2300 = new DateTime(start.getYear(),
						start.getMonthOfYear(), start.getDayOfMonth(), 23, 0, 0);
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
				DateTime end = start.plusMillis((int) duration);
				if (!end.isAfter(startDay2300)) {
					ArrayList<DateTime> range = new ArrayList<DateTime>(2);
					range.add(start);
					
					range.add(end);
					
					TaskManagerUtils.editTaskHelper(null, range, index, tasks);
					GUI.print_add(String.format(Constants.MESSAGE_ADD_TIMED,
							description, DisplayUtils.formatDate(start),
							DisplayUtils.formatDate(end)),1);
					
					TaskManagerUtils.sortAndUpdateIds(tasks);
					
					storage.save(true);
                    //TaskManagerUtils.saveToHistory(storage);
					
					return;
				} else {
					break;
				}
			}
		}
		GUI.print_add(Constants.MESSAGE_NO_FREE_SLOTS,3);
	}

	/**
	 * Removes the task by first searching for the search string in the task
	 * description. If there is exactly one match, just delete it. If there are
	 * multiple matches, display all searchResults to user. By default,
	 * searchResults searches through all tasks. Wait for user input to delete
	 * again.
	 * 
	 * @param searchStr
	 *            string to searched for
	 * @throws IOException
	 * @throws Exception
	 */
	public int delete(String searchStr) throws IOException {

		boolean isInvalidString = (searchStr == null || searchStr == "");
		if (isInvalidString) {
			throw new NullPointerException(Constants.MESSAGE_INVALID_DELETE);
		}

		ArrayList<Task> searchResults = search(searchStr, true);
		int numResults = searchResults.size();

		if (numResults == 0) {
			throw new IllegalStateException(Constants.MESSAGE_NO_SEARCH_RESULTS);
		} else if (numResults == 1) {
			delete(searchResults.get(0).getId());
		} else {
			TaskManagerUtils.displaySearchResults(searchResults);
		}

		return numResults;
	}

	/**
	 * The task with the specified id is deleted from the task list.
	 * 
	 * @param taskId
	 *            id of task to be deleted
	 * @throws IOException
	 */
	public void delete(int taskId) throws IOException {
		int index = taskId - 1;

		TaskManagerUtils.checkValidityIndex(index, tasks);

		GUI.print_add(String.format(Constants.MESSAGE_DELETE,
				tasks.get(index).description),3);

		tasks.remove(index);
		TaskManagerUtils.sortAndUpdateIds(tasks);
		storage.save(true);
		//TaskManagerUtils.saveToHistory(storage);
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
			GUI.print_add(Constants.MESSAGE_UNDO,2);
			// saveTasks();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			GUI.print_add(Constants.MESSAGE_HISTORY_LOAD_ERROR,3);
		} catch (NoHistoryException e) {
			GUI.print_add(Constants.MESSAGE_LAST_HISTORY,2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TaskManagerUtils.updateAllIds(tasks);
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
			GUI.print_add(Constants.MESSAGE_REDO,2);
			// saveTasks();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			GUI.print_add(Constants.MESSAGE_HISTORY_LOAD_ERROR,3);
		} catch (NoHistoryException e) {
			GUI.print_add(Constants.MESSAGE_LAST_HISTORY,2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TaskManagerUtils.updateAllIds(tasks);
		// return tasks;
	}

	//@author A0099317U
	/**
	 * Helps the user get started with using sudo
	 * 
	 * @param topic
	 */
	public void help(String topic) {
		if (topic == null) {
			GUI.print_add(HelpConstants.MESSAGE_WELCOME_HELP_PAGE,1);
		} else {
			String helpMessage = HelpConstants.helpTopics.get(topic
					.toUpperCase());
			if (helpMessage == null) {
				GUI.print_add(String.format(HelpConstants.HELP_NOT_FOUND, topic),3);
			} else {
				GUI.print_add(helpMessage,1);
			}
		}
	}

	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	public void clearTasks() {
		TaskManagerUtils.clearTasks(tasks);
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
			// GUI.print_add("You have finished all tasks!");
		}
		return toReturn;
	}

}
