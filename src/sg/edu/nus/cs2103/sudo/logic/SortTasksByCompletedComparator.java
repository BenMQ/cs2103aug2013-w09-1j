package sg.edu.nus.cs2103.sudo.logic;

import java.util.Comparator;

public class SortTasksByCompletedComparator implements Comparator<Task> {

	@Override
	public int compare(Task task1, Task task2) {
		return (task1.isComplete() == task2.isComplete() ? 0 : (task1.isComplete() ? 1 : -1));
	}

}
