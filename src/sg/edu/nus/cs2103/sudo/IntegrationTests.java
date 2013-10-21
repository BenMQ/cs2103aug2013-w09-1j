package sg.edu.nus.cs2103.sudo;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.text.Format;
import java.util.ArrayList;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;
import sg.edu.nus.cs2103.sudo.storage.StorageHandler;

public class IntegrationTests {

	/**
	 * Integration Test ideas: 1. Test task adding, check state of all
	 * components: UI, parser, taskmanager, storage 2.
	 * 
	 * 
	 */

	private static final String SAVE_FILENAME = "integration_test.sav";
	private static final String HISTORY_FILENAME = "integration_history.sav";

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	BufferedReader savefile_reader;
	private static StorageHandler storage;
	private static TaskManager manager;
	private static InputParser parser;

	private File savefile;
	private File historyfile;

	@Before
	public void setUp() throws FileNotFoundException {
		savefile = new File(SAVE_FILENAME);
		historyfile = new File(HISTORY_FILENAME);
		storage = StorageHandler.getStorageHandler(SAVE_FILENAME);
		manager = TaskManager.getTaskManager();
		parser = InputParser.getInputParser(manager);
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
				taskDescription);

		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testAddDeadlineTask() throws IOException {
		String userInput = "add 'make waffles for breakfast' by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		String expectedOutput = String.format(Constants.MESSAGE_ADD_DEADLINE,
				taskDescription);

		testCommand(userInput, expectedOutput);
		testStorageContent();
	}

	@Test
	public void testDelete() throws IOException {
		String userInput = "add 'make waffles for breakfast' by Monday 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		testCommand(userInput,
				String.format(Constants.MESSAGE_ADD_DEADLINE, taskDescription));
		String expectedOutput = Constants.MESSAGE_DELETE + taskDescription;

		testCommand("delete 'waffle'", expectedOutput);
		testStorageContent();
	}

	@Test
	public void testEdit() {
		assert true;
	}

	@Test
	public void testSearch() throws IOException {
		String userInput = "add 'submit proposal to tutor at NUS' by 26 October 6pm";
		String taskDescription = InputParser.parseDescription(userInput);
		testCommand(userInput,
				String.format(Constants.MESSAGE_ADD_DEADLINE, taskDescription));

		userInput = "add 'have coffee with mentor in Nus' from 27 Oct 9am to 27 Oct 10am";
		taskDescription = InputParser.parseDescription(userInput);
		testCommand(userInput,
				String.format(Constants.MESSAGE_ADD_TIMED, taskDescription));

		userInput = "search 'nus'";
		taskDescription = InputParser.parseDescription(userInput);
		
	}

	@Test
	public void testSort() {
		assert true;
	}

	// Helper test method to also test console output
	private void testCommand(String userInput, String expectedOutput)
			throws IOException {
		parser.parseCommand(userInput);
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
