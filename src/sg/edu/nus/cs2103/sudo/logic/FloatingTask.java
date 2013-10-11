package sg.edu.nus.cs2103.sudo.logic;

/**
 * @author Ipsita 
 *
 */
public class FloatingTask extends Task {
	
	// Floating object constructor
	public FloatingTask(String taskDescription) {
		assert (taskDescription != null);
		
		this.description = taskDescription;
	}
	
	// To be used to store in .txt 
	public String toStringForFile() {
		return "floating"+"#"+description+"#"+isComplete+"\n";
	}

}