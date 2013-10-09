package sg.edu.nus.cs2103.sudo.logic;

import static org.junit.Assert.*; 
import org.junit.Test;

import java.util.ArrayList;


/**
 * @author Ipsita
 *
 */
public class TaskManagerTest {
	TaskManager manager = new TaskManager();
	
	/**
	 * Tests adding floating tasks and 
	 * displaying of all uncompleted floating tasks. 
	 * TODO: Test displaying of completed floating tasks  
	 */
	@Test
	public void testAddFloatingTasks() {
		ArrayList<Task> floatingTasks; 
		floatingTasks = manager.addFloatingTask(new FloatingTask("learn how to fish"));
		
		assertEquals("1. learn how to fish\n", displayTasks(floatingTasks));
	}
	
	@Test
	public void testEditFloatingTasks() {
		ArrayList<Task> floatingTasks; 
		
		floatingTasks = manager.addFloatingTask(new FloatingTask("learn how to fish"));
		floatingTasks = manager.editFloatingTask(1, new FloatingTask("learn how to fish with dad"));
		assertEquals("1. learn how to fish with dad\n", displayTasks(floatingTasks));
		
		floatingTasks = manager.addFloatingTask(new FloatingTask("resume pilates"));
		floatingTasks = manager.addFloatingTask(new FloatingTask("start salsa lessons"));
		floatingTasks = manager.editFloatingTask(2, new FloatingTask("resume pilates classes in NUS"));
		assertEquals("1. learn how to fish with dad\n2. resume pilates classes in NUS\n3. start salsa lessons\n", displayTasks(floatingTasks));
	}
	
	/**
	 * Tests searching for a string in the incomplete floating tasks
	 * TODO: Test search for both complete and incomplete floating tasks
	 */
	@Test
	public void testSearchFloatingTasks() {
		ArrayList<Task> floatingTasks; 
		
		floatingTasks = manager.addFloatingTask(new FloatingTask("learn how to fish"));
		floatingTasks = manager.addFloatingTask(new FloatingTask("resume pilates in NUS"));
		floatingTasks = manager.addFloatingTask(new FloatingTask("start salsa lessons at Nus"));
		
		assertEquals("1. learn how to fish\n2. resume pilates in NUS\n3. start salsa lessons at Nus\n", displayTasks(floatingTasks));
		assertEquals("1. learn how to fish\n", displayTasks(manager.search("fish")));
		assertEquals("2. resume pilates in NUS\n3. start salsa lessons at Nus\n", displayTasks(manager.search("nus")));
		assertEquals("Nothing to display.\n", displayTasks(manager.search("fishes")));

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