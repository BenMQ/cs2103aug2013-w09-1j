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
}
