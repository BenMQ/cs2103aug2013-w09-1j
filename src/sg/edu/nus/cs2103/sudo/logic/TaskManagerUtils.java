package sg.edu.nus.cs2103.sudo.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;

/**
 * This class contains static methods that help execute some of the methods in
 * TaskManager.
 * 
 * @author Ipsita Mohapatra A0101286N
 * 
 */
public class TaskManagerUtils {

	/**
	 * Helper method to edit the description in a given task
	 * 
	 * @param taskDescription
	 * @param task
	 * @return
	 */
	public static Task editDescription(String taskDescription, Task task) {
		if (taskDescription != "" && taskDescription != null) {
			task.setDescription(taskDescription);
		}
		return task;
	}

	/**
	 * Helper method to edit the date and time in a given task
	 * 
	 * @param dates
	 * @param task
	 * @return
	 */
	public static Task editDateTime(ArrayList<DateTime> dates, Task task) {
		if (dates.size() == 1) {
			if (!(task instanceof DeadlineTask)) {
				task = new DeadlineTask(task.getId(), task.getDescription(),
						task.isComplete(), dates.get(0));
			} else {
				task.setEndTime(dates.get(0));
			}
		} else if (dates.size() == 2) {
			checkValidityTimes(dates.get(0), dates.get(1));
			if (!(task instanceof TimedTask)) {
				task = new TimedTask(task.getId(), task.getDescription(),
						task.isComplete(), dates.get(0), dates.get(1));
			} else {
				task.setStartTime(dates.get(0));
				task.setEndTime(dates.get(1));
			}
		}

		return task;
	}

	/**
	 * Helper method Checks the validity of 2 DateTimes. To be valid, the first
	 * DateTime must occur chronologically before the second DateTime. If
	 * invalid, throw exception.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public static void checkValidityTimes(DateTime startTime, DateTime endTime) {
		checkStartAndEndTime(startTime, endTime);
	}

	/**
	 * Helper method.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public static void checkStartAndEndTime(DateTime startTime, DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();

		int check = dtComp.compare(endTime, startTime);

		// check = 0 if the startTime and endTime are the same (Invalid
		// TimedTask)
		// check = -1 if endTime occurs before startTime (Invalid TimedTask)
		// check = 1 if endTime occurs after startTime (Valid TimedTask)
		boolean sameStartAndEnd = check == 0;
		if (sameStartAndEnd) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_SAME_START_END_TIME);
		} else {
			boolean invalidStartAndEnd = check == -1;
			if (invalidStartAndEnd) {
				throw new IllegalArgumentException(
						Constants.MESSAGE_END_BEFORE_START_TIME);
			}
		}
	}

	/**
	 * To check if the task list is empty. If yes, throw exception.
	 */
	public static void checkEmptyList(ArrayList<Task> list)
			throws IllegalStateException {
		if (list.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_EMPTY_LIST);
		}
	}

	/**
	 * Checks for the validity of the index used in operations. If invalid,
	 * throw exception.
	 * 
	 * @param index
	 * @param list
	 * @throws IndexOutOfBoundsException
	 */
	public static void checkValidityIndex(int index, ArrayList<Task> list)
			throws IndexOutOfBoundsException {

		if (index < 0 || index >= list.size()) {
			throw new IndexOutOfBoundsException(
					Constants.MESSAGE_INVALID_TASK_INDEX);
		}
	}

	/**
	 * Updates the id of each of the Task objects. To be done after every
	 * operation.
	 */
	public static void updateAllIds(ArrayList<Task> tasks) {
		for (int i = 0; i < tasks.size(); i++) {
			tasks.get(i).setId(i + 1);
		}
	}

	/**
	 * Sorts all the Task objects according to end time and incomplete tasks
	 * before completed tasks.
	 * 
	 * @throws Exception
	 */
	public static ArrayList<Task> sortTasks(ArrayList<Task> tasks) {
		Collections.sort(tasks, new SortTasksByEndTimeComparator());
		Collections.sort(tasks, new SortTasksByCompletedComparator());
		return tasks;
	}

	/**
	 * Sorts all the Task objects and updates all the ids. The ArrayList is now
	 * ready for saving to history and file.
	 */
	public static void sortAndUpdateIds(ArrayList<Task> tasks) {
		sortTasks(tasks);
		updateAllIds(tasks);
	}

	/**
	 * Saves the task list to history and also saves the file. To be executed
	 * after each operation is completed.
	 * 
	 * @throws IOException
	 */
	public static void saveToHistory(StorageHandler storage) throws IOException {
		storage.save(true);
	}

}
