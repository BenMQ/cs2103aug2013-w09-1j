/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

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
		this.endTime = dateTimes.get(0);
	}

	public String toStringForFile() {
		int bound = endTime.toString().indexOf(":");
		String returnedEndTime = this.endTime.toString()
				.substring(0, bound + 3);

		return "DEADLINE" + "#" + description + "#" + returnedEndTime + "#"
				+ isComplete;
	}
}
