package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.format.PeriodFormatterBuilder;
import org.joda.time.format.PeriodParser;

import sg.edu.nus.cs2103.sudo.COMMAND_TYPE;
import sg.edu.nus.cs2103.sudo.Constants;

import com.joestelmach.natty.DateGroup;

//@author A0099317U
/**
 * This class contains some static methods to parse task 
 * information such as task ID, description, and natty-JodaDateTime
 * conversion.
 */
public class InputParserUtils {
	/**
	 * Retrieves date List objects from joda-datetime DateGroup objects.
	 * 
	 * @param dateGroups
	 *            A list of joda-datetime DateGroup objects
	 * @return ArrayList of Date Lists
	 */
	public static ArrayList<List<Date>> getDateLists(List<DateGroup> dateGroups) {
		ArrayList<List<Date>> dateLists = new ArrayList<List<Date>>();
		for (DateGroup dateGroup : dateGroups) {
			List<Date> dateList = dateGroup.getDates();
			dateLists.add(dateList);
		}

		return dateLists;
	}

	/**
	 * Convert DateLists to joda-DateTime objects.
	 * 
	 * @param dateLists
	 *            ArrayList of Date Lists
	 * @return ArrayList of DateTime objects
	 */
	public static ArrayList<DateTime> convertToDateTimes(
			ArrayList<List<Date>> dateLists) {
		ArrayList<DateTime> dateTimes = new ArrayList<DateTime>();
		if (dateLists.isEmpty()) {
			return dateTimes;
		}

		List<Date> dates = dateLists.get(0);
		for (Date date : dates) {
			DateTime dt = new DateTime(date);
			dateTimes.add(dt);
		}
		return dateTimes;
	}

	/**
	 * Validates that the first word (command word) is a valid COMMAND_TYPE. In
	 * other words, this method checks if the command is in COMMAND_TYPE.
	 * 
	 * @param userCommand
	 *            The user's command word
	 * @return COMMAND_TYPE
	 */
	public static COMMAND_TYPE getCommandType(String userCommand) {
		COMMAND_TYPE commandType = Constants.aliases.get(userCommand
				.toUpperCase());
		if (commandType == null) {
			return COMMAND_TYPE.INVALID;
		}
		return commandType;
	}

	/**
	 * Returns the minimum number of words required for a valid command.
	 * 
	 * @param commandType
	 * @return number of words
	 */
	public static int getNumOfWordsNeeded(COMMAND_TYPE commandType) {
		assert commandType != null;
		switch (commandType) {
		case EDIT:
        case SCHEDULE:
			return 3;
		case ADD:
		case DELETE:
		case SEARCH:
			return 2;
		case FINISH:
		default:
			return 1;
		}
	}
	
	//@author A0099314Y
	/**
     * Parse a string of the form 2h3m into milliseconds
     * http://stackoverflow.com/posts/11021986/revisions
     * @param periodString
     * @return the input string in milliseconds
     */
    public static long parseDurationToMillis(String periodString) {
        PeriodParser parser = new PeriodFormatterBuilder()
           .appendHours().appendSuffix("h")
           .appendMinutes().appendSuffix("m")
           .toParser();

        MutablePeriod period = new MutablePeriod();
        parser.parseInto(period, periodString, 0, Locale.getDefault());

        return period.toDurationFrom(new DateTime(0)).getMillis();
    }

    /**
     * Calculates the number of words that are irrelevant to date time parsing
     * @param command type of command
     * @return the number of words at the beginning of the command that are not
     * relevant to date time parsing
     */
    public static int getNumWordsToTrim(COMMAND_TYPE command) {
        switch (command) {
        case SCHEDULE:
            return 3;
        case EDIT:
            return 2;
        case INVALID:
            return 0;
        default:
            return 1;
        }
    }
    
	/**
	 * Strips the command and other irrelevant arguments in the user input.
	 * @param userInput user input string
	 * @param command the type of the command
	 * @return the input string with irrelevant keywords stripped away
	 */
	public static String getDateTimeString(String userInput,
	        COMMAND_TYPE command) {
        String desc = InputParser.parseDescription(userInput);
        userInput = StringUtils.trimDescription(userInput, desc);
	    String[] inputWords = userInput.trim().split("\\s+");	    
	    String[] relevantWord = 
	            Arrays.copyOfRange(inputWords,
	                               InputParserUtils.getNumWordsToTrim(command),
	                               inputWords.length);
	    return StringUtils.joinString(relevantWord, " ");
	}
    
}
