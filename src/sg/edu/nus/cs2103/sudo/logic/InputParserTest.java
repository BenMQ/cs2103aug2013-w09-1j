package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.logging.Level;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InputParserTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	TaskManager manager;
	InputParser parser;
	
	@Before
	public void setUp() {
		manager = TaskManager.getTaskManager();
		parser = InputParser.getInputParser(manager);
		System.setOut(new PrintStream(outContent));
    }
	
	@After
    public void tearDown() {
        manager.clearTasks();
    }	
	
	@Test
	public void testParseStringDate(){
		ArrayList<DateTime> dates = InputParser.parseDateTime("fake 'fake' 2013-10-14T14:00 to 2013-10-14T16:00");
		assertEquals(2, dates.size());		
	}
	
	@Test
	public void testParseOneDate() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add 'finish homework' by 29 december");
		assertEquals(1, dates.size());
	}
	
	@Test
	public void testParseTwoDates() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add 'golf' from the day before next thursday to 19 december");
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
		assertEquals(2, InputParser.parseId("edit 2 'make pancakes for breakfast'"));
	}	
	
	@Test
	public void testParseIdNotFound(){	
		assertEquals(-1, InputParser.parseId("add 'make waffles for breakfast'"));
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
		testCommand("add 'make waffles for breakfast'", "Add floating task: make waffles for breakfast\n");
		assertEquals(1, manager.getTasks().size());
	}

	@Test
	public void testParseAddDeadlineTask() throws IOException{
		testCommand("add 'make waffles for breakfast' by Monday 14 October 2pm", "Add deadline task: make waffles for breakfast\n");
		assertEquals("Mon Oct 14 14:00:00 SGT 2013", manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
	}

	@Test
	public void testParseAddTimedTask() throws IOException{
		testCommand("add 'make waffles for breakfast' from 13 October to 14 October 2pm", "Add timed task: make waffles for breakfast\n");
		assertEquals("Sun Oct 13 14:00:00 SGT 2013", manager.getTasks().get(0).startTime.toDate().toString());
		assertEquals("Mon Oct 14 14:00:00 SGT 2013", manager.getTasks().get(0).endTime.toDate().toString());
		assertEquals(1, manager.getTasks().size());
	}	
	
	@Test
	public void testParseAddMissingDescription() throws IOException{
		testCommand("add nothing", Constants.MESSAGE_MISSING_DESCRIPTION);
		assertEquals(0, manager.getTasks().size());
	}	

	@Test
	public void testParseAddInvalidNumberOfDates() throws IOException{
		testCommand("add 'time travel' from 19 October 2013 to 22 October 2014 to 24 November 2016", Constants.MESSAGE_INVALID_NUMBER_OF_DATES);
		assertEquals(0, manager.getTasks().size());
	}		
	
	// Helper test method to test console output
	private void testCommand(String userInput, String expectedOutput) throws IOException{		
		parser.executeCommand(userInput);
		assertEquals(outContent.toString(), expectedOutput);
		outContent.reset();
	}
	
}
