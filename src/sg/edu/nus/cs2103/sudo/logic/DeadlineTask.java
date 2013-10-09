/**
 * 
 */
package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita
 *
 */
public class DeadlineTask extends Task {
	
	// Deadline Task constructor
	public DeadlineTask() {
	}
	
	public String toStringForFile() {
		return "DEADLINE"+"#"+description+"#"+ "BY"+endTime + "#" + isComplete + "\n";
	}
}
