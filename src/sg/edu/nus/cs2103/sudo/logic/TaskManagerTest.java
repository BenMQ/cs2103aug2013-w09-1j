package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*; 

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;


/**
 * @author Ipsita
 *
 */
public class TaskManagerTest {
	private static TaskManager manager = TaskManager.getTaskManager();
	private static ArrayList<Task> tasks;
	
	/**
	 * Tests adding floating tasks and 
	 * displaying of all uncompleted floating tasks. 
	 * TODO: Test displaying of completed floating tasks  
	 */
	@Test
	public void testAddTasks() {
		// ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		
		tasks = manager.addTask(new FloatingTask("learn how to fish"));
		assertEquals("1. learn how to fish\n", displayTasks(tasks));
		
		// tasks = manager.addTask(new DeadlineTask("Buy birthday gift", dateTimes));
		// assertEquals("", displayTasks(tasks));
		
		// tasks = manager.addTask(new FloatingTask("learn how to fish"));
		// assertEquals("", displayTasks(tasks));
	}
	
	@Test
	public void testEditTasks() {
		tasks = manager.editTask(1, new FloatingTask("learn how to fish with dad"));
		assertEquals("1. learn how to fish with dad\n", displayTasks(tasks));
		
		tasks = manager.addTask(new FloatingTask("resume pilates"));
		tasks = manager.addTask(new FloatingTask("start salsa lessons"));
		tasks = manager.editTask(2, new FloatingTask("resume pilates classes in NUS"));
		tasks = manager.editTask(3, new FloatingTask("start salsa lessons in Nus"));
		
		assertEquals("1. learn how to fish with dad\n2. resume pilates classes in NUS\n3. start salsa lessons in Nus\n", displayTasks(tasks));
	}
	
	/**
	 * Tests searching for a string in the incomplete floating tasks
	 * TODO: Test search for both complete and incomplete floating tasks
	 */
	@Test
	public void testSearchTasks() {
		assertEquals("1. learn how to fish with dad\n2. resume pilates classes in NUS\n3. start salsa lessons in Nus\n", displayTasks(tasks));
		assertEquals("1. learn how to fish with dad\n", displayTasks(manager.search("fish", true)));
		assertEquals("2. resume pilates classes in NUS\n3. start salsa lessons in Nus\n", displayTasks(manager.search("nus", false)));
		assertEquals("Nothing to display.\n", displayTasks(manager.search("fishes", true)));

	}
	
	private String displayTasks(ArrayList<Task> tasks) {
		String str = "";
		
		if (tasks.isEmpty()) {
			str += "Nothing to display.\n";
			return str;
		}
		
		for (int i=0; i< tasks.size(); i++) {
			str += tasks.get(i) + "\n";
		}
		
		return str;
	}
}
