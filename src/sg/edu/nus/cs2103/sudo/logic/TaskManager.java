package sg.edu.nus.cs2103.sudo.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.MutableInterval;

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
		if (taskDescription != null) {
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
		for (int i = 0; i < tasks.size(); i++) {

			Task task = tasks.get(i);
			String completed = "";
			if (task.isComplete()) {
				completed = "Done!";
			}
			if (showAll || !task.isComplete) {
				System.out.println(task.toString() + " " + completed);
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
	 * Displays the floating tasks only. To be shown in the side bar in the GUI
	 */
	public void displayFloatingTasks() throws IllegalStateException {
		checkEmptyList();

		int count = 0;
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			if (task instanceof FloatingTask) {
				count++;
				System.out.println(task.toString() + " " + task.isComplete());
			}
		}

		if (count == 0) {
			System.out.println(Constants.MESSAGE_NO_FLOATING_TASKS);
		}
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
		System.out.print(Constants.MESSAGE_FINISH);
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
		System.out.print(Constants.MESSAGE_UNFINISH);
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

		if (searchStr == null) {
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
	 * @author chenminqi
	 */
	public void searchForFreeIntervals() {
		ArrayList<MutableInterval> free = getFreeIntervals();
		boolean noSlotsFound = true;

		for (int i = 0; i < free.size(); i++) {
			MutableInterval interval = free.get(i);
			if (interval.toDurationMillis() >= Constants.FREE_SLOT_MINIMUM_DURATION) {
				if (noSlotsFound) {
					System.out.println(Constants.MESSAGE_FREE_SLOTS_PREFIX);
					noSlotsFound = false;
				}
				String output = interval.getStart().toString("hh:mm a")
						+ " to " + interval.getEnd().toString("hh:mm a");
				System.out.println(output);
			}
		}
		if (noSlotsFound) {
			System.out.println(Constants.MESSAGE_NO_FREE_SLOTS);
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
	 */
	public ArrayList<MutableInterval> getOccupiedIntervals() {
		sortTasks();

		DateTime now = new DateTime();
		DateTime startOfToday = new DateTime(now.getYear(),
				now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);
		DateTime endOfToday = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);

		ArrayList<MutableInterval> occupied = new ArrayList<MutableInterval>();
		MutableInterval last = new MutableInterval(endOfToday, endOfToday);
		occupied.add(last);

		for (int i = tasks.size() - 1; i >= 0; i--) {
			Task task = tasks.get(i);

			if (task.isComplete() || !(task instanceof TimedTask)) {
				// we are only concerned with incomplete TimedTask
				continue;
			} else if (!task.endTime.isAfter(startOfToday)) {
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
				if (!task.startTime.isAfter(startOfToday)) {
					// reached the start of the day, no more processing needed.
					last.setStart(startOfToday);
					break;
				}
			}

		}
		Collections.reverse(occupied);
		return occupied;
	}

	/**
	 * Searches for all free time slots of today
	 * 
	 * @return intervals that are free today
	 * @author chenminqi
	 */
	public ArrayList<MutableInterval> getFreeIntervals() {
		DateTime now = new DateTime();
		DateTime startOfToday = new DateTime(now.getYear(),
				now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);
		ArrayList<MutableInterval> free = new ArrayList<MutableInterval>();

		ArrayList<MutableInterval> occupied = getOccupiedIntervals();

		if (occupied.get(0).getStart().isAfter(startOfToday)) {
			free.add(new MutableInterval(startOfToday, occupied.get(0)
					.getStart()));
		}

		for (int i = 0; i < occupied.size() - 1; i++) {
			free.add(new MutableInterval(occupied.get(i).getEnd(), occupied
					.get(i + 1).getStart()));
		}

		return free;
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

		if (searchStr == null) {
			throw new NullPointerException(Constants.MESSAGE_INVALID_DELETE);
		}

		ArrayList<Task> searchResults = search(searchStr, true);
		int numResults = searchResults.size();

		if (numResults == 0) {
			throw new IllegalStateException(Constants.MESSAGE_NO_SEARCH_RESULTS);
		} else if (numResults == 1) {
			delete(searchResults.get(0).getId());
			System.out.printf(Constants.MESSAGE_DELETE,
					searchResults.get(0).description);
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
			System.out.println("Undo...");
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			System.out
					.println("History file missing, New history file was built.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoHistoryException e) {
			System.out.println("No more undo steps recorded.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
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
			System.out.println("Redo...");
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			System.out
					.println("History file missing, New history file was built.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoHistoryException e) {
			System.out.println("No more redo steps recorded.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		updateAllIds();
		// return tasks;
	}

	/**
	 * To check if the task list is empty. If yes, throw exception.
	 */
	private void checkEmptyList() {
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
	 * Help method shows help message when called
	 * 
	 */
	public void getHelp() {

	}

	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	public ArrayList<FloatingTask> getFloatingTask() {
		ArrayList<FloatingTask> toReturn = new ArrayList<FloatingTask>();
		for (Task tsk : this.tasks) {
			if ((tsk instanceof FloatingTask)) {
				toReturn.add((FloatingTask) tsk);
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
}
