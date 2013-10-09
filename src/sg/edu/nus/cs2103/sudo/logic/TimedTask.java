package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita 
 *
 */

public class TimedTask extends Task {
	
	// Timed Task constructor
	public TimedTask(){
		
	}
	
	// To be used to store in .txt 
	public String toStringForFile() {
		return "TIMED"+"#"+description+"#"+"FROM"+startTime+"#"+"TO"+"#"+endTime+"#"+isComplete+"\n";
	}

	
}
