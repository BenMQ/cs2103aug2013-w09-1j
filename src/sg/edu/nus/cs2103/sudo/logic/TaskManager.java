package sg.edu.nus.cs2103.sudo.logic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

	private TaskManager() throws Exception {
		tasks = new ArrayList<Task>();
		storage = StorageHandler.getStorageHandler(Constants.FILE_NAME);
		storage.prepareFile(tasks);
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
	 * each add. TODO: Unit testing of deadline and timed tasks
	 * 
	 * @param newTask
	 * @return floatingTasks with new additions
	 * @throws Exception
	 */
	public ArrayList<Task> addTask(Task newTask) throws Exception {
		assert (newTask != null);

		newTask.setId(tasks.size() + 1);
		tasks.add(newTask);

		sortTasks();
		updateAllIds();
		storage.save(true);
		return tasks;
	}

	/**
	 * Replaces the task indicated by the displayId with the newTask TODO: Unit
	 * testing of deadline and timed tasks
	 * 
	 * @param displayId
	 * @param newTask
	 * @return floatingTasks after editing
	 * @throws Exception
	 */
	public ArrayList<Task> editTask(int displayId, Task newTask)
			throws Exception {

		assert (newTask != null);

		if (tasks.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}

		int index = displayId - 1;
		checkValidityIndex(index);

		newTask.setId(displayId);
		tasks.set(index, newTask);

		sortTasks();
		updateAllIds();
		storage.save(true);
		return tasks;
	}

	/**
	 * Prints tasks to stdout. Incomplete tasks are always printed by default.
	 * If showAll is set to true, completed tasks are printed as well.
	 * 
	 * @param showAll
	 *            set to true to include completed tasks
	 */
	public void displayAllTasks(boolean showAll) throws IllegalStateException {

		if (tasks.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);

			if (showAll || !task.isComplete) {
				System.out.println(task.toString() + " " + task.isComplete());
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
		int count = 0;

		if (tasks.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}

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
		sortTasks();
		updateAllIds();
		storage.save(true);
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
		sortTasks();
		updateAllIds();
		storage.save(true);
		return tasks;
	}

	/**
	 * Search for Task objects matching the input search string. By default,
	 * only incomplete tasks will be searched.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public void searchAndDisplay(String searchStr) throws NullPointerException,
			IllegalStateException {

		ArrayList<Task> searchResults = search(searchStr, false);
		displaySearchResults(searchResults);
	}

	/**
	 * Search for Task objects matching the input search string. Searches all
	 * Task objects.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public void searchAllAndDisplay(String searchStr)
			throws NullPointerException, IllegalStateException {

		ArrayList<Task> searchResults = search(searchStr, true);
		displaySearchResults(searchResults);
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
			System.out.println(Constants.MESSAGE_DELETE
					+ searchResults.get(0).description);
		} else {
			displaySearchResults(searchResults);
		}

		return numResults;
	}

	/**
	 * Given the id of the task, the task is deleted from floatingTasks
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	public void delete(int taskId) throws IOException {
		int index = taskId - 1;
		checkValidityIndex(index);

		tasks.remove(index);
		storage.save(true);
		updateAllIds();
	}

	/**
	 * @author Liu Dake
	 * 
	 * If history does not exist, throw Exception
	 * 
	 * @return
	 */
	public void undo() {
		try {
			tasks = (ArrayList<Task>) storage.undo().clone();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			UI.forcePrint("History file missing, New history file was built.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoHistoryException e) {
			UI.forcePrint("No more undo steps recorded.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		updateAllIds();
		// return tasks;
	}

	public ArrayList<Task> saveTasks() throws IOException {
		storage.save(false);
		return tasks;
	}

	/**
	 * @author Liu Dake
	 * 
	 * If no redo provision exists in history, throw Exception
	 * 
	 * @return
	 */
	public void redo() {
		try {
			tasks = (ArrayList<Task>) storage.redo().clone();
		} catch (FileNotFoundException e) {
			storage.rebuildHistory();
			UI.forcePrint("History file missing, New history file was built.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (NoHistoryException e) {
			UI.forcePrint("No more redo steps recorded.");
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		updateAllIds();
		// return tasks;
	}

	/**
	 * Sorts all the Task objects according to end time. TODO: Unit Testing
	 * 
	 * @throws Exception
	 */
	private ArrayList<Task> sortTasks() {
		Collections.sort(tasks, new SortTasksByCompletedComparator());
		Collections.sort(tasks, new SortTasksByEndTimeComparator());
		updateAllIds();
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

	private void checkValidityIndex(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index > tasks.size()) {
			throw new IndexOutOfBoundsException(
					Constants.MESSAGE_INVALID_TASK_INDEX);
		}
	}

	public ArrayList<Task> getTasks() {
		return this.tasks;
	}

	public void clearTasks() {
		this.tasks.clear();
	}
}
