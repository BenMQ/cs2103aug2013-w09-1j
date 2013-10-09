package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

/**
 * 
 * @author chenminqi
 * @author Ipsita
 * 
 */
public class TaskManager {

	private static final String NOTHING_TO_DELETE = "Nothing to delete!";
	// A list of non-floating tasks
	private ArrayList<Task> normalTasks;
	private ArrayList<Task> floatingTasks;

	public TaskManager() {
		normalTasks = new ArrayList<Task>();
		floatingTasks = new ArrayList<Task>();
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
		normalTasks = tasks;
		// TODO: split tasks into normal and floating, call
		// preloadFloatingTasks and preloadNormalTasks accordingly
	}

	/**
	 * Loads an ArrayList of floatingTasks into memory.
	 * 
	 * @see preloadTasks;
	 */
	public void preloadFloatingTasks(ArrayList<Task> floatingTasks) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Loads an ArrayList of normal (timed or deadline) tasks into memory.
	 * 
	 * @see preloadTasks;
	 */

	public void preloadNormalTasks(ArrayList<Task> normalTasks) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Adds a new constructed floating task.
	 * 
	 * @param newTask
	 * @return floatingTasks with new additions
	 */
	public ArrayList<Task> addFloatingTask(Task newTask) {
		// the new task should be inserted such that the memory list is
		// maintained as a sorted list

		newTask.setId(floatingTasks.size() + 1);
		floatingTasks.add(newTask);
		return floatingTasks;
	}

	/**
	 * Adds a new constructed normal task which includes timed and/or deadline
	 * tasks
	 * TODO: Sorting after adding 
	 * 
	 * @param newTask
	 * @return
	 */
	public ArrayList<Task> addNormalTask(Task newTask) {
		// the new task should be inserted such that the memory list is
		// maintained as a sorted list
		newTask.setId(normalTasks.size() + 1);
		normalTasks.add(newTask);
		return normalTasks;
	}

	
	/**
	 * Replaces the floating task indicated by the displayId with the newTask
	 * 
	 * @param displayId
	 * @param newTask
	 * @return floatingTasks after editing
	 */
	public ArrayList<Task> editFloatingTask(int displayId, Task newTask) {
		int index = displayId - 1;
		if (index < 0 || index > floatingTasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		newTask.setId(displayId);
		floatingTasks.set(index, newTask);

		// the new task should be inserted such that the memory list is
		// maintained as a sorted list
		return floatingTasks;
	}

	/**
	 * Replaces the task with the index with the newTask
	 * and sorts the normalTasks again
	 * 
	 * @param displayId
	 * @param newTask
	 * @return
	 */
	public ArrayList<Task> editNormalTask(int index, Task newTask) {
		newTask.setId(index);
		normalTasks.set(index, newTask);

		// TODO: the new task should be inserted such that the memory list is
		// maintained as a sorted list
		
		return normalTasks;
	}

	/**
	 * InputParser calls this. 
	 * Edits the description of Timed or Deadline Task
	 * 
	 * @param displayId
	 * @param description
	 * @return
	 */
	public ArrayList<Task> editNormalTask(int displayId, String description) {
		int index = displayId - 1;
		if (index < 0 || index > normalTasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		Task newTask = normalTasks.get(index);
		newTask.setDescription(description);
		
		editNormalTask(index, newTask);
		
		return normalTasks;
	}

	/**
	 * InputParser calls this. 
	 * Edits the endTime for Deadline Task
	 * 
	 * @param displayId
	 * @param startTime
	 * @return
	 */
	public ArrayList<Task> editNormalTask(int displayId, DateTime endTime) {
		int index = displayId - 1;
		if (index < 0 || index > normalTasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		Task newTask = normalTasks.get(index);
		newTask.setEndTime(endTime);
		
		editNormalTask(index, newTask);

		return normalTasks;
	}

	/**
	 * InputParser calls this. 
	 * Edits the startTime and endTime of Timed Task
	 * 
	 * @param displayId
	 * @param description
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public ArrayList<Task> editNormalTask(int displayId, DateTime startTime,
			DateTime endTime) {
		int index = displayId - 1;
		if (index < 0 || index > normalTasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		
		Task newTask = normalTasks.get(index);
		newTask.setEndTime(endTime);
		
		editNormalTask(index, newTask);

		return normalTasks;
	}

	/**
	 * InputParser calls this. Edits the Timed Task
	 * 
	 * @param displayId
	 * @param description
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public ArrayList<Task> editNormalTask(int displayId, String description,
			DateTime startTime, DateTime endTime) {
		int index = displayId - 1;
		if (index < 0 || index > normalTasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		
		Task newTask = normalTasks.get(index);
		newTask.setDescription(description);
		newTask.setStartTime(startTime);
		newTask.setEndTime(endTime);
		
		editNormalTask(index, newTask);
		
		return normalTasks;
	}

	/**
	 * Search a text in all tasks. By default completed tasks will not be
	 * searched.
	 * 
	 * @param text
	 *            the text string to be searched for, case insensitive
	 * @param searchAll
	 *            set to true if completed tasks should be searched for.
	 * @return ArrayList of IDs of the tasks that meets the search criteria
	 */
	public ArrayList<Integer> searchTaskID(String text, boolean searchAll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Search a text in all tasks. By default completed tasks will not be
	 * searched.
	 * 
	 * @param text
	 *            the text string to be searched for, case insensitive
	 * @param searchAll
	 *            set to true if completed tasks should be searched for.
	 * @return ArrayList of the tasks that meets the search criteria.
	 */
	public ArrayList<Task> searchTask(String text, boolean searchAll) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Search a task by the internal ID
	 * 
	 * @param ID
	 *            ID of the internal task
	 * @return returns the task if found, null if it doesn't exist
	 */
	public Task getTask(int ID) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Prints tasks to stdout. Not completed tasks are always printed. If
	 * showAll is set to true, completed tasks are printed as well.
	 * 
	 * @param showAll
	 *            set to true to include completed task
	 */
	public void displayAllTasks(boolean showAll) {
		for (int i = 0; i < floatingTasks.size(); i++) {
			Task task = floatingTasks.get(i);
			if (showAll || !task.isComplete) {
				System.out.println(task.toString());
			}
		}
	}

	/**
	 * Prints all tasks that are not yet completed
	 */
	public void displayAllTasks() {
		displayAllTasks(false);
	}

	/**
	 * Search for Task objects matching the input search string. By default only
	 * incomplete tasks will be searched.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public void searchAndDisplay(String searchStr) {
		ArrayList<Task> searchResults = search(searchStr);
		displaySearchResults(searchResults);
	}

	/**
	 * Removes the task by first searching for the search string in the task
	 * description. If there is exactly one match, just delete it. If there are
	 * multiple matches, display all searchResults to user. Wait for user input
	 * to delete again.
	 */
	public int delete(String searchStr) {
		ArrayList<Task> searchResults = search(searchStr);
		int numResults = searchResults.size();
		if (numResults == 0) {
			System.out.println(NOTHING_TO_DELETE);
		} else if (numResults == 1) {
			delete(searchResults.get(0).getId());
		} else {
			displaySearchResults(searchResults);
		}
		return numResults;
	}

	/**
	 * Given the id of the task, the task is deleted from floatingTasks
	 */
	public void delete(int id) {
		floatingTasks.remove(id - 1);
		// update the id of all subsequent tasks
		for (int i = id - 1; i < floatingTasks.size(); i++) {
			floatingTasks.get(i).setId(i + 1);
		}
	}

	/**
	 * Searches the floatingTasks for matches with the searchStr By default,
	 * only incomplete tasks will be searched
	 * 
	 * @param searchStr
	 * @return ArrayList of Task objects
	 */
	public ArrayList<Task> search(String searchStr) {
		searchStr = searchStr.trim();
		ArrayList<Task> searchResults = new ArrayList<Task>();

		for (int i = 0; i < floatingTasks.size(); i++) {
			Task currTask = floatingTasks.get(i);
			String currTaskStr = currTask.toString();
			if (currTaskStr.toLowerCase().contains(searchStr.toLowerCase())
					&& !currTask.getComplete()) {
				searchResults.add(currTask);
			}
		}
		return searchResults;
	}

	/**
	 * Prints out the list of search results containing Task objects.
	 */
	public void displaySearchResults(ArrayList<Task> searchResults) {
		if (searchResults.isEmpty()) {
			System.out.println("No search results!");
			return;
		}

		System.out.println();
		System.out.println("Search Results");
		for (int i = 0; i < searchResults.size(); i++) {
			System.out.println(searchResults.get(i).toString());
		}
	}

}
