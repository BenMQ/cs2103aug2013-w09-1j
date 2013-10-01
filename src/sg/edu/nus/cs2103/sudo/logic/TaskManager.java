package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

/**
 * 
 * @author chenminqi
 *
 */
public class TaskManager {
    
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
