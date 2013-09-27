/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita
 *
 */
public class DeadlineTask extends Task {
	
	private final String BY = " by ";
	
	// Deadline Task constructor
	public DeadlineTask() {
		
	}
	
	@Override
	public String toString() {
		return description + BY + endTime + ", "+ endDate + "\n";
	}

	// To be used to store in .txt 
	public String toStringForFile() {
		return description + BY + endTime + ", "+ endDate + " " + isComplete + "\n";
	}
}
