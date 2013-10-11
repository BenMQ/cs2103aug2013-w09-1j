package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;

import org.joda.time.DateTime;

/**
 * @author Ipsita 
 *
 */

public class TimedTask extends Task {
	
	// Timed Task constructor	
	public TimedTask(String taskDescription, ArrayList<DateTime> dateTimes) {
		assert (taskDescription != null); 
		assert (dateTimes.size() == 2);

		this.description = taskDescription;
		this.startTime = dateTimes.get(0);
		this.endTime = dateTimes.get(1); 
	}

	// To be used to store in .txt 
	public String toStringForFile() {
		return "TIMED"+"#"+description+"#"+"FROM"+startTime+"#"+"TO"+"#"+endTime+"#"+isComplete+"\n";
	}

	
}
