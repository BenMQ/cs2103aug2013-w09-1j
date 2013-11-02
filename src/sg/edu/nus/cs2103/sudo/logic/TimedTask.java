package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;

/**
 * @author Ipsita Mohapatra A0101286N
 * 
 *         This class stores the information about Timed Tasks. Timed Tasks will
 *         have an id, description, isComplete, startTime and endTime. None of
 *         these fields will be null.
 */

public class TimedTask extends Task {


	// Timed Task constructor
	public TimedTask(String taskDescription, ArrayList<DateTime> dateTimes)
			throws IllegalArgumentException {
		assert (taskDescription != null);
		assert (dateTimes.size() == 2);

		this.description = taskDescription;
		DateTime startTime = dateTimes.get(0);
		DateTime endTime = dateTimes.get(1);

		checkValidityTimes(startTime, endTime);

		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public TimedTask(int id, String taskDescription, boolean isComplete, DateTime startTime, DateTime endTime) {
		checkValidityTimes(startTime, endTime);
		
		this.id = id;
		this.description = taskDescription;
		this.isComplete = isComplete;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	// @author Liu Dake
	// To be used to store in .txt
	public String toStringForFile() {
		int bound = startTime.toString().indexOf(":");
		String returnedStartTime = this.startTime.toString().substring(0,
				bound + 3);
		int bound2 = endTime.toString().indexOf(":");
		String returnedEndTime = this.endTime.toString()
				.substring(0, bound + 3);
		return "TIMED" + "#" + description + "#" + returnedStartTime + " to "
				+ returnedEndTime + "#" + isComplete;
	}
	
	private void checkValidityTimes(DateTime startTime, DateTime endTime) {
		checkStartAndEndTime(startTime, endTime);
	}
	
	private void checkStartAndEndTime(DateTime startTime, DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();

		int check = dtComp.compare(endTime, startTime);

		// check == 0 if the startTime and endTime are the same (Invalid
		// TimedTask)
		// check == -1 if endTime occurs before startTime (Invalid TimedTask)
		// check == 1 if endTime occurs after startTime (Valid TimedTask)
		boolean sameStartAndEnd = check == 0;
		if (sameStartAndEnd) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_SAME_START_END_TIME);
		} else {
			boolean invalidStartAndEnd = check == -1;
			if (invalidStartAndEnd) {
				throw new IllegalArgumentException(
						Constants.MESSAGE_END_BEFORE_START_TIME);
			}
		}
	}
}


