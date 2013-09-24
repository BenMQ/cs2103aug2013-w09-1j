/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita
 *
 */
public class Deadline extends Task {
	
	private final String BY = " by ";
	
	// Deadline Task constructor
	public Deadline() {
		
	}
	
	@Override
	public String toString() {
		return description + BY + endTime + ", "+ endDate + "\n";
	}
}
