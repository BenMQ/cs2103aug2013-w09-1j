package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*;
import java.util.ArrayList;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import org.joda.time.DateTime;
import org.junit.Test;

public class InputParserTest {

	@Test
	public void testParseTwoDates() {
		ArrayList<DateTime> dates = InputParser.parseDateTime("add golf from the day before next thursday to 19 december");
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
	
}
