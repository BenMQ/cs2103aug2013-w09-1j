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
    private ArrayList<Task> floatingTasks;
    
    public TaskManager() {
        normalTasks = new ArrayList<Task>();
        floatingTasks = new ArrayList<Task>();
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
        // TODO: split tasks into normal and floating, call
        // preloadFloatingTasks and preloadNormalTasks accordingly
    }
    
    /**
     * Loads an ArrayList of floatingTasks into memory.
     * @see preloadTasks; 
     */
    public void preloadFloatingTasks(ArrayList<Task> floatingTasks) {
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * Loads an ArrayList of normal (timed or deadline) tasks into memory.
     * @see preloadTasks;
     */
    
    public void preloadNormalTasks(ArrayList<Task> normalTasks) {
        throw new UnsupportedOperationException();
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
     * Search a text in all tasks. By default completed tasks will not be searched.
     * 
     * @param text the text string to be searched for, case insensitive
     * @param searchAll set to true if completed tasks should be searched for.
     * @return ArrayList of IDs of the tasks that meets the search criteria
     */
    public ArrayList<Integer> searchTaskID(String text, boolean searchAll) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Search a text in all tasks. By default completed tasks will not be searched.
     * @param text the text string to be searched for, case insensitive
     * @param searchAll set to true if completed tasks should be searched for.
     * @return ArrayList of the tasks that meets the search criteria.
     */
    public ArrayList<Task> searchTask(String text, boolean searchAll) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Search a task by the internal ID
     * @param ID ID of the internal task
     * @return returns the task if found, null if it doesn't exist
     */
    public Task getTask(int ID) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Prints tasks to stdout. Not completed tasks are always printed.
     * If showAll is set to true, completed tasks are printed as well.
     * @param showAll set to true to include completed task
     */
    public void displayAllTasks(boolean showAll) {
        for (int i = 0; i < normalTasks.size(); i ++) {
            Task task = normalTasks.get(i);
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
     * Search for Task objects matching the input search string.
     * By default only incomplete tasks will be searched.
     * 
     * Prints out the list of searched Task objects.
     */
    public void searchAndDisplay(String searchStr) {
    	ArrayList<Task> searchResults = search(searchStr);
    	displaySearchResults(searchResults);
    }  
   
	/**
     * Removes the task by first searching for the search string
     * in the task description. 
     * If there is exactly one match, just delete it. 
     * If there are multiple matches, display 
     * all searchResults to user. Wait for user input to delete again. 
     */
    public int delete(String searchStr) {
    	ArrayList<Task> searchResults = search(searchStr);
    	int numResults = searchResults.size();
    	if (numResults == 0) {
    		System.out.println(NOTHING_TO_DELETE);
    	} else if (numResults == 1) {
    		delete(searchResults.get(0).getId());
    	} 
    	else { 
    		displaySearchResults(searchResults);
    	}
    	return numResults;
    }
    
    
    /**
     * Given the id of the task, the task is deleted from normalTasks 
     */
    public void delete(int id) {
    	normalTasks.remove(id-1);
    	Task.editNumOfTasks(Task.getNumOfTasks()-1);
    	// update the id of all subsequent tasks
    	for (int i=id-1; i<normalTasks.size(); i++) {
    		normalTasks.get(i).editId(i+1);
    	}
    }
    
    
    
    
    
    /**
     * Searches for Task objects matching the input search string.
     * By default only incomplete tasks will be searched.
     * Returns searchResults in the form of an ArrayList of Task objects 
     * with the same id as the index in the ArrayList 
     */
    private ArrayList<Task> search(String searchStr) {
		searchStr = searchStr.trim();
		ArrayList<Task> searchResults = new ArrayList<Task>();
		
		for (int i=0; i<normalTasks.size(); i++) {
			Task currTask = normalTasks.get(i);
			String currTaskStr = currTask.toString();
			if (currTaskStr.contains(searchStr) && !currTask.getComplete()) {
				searchResults.add(currTask);
			}
		}
		return searchResults;
	}
    
    /**
     * Prints out the list of search results containing Task objects.
     */
    private void displaySearchResults(ArrayList<Task> searchResults) {
    	if (searchResults.isEmpty()) {
    		System.out.println("No search results!");
    		return;
    	} 
    	
    	System.out.println();
		System.out.println("Search Results");
		for (int i=0; i<searchResults.size(); i++) {
			System.out.println(searchResults.get(i).toString());
		}
	}
    

}
