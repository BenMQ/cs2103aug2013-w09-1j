package sg.edu.nus.cs2103.sudo;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
	 * Integration Test ideas:
	 * 1. Test task adding, check state of all components: UI, parser, taskmanager, storage  
	 * 2.
	 * 
	 * 
	 */	

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	StorageHandler storage;
	TaskManager manager;
	InputParser parser;	
	
	@Before
	public void setUp() {
//		storage = StorageHandler.getStorageHandler(Constants.FILE_NAME, new ArrayList<Task>());
		manager = TaskManager.getTaskManager();
		parser = InputParser.getInputParser(manager);
		System.setOut(new PrintStream(outContent));
    }
	
	@After
    public void tearDown() {
        manager.clearTasks();
        //have to clear storage also
        
    }		
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
