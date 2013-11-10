package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.MutableInterval;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

// @author A0101286N
public class TaskManagerTest {
	private static final String SAVE_FILENAME = "taskmanagertest_save.sav";
	private static final String HISTORY_FILENAME = "taskmanager_history.sav";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	BufferedReader savefile_reader;
	Scanner user = new Scanner(System.in);
	private static TaskManager manager;
	private File savefile;
	private File historyfile;
	private final static String floatingTaskDescription = "learn how to fish";
	private final static String timedTaskDescription = "Trying an invalid time interval";

	@Before
	public void setUp() throws FileNotFoundException {
		savefile = new File(SAVE_FILENAME);
		historyfile = new File(HISTORY_FILENAME);
		StorageHandler.getStorageHandler(SAVE_FILENAME, HISTORY_FILENAME);
		manager = TaskManager.getTaskManager(SAVE_FILENAME, HISTORY_FILENAME);
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws IOException {
		manager.clearTasks();
		savefile.delete();
		historyfile.delete();
	}

	/**
	 * Tests adding floating tasks, timed tasks and deadline tasks and
	 * displaying of all uncompleted floating tasks.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddTasks() throws Exception {
		// adding valid floating task
		manager.addTaskAndSort(new FloatingTask(floatingTaskDescription));
		assertEquals("1. learn how to fish false\n",
				displayTasks(manager.getTasks()));

		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		dateTimes.add(new DateTime());
		dateTimes.add(new DateTime(2006, 3, 26, 12, 0, 0, 0));

		// boundary case: add task with invalid time interval
		try {
			manager.addTaskAndSort(new TimedTask(timedTaskDescription, dateTimes));
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_END_BEFORE_START_TIME,
					e.getMessage());
		}

		// boundary case: add task with same start and end time
		dateTimes.clear();
		dateTimes.add(new DateTime());
		dateTimes.add(new DateTime());
		
		// boundary case: adding the same start and end time
		/*
		try {
			manager.addTask(new TimedTask("Trying same start and end time",
					dateTimes));
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_SAME_START_END_TIME, e.getMessage());
		}
		*/
		
		manager.addTaskAndSort(new TimedTask(0, "Have dinner with family in NUS",
				false, new DateTime(2013, 10, 23, 19, 0, 0, 0), new DateTime(
						2013, 10, 23, 21, 0, 0, 0)));
		assertEquals(
				"1. Have dinner with family in NUS from Wed 23 October 07:00 PM" +
				" to Wed 23 October 09:00 PM false\n2. learn how to fish false\n",
				displayTasks(manager.getTasks()));

		// adding valid deadline task
		manager.addTaskAndSort(new DeadlineTask(0, "Submit proposal at Nus", false,
				new DateTime(2013, 10, 21, 0, 0, 0, 0)));
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM" +
				" to Wed 23 October 09:00 PM false\n3. learn how to fish false\n",
				displayTasks(manager.getTasks()));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFinishTasks() throws Exception {
		testAddTasks();

		// Equivalence partitioning
		// boundary case: invalid task id
		try {
			manager.markAsComplete(-1);
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_INVALID_TASK_INDEX, e.getMessage());
		}

		// Equivalence partitioning & boundary value analysis
		// boundary case: invalid task id
		try {
			manager.markAsComplete(0);
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_INVALID_TASK_INDEX, e.getMessage());
		}

		// Equivalence partitioning & boundary value analysis
		// boundary case: invalid task id
		try {
			manager.markAsComplete(manager.getTasks().size() + 1);
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_INVALID_TASK_INDEX, e.getMessage());
		}

		// valid task id
		manager.markAsComplete(2);
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. learn how to fish false\n3. Have dinner with family in NUS " +
				"from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM true\n",
				displayTasks(manager.getTasks()));
	}

	@Test
	public void testEditTasks() throws Exception {
		testAddTasks();
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM" +
				" to Wed 23 October 09:00 PM false\n3. learn how to fish false\n",
				displayTasks(manager.getTasks()));

		// boundary case where number of dateTimes taken in by method is 0
		// (floating task)
		// boundary case for taskId: last task in the list
		manager.editTask(3, "learn how to fish with dad",
				new ArrayList<DateTime>());
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM " +
				"to Wed 23 October 09:00 PM false\n" +
				"3. learn how to fish with dad false\n",
				displayTasks(manager.getTasks()));

		// boundary case for taskId; invalid task id
		try {
			manager.editTask(4, "learn to sew from mom",
					new ArrayList<DateTime>());
		} catch (Exception e) {
			assertEquals("Invalid task index.\n", e.getMessage());
		}

		// boundary case: string input is empty
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		dateTimes.add(new DateTime(2013, 10, 22, 9, 0, 0, 0));
		manager.editTask(1, "", dateTimes);
		assertEquals(
				"1. Submit proposal at Nus by Tue 22 October 09:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM " +
				"to Wed 23 October 09:00 PM false\n" +
				"3. learn how to fish with dad false\n",
				displayTasks(manager.getTasks()));

		// boundary case: number of dateTimes is 2 (timed task)
		dateTimes.add(new DateTime(2013, 10, 22, 17, 0, 0, 0));
		manager.editTask(1, "", dateTimes);
		assertEquals(
				"1. Submit proposal at Nus from Tue 22 October 09:00 AM to " +
				"Tue 22 October 05:00 PM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM " +
				"to Wed 23 October 09:00 PM false\n" +
				"3. learn how to fish with dad false\n",
				displayTasks(manager.getTasks()));

	}

	/**
	 * Tests searching for a string in the incomplete floating tasks TODO: Test
	 * search for both complete and incomplete floating tasks
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSearchTasks() throws Exception {
		testAddTasks();

		assertEquals(
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM " +
				"to Wed 23 October 09:00 PM false\n",
				displayTasks(manager.search("family", true)));
		
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM" +
				" to Wed 23 October 09:00 PM false\n",
				displayTasks(manager.search("nus", false)));
		
		assertEquals("Nothing to display.\n",
				displayTasks(manager.search("fishes", true)));

		// boundary case: search string is empty
		try {
			assertEquals(Constants.NOTHING_TO_DISPLAY,
					displayTasks(manager.search("", true)));
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_INVALID_SEARCH, e.getMessage());
		}	

	}

	/**
	 * Tests delete with task id as well as delete with search string
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		testAddTasks();
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM " +
				"to Wed 23 October 09:00 PM false\n3. learn how to fish false\n",
				displayTasks(manager.getTasks()));

		// boundary case: task id is 0
		try {
			manager.delete(0);
		} catch (Exception e) {
			assertEquals(Constants.MESSAGE_INVALID_TASK_INDEX, e.getMessage());
		}

		// equivalence partitioning: when search results of delete is 1
		manager.delete("fish");
		assertEquals("Deleted: learn how to fish\n", outContent.toString());
		outContent.reset();
		
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM false\n" +
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM" +
				" to Wed 23 October 09:00 PM false\n",
				displayTasks(manager.getTasks()));

	}
	
	//@author A0099314Y 
	@Test
	public void testGetFlexibleTimeRangeEmpty() {
	    ArrayList<DateTime> input = new ArrayList<DateTime>();
	    ArrayList<DateTime> actual = 
	            TaskManagerUtils.getFlexibleTimeRange(input); 
	    DateTime dt0000 = today(0, 0);
        DateTime dt2359 = today(23, 59, 59);
        ArrayList<DateTime> expected = new ArrayList<DateTime>();
        expected.add(dt0000);
        expected.add(dt2359);
	    assertEquals(expected, actual);
	}
	
	@Test
    public void testGetFlexibleTimeRangeOneInput() {
        DateTime dt0000 = today(0, 0);
        DateTime dt2359 = today(23, 59, 59);
        
        ArrayList<DateTime> input = new ArrayList<DateTime>();
        input.add(dt2359);
        
        ArrayList<DateTime> actual = 
                TaskManagerUtils.getFlexibleTimeRange(input); 
        
        ArrayList<DateTime> expected = new ArrayList<DateTime>();
        expected.add(dt0000);
        expected.add(dt2359);
        assertEquals(expected, actual);
    }
    
	@Test
    public void testGetFlexibleTimeRangeTwoInputs() {
        DateTime tmr1300 = today(13, 0).plusDays(1);
        DateTime dt2359 = today(23, 59, 59);
        
        ArrayList<DateTime> input = new ArrayList<DateTime>();
        input.add(dt2359);
        input.add(tmr1300);
        
        ArrayList<DateTime> actual = 
                TaskManagerUtils.getFlexibleTimeRange(input); 
        
        ArrayList<DateTime> expected = new ArrayList<DateTime>();
        expected.add(dt2359);
        expected.add(tmr1300);
        assertEquals(expected, actual);
    }
	
	@Test
    public void testGetFlexibleTimeRangeTwoInputsReversed() {
        DateTime tmr1300 = today(13, 0).plusDays(1);
        DateTime dt2359 = today(23, 59, 59);
        
        ArrayList<DateTime> input = new ArrayList<DateTime>();

        input.add(tmr1300);
        input.add(dt2359);
        
        ArrayList<DateTime> actual = 
                TaskManagerUtils.getFlexibleTimeRange(input); 
        
        ArrayList<DateTime> expected = new ArrayList<DateTime>();
        expected.add(dt2359);
        expected.add(tmr1300);
        assertEquals(expected, actual);
    }
	
	@Test
    public void testGetFlexibleTimeRangeThreeInputs() {

        DateTime dt0800 = today(8, 0);
        DateTime dt0900 = today(9, 0);
        DateTime dt2359 = today(23, 59, 59);
        ArrayList<DateTime> input = new ArrayList<DateTime>();

        input.add(dt0800);
        input.add(dt0900);
        input.add(dt2359);
        
        try {
            @SuppressWarnings("unused")
            ArrayList<DateTime> actual = 
                    TaskManagerUtils.getFlexibleTimeRange(input); 
            assertTrue(false); // unreachable, assertion should always fail
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
    
	@Test
	public void testGetFreeIntervalsNoTask() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// No task
		ArrayList<MutableInterval> actual = manager.getFreeIntervals(range);
		ArrayList<MutableInterval> expected = new ArrayList<MutableInterval>();
		expected.add(new MutableInterval(dt0000, dt2359));
		
		assertEquals(expected, actual);
	}

	@Test
	public void testGetFreeIntervalsSomeTasks() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// some slots
		manager.addTaskAndSort(new TimedTask(0, "timed", false, today(11, 0), today(
				12, 0)));
		manager.addTaskAndSort(new TimedTask(0, "timed", false, today(17, 0), today(
				18, 0)));
		ArrayList<MutableInterval> actual = manager.getFreeIntervals(range);
		ArrayList<MutableInterval> expected = new ArrayList<MutableInterval>();
        expected.add(new MutableInterval(dt0000, today(11, 0)));
        expected.add(new MutableInterval(today(12, 0), today(17, 0)));
        expected.add(new MutableInterval(today(18, 0), dt2359));
		assertEquals(expected, actual);
	}

	@Test
	public void testGetFreeIntervalsWholeDay() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// whole day
		manager.addTaskAndSort(new TimedTask(0, "timed", false, dt0000, dt2359));
		ArrayList<MutableInterval> actual = manager.getFreeIntervals(range);
		ArrayList<MutableInterval> expected = new ArrayList<MutableInterval>();
        assertEquals(expected, actual);
    }

	private String displayTasks(ArrayList<Task> tasks) {

		String str = "";

		if (tasks.isEmpty()) {
			str += Constants.NOTHING_TO_DISPLAY;
			return str;
		}

		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			str += task.toString() + " " + task.isComplete() + "\n";
		}

		return str;

	}

	private DateTime today(int hours, int minutes, int seconds) {
		DateTime now = DateTime.now();
		return new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), hours, minutes, seconds);
	}
	
	private DateTime today(int hours, int minutes) {
	    return today(hours, minutes, 0);
	}
}
