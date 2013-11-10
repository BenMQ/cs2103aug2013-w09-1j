package sg.edu.nus.cs2103.sudo.logic;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;

//@author A0099317U
public class DateTimeUtils {

	/**
	 * Determines if a DateTime has zero minute values.
	 * @param DateTime
	 * @return boolean
	 */		
	public static boolean hasZeroMinutes(final DateTime datetime) {
		boolean isNotNull = (datetime != null);
		boolean hasZeroMinutes = datetime.getMinuteOfHour() > 0;
		return isNotNull && hasZeroMinutes;
	}
	
    /**
     * Gets the last second of the day as a DateTime
     * @param day date to be used
     * @return the same day, but with 23:59:59
     */
    public static DateTime getEndOfDay(DateTime day) {
        return new DateTime(day.getYear(),
                day.getMonthOfYear(), day.getDayOfMonth(), 23, 59, 59);
    }
    
    /**
     * Gets the first second of the day as a Date
     * @param day date to be used
     * @return the same day, but with 00:00:00
     */
    public static DateTime getStartOfDay(DateTime day) {
        return new DateTime(day.getYear(),
                day.getMonthOfYear(), day.getDayOfMonth(), 0, 0, 0);
    }

	/**
	 * Helper method Checks the validity of 2 DateTimes. To be valid, the first
	 * DateTime must occur chronologically before the second DateTime. If
	 * invalid, throw exception.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public static void checkValidityTimes(DateTime startTime, DateTime endTime) {
		checkStartAndEndTime(startTime, endTime);
	}

	/**
	 * Helper method.
	 * 
	 * @param startTime
	 * @param endTime
	 */
	public static void checkStartAndEndTime(DateTime startTime, DateTime endTime) {
		DateTimeComparator dtComp = DateTimeComparator.getInstance();

		int check = dtComp.compare(endTime, startTime);

		// check = 0 if the startTime and endTime are the same (Invalid
		// TimedTask)
		// check = -1 if endTime occurs before startTime (Invalid TimedTask)
		// check = 1 if endTime occurs after startTime (Valid TimedTask)
		boolean sameStartAndEnd = check == 0;
		if (sameStartAndEnd) {
			throw new IllegalArgumentException(
					Constants.MESSAGE_SAME_START_END_TIME);
		} else {
			boolean invalidStartAndEnd = check == -1;
			if (invalidStartAndEnd) {
				throw new IllegalArgumentException(
						Constants.MESSAGE_END_BEFORE_START_TIME);
			}
		}
	}    
    
}
