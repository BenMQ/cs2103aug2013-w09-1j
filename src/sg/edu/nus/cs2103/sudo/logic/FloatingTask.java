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
	public FloatingTask(String description, boolean isComplete) {
		editDescription(description);
		markCompleted(isComplete);
	}
	
	@Override
	public String toString() {
		return description + "\n";
	}
	
	// To be used to store in .txt 
	public String toStringForFile() {
		return description + " " + isComplete + "\n";
	}

}
