package sg.edu.nus.cs2103.sudo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sg.edu.nus.cs2103.sudo.logic.InputParserTest;
import sg.edu.nus.cs2103.sudo.logic.TaskManagerTest;
import sg.edu.nus.cs2103.sudo.storage.StorageHandlerTest;

/**
 * When writing the code, think of the test.
 * When writing the test, think of the code. 
 * 
 * When you think of code and test as one, 
 * testing is easy and code is beautiful.
 */

//@author A0099317U
@RunWith(Suite.class)
@SuiteClasses({StorageHandlerTest.class, InputParserTest.class,  TaskManagerTest.class  })
public class UnitTestSuite {

}
