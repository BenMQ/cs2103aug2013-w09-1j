package sg.edu.nus.cs2103.sudo.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;
import sg.edu.nus.cs2103.sudo.logic.TaskManager;

import org.joda.time.DateTime;
import org.junit.Test;

public class StorageHandlerTest {

	private static ArrayList<Task> tasks;
	private static StorageHandler storage;
	
	@Test
	public void initializatinoTest() throws Exception {
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		storage = StorageHandler.getStorageHandler("Test", tasks);
	}
	

	
}
