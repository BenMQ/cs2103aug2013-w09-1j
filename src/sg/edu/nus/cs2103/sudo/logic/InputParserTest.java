package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import org.joda.time.DateTime;
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
	
	@Test
	public void testParseOneDate() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add 'finish homework' by 29 december");
		assertEquals(dates.size(), 1);
	}
	
	@Test
	public void testParseTwoDates() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add 'golf' from the day before next thursday to 19 december");
		assertEquals(dates.size(), 2);
	}

	@Test
	public void testParseDescription(){
	    String[] testcases = new String[] {
	        "'sudo' is an amazing app",
		    "sudo is an amazing 'app'",
		    "sudo is an 'amazing' app",
	    };	
	    
	    assertEquals(InputParser.parseDescription(testcases[0]),"sudo");
	    assertEquals(InputParser.parseDescription(testcases[1]), "app");
	    assertEquals(InputParser.parseDescription(testcases[2]), "amazing");
	}
	
	@Test
	public void testIncompleteCommand() throws IOException{
		testCommand("add", Constants.MESSAGE_INCOMPLETE_COMMAND);
	}
	
	@Test
	public void testInvalidCommand() throws IOException{
		testCommand("fishing", Constants.MESSAGE_INVALID_COMMAND);
	}

	// Helper test method to test console output
	private void testCommand(String userInput, String expectedOutput) throws IOException{		
		parser.executeCommand(userInput);
		assertEquals(outContent.toString(), expectedOutput);
		outContent.reset();
	}
	
}
