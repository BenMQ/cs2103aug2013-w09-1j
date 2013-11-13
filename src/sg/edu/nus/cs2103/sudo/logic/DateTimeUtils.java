package sg.edu.nus.cs2103.sudo.logic;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import sg.edu.nus.cs2103.sudo.Constants;

//@author A0099317U

/**
 * This class contains static methods used to compare and 
 * modify Joda DateTime objects.
 *  
 */
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
	 * This operation check if 2 DateTime objects have exactly same date
	 * 
	 * @param dt1
	 *            is first date time
	 * @param dt2
	 *            is second date time
	 * @return true if 2 objects have same date or false if otherwise
	 * 
	 */
	public static boolean isSameDate(DateTime dt1, DateTime dt2) {
		if (dt1 == null && dt2 == null)
			return true;
		if (dt1 == null && dt2 != null)
			return false;
		if (dt1 != null && dt2 == null)
			return false;

		return ((dt1.getYear() == dt2.getYear())
				&& (dt1.getMonthOfYear() == dt2.getMonthOfYear()) 
				&& (dt1.getDayOfMonth() == dt2.getDayOfMonth()));
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

	//@author A0099314Y
	/**
	 * Produces a start DateTime and an end DateTime based on the argument
	 * given. If the input is an empty array, the range will be the current day.
	 * If the input has one DateTime, the range will be that particular day. If
	 * the input has two DateTimes, the range will be that.
	 * 
	 * @param dateTimes
	 *            arrayList of start and end DateTime
	 * @return range calculated
	 */
	public static ArrayList<DateTime> getFlexibleTimeRange(
			ArrayList<DateTime> dateTimes) {
		assert (dateTimes.size() >= 0 && dateTimes.size() <= 2);
		if (dateTimes.size() == 2) {
			if (dateTimes.get(0).isAfter(dateTimes.get(1))) {
				Collections.reverse(dateTimes);
			}
			return dateTimes;
		} else {
			DateTime day;
			if (dateTimes.size() == 1) {
				day = dateTimes.get(0);
			} else {
				day = DateTime.now();
			}
			DateTime startOfDay = DateTimeUtils.getStartOfDay(day);
			DateTime endOfDay = DateTimeUtils.getEndOfDay(day);
			ArrayList<DateTime> range = new ArrayList<DateTime>(2);
			range.add(startOfDay);
			range.add(endOfDay);
			return range;
		}
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
