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

}
