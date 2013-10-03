package sg.edu.nus.cs2103.sudo.storage;

import java.util.ArrayList;
import sg.edu.nus.cs2103.sudo.logic.Task;

public class HistorySlice {
	/**
	 * This HistorySlice class is to save one slice of history. This class is
	 * built to make sure in the future, more functions can be added to history,
	 * e.g. jump back to one specific time. etc.
	 * 
	 * @author Liu Dake
	 */
	private ArrayList<Task> taskList;
	private Boolean isNull = false;

	public HistorySlice(ArrayList<Task> taskList) {
		this.taskList = taskList;
	}

	public HistorySlice(boolean isNull) {
		this.isNull = true;
	}

	public ArrayList<Task> getTasks() {
		return taskList;
	}

	public boolean nullSlice() {
		return isNull;
	}

}
