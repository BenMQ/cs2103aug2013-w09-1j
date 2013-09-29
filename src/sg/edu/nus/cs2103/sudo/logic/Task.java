package sg.edu.nus.cs2103.sudo.logic;

import org.joda.time.DateTime;

/**
 * @author Ipsita 
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
	
	public abstract String toString();	
	
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
	
}
