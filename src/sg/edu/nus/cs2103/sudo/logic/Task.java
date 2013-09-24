package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita 
 *
 */

public abstract class Task {
	
	protected String description = "";
	protected Boolean isComplete = false;
	protected String startTime = "";
	protected String startDate = "";
	protected String endTime = "";
	protected String endDate = "";
	
	// Task object constructor
	public Task() {
	}
	
	public abstract String toString();	
	
	public void editDescription(String newDescription) {
		description = newDescription;
	}
	
	public void editStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public void editStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void editEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public void editEndDate(String endDate) {
		this.endDate = endDate;
	}
	public void markCompleted(boolean isComplete) {
		this.isComplete = isComplete;
	}
}
