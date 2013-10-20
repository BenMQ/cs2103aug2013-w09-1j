package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

/**
 * @author Ipsita Mohapatra A0101286N
 * 
 * This class stores the information about Timed Tasks.
 * Timed Tasks will have an id, description, isComplete, startTime and endTime.
 * None of these fields will be null. 
 */

public class TimedTask extends Task {
	
	// Timed Task constructor	
	public TimedTask(String taskDescription, ArrayList<DateTime> dateTimes) throws IllegalArgumentException {
		assert (taskDescription != null); 
		assert (dateTimes.size() == 2);

		this.description = taskDescription;
		DateTime startTime = dateTimes.get(0);
		DateTime endTime = dateTimes.get(1);
		
		DateTimeComparator dtComp = DateTimeComparator.getInstance();
		int check = dtComp.compare(endTime, startTime);
		
		// check == 0 if the startTime and endTime are the same (Invalid TimedTask)
		// check == -1 if endTime occurs before startTime (Invalid TimedTask)
		if (check == 0) {
			throw new IllegalArgumentException("Start and end time for the task are the same!");
		} else if (check == -1) {
			throw new IllegalArgumentException("End time of the task occurs before start time!");
		}

		// check == 1 if endTime occurs after startTime (Valid TimedTask)
		this.startTime = startTime;
		this.endTime = endTime; 
	}
	
	// @author Liu Dake 
	// To be used to store in .txt 
	public String toStringForFile() {
		int bound = startTime.toString().indexOf(":");
		String returnedStartTime = this.startTime.toString().substring(0, bound+3);
		int bound2 = endTime.toString().indexOf(":");
		String returnedEndTime = this.endTime.toString().substring(0, bound+3);
		return "TIMED"+"#"+description+"#"+returnedStartTime+" to "+returnedEndTime+"#"+isComplete;
	}
	

	
}
