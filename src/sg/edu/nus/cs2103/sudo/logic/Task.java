package sg.edu.nus.cs2103.sudo.logic;

import org.joda.time.DateTime;

/**
 * @author Ipsita 
 *
 */

public abstract class Task implements Comparable<Task>{
	protected int id;
	protected String description = "";
	protected Boolean isComplete = false;
	protected DateTime startTime;
	protected DateTime endTime;
	
	// Task object constructor
	public Task() {
	}
	
	public Task(int id, String desc, DateTime startTime, DateTime endTime) {
	    this.id = id;
	    this.description = desc;
	    this.startTime = startTime;
	    this.endTime = endTime;
	}
	
	/**
	 * Converts the task object into a string with the following format:
	 * 1) Timed task: desc from x to y
	 * 2) Deadline task: desc by x
	 * 3) Floating task: desc
	 */
	public String toString() {
	    String output = description;
	    if (startTime != null) {
	        output += " from " + startTime.toString() + " to " + endTime.toString();
	    } else if (endTime != null) {
	        output += " by " + endTime.toString();
	    }
	    
	    return output;
	}
	
	public void editDescription(String newDescription) {
		description = newDescription;
	}
	
	public void editStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	public void editEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	public void markCompleted() {
		this.isComplete = true;
	}
	
	public void markNotCompleted() {
	    this.isComplete = false;
	}
	
	public void toggleCompleted() {
	    this.isComplete = !this.isComplete;
	}
	
	/**
	 * Implements comparable interface. Tasks are sorted by their end time.
	 * Floating tasks are always at the end, but they are managed separately anyway.
	 */
	public int compareTo(Task x) {
        if (this.endTime != null) {
            if (x.endTime != null) {
                return this.endTime.compareTo(x.endTime);
            } else {
                return -1;
            }
        } else {
            if (x.endTime != null) {
                return 1;
            } else {
                return 0;
            }
        }
	}
	
}
