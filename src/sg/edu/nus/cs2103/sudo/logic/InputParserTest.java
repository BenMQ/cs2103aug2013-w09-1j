package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
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
	
	@Before
	public void setUp() {
		manager = TaskManager.getTaskManager();
		logicHandler = LogicHandler.getLogicHandler(manager, user);
		System.setOut(new PrintStream(outContent));
    }
	
	@After
    public void tearDown() {
        manager.clearTasks();
    }	
	
	@Test
	public void testParseStringDate(){
		ArrayList<DateTime> dates = InputParser.parseDateTime("fake " +
				"'fake' 2013-10-14T14:00 to 2013-10-14T16:00", 
				COMMAND_TYPE.INVALID);
		assertEquals(2, dates.size());		
	}
	
	@Test
	public void testParseOneDate() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add " +
				"'finish homework' by 29 december", COMMAND_TYPE.ADD);
		assertEquals(1, dates.size());
	}
	
	@Test
	public void testParseTwoDates() {
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
	public void testParseId(){
		assertEquals(2, InputParser.parseId("edit 2 'make pancakes " +
				"for breakfast'"));
	}	
	
	@Test
	public void testParseIdNotFound(){	
		assertEquals(InputParser.NOT_FOUND, InputParser.parseId("add 'make waffles " +
				"for breakfast'"));
	}	
	
	@Test
	public void testIncompleteCommand() throws IOException{
		testCommand("add", Constants.MESSAGE_INCOMPLETE_COMMAND);
	}
	
	@Test
	public void testInvalidCommand() throws IOException{
		testCommand("fishing", Constants.MESSAGE_INVALID_COMMAND);
	}

	@Test
	public void testParseAddFloatingTask() throws IOException{
		String userInput = "add 'make waffles for breakfast'";
		String taskDescription = InputParser.parseDescription(userInput);
		runCommand(userInput);
		assertEquals(1, manager.getTasks().size());
	}

	@Test
	public void testParseAddDeadlineTask() throws IOException{
		String userInput = "add 'make waffles for breakfast' by Monday" +
				" 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput, 
				COMMAND_TYPE.ADD);
		String endTime = dateTimes.get(0).toString(
				"EEE dd MMMM hh:mm a");
		String expectedOutput = String.format(Constants.MESSAGE_ADD_DEADLINE, 
				taskDescription, endTime);
		runCommand(userInput);
		assertEquals("Mon Oct 14 14:00:00 SGT 2013", 
				manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
	}

	@Test
	public void testParseAddTimedTask() throws IOException{
		String userInput = "add 'make waffles for breakfast' from 13 October" +
				" to 14 October 2pm";
		String taskDescription = InputParser.parseDescription(userInput);
		ArrayList<DateTime> dateTimes = InputParser.parseDateTime(userInput, 
				COMMAND_TYPE.ADD);
		String startTime = dateTimes.get(0).toString(
				"EEE dd MMMM hh:mm a");
		String endTime = dateTimes.get(1).toString(
				"EEE dd MMMM hh:mm a");
		String expectedOutput = String.format(Constants.MESSAGE_ADD_TIMED, 
				taskDescription, startTime, endTime);
		runCommand(userInput);
		
		assertEquals("Sun Oct 13 14:00:00 SGT 2013", 
				manager.getTasks().get(0).startTime.toDate().toString());
		assertEquals("Mon Oct 14 14:00:00 SGT 2013", 
				manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
	}	
	
	@Test
	public void testParseAddMissingDescription() throws IOException{
		String userInput = "add nothing";
		testCommand(userInput, Constants.MESSAGE_MISSING_DESCRIPTION + Constants.MESSAGE_EMPTY_LIST);
		assertEquals(0, manager.getTasks().size());
	}
	
	@Test
	public void testEmptyStringDescription() throws IOException{
		String userInput = "add '' from 19 October 2013 to 22 October 2014";
		runCommand(userInput);
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
