package sg.edu.nus.cs2103.sudo.logic;

import java.util.Comparator;

/**
 * @author Ipsita
 *
 * This class allows sorting of tasks in a list according to their end times. 
 * Floating Tasks will always appear at the end. 
 * 
 */
public class SortTasksByEndTimeComparator implements Comparator<Task> {
	
	@Override
	public int compare(Task task1, Task task2) {
		if (task1.endTime != null) {
			if (task2.endTime != null) {
				return task1.endTime.compareTo(task2.endTime);
			} else {
				return -1;
			}
		} else {
			if (task2.endTime != null) {
				return 1;
			} else {
				return 0;
			}
		}
	}

	/*
	public int compareTo(Task x) {
		if (this.endTime != null) {
			if (x.endTime != null) {
				return this.endTime.compareTo(x.endTime);
			} else {
				return -1;
			}
		} else {
			if (x.endTime != null) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	*/
}
