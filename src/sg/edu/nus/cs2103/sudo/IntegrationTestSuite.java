package sg.edu.nus.cs2103.sudo;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;
import sg.edu.nus.cs2103.ui.DisplayUtils;

//@author A0099317U
public class IntegrationTestSuite {

	private static final String SAVE_FILENAME = "integration_test.sav";
	private static final String HISTORY_FILENAME = "integration_test.his";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	BufferedReader savefile_reader;
	Scanner user = new Scanner(System.in);
	private static StorageHandler storage;
	private static LogicHandler logicHandler;
	private static TaskManager manager;

	private File savefile;
	private File historyfile;

	@Before
	public void setUp() throws FileNotFoundException {
		
		savefile = new File(SAVE_FILENAME);
		historyfile = new File(HISTORY_FILENAME);
		storage = StorageHandler.getStorageHandler(SAVE_FILENAME, HISTORY_FILENAME);
		manager = TaskManager.getTaskManager(SAVE_FILENAME, HISTORY_FILENAME);
		logicHandler = LogicHandler.getLogicHandler(manager, user);
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws IOException {
		manager.clearTasks();
		savefile.delete();
		historyfile.delete();
	}

	@Test
	public void testAddFloatingTask() throws IOException {
		String userInput = "add 'make waffles for breakfast'";
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_FLOATING,
				taskDescription)
				+ "Remaining tasks:\n"
				+ Constants.FLOATING_TASK_SEPARATOR
				+ "\n1. make waffles for breakfast ";

		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testAddDeadlineTask() throws IOException {
		String userInput = "add 'make waffles for breakfast' "
				+ "by Monday 14 October 2pm";

		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput,
				COMMAND_TYPE.ADD);
		String endTime = dateTimes.get(0).toString("dd MMMM hh:mm a");
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_DEADLINE,
				taskDescription, endTime);
		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testTimedTask() throws IOException {
		String userInput = "add 'make waffles for breakfast' "
				+ "from Monday 14 October 2pm to Wednesday 16 October";
		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput,
				COMMAND_TYPE.ADD);
		String startTime = dateTimes.get(0).toString("dd MMMM hh:mm a");
		String endTime = dateTimes.get(1).toString("dd MMMM hh:mm a");
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_TIMED,
				taskDescription, startTime, endTime);
		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testValidAliases() throws IOException {
		String userInput = "do 'make waffles for breakfast' "
				+ "by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		runCommand(userInput);

		String expectedOutput = String.format(Constants.MESSAGE_DELETE
				+ Constants.MESSAGE_EMPTY_LIST, taskDescription);
		testCommand("remove 'waffle'", expectedOutput);

		testStorageContent();
	}

	@Test
	public void testInvalidAliases() throws IOException {
		String userInput = "bamboozle 'make waffles for breakfast'"
				+ " by Monday 14 October 2pm";
		testCommand(userInput, Constants.MESSAGE_INVALID_COMMAND);
	}

	@Test
	public void testDelete() throws IOException {
		String userInput = "add 'make waffles for breakfast' "
				+ "by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		runCommand(userInput);

		String expectedOutput = String.format(Constants.MESSAGE_DELETE
				+ Constants.MESSAGE_EMPTY_LIST, taskDescription);

		testCommand("delete 'waffle'\n", expectedOutput);
		testStorageContent();
	}

	@Test
	public void testPrettyPrint() throws IOException {
		String userInput = "add 'make waffles for lunch' "
				+ "from 14 October 10am to 14 October 2pm";
		runCommand(userInput);

		userInput = "add 'make cake for breakfast' by 14 October 9am";
		runCommand(userInput);

		Task deadlineTask = IntegrationTestSuite.manager.getTasks().get(0);
		Task timedTask = IntegrationTestSuite.manager.getTasks().get(1);

		assertEquals("1. [by 9AM] make cake for breakfast",
				DisplayUtils.prettyPrint(deadlineTask));
		assertEquals("2. [10AM - 2PM] make waffles for lunch",
				DisplayUtils.prettyPrint(timedTask));
	}

	@Test
	public void testEdit() {
		String userInput = "add 'make waffles for lunch' "
				+ "from 12 December 10am to 2pm";
		runCommand(userInput);

		userInput = "edit 1 from 8am to 12pm";
		runCommand(userInput);

		Task task = IntegrationTestSuite.manager.getTasks().get(0);
		assertEquals("1. [8AM - 12PM] make waffles for lunch",
				DisplayUtils.prettyPrint(task));
	}

	//@author A0101286N
	@Test
	public void testValidSearch() throws IOException {
		prepareTaskListForTestSearch();

		String userInput = "search 'nus'";
		String expectedOutput = "\nSearch Results\n"
				+ "1. [by 6PM] submit proposal to tutor at NUS \n"
				+ "2. [9AM - 10AM] have coffee with mentor in Nus \n\n";
		testCommand(userInput, expectedOutput);
	}

	@Test
	public void testEmptyStringSearch() throws IOException {
		prepareTaskListForTestSearch();

		// boundary case: invalid search
		String userInput = "search ''";
		testCommand(userInput, "Search is invalid.\n");
	}

	@Test
	public void testNoSearchResults() throws IOException {
		prepareTaskListForTestSearch();

		// boundary case: search with no search results
		String userInput = "search 'fishes'";
		testCommand(userInput, "No search results found.\n");
	}

	private void prepareTaskListForTestSearch() {
		String userInput = "add 'submit proposal to tutor at NUS' "
				+ "by 26 October 6pm";
		runCommand(userInput);

		userInput = "add 'have coffee with mentor in Nus' from "
				+ "27 Oct 9am to 27 Oct 10am";
		runCommand(userInput);
	}

	@Test
	public void testSort() throws IOException {
		String userInput = "add 'make dinner' by 10 November 2015 5pm";
		runCommand(userInput);

		userInput = "add 'make lunch' by 10 November 2015 11am";
		runCommand(userInput);

		// equivalence partitioning: sorting by date
		userInput = "all";
		testCommand(userInput, "Displaying all tasks\n\n\n"
				+ "[Tue 10 Nov 2015]===================================\n"
				+ "1. [by 11AM] make lunch \n" + "2. [by 5PM] make dinner ");

		userInput = "finish 1";
		runCommand(userInput);

		// equivalence partitioning: sorting by incomplete tasks
		userInput = "all";
		testCommand(userInput, "Displaying all tasks\n\n\n"
				+ "[Tue 10 Nov 2015]===================================\n"
				+ "1. [by 5PM] make dinner \n\n"
				+ "[Finished tasks]====================================\n"
				+ "2. [by 11AM] make lunch Done!");
	}

	@Test
	public void testInvalidFinish() throws IOException {
		// boundary case: finish task which does not exist
		String userInput = "finish 1";
		finishInvalidTaskId(userInput);
		userInput = "finish 0";
		finishInvalidTaskId(userInput);
		userInput = "finish -1";
		finishInvalidTaskId(userInput);
	}

	@Test
	public void testValidFinish() throws IOException {
		prepareTaskListForTestFinish();

		String userInput = "finish 1";
		finishValidTask(userInput);

	}
	
	@Test
	public void testInvalidFinishCompletedTask() throws IOException {
		prepareTaskListForTestFinish();
			
		String userInput = "finish 1";
		runCommand(userInput);
		
		// boundary case: finish a completed task
		userInput = "finish 1";
		finishFinishedTask(userInput);
	}
	
	private void finishFinishedTask(String userInput) throws IOException {
		testCommand(userInput, Constants.MESSAGE_ALREADY_COMPLETE);
	}

	private void finishValidTask(String userInput) throws IOException {
		testCommand(userInput, "Finished task: make waffles for breakfast\n"
				+ "Remaining tasks:\n");
	}

	private void prepareTaskListForTestFinish() {
		String userInput;
		userInput = "add 'make waffles for breakfast' by 29 Nov 2pm";
		InputParser.parseDescription(userInput);
		runCommand(userInput);
		
	}

	private void prepareTaskListForTestUnfinish() {
		prepareTaskListForTestFinish();
		String userInput = "finish 1";
		runCommand(userInput);
	}

	private void finishInvalidTaskId(String userInput) throws IOException {
		testCommand(userInput, Constants.MESSAGE_INVALID_TASK_INDEX);
	}

	private void unfinishInvalidTaskId(String userInput) throws IOException {
		finishInvalidTaskId(userInput);

	}

	@Test
	public void testInvalidUnfinish() throws IOException {
		// boundary case: unfinish task which does not exist
		String userInput = "unfinish 1";
		unfinishInvalidTaskId(userInput);
	}
	
	@Test 
	public void testValidUnfinish() throws IOException {
		prepareTaskListForTestUnfinish();

		String userInput = "unfinish 1";
		String expectedOutput = "Un-Finished task: make waffles for breakfast\n"
				+ "Remaining tasks:\n\n\n"
				+ "[Fri 29 Nov]========================================\n"
				+ "1. [by 2PM] make waffles for breakfast ";
		testCommand(userInput, expectedOutput);
	}


	@Test
	public void testDisplayAll() throws IOException {
		String userInput;
		prepareTaskListForTestDisplayAll();

		userInput = "all";
		displayIncompleteAndCompleteTasks(userInput);
	}

	private void displayIncompleteAndCompleteTasks(String userInput)
			throws IOException {
		testCommand(userInput, "Displaying all tasks\n\n\n"
				+ "[Sat 30 Nov]========================================\n"
				+ "1. [7PM - 9PM] do assignment for 2103 \n\n"
				+ "[Finished tasks]====================================\n"
				+ "2. [by 9PM] make waffles Done!");
	}

	private void prepareTaskListForTestDisplayAll() {
		String userInput = "add 'make waffles' by 29 nov 9pm";
		runCommand(userInput);
		userInput = "add 'do assignment for 2103' from 30 nov 7pm to 9pm";
		runCommand(userInput);
		userInput = "finish 1";
		runCommand(userInput);
	}

	@Test
	public void testEmptyDisplay() throws IOException {
		// boundary case: display when task list is empty
		String userInput = "display";
		testCommand(userInput, Constants.MESSAGE_EMPTY_LIST);
	}

	//@author A0099317U
	/**
	 * Helper test method for operations not currently being tested.
	 * 
	 * @param String
	 */
	public void runCommand(String userInput) {
		logicHandler.executeCommand(userInput);
		outContent.reset();
	}

	/**
	 * Helper test method to test logic correctness and console output.
	 * 
	 * @param String
	 * @param String
	 */
	private void testCommand(String userInput, String expectedOutput)
			throws IOException {
		logicHandler.executeCommand(userInput);
		assertTrue(outContent.toString().contains(expectedOutput));
		outContent.reset();
	}

	/**
	 * Helper test method to test storage content.
	 * 
	 * @param String
	 */
	public void testStorageContent() throws FileNotFoundException, IOException {
		savefile_reader = new BufferedReader(new FileReader(SAVE_FILENAME));
		for (Task task : manager.getTasks()) {
			String taskString = task.toStringForFile();
			assertEquals(savefile_reader.readLine(), taskString);
		}
		savefile_reader.close();
	}

}
