package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 
 * @author chenminqi
 *
 */
public class TaskManager {
    
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
        throw new NotImplementedException();
    }
    
    
    /**
     * Loads an ArrayList of normal (timed or deadline) tasks into memory.
     * @see preloadTasks;
     */
    
    public void preloadNormalTasks(ArrayList<Task> normalTasks) {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }
    
    /**
     * Search a text in all tasks. By default completed tasks will not be searched.
     * @param text the text string to be searched for, case insensitive
     * @param searchAll set to true if completed tasks should be searched for.
     * @return ArrayList of the tasks that meets the search criteria.
     */
    public ArrayList<Task> searchTask(String text, boolean searchAll) {
        throw new NotImplementedException();
    }
    
    /**
     * Search a task by the internal ID
     * @param ID ID of the internal task
     * @return returns the task if found, null if it doesn't exist
     */
    public Task getTask(int ID) {
        throw new NotImplementedException();
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
     * Searches tasks and returns an ArrayList of the tasks that match the input search string
     */
    private ArrayList<Task> search(String searchStr) {
		searchStr = searchStr.trim();
		ArrayList<Task> searchedTasks = new ArrayList<Task>();
		
		for (int i=0; i<normalTasks.size(); i++) {
			Task currTask = normalTasks.get(i);
			String currTaskStr = currTask.toString();
			if (currTaskStr.contains(searchStr)) {
				searchedTasks.add(currTask);
			}
		}
		return searchedTasks;
	}
    
}
