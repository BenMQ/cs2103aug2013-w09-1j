package sg.edu.nus.cs2103.logic;

/**
 * @author Ipsita 
 *
 */

public class Timed extends Task {
	
	private final String FROM = " from ";
	private final String TO = " to ";
	
	// Timed Task constructor
	public Timed(){
		
	}
	
	@Override
	public String toString() {
		return description + FROM + startTime + ", " + startDate + TO + endTime + ", "+ endDate + "\n";
	}
	
}
