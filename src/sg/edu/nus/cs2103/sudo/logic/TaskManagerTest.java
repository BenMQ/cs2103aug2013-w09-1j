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

/**
 * @author Ipsita Mohapatra A0101286N
 * 
 */
public class TaskManagerTest {
	private static final String SAVE_FILENAME = "taskmanagertest_save.sav";
	private static final String HISTORY_FILENAME = "taskmanager_history.sav";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	BufferedReader savefile_reader;
	Scanner user = new Scanner(System.in);
	private static TaskManager manager;
	private File savefile;

	@Before
	public void setUp() throws FileNotFoundException {
		savefile = new File(SAVE_FILENAME);
		new File(HISTORY_FILENAME);
		StorageHandler.getStorageHandler(SAVE_FILENAME);
		manager = TaskManager.getTaskManager();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws IOException {
		manager.clearTasks();
		savefile.delete();
	}

	/**
	 * Tests adding floating tasks and displaying of all uncompleted floating
	 * tasks. TODO: Test displaying of completed floating tasks
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAddTasks() throws Exception {
		manager.addTask(new FloatingTask("learn how to fish"));
		assertEquals("1. learn how to fish\n", displayTasks(manager.getTasks()));

		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		dateTimes.add(new DateTime());
		dateTimes.add(new DateTime(2006, 3, 26, 12, 0, 0, 0));

		manager.addTask(new DeadlineTask(0, "Submit proposal at Nus", false,
				new DateTime(2013, 10, 21, 0, 0, 0, 0)));
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. learn how to fish\n",
				displayTasks(manager.getTasks()));

		manager.addTask(new TimedTask(0, "Have dinner with family in NUS",
				false, new DateTime(2013, 10, 23, 19, 0, 0, 0), new DateTime(
						2013, 10, 23, 21, 0, 0, 0)));
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish\n",
				displayTasks(manager.getTasks()));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testFinishTasks() throws Exception {
		testAddTasks();

		// Equivalence partitioning
		try {
			manager.markAsComplete(-1);
		} catch (Exception e) {
			assertEquals("Invalid task index\n", e.getMessage());
		}

		// Equivalence partitioning & boundary value analysis
		// boundary case for taskId
		try {
			manager.markAsComplete(0);
		} catch (Exception e) {
			assertEquals("Invalid task index\n", e.getMessage());
		}

		// Equivalence partitioning & boundary value analysis
		// boundary case for taskId
		try {
			manager.markAsComplete(manager.getTasks().size() + 1);
		} catch (Exception e) {
			assertEquals("Invalid task index\n", e.getMessage());
		}

		manager.markAsComplete(2);
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. learn how to fish\n3. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n",
				displayTasks(manager.getTasks()));
	}

	@Test
	public void testEditTasks() throws Exception {
		testAddTasks();
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish\n",
				displayTasks(manager.getTasks()));

		// boundary case where number of dateTimes taken in by method is 0
		// (floating task)
		// boundary case for taskId: last task in the list
		manager.editTask(3, "learn how to fish with dad",
				new ArrayList<DateTime>());
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish with dad\n",
				displayTasks(manager.getTasks()));

		// boundary case for taskId; invalid task id
		try {
			manager.editTask(4, "learn to sew from mom",
					new ArrayList<DateTime>());
		} catch (Exception e) {
			assertEquals("Invalid task index\n", e.getMessage());
		}
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish with dad\n",
				displayTasks(manager.getTasks()));

		// boundary case where string input is empty
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		dateTimes.add(new DateTime(2013, 10, 22, 9, 0, 0, 0));
		manager.editTask(1, "", dateTimes);
		assertEquals(
				"1. Submit proposal at Nus by Tue 22 October 09:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish with dad\n",
				displayTasks(manager.getTasks()));

		// boundary case where the number of dateTimes taken in by the method is
		// 2 (timed task)
		dateTimes.add(new DateTime(2013, 10, 22, 17, 0, 0, 0));
		manager.editTask(1, "", dateTimes);
		assertEquals(
				"1. Submit proposal at Nus from Tue 22 October 09:00 AM to Tue 22 October 05:00 PM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n3. learn how to fish with dad\n",
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
				"2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n",
				displayTasks(manager.search("family", true)));
		assertEquals(
				"1. Submit proposal at Nus by Mon 21 October 12:00 AM\n2. Have dinner with family in NUS from Wed 23 October 07:00 PM to Wed 23 October 09:00 PM\n",
				displayTasks(manager.search("nus", false)));
		assertEquals("Nothing to display.\n",
				displayTasks(manager.search("fishes", true)));
		
		
		// boundary case where the search string is empty
		try{
			assertEquals("Nothing to display.\n",
				displayTasks(manager.search("", true)));
		} catch (Exception e) {
			assertEquals("Search is invalid.\n", e.getMessage());
		}

	}
	
	/**
	 * Tests delete with task id as well as delete with search string  
	 * @throws Exception 
	 */
	@Test
	public void testDelete() throws Exception {
		testAddTasks();
		
		// delete by task id boundary case when task id is 0
		try {
			manager.delete(0);
		} catch (Exception e) {
			assertEquals("Invalid task index\n", e.getMessage());
		}

		
	}
	
	

	@Test
	public void testSearchForFreeIntervalsNoTask() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// No task
		manager.searchForFreeIntervals(range);
		assertEquals(
				Constants.MESSAGE_FREE_SLOTS_PREFIX
						+ range.get(0).toString("dd MMMM hh:mm a") + " to "
						+ range.get(1).toString("dd MMMM hh:mm a") + "\n"
						+ dt0000.toString("dd MMMM hh:mm a") + " to "
						+ dt2359.toString("dd MMMM hh:mm a") + "\n",
				outContent.toString());
		outContent.reset();
	}

	@Test
	public void testSearchForFreeIntervalsSomeTasks() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// some slots
		manager.addTask(new TimedTask(0, "timed", false, today(11, 0), today(
				12, 0)));
		manager.addTask(new TimedTask(0, "timed", false, today(17, 0), today(
				18, 0)));
		manager.searchForFreeIntervals(range);
		assertEquals(
				Constants.MESSAGE_FREE_SLOTS_PREFIX
						+ range.get(0).toString("dd MMMM hh:mm a") + " to "
						+ range.get(1).toString("dd MMMM hh:mm a") + "\n"
						+ today(0, 0).toString("dd MMMM hh:mm a") + " to "
						+ today(11, 0).toString("dd MMMM hh:mm a") + "\n"
						+ today(12, 0).toString("dd MMMM hh:mm a") + " to "
						+ today(17, 0).toString("dd MMMM hh:mm a") + "\n"
						+ today(18, 0).toString("dd MMMM hh:mm a") + " to "
						+ today(23, 59).toString("dd MMMM hh:mm a") + "\n",
				outContent.toString());
		outContent.reset();
	}

	@Test
	public void testSearchForFreeIntervalsSmallIntervalOnly() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// almost whole day
		manager.addTask(new TimedTask(0, "timed", false, today(0, 0), today(12,
				0)));
		manager.addTask(new TimedTask(0, "timed", false, today(12, 1), dt2359));
		manager.searchForFreeIntervals(range);
		assertEquals(Constants.MESSAGE_NO_FREE_SLOTS + "\n",
				outContent.toString());
		outContent.reset();

	}

	@Test
	public void testSearchForFreeIntervalsWholeDay() throws Exception {
		DateTime now = new DateTime();
		DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 0, 0, 0);
		DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), 23, 59, 59);
		ArrayList<DateTime> range = new ArrayList<DateTime>();
		range.add(dt0000);
		range.add(dt2359);
		// whole day
		manager.addTask(new TimedTask(0, "timed", false, dt0000, dt2359));
		manager.searchForFreeIntervals(range);
		assertEquals(Constants.MESSAGE_NO_FREE_SLOTS + "\n",
				outContent.toString());
		outContent.reset();

	}

	private String displayTasks(ArrayList<Task> tasks) {

		String str = "";

		if (tasks.isEmpty()) {
			str += "Nothing to display.\n";
			return str;
		}

		for (int i = 0; i < tasks.size(); i++) {
			str += tasks.get(i) + "\n";
		}

		return str;

	}

	private DateTime today(int hours, int minutes) {
		DateTime now = DateTime.now();
		return new DateTime(now.getYear(), now.getMonthOfYear(),
				now.getDayOfMonth(), hours, minutes, 0);
	}
}
