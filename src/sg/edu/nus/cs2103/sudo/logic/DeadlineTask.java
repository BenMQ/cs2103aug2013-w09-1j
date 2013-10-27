/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;

/**
 * @author Ipsita Mohapatra A0101286N
 * 
 *         This class stores the information about Deadline Tasks. Deadline
 *         Tasks will have an id, description, isComplete and endTime. None of
 *         these fields will be null.
 * 
 */
public class DeadlineTask extends Task {

	private static final int INVALID = -1;

	// Deadline Task constructor
	public DeadlineTask(String taskDescription, ArrayList<DateTime> dateTimes) {
		assert (taskDescription != null);
		assert (dateTimes.size() == 1);

		this.description = taskDescription;
		DateTime endTime = dateTimes.get(0);

		// checkValidityTimes(endTime);

		this.endTime = endTime;
	}
	
	public DeadlineTask(int id, String taskDescription, boolean isComplete, DateTime endTime) {
		this.id = id;
		this.description = taskDescription;
		this.isComplete = isComplete;
		this.endTime = endTime;
	}
	
	/* TO BE REMOVED
	private void checkValidityTimes(DateTime endTime) {
		checkTimeWithCurrentTime(endTime);
	}

	private void checkTimeWithCurrentTime(DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();
		DateTime currTime = new DateTime();
		
		int checkEndWithCurrent = dtComp.compare(endTime, currTime);
		boolean invalidEndTime = checkEndWithCurrent == INVALID;
		
		if (invalidEndTime) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_INVALID_END_TIME);
		}
		
	}
	*/
	public String toStringForFile() {
		int bound = endTime.toString().indexOf(":");
		String returnedEndTime = this.endTime.toString()
				.substring(0, bound + 3);

		return "DEADLINE" + "#" + description + "#" + returnedEndTime + "#"
				+ isComplete;
	}
}
