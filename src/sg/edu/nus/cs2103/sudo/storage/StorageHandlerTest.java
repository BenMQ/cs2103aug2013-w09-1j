package sg.edu.nus.cs2103.sudo.storage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

import sg.edu.nus.cs2103.sudo.Constants;
import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StorageHandlerTest {

	private static ArrayList<Task> tasks;
	private static StorageHandler storage;
	private static final String TEST_NAME = "Test";
	private File file;
	private File historyFile;

	@Before
	public void setUp() {
		file = new File(TEST_NAME);
		historyFile = new File(Constants.HISTORY_NAME);
    }
	
	@After
    public void tearDown() {
		file.delete();
		historyFile.delete();
    }
	
	
	@Test
	public void initializationTest() throws Exception {
		storage = StorageHandler.getStorageHandler(TEST_NAME);
		storage.prepareFile(tasks);
		assertEquals(true, file.exists());
		assertEquals(true, historyFile.exists());
	}
	
}
