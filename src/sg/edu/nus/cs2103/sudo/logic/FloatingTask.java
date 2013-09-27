package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita 
 *
 */
public class FloatingTask extends Task {
	
	// Floating object constructor
	public FloatingTask() {
	}
	
	// Floating object constructor
	public FloatingTask(String description) {
		editDescription(description);
	}
	
	@Override
	public String toString() {
		return description+"\n";
	}

}
