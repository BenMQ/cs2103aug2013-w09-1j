package sg.edu.nus.cs2103.sudo.storage;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import sg.edu.nus.cs2103.sudo.logic.FloatingTask;
import sg.edu.nus.cs2103.sudo.logic.InputParser;
import sg.edu.nus.cs2103.sudo.logic.Task;

import org.joda.time.DateTime;
import org.junit.Test;

public class StorageHandlerTest {

	public static void main(String[] args) throws Exception {
		
		//dummy
		ArrayList<Task> tasks = new ArrayList<Task>();
		FloatingTask floating1 = new FloatingTask("eat pizza");
		FloatingTask floating2 = new FloatingTask("drink coke");
		StorageHandler testee = new StorageHandler("testing", tasks);
		tasks.add(floating1);
		//testee.save(tasks);
		tasks.add(floating2);
		//testee.save(tasks);
	 //   assertEquals(InputParser.parseDescription(testcases[1]), "app");
	   // assertEquals(InputParser.parseDescription(testcases[2]), "amazing");
		
		//taskList
		
	}
	
}