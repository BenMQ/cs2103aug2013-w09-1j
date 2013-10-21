package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.cs2103.sudo.storage.StorageHandler;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * @author Ipsita Mohapatra A0101286N
 * 
 */
public class TaskManagerTest {
	private static final String SAVE_FILENAME = "integration_test.sav";
	private static final String HISTORY_FILENAME = "integration_history.sav";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	BufferedReader savefile_reader;
	private static TaskManager manager;
	private File savefile;

	@Before
	public void setUp() throws FileNotFoundException {
		savefile = new File(SAVE_FILENAME);
		new File(HISTORY_FILENAME);
		StorageHandler.getStorageHandler(SAVE_FILENAME);
		manager = TaskManager.getTaskManager();
		InputParser.getInputParser(manager);
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

	// TODO
	@Test
	public void testEditTasks() throws Exception {
		/*
		 * tasks = manager.editTask(1, new
		 * FloatingTask("learn how to fish with dad"));
		 * assertEquals("1. learn how to fish with dad\n", displayTasks(tasks));
		 * 
		 * tasks = manager.addTask(new FloatingTask("resume pilates")); tasks =
		 * manager.addTask(new FloatingTask("start salsa lessons")); tasks =
		 * manager.editTask(2, new
		 * FloatingTask("resume pilates classes in NUS")); tasks =
		 * manager.editTask(3, new FloatingTask("start salsa lessons in Nus"));
		 * 
		 * assertEquals(
		 * "1. learn how to fish with dad\n2. resume pilates classes in NUS\n3. start salsa lessons in Nus\n"
		 * , displayTasks(tasks));
		 */

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

	}
	/**
	 * Tests getOccupiedIntervals
	 * @throws Exception
	 * @author chenminqi
	 */
	@Test
	public void testGetOccupiedIntervals() throws Exception {
        DateTime now = new DateTime();
        DateTime dt0000 = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 0, 0, 0);
        DateTime dt2359 = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), 23, 59, 59);
        
        ArrayList<MutableInterval> actual;
        ArrayList<MutableInterval> expected;
        // Floating and Deadline tasks should be ignored 
	    manager.addTask(new FloatingTask("floating task1"));
	    manager.addTask(new DeadlineTask(0, "deadline", false, now));
	    actual = manager.getOccupiedIntervals();
	    expected = new ArrayList<MutableInterval>();
	    expected.add(new MutableInterval(dt2359, dt2359));
	    assertEquals(expected, actual);
	    
	    
	    // Test task that is at the middle of the day
        manager.addTask(new TimedTask(0, "timed", false, today(8, 0), today(10, 0)));
        actual = manager.getOccupiedIntervals();
        expected = new ArrayList<MutableInterval>();
        expected.add(new MutableInterval(today(8, 0), today(10, 0)));
        expected.add(new MutableInterval(dt2359, dt2359));
        assertEquals(expected, actual);
        
        // boundary case for overlapping tasks
        manager.addTask(new TimedTask(0, "timed", false, today(9, 0), today(11, 0)));
        actual = manager.getOccupiedIntervals();
        expected = new ArrayList<MutableInterval>();
        expected.add(new MutableInterval(today(8, 0), today(11, 0)));
        expected.add(new MutableInterval(dt2359, dt2359));
        assertEquals(expected, actual);
        
        // boundary case for abut tasks
        manager.addTask(new TimedTask(0, "timed", false, today(11, 0), today(12, 0)));
        actual = manager.getOccupiedIntervals();
        expected = new ArrayList<MutableInterval>();
        expected.add(new MutableInterval(today(8, 0), today(12, 0)));
        expected.add(new MutableInterval(dt2359, dt2359));
        assertEquals(expected, actual);
        
        // boundary case for gap between tasks
	    
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
        return new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), hours, minutes, 0);
	}
}
