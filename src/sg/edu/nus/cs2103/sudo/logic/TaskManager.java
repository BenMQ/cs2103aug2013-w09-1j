package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

/**
 * 
 * @author chenminqi
 * @author Ipsita
 * 
 */
public class TaskManager {

	private static final String NOTHING_TO_DELETE = "Nothing to delete!";

	// A list of timed, deadline and floating tasks
	private ArrayList<Task> tasks;

	public TaskManager() {
		tasks = new ArrayList<Task>();
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
	 * each add. TODO: Sort!
	 * 
	 * @param newTask
	 * @return floatingTasks with new additions
	 */
	public ArrayList<Task> addTask(Task newTask) {
		newTask.setId(tasks.size() + 1);
		tasks.add(newTask);
		// Note to self: id of newTask has not been set!
		return tasks;
	}

	/**
	 * Replaces the floating task indicated by the displayId with the newTask
	 * 
	 * @param displayId
	 * @param newTask
	 * @return floatingTasks after editing
	 */
	public ArrayList<Task> editTask(int displayId, Task newTask) {
		int index = displayId - 1;
		if (index < 0 || index > tasks.size()) {
			// throw exception here!
			// throw new IndexOutOfBoundsException("Invalid id.");
		}
		newTask.setId(displayId);
		tasks.set(index, newTask);

		// the new task should be inserted such that the memory list is
		// maintained as a sorted list
		return tasks;
	}

	/**
	 * Prints tasks to stdout. Incomplete tasks are always printed by default.
	 * If showAll is set to true, completed tasks are printed as well.
	 * 
	 * @param showAll
	 *            set to true to include completed tasks
	 */
	public void displayAllTasks(boolean showAll) {
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			if (showAll || !task.isComplete) {
				System.out.println(task.toString());
			}
		}
	}

	/**
	 * Prints all incomplete tasks only
	 */
	public void displayAllTasks() {
		displayAllTasks(false);
	}
	
	/**
	 * Search for Task objects matching the input search string.
	 * Searches all Task objects.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public void searchAllAndDisplay(String searchStr) {
		ArrayList<Task> searchResults = search(searchStr, true);
		displaySearchResults(searchResults);
	}
	
	
	/**
	 * Search for Task objects matching the input search string. By default,
	 * only incomplete tasks will be searched.
	 * 
	 * Prints out the list of searched Task objects.
	 */
	public void searchAndDisplay(String searchStr) {
		ArrayList<Task> searchResults = search(searchStr, false);
		displaySearchResults(searchResults);
	}

	/**
	 * Searches the floatingTasks for matches with the searchStr By default,
	 * only incomplete tasks will be searched
	 * 
	 * @param searchStr
	 * @return ArrayList of Task objects
	 */
	public ArrayList<Task> search(String searchStr, boolean searchAll) {
		searchStr = searchStr.trim();
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
	 * Removes the task by first searching for the search string in the task
	 * description. If there is exactly one match, just delete it. If there are
	 * multiple matches, display all searchResults to user. By default,
	 * searchResults search through all the tasks. Wait for user input to delete
	 * again.
	 */
	public int delete(String searchStr) {
		ArrayList<Task> searchResults = search(searchStr, false);
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
		tasks.remove(id - 1);
		// update the id of all subsequent tasks
		for (int i = id - 1; i < tasks.size(); i++) {
			tasks.get(i).setId(i + 1);
		}
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
