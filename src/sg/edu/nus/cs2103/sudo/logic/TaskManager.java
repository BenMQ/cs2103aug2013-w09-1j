package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

/**
 * 
 * @author chenminqi
 *
 */
public class TaskManager {
    
    private static final String NOTHING_TO_DELETE = "Nothing to delete!";
	// A list of non-floating tasks
    private ArrayList<Task> normalTasks;
    
    public TaskManager() {
        normalTasks = new ArrayList<Task>();
    }
    
    /**
     * Load an ArrayList of tasks into memory.
     * This method should be called upon launch after the storage unit
     * has read the stored item from disk. This action overrides any
     * exiting tasks stored in memory
     * 
     * @param tasks ArrayList of tasks that is provided by the storage unit
     */
    public void preloadTasks(ArrayList<Task> tasks) {
        normalTasks = tasks;
    }
    
    /**
     * Adds a new constructed task.
     * @param newTask
     */
    public void addTask(Task newTask) {
        // the new task should be inserted such that the memory list is 
        // maintained as a sorted list
        normalTasks.add(newTask);
    }
    
    /**
     * Prints tasks to stdout. Not completed tasks are always printed.
     * If showAll is set to true, completed tasks are printed as well.
     * @param showAll set to true to include completed task
     */
    public void displayAllTasks(boolean showAll) {
        int serial = 0;
        for (int i = 0; i < normalTasks.size(); i ++) {
            Task task = normalTasks.get(i);
            if (showAll || !task.isComplete) {
                serial++;
                System.out.println(serial + " " + task.toString());
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
     * Searches for Task objects matching the input search string.
     * Prints out the list of searched Task objects.
     */
    public void searchAndDisplay(String searchStr) {
    	ArrayList<Task> searchResults = search(searchStr);
    	displaySearchResults(searchResults);
    }  
   
	/**
     * Removes the task by first searching for the search string
     * in the task description. If there is exactly one match,
     * just delete it. If there are multiple matches, display 
     * all searchResults to user. Wait for user input to delete again. 
     */
    public void delete(String searchStr) {
    	ArrayList<Task> searchResults = search(searchStr);
    	int numResults = searchResults.size();
    	if (numResults == 0) {
    		System.out.println(NOTHING_TO_DELETE);
    	} else if (numResults == 1) {
    		delete(searchResults.get(0).getId());
    	} else {
    		displaySearchResults(searchResults);
    	}
    }
    
    
    
    
    
    /**
     * Searches for Task objects matching the input search string.
     * Returns searchResults in the form of an ArrayList of Task objects. 
     */
    private ArrayList<Task> search(String searchStr) {
		searchStr = searchStr.trim();
		ArrayList<Task> searchResults = new ArrayList<Task>();
		
		for (int i=0; i<normalTasks.size(); i++) {
			Task currTask = normalTasks.get(i);
			String currTaskStr = currTask.toString();
			if (currTaskStr.contains(searchStr)) {
				searchResults.add(currTask);
			}
		}
		return searchResults;
	}
    
    /**
     * Prints out the list of search results containing Task objects.
     */
    private void displaySearchResults(ArrayList<Task> searchResults) {
		for (int i=0; i<searchResults.size(); i++) {
			System.out.println((i+1) + ". " + searchResults.get(i).toString());
		}
	}
    
    /**
     * Given the id of the task, the task is deleted from normalTasks 
     */
    private void delete(int id) {
    	normalTasks.remove(id-1);
    }
}
