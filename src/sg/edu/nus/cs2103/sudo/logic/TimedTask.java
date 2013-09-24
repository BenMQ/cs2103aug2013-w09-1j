package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita 
 *
 */

public class TimedTask extends Task {
	
	private final String FROM = " from ";
	private final String TO = " to ";
	
	// Timed Task constructor
	public TimedTask(){
		
	}
	
	@Override
	public String toString() {
		return description + FROM + startTime + ", " + startDate + TO + endTime + ", "+ endDate + "\n";
	}
	
}
