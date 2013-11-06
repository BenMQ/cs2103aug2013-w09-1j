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

	private static final int NUM_PRECEDING_CHARACTERS = 3;
	private static final int MAX_CHARACTER_LENGTH = 17;

	/**
	 * Helper method to editTask given the index in the ArrayList<Task>
	 * 
	 * @param taskDescription
	 * @param dates
	 * @param index
	 */
	public static void editTaskHelper(String taskDescription,
			ArrayList<DateTime> dates, int index, ArrayList<Task> tasks) {

		Task oldTask = tasks.remove(index);
		Task newTask = TaskManagerUtils.editDescription(taskDescription,
				oldTask);

		newTask = TaskManagerUtils.editDateTime(dates, newTask);
		tasks.add(newTask);
	}

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

	/**
	 * Helper method to format the floating tasks nicely.
	 * 
	 * @param floatingTasks
	 *            ArrayList<FloatingTask> that is to be formatted
	 * @return String of formatted floating tasks
	 */
	public static String formatFloatingTasks(
			ArrayList<FloatingTask> floatingTasks) {
		String toReturn = "";
		for (FloatingTask task : floatingTasks) {
			if (!task.isComplete()) {
				String str = task.toString();

				assert (!str.isEmpty());
				if (str.length() > MAX_CHARACTER_LENGTH) {
					String[] tokens = str.split(" ");
					int currLength = 0;

					for (int j = 0; j < tokens.length; j++) {
						String token = tokens[j];
						currLength += token.length();
						if (currLength > MAX_CHARACTER_LENGTH && j > 1) {
							currLength = NUM_PRECEDING_CHARACTERS;
							currLength += token.length();
							toReturn += "\n   ";
						}
						toReturn += token + " ";
					}
				} else {
					toReturn += str;
				}
				toReturn += "\n";
			}
		}
		return toReturn;
	}

	/**
	 * @param searchStr
	 *            string to be searched for
	 * @param searchAll
	 *            set to true if search for incomplete and completed tasks
	 * @return ArrayList<Task> list of search results
	 */
	public static ArrayList<Task> searchHelper(ArrayList<Task> tasks,
			String searchStr, boolean searchAll) {
		ArrayList<Task> searchResults = new ArrayList<Task>();

		for (int i = 0; i < tasks.size(); i++) {
			Task currTask = tasks.get(i);
			String currTaskStr = currTask.getDescription();

			if (currTaskStr.toLowerCase().contains(searchStr.toLowerCase())) {
				if (searchAll || !currTask.isComplete) {
					searchResults.add(currTask);
				}
			}
		}
		return searchResults;
	}
	
	/**
	 * Prints out the list of search results containing Task objects.
	 * 
	 * @param searchResults
	 * @throws IllegalStateException
	 */
	public static void displaySearchResults(ArrayList<Task> searchResults)
			throws IllegalStateException {

		if (searchResults.isEmpty()) {
			throw new IllegalStateException(Constants.MESSAGE_NO_SEARCH_RESULTS);
		}

		System.out.println();
		System.out.println(Constants.MESSAGE_SEARCH_RESULTS);
		for (int i = 0; i < searchResults.size(); i++) {
			System.out.println(searchResults.get(i).toString());
		}
	}
	/**
     * Produces a start DateTime and an end DateTime based on the argument
     * given. If the input is an empty array, the range will be the current day.
     * If the input has one DateTime, the range will be that particular day. If
     * the input has two DateTimes, the range will be that.
     * 
     * @param dateTimes
     * @return range calculated
     */
    public static ArrayList<DateTime> getFlexibleTimeRange(
            ArrayList<DateTime> dateTimes) {
        assert (dateTimes.size() >= 0 && dateTimes.size() <= 2);
        if (dateTimes.size() == 2) {
            if (dateTimes.get(0).isAfter(dateTimes.get(1))) {
                Collections.reverse(dateTimes);
            }
            return dateTimes;
        } else {
            DateTime day;
            if (dateTimes.size() == 1) {
                day = dateTimes.get(0);
            } else {
                day = DateTime.now();
            }
            DateTime startOfDay = new DateTime(day.getYear(),
                    day.getMonthOfYear(), day.getDayOfMonth(), 0, 0, 0);
            DateTime endOfDay = new DateTime(day.getYear(),
                    day.getMonthOfYear(), day.getDayOfMonth(), 23, 59, 59);
            ArrayList<DateTime> range = new ArrayList<DateTime>(2);
            range.add(startOfDay);
            range.add(endOfDay);
            return range;
        }
    }
    
	/**
	 * Shows the correct display message depending on showAll.
	 */
	public static void showDisplayMessage(boolean showAll) {
		if (showAll) {
			System.out.print(Constants.MESSAGE_DISPLAY_ALL);
		} else {
			System.out.print(Constants.MESSAGE_DISPLAY);
		}
	}
	
	/**
	 * Shows the correct display message for finished tasks.
	 */
	public static void showDisplayMessage() {
		System.out.print(Constants.MESSAGE_DISPLAY_FINISHED);
		System.out.println(Constants.FINISHED_TASK_SEPARATOR);
	}
	
	public static void clearTasks(ArrayList<Task> tasks) {
		tasks.clear();
	}
	
	public static ArrayList<Task> getFinishedTasks(ArrayList<Task> tasks) {
		ArrayList<Task> toReturn = new ArrayList<Task>();
		
		for (Task task: tasks) {
			if (task.isComplete()) {
				toReturn.add(task);
			}
		}
		return toReturn;
	}
	
	public static ArrayList<FloatingTask> getFloatingTasks(ArrayList<Task> tasks) {
		ArrayList<FloatingTask> toReturn = new ArrayList<FloatingTask>();

		for (Task task : tasks) {
			if ((task instanceof FloatingTask)) {
				toReturn.add((FloatingTask) task);
			}
		}
		return toReturn;
	}
}
