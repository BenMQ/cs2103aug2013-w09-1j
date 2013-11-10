package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.InputParser;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//@author A0099317U
public class InputParserTest {
	private final ByteArrayOutputStream outContent = 
			new ByteArrayOutputStream();
	Scanner user = new Scanner(System.in);
	TaskManager manager;
	LogicHandler logicHandler;
	InputParser parser;
	private static final String SAVE_FILENAME = "inputParser_test.sav";
	private static final String HISTORY_FILENAME = "inputParser_test.his";
	private File savefile;
	private File historyfile;

	@Before
	public void setUp() {
		savefile = new File(SAVE_FILENAME);
		historyfile = new File(HISTORY_FILENAME);
		manager = TaskManager.getTaskManager(SAVE_FILENAME, HISTORY_FILENAME);
		logicHandler = LogicHandler.getLogicHandler(manager, user);
		System.setOut(new PrintStream(outContent));
    }

	@After
    public void tearDown() {
        manager.clearTasks();
        savefile.delete();
		historyfile.delete();
    }	
	
	@Test
	public void testParseEmptyDates() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add " +
				"'finish homework'", COMMAND_TYPE.ADD);
		assertEquals(0, dates.size());
	}
	
	@Test
	public void testParseDates() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add 'golf' " +
				"from the day before next thursday to 19 december", 
				COMMAND_TYPE.ADD);
		assertEquals(2, dates.size());
	}

	@Test
	public void testParseDescription(){
	    String[] testcases = new String[] {
	        "'sudo' is an amazing app",
		    "sudo is an amazing 'app'",
		    "sudo is an 'amazing' app",
	    };	
	    
	    assertEquals("sudo", InputParser.parseDescription(testcases[0]));
	    assertEquals("app", InputParser.parseDescription(testcases[1]));
	    assertEquals("amazing", InputParser.parseDescription(testcases[2]));
	}

	@Test
	public void testNullDescription() throws IOException{
		String userInput = "add from 19 October 2013 to 22 October 2014";
		String description = InputParser.parseDescription(userInput);
		
		assertEquals(description, null);
	}	
	
	@Test
	public void testEmptyStringDescription() throws IOException{
		String userInput = "add '' from 19 October 2013 to 22 October 2014";
		String description = InputParser.parseDescription(userInput);
		
		assertEquals(description, null);
	}		
	
	@Test
	public void testParseId(){
		int actual = InputParser.parseId("edit 2 'make pancakes " +
				"for breakfast'");
		assertEquals(2, actual);
	}
	
	@Test
	public void testParseIdNotFound(){	
		int actual = InputParser.parseId("edit 'make waffles " +
				"for breakfast'");
		assertEquals(InputParser.NOT_FOUND, actual);
	}
	
	@Test
	public void testIncompleteCommands() throws IOException{
		testCommand("add", Constants.MESSAGE_INCOMPLETE_COMMAND);
		testCommand("search", Constants.MESSAGE_INCOMPLETE_COMMAND);
		testCommand("edit 1", Constants.MESSAGE_INCOMPLETE_COMMAND);
		testCommand("schedule 1", Constants.MESSAGE_INCOMPLETE_COMMAND);
	}
	
	@Test
	public void testInvalidCommands() throws IOException{
		testCommand("fishing", Constants.MESSAGE_INVALID_COMMAND);
	}

	@Test
	public void testParseAddFloatingTask() throws IOException{
		String userInput = "add 'make waffles for breakfast'";
		runCommand(userInput);
		
		assertEquals(1, manager.getTasks().size());
		assertTrue(manager.getTasks().get(0).isFloatingTask());
	}

	@Test
	public void testParseAddDeadlineTask() throws IOException{
		String userInput = "add 'make waffles for breakfast' by Monday" +
				" 14 October 2pm";
		String expectedEndDate = "Mon Oct 14 14:00:00 SGT 2013";
		runCommand(userInput);
		
		assertEquals(expectedEndDate, 
				manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
		assertTrue(manager.getTasks().get(0).isDeadlineTask());
	}

	@Test
	public void testParseAddTimedTask() throws IOException{
		String userInput = "add 'make waffles for breakfast' from 13 October" +
				" to 14 October 2pm";
		String expectedStartDate = "Sun Oct 13 14:00:00 SGT 2013";
		String expectedEndDate = "Mon Oct 14 14:00:00 SGT 2013";
		runCommand(userInput);
		
		assertEquals(expectedStartDate, 
				manager.getTasks().get(0).startTime.toDate().toString());
		assertEquals(expectedEndDate, 
				manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
		assertTrue(manager.getTasks().get(0).isTimedTask());
	}	
	
	@Test
	public void testParseAddNullDescription() throws IOException{
		String userInput = "add nothing";
		testCommand(userInput, Constants.MESSAGE_MISSING_DESCRIPTION + 
				Constants.MESSAGE_EMPTY_LIST);
		
		assertEquals(0, manager.getTasks().size());
	}	
	
	@Test
	public void testParseAddInvalidNumberOfDates() throws IOException{
		String userInput = "add 'time travel' from 19 October 2013 to 22 " +
				"October 2014 to 24 November 2016";
		runCommand(userInput);
		
		assertEquals(0, manager.getTasks().size());
	}
	
	/**
	 * Helper test method for operations not currently being tested.
	 * @param String
	 */
	public void runCommand(String userInput) {
		logicHandler.executeCommand(userInput);
		outContent.reset();
	}	
	
	/**
	 * Helper test method to test logic correctness and console output.
	 * @param String
	 * @param String
	 */	
	private void testCommand(String userInput, String expectedOutput) 
			throws IOException{		
		logicHandler.executeCommand(userInput);
		assertEquals(expectedOutput,outContent.toString());
		outContent.reset();
	}
	
}
