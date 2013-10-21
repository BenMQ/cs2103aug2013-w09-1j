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

	private static final int INVALID = -1;
	private static final int SAME = 0;

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

	private void checkValidityTimes(DateTime startTime, DateTime endTime) {
		// checkTimesWithCurrentTime(startTime, endTime); 
		checkStartAndEndTime(startTime, endTime);
	}

	private void checkStartAndEndTime(DateTime startTime, DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();

		int check = dtComp.compare(endTime, startTime);

		// check == 0 if the startTime and endTime are the same (Invalid
		// TimedTask)
		// check == -1 if endTime occurs before startTime (Invalid TimedTask)
		// check == 1 if endTime occurs after startTime (Valid TimedTask)
		boolean sameStartAndEnd = check == SAME;
		if (sameStartAndEnd) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_SAME_START_END_TIME);
		} else {
			boolean invalidStartAndEnd = check == INVALID;
			if (invalidStartAndEnd) {
				throw new IllegalArgumentException(
						Constants.MESSAGE_END_BEFORE_START_TIME);
			}
		}
	}

	private void checkTimesWithCurrentTime(DateTime startTime,
			DateTime endTime) {
		
		DateTimeComparator dtComp = DateTimeComparator.getInstance();
		DateTime currTime = new DateTime();

		int checkStartWithCurrent = dtComp.compare(startTime, currTime);
		
		// if startTime is before currTime
		boolean invalidStartTime = checkStartWithCurrent == INVALID;
		if (invalidStartTime) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_INVALID_START_TIME);
		}
		
		int checkEndWithCurrent = dtComp.compare(endTime, currTime);
		boolean invalidEndTime = checkEndWithCurrent == INVALID;
		if (invalidEndTime) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_INVALID_END_TIME);
		}
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

}
