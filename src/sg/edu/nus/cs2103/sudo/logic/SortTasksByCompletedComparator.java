//@author A0101286N
package sg.edu.nus.cs2103.sudo.logic;

import java.util.Comparator;

/**
 * This class is implements the comparator in order to allow sorting of tasks by
 * completed parameter
 */
public class SortTasksByCompletedComparator implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		return (task1.isComplete() == task2.isComplete() ? 0 : (task1
				.isComplete() ? 1 : -1));
	}

}
