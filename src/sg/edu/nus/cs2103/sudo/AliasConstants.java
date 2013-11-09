package sg.edu.nus.cs2103.sudo;

import java.util.HashMap;

//@author A0099317U
public class AliasConstants {

	/**
	 *  This key-value table maps aliases to their corresponding COMMAND_TYPE.
	 */
	public static final HashMap<String, COMMAND_TYPE> aliases = new HashMap<String, COMMAND_TYPE>() {
		private static final long serialVersionUID = 1L;
		{
			put("ADD", COMMAND_TYPE.ADD);
			put("DO", COMMAND_TYPE.ADD);
			put("SUDO", COMMAND_TYPE.ADD);

			put("DELETE", COMMAND_TYPE.DELETE);
			put("DEL", COMMAND_TYPE.DELETE);
			put("REMOVE", COMMAND_TYPE.DELETE);

			put("EDIT", COMMAND_TYPE.EDIT);
			put("CHANGE", COMMAND_TYPE.EDIT);
			put("MODIFY", COMMAND_TYPE.EDIT);

			put("FINISH", COMMAND_TYPE.FINISH);
			put("COMPLETE", COMMAND_TYPE.FINISH);
			put("DONE", COMMAND_TYPE.FINISH);

			put("SEARCH", COMMAND_TYPE.SEARCH);
			put("FIND", COMMAND_TYPE.SEARCH);

			put("DISPLAY", COMMAND_TYPE.DISPLAY);
			put("SHOW", COMMAND_TYPE.DISPLAY);

			put("UNDO", COMMAND_TYPE.UNDO);
			put("REDO", COMMAND_TYPE.REDO);
			put("FREE", COMMAND_TYPE.FREE);
			put("SCHEDULE", COMMAND_TYPE.SCHEDULE);
			put("SORT", COMMAND_TYPE.SORT);
			
			put("HELP", COMMAND_TYPE.HELP);
			put("COMMANDS", COMMAND_TYPE.HELP);
			
			put("ALL", COMMAND_TYPE.ALL);
			put("UNFINISH", COMMAND_TYPE.UNFINISH);

			put("EXIT", COMMAND_TYPE.EXIT);
			put("QUIT", COMMAND_TYPE.EXIT);
		}
	};
	
	
}
