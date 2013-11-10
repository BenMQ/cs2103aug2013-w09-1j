package sg.edu.nus.cs2103.sudo.storage;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.DeadlineTask;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TimedTask;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageHandlerTest {

	private static ArrayList<Task> tasks = new ArrayList<Task>();
	private static StorageHandler storage;
	private static final String TEST_NAME = "Test.sav";
	
	//private static final String HISTORY_NAME = "Test.his";
	private File file;
	private File historyFile;
	
	private final static String userInput1 = "add 'Sleeping' from october 14th 1am to october 14th 7am";
	private final static String userInput2 = "add 'Kanji Homework' by october 14th";
	private final static String userInput3 = "add 'CS2101' from october 14th 10am to 12pm";
	private final static String userInput4 = "add 'CS2100 Tutorial' from october 11th 12pm to october 14th 1 pm";
	private final static String userInput5 = "add 'Japanese Tutorial A' from october 14th 2pm to october 14th 4pm";
	private final static String userInput6 = "add 'Have Dinner With A Friend'";
	private final static String userInput7 = "add 'Study' by october 14th 11pm";
	

	@Before
	public void setUp() {
		file = new File(TEST_NAME);
		historyFile = new File(Constants.HISTORY_NAME);
    }
	
	@After
    public void tearDown() {
		file.delete();
		historyFile.delete();
		tasks.clear();
    }
	
	
	@Test
	public void initializationTest() throws Exception {
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
	}
	
	@Test
	public void writeTest() throws Exception {
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		String taskDescription = InputParser.parseDescription(userInput1);
		ArrayList<DateTime> dates = InputParser.parseDateTime(userInput1, COMMAND_TYPE.ADD);
		TimedTask timed = new TimedTask(taskDescription, dates);
		tasks.add(timed);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput2);
		//dates = InputParser.parseDateTime(userInput2);
		FloatingTask dead = new FloatingTask(taskDescription);
		tasks.add(dead);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput3);
		dates = InputParser.parseDateTime(userInput3, COMMAND_TYPE.ADD);
		TimedTask timed2 = new TimedTask(taskDescription, dates);
		tasks.add(timed2);
		storage.save(true);
		StorageHandler.resetStorageHandler();
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
		BufferedReader iptBuff = new BufferedReader(new FileReader(
				TEST_NAME));
		String temp = iptBuff.readLine();
		assertEquals("TIMED#Sleeping#2013-10-14T01:00 to 2013-10-14T07:00#false", temp);
		temp = iptBuff.readLine();
		//assertEquals("DEADLINE#Kanji Homework#2013-10-18T10:00#false", temp);
		assertEquals("floating#Kanji Homework#false", temp);
		temp = iptBuff.readLine();
		assertEquals("TIMED#CS2101#2013-10-18T10:00 to 2013-10-18T12:00#false", temp);
		iptBuff.close();
		
	}
	
	@Test
	public void readTest() throws Exception {
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		String taskDescription = InputParser.parseDescription(userInput4);
		ArrayList<DateTime> dates = InputParser.parseDateTime(userInput4, COMMAND_TYPE.ADD);
		TimedTask timed = new TimedTask(taskDescription, dates);
		tasks.add(timed);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput5);
		dates = InputParser.parseDateTime(userInput5, COMMAND_TYPE.ADD);
		TimedTask timed2 = new TimedTask(taskDescription, dates);
		tasks.add(timed2);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput6);
		FloatingTask floating = new FloatingTask(taskDescription);
		tasks.add(floating);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput7);
		dates = InputParser.parseDateTime(userInput7, COMMAND_TYPE.ADD);
		DeadlineTask dead = new DeadlineTask(taskDescription, dates);
		tasks.add(dead);
		storage.save(true);
		StorageHandler.resetStorageHandler();
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
		
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		assertEquals("TIMED#CS2100 Tutorial#2013-10-18T12:00 to 2013-10-14T13:00#false", tasks.get(0).toStringForFile());
		assertEquals("TIMED#Japanese Tutorial A#2013-10-14T14:00 to 2013-10-14T16:00#false", tasks.get(1).toStringForFile());
		assertEquals("floating#Have Dinner With A Friend#false", tasks.get(2).toStringForFile());
		assertEquals("DEADLINE#Study#2013-10-18T23:00#false", tasks.get(3).toStringForFile());
	}
	
	@Test
	public void historyTest() throws Exception {
		storage.resetStorageHandler();
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		String taskDescription = InputParser.parseDescription(userInput4);
		ArrayList<DateTime> dates = InputParser.parseDateTime(userInput4, COMMAND_TYPE.ADD);
		TimedTask timed = new TimedTask(taskDescription, dates);
		tasks.add(timed);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput5);
		dates = InputParser.parseDateTime(userInput5, COMMAND_TYPE.ADD);
		TimedTask timed2 = new TimedTask(taskDescription, dates);
		tasks.add(timed2);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput6);
		FloatingTask floating = new FloatingTask(taskDescription);
		tasks.add(floating);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput7);
		dates = InputParser.parseDateTime(userInput7, COMMAND_TYPE.ADD);
		DeadlineTask dead = new DeadlineTask(taskDescription, dates);
		tasks.add(dead);
		storage.save(true);
		taskDescription = InputParser.parseDescription(userInput1);
		dates = InputParser.parseDateTime(userInput1, COMMAND_TYPE.ADD);
		TimedTask timed3 = new TimedTask(taskDescription, dates);
		tasks.add(timed3);
		storage.save(true);
		tasks=(ArrayList<Task>) storage.undo().clone();
		tasks=(ArrayList<Task>) storage.undo().clone();
		tasks=(ArrayList<Task>) storage.redo().clone();
		storage.save(false);
		StorageHandler.resetStorageHandler();
		tasks.clear();
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		assertEquals("TIMED#CS2100 Tutorial#2013-10-18T12:00 to 2013-10-14T13:00#false", tasks.get(0).toStringForFile());
		assertEquals("TIMED#Japanese Tutorial A#2013-10-14T14:00 to 2013-10-14T16:00#false", tasks.get(1).toStringForFile());
		assertEquals("floating#Have Dinner With A Friend#false", tasks.get(2).toStringForFile());
		assertEquals("DEADLINE#Study#2013-10-18T23:00#false", tasks.get(3).toStringForFile());
		assertEquals(4, tasks.size());
		tasks.clear();
		StorageHandler.resetStorageHandler();
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		tasks=(ArrayList<Task>) storage.undo().clone();
		tasks=(ArrayList<Task>) storage.undo().clone();
		storage.save(false);
		assertEquals("TIMED#CS2100 Tutorial#2013-10-18T12:00 to 2013-10-14T13:00#false", tasks.get(0).toStringForFile());
		assertEquals("TIMED#Japanese Tutorial A#2013-10-14T14:00 to 2013-10-14T16:00#false", tasks.get(1).toStringForFile());
		assertEquals(2, tasks.size());
		tasks=(ArrayList<Task>) storage.undo().clone();
		tasks=(ArrayList<Task>) storage.undo().clone();
		assertEquals(true, tasks.isEmpty());
		tasks=(ArrayList<Task>) storage.redo().clone();
		assertEquals("TIMED#CS2100 Tutorial#2013-10-18T12:00 to 2013-10-14T13:00#false", tasks.get(0).toStringForFile());
	}
	
}
