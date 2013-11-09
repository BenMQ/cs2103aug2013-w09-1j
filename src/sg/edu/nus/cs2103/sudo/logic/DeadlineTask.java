/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.ui.DisplayUtils;


/**
 * @author Ipsita Mohapatra A0101286N
 * 
 *         This class stores the information about Deadline Tasks. Deadline
 *         Tasks will have an id, description, isComplete and endTime. None of
 *         these fields will be null.
 * 
 */
public class DeadlineTask extends Task {

	// Deadline Task constructor
	public DeadlineTask(String taskDescription, ArrayList<DateTime> dateTimes) {
		assert (taskDescription != null);
		assert (dateTimes.size() == 1);

		this.description = taskDescription;
		DateTime endTime = dateTimes.get(0);

		this.endTime = endTime;
	}
	
	public DeadlineTask(int id, String taskDescription, boolean isComplete, DateTime endTime) {
		this.id = id;
		this.description = taskDescription;
		this.isComplete = isComplete;
		this.endTime = endTime;
	}
	
	public String toStringForFile() {
		int bound = endTime.toString().indexOf(":");
		String returnedEndTime = this.endTime.toString()
				.substring(0, bound + 3);

		return "DEADLINE" + "#" + description + "#" + returnedEndTime + "#"
				+ isComplete;
	}
	
	public String getAddMessage() {
		return String.format(Constants.MESSAGE_ADD_DEADLINE, 
				this.description,
				DisplayUtils.formatDate(this.endTime));
	}

	/**
	 * Helper method to return a formatted string for Deadline tasks.
	 * @param Task
	 * @return String
	 */
	public String getDateString() {
		DateTimeFormatter onlytimeformat = Constants.HOUR_FORMAT;
		if (DateTimeUtils.hasZeroMinutes(this.getEndTime())) {
			onlytimeformat = Constants.HOUR_MINUTE_FORMAT;
		}		
		
		return new StringBuilder(this.getId() + ". [by " + this.getEndTime().
				toString(onlytimeformat) + "] ").toString();
	}
	
}
