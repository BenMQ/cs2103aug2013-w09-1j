package sg.edu.nus.cs2103.sudo.logic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sg.edu.nus.cs2103.sudo.storage.StorageHandlerTest;

@RunWith(Suite.class)
@SuiteClasses({ InputParserTest.class, TaskManagerTest.class, StorageHandlerTest.class })
public class UnitTestDriver {

}
