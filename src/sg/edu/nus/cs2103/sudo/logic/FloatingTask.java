package sg.edu.nus.cs2103.sudo.logic;

import sg.edu.nus.cs2103.sudo.Constants;

//@author A0101286N
/**
 * @author Ipsita Mohapatra A0101286N
 * 
 *         This class stores the information about Floating Tasks. Floating
 *         Tasks will have an id, description and isComplete. None of these
 *         fields will be null.
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
		return "floating" + "#" + description + "#" + isComplete;
	}
	
	//@author A0099317U
	public String getAddMessage(){
		return String.format(Constants.MESSAGE_ADD_FLOATING, this.description);
	}

	public String getDateString() {
		return "";
	}

}