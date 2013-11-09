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
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.LogicHandler;
import sg.edu.nus.cs2103.sudo.logic.ParserUtils;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;
import sg.edu.nus.cs2103.ui.DisplayUtils;
import sg.edu.nus.cs2103.ui.UI;

//@author A0099317U
public class IntegrationTestSuite {

	/**
	 * Integration Test ideas: 1. Test task adding, check state of all
	 * components: UI, parser, taskmanager, storage 2. Need to test history
	 * 
	 */

	private static final String SAVE_FILENAME = "integration_test.sav";
	private static final String HISTORY_FILENAME = "integration_history.sav";

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
		storage = StorageHandler.getStorageHandler(SAVE_FILENAME);
		manager = TaskManager.getTaskManager();
		logicHandler = LogicHandler.getLogicHandler(manager, user);
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() throws IOException {
		manager.clearTasks();
		savefile.delete();
	}

	@Test
	public void testAddFloatingTask() throws IOException {
		String userInput = "add 'make waffles for breakfast'";
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_FLOATING,
				taskDescription)
				+ "Remaining tasks:\n[Floating tasks]==========================\n1. make waffles for breakfast ";
		expectedOutput += "Displaying incomplete tasks\n\n[Floating tasks]==========================\n1. make waffles for breakfast \n";

		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testAddDeadlineTask() throws IOException {
		String userInput = "add 'make waffles for breakfast' by Monday 14 October 2pm";

		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput,
		                                                    COMMAND_TYPE.ADD);
		String endTime = dateTimes.get(0).toString("dd MMMM hh:mm a");
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_DEADLINE,
				taskDescription, endTime)
				+ "Remaining tasks:\n[Overdue: Mon 14 Oct]=====================\n1. [by 2PM] make waffles for breakfast ";
		expectedOutput += "Displaying incomplete tasks\n\n[Overdue: Mon 14 Oct]=====================\n1. [by 2PM] make waffles for breakfast \n";
		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testTimedTask() throws IOException {
		String userInput = "add 'make waffles for breakfast' from Monday 14 October 2pm to Wednesday 16 October";
		ArrayList<DateTime> dateTimes =
		    InputParser.parseDateTime(userInput, COMMAND_TYPE.ADD);
		String startTime = dateTimes.get(0).toString("dd MMMM hh:mm a");
		String endTime = dateTimes.get(1).toString("dd MMMM hh:mm a");
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_TIMED,
				taskDescription, startTime, endTime)
				+ "Remaining tasks:\n[Overdue: Mon 14 Oct]=====================\n1. [2PM - Wed 16 Oct 2PM] make waffles for breakfast ";
		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testValidAliases() throws IOException {
		String userInput = "do 'make waffles for breakfast' by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		runCommand(userInput);

		String expectedOutput = String.format(Constants.MESSAGE_DELETE
				+ Constants.MESSAGE_EMPTY_LIST, taskDescription);
		testCommand("remove 'waffle'", expectedOutput);

		testStorageContent();
	}

	@Test
	public void testInvalidAliases() throws IOException {
		String userInput = "bamboozle 'make waffles for breakfast' by Monday 14 October 2pm";
		testCommand(userInput, Constants.MESSAGE_INVALID_COMMAND);
	}

	@Test
	public void testDelete() throws IOException {
		String userInput = "add 'make waffles for breakfast' by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		runCommand(userInput);

		String expectedOutput = String.format(Constants.MESSAGE_DELETE
				+ Constants.MESSAGE_EMPTY_LIST, taskDescription);

		testCommand("delete 'waffle'\n", expectedOutput);
		testStorageContent();
	}

	@Test
	public void testPrettyPrint() throws IOException {
		String userInput = "add 'make waffles for lunch' from 14 October 10am to 14 October 2pm";
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
		String userInput = "add 'make waffles for lunch' from 12 December 10am to 2pm";
		runCommand(userInput);

		userInput = "edit 1 from 8am to 12pm";
		runCommand(userInput);

		Task task = IntegrationTestSuite.manager.getTasks().get(0);
		assertEquals("1. [8AM - 12PM] make waffles for lunch",
				DisplayUtils.prettyPrint(task));
	}

	@Test
	public void testSearch() throws IOException {
		String userInput = "add 'submit proposal to tutor at NUS' by 26 October 6pm";
		runCommand(userInput);

		userInput = "add 'have coffee with mentor in Nus' from 27 Oct 9am to 27 Oct 10am";
		runCommand(userInput);

		userInput = "search 'nus'";
		String searchTerm = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_SEARCH,
				searchTerm);

		testCommand(
				userInput,
				expectedOutput
						+ "\nSearch Results\n1. submit proposal to tutor at NUS by Sat 26 October 06:00 PM\n2. have coffee with mentor in Nus from Sun 27 October 09:00 AM to Sun 27 October 10:00 AM");
	}

	@Test
	public void testSort() {
		assert true;
	}

	@Test
	public void testFinishTask() throws IOException {
		String userInput = "finish 1";
		testCommand(userInput, Constants.MESSAGE_INVALID_TASK_INDEX);

		userInput = "add 'make waffles for breakfast' by tomorrow 2pm";
		InputParser.parseDescription(userInput);
		runCommand(userInput);

		userInput = "finish 1";
		testCommand(userInput, "Finished task: make waffles for breakfast\n"
				+ "Remaining tasks:");

		userInput = "finish 1";
		testCommand(userInput, Constants.MESSAGE_ALREADY_COMPLETE);
	}

	@Test
	public void testUnfinishTask() throws IOException {
		String userInput = "unfinish 1";
		testCommand(userInput, Constants.MESSAGE_INVALID_TASK_INDEX);

		userInput = "add 'make waffles for breakfast' by tomorrow 2pm";
		InputParser.parseDescription(userInput);
		runCommand(userInput);
		userInput = "finish 1";
		runCommand(userInput);

		userInput = "unfinish 1";
		testCommand(userInput, "Un-Finished task: make waffles for breakfast\n"
				+ "Remaining tasks:\n"
				+ "[Tomorrow: Sun 10 Nov]==============================\n"
				+ "1. [by 2PM] make waffles for breakfast ");
	}

	@Test
	public void testDisplayAll() throws IOException {
		String userInput = "add 'make waffles' by tomorrow 9pm"; 
		runCommand(userInput);
		userInput = "add 'do assignment for 2103' from today 7pm to 9pm";
		runCommand(userInput);
		userInput = "finish 1";
		runCommand(userInput);
				
		userInput = "all";
		testCommand(userInput, "Displaying all tasks\n\n" +
				"[Tomorrow: Sun 10 Nov]==============================\n" +
				"1. [by 9PM] make waffles \n\n" +
				"[Finished tasks]====================================\n" +
				"2. [7PM - 9PM] do assignment for 2103 Done!");
	}

	@Test
	public void testEmptyDisplay() throws IOException {
		String userInput = "display";
		testCommand(userInput, Constants.MESSAGE_EMPTY_LIST);
	}

	// Helper test method for operations not directly being tested
	public void runCommand(String userInput) {
		logicHandler.executeCommand(userInput);
		outContent.reset();
	}

	// Helper test method to also test console output
	private void testCommand(String userInput, String expectedOutput)
			throws IOException {
		logicHandler.executeCommand(userInput);
		assertEquals(expectedOutput, outContent.toString());
		outContent.reset();
	}

	// Helper test method to automatically test storage content
	public void testStorageContent() throws FileNotFoundException, IOException {
		savefile_reader = new BufferedReader(new FileReader(SAVE_FILENAME));
		for (Task task : manager.getTasks()) {
			String taskString = task.toStringForFile();
			assertEquals(savefile_reader.readLine(), taskString);
		}
		savefile_reader.close();
	}

}
