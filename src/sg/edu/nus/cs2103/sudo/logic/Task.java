package sg.edu.nus.cs2103.sudo.logic;

import org.joda.time.DateTime;

/**
 * @author Ipsita Mohapatra A0101286N
 *
 * This class stores the information about all types of Tasks.
 * This abstract class is the parent class of TimedTask, DeadlineTask and FloatingTask
 * Some of the fields like endTime and/or startTime may be empty.
 * 
 */

public abstract class Task {
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
	
	public int getId() {
		return id;
	}

	public boolean isComplete() {
		return isComplete;
	}
	
	/**
	 * Converts the task object into a string with the following format:
	 * 1) Timed task: desc from x to y
	 * 2) Deadline task: desc by x
	 * 3) Floating task: desc
	 */
	public String toString() {
		String output = id + ". " + description;
	    if (startTime != null) {
	        output += " from " + startTime.toString("E dd MMMM, yyyy HH:mm a") + " to " + endTime.toString("E dd MMMM, yyyy HH:mm a");
	    } else if (endTime != null) {
	        output += " by " + endTime.toString("E dd MMMM, yyyy HH:mm a");
	    }
	    
	    return output;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setDescription(String newDescription) {
		description = newDescription;
	}
	
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	public void setComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}
	
	public abstract String toStringForFile();
	
}
