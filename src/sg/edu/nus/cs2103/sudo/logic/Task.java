package sg.edu.nus.cs2103.sudo.logic;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;

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
	
	public Task() {}
	
	public Task(int id, String description, DateTime startTime, DateTime endTime) {
	    this.id = id;
	    this.description = description;
	    this.startTime = startTime;
	    this.endTime = endTime;
	}
	
	public int getId() {
		return id;
	}
	
	public String getDescription() {
		return description;
	}

	public DateTime getStartTime() {
		return startTime;
	}
	
	public DateTime getEndTime() {
		return endTime;
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
	        output += " from " + startTime.toString("EEE dd MMMM hh:mm a") + " to " + endTime.toString("EEE dd MMMM hh:mm a");
	    } else if (endTime != null) {
	        output += " by " + endTime.toString("EEE dd MMMM hh:mm a");
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
