/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

/**
 * @author Ipsita
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
		String returnedEndTime = this.endTime.toString().substring(0, bound+3);
		
		return "DEADLINE"+"#"+description+"#"+ returnedEndTime + "#" + isComplete;
	}
}
