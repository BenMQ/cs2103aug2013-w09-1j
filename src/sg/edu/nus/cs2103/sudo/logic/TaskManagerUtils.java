package sg.edu.nus.cs2103.sudo.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;
import sg.edu.nus.cs2103.ui.GUI;
import sg.edu.nus.cs2103.ui.GUIConstants;

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

		GUI.print_add("\n"+Constants.MESSAGE_SEARCH_RESULTS, GUIConstants.COLOR_CODE_YELLOW);
		for (int i = 0; i < searchResults.size(); i++) {
			GUI.print_add("\n"+searchResults.get(i).toString(), GUIConstants.COLOR_CODE_GREEN);
		}
		GUI.print_add("\n\n",GUIConstants.COLOR_CODE_GREEN);
	}
	
	//@author A0099314Y
	/**
     * Produces a start DateTime and an end DateTime based on the argument
     * given. If the input is an empty array, the range will be the current day.
     * If the input has one DateTime, the range will be that particular day. If
     * the input has two DateTimes, the range will be that.
     * 
     * @param dateTimes arrayList of start and end DateTime
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
            DateTime startOfDay = getStartOfDay(day);
            DateTime endOfDay = getEndOfDay(day);
            ArrayList<DateTime> range = new ArrayList<DateTime>(2);
            range.add(startOfDay);
            range.add(endOfDay);
            return range;
        }
    }
    /**
     * Gets the last second of the day as a DateTime
     * @param day date to be used
     * @return the same day, but with 23:59:59
     */
    public static DateTime getEndOfDay(DateTime day) {
        return new DateTime(day.getYear(),
                day.getMonthOfYear(), day.getDayOfMonth(), 23, 59, 59);
    }
    
    /**
     * Gets the first second of the day as a Date
     * @param day date to be used
     * @return the same day, but with 00:00:00
     */
    public static DateTime getStartOfDay(DateTime day) {
        return new DateTime(day.getYear(),
                day.getMonthOfYear(), day.getDayOfMonth(), 0, 0, 0);
    }
    
    /**
     * Validates parameters for schedule command, must have a valid index that
     * is not completed, the dateTimes must have at most 1 date, duration must
     * be positive. 
     * @param duration duration in milliseconds
     * @param dateTimes DateTimes parsed from the input
     * @param index parsed index from the input
     */
    public static void validateScheduleParameters(long duration,
            ArrayList<DateTime> dateTimes, int index, ArrayList<Task> tasks) {
        try {
            TaskManagerUtils.checkValidityIndex(index, tasks);  
        } catch (IndexOutOfBoundsException e) {
            GUI.print_add(Constants.MESSAGE_INVALID_TASK_INDEX, 
                          GUIConstants.COLOR_CODE_RED);
            
            return;
        }
        if (dateTimes.size() > 1) {
            GUI.print_add(Constants.MESSAGE_INVALID_NUMBER_OF_DATES, 
                          GUIConstants.COLOR_CODE_RED);
            return;
        } else if (duration <= 0) {
            GUI.print_add(Constants.MESSAGE_INCOMPLETE_COMMAND, 
                          GUIConstants.COLOR_CODE_RED);
            return;
        }
        
        if (tasks.get(index).isComplete()) {
            GUI.print_add(Constants.MESSAGE_ALREADY_COMPLETE, 
                          GUIConstants.COLOR_CODE_RED);
            return;
        }
    }
    
	/**
	 * Shows the correct display message depending on showAll.
	 */
	public static void showDisplayMessage(ArrayList<Task> tasks,
	        boolean showAll) {
		if (showAll) {
			GUI.print_add(Constants.MESSAGE_DISPLAY_ALL,
			              GUIConstants.COLOR_CODE_BLUE);
		} else {
			if (tasks.isEmpty()) {
				GUI.print_add(Constants.MESSAGE_EMPTY_LIST,
				              GUIConstants.COLOR_CODE_BLUE); 
			} else {
				GUI.print_add(Constants.MESSAGE_DISPLAY,
				              GUIConstants.COLOR_CODE_BLUE);

			}
		}
	}

	//@author A0101286N
	/**
	 * Shows the correct display message for finished tasks.
	 */
	public static void showDisplayMessage() {
		GUI.print_add(Constants.MESSAGE_DISPLAY_FINISHED, GUIConstants.COLOR_CODE_BLUE);
		GUI.print_add(Constants.FINISHED_TASK_SEPARATOR, GUIConstants.COLOR_CODE_BLUE);
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
	
	/**
	 * Determines if a DateTime has zero minute values.
	 * @param DateTime
	 * @return boolean
	 */		
	public static boolean hasZeroMinutes(final DateTime datetime) {
		return datetime != null 
				&& datetime.getMinuteOfHour() > 0;
	}	
}
