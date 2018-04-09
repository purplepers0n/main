package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;

//@@author Godxin-functional
/**
 * Represents an Appointment's date in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Appointment date should be all integers in format YYYY-MM-DD, and it should not be blank";
    public static final String MESSAGE_YEAR_CONSTRAINTS =
            "Appointment year should be later than 2018";
    public static final String MESSAGE_DAYINMONTH_CONSTRAINTS =
            "Appointment day does not exist in the month";
    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DATE_VALIDATION_REGEX =
            "([2-9][0-9][1-9][0-9])-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";
    private static final int YEAR_START_INDEX = 0;
    private static final int YEAR_END_INDEX = 4;
    private static final int YEAR_LOWER_BOUND = 2018;
    private static final int LEAP_YEAR_DIVIDER = 4;
    private static final int CENTURY_YEAR_DIVIDER = 100;
    private static final int CENTURY_LEAP_YEAR_DIVIDER = 400;
    private static final int LEAP_YEAR_REMAINDER = 0;
    private static final int MONTH_START_INDEX = 5;
    private static final int MONTH_END_INDEX = 7;
    private static final int DAY_START_INDEX = 8;
    private static final int BIG_MONTH_DAY = 31;
    private static final int SMALL_MONTH_DAY = 30;
    private static final int FEB_LEAP_YEAR_DAY = 29;
    private static final int FEB_NONLEAP_YEAR_DAY = 28;
    private  static final String[] BIG_MONTH = {"01", "03", "05", "07", "08", "10", "12"};
    private  static final String[] SMALL_MONTH = {"04", "06", "09", "11"};


    public final String date;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        checkArgument(isValidYear(getYear(date)), MESSAGE_YEAR_CONSTRAINTS);
        checkArgument(isValidDaysInMonth(date), MESSAGE_DAYINMONTH_CONSTRAINTS);
        this.date = date;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidYear(int test) {
        return test >= YEAR_LOWER_BOUND;
    }

    /**
     * Returns true if a given string is a leap date.
     */
    public static boolean isLeapYear(int test) {
        if ((test % CENTURY_LEAP_YEAR_DIVIDER == LEAP_YEAR_REMAINDER)
                || ((test % LEAP_YEAR_DIVIDER == LEAP_YEAR_REMAINDER)
                && (test % CENTURY_YEAR_DIVIDER != LEAP_YEAR_REMAINDER))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if a given string is a valid date in month.
     */
    public static Boolean isValidDaysInMonth(String test) {
        int testYear = getYear(test);
        String testMonth = getMonth(test);
        int testDay = getDay(test);
        int daysInMonth;

        if (Arrays.asList(BIG_MONTH).contains(testMonth)) {
            daysInMonth = BIG_MONTH_DAY;
        } else if (Arrays.asList(SMALL_MONTH).contains(testMonth)) {
            daysInMonth = SMALL_MONTH_DAY;
        } else {
            if (isLeapYear(testYear)) {
                daysInMonth = FEB_LEAP_YEAR_DAY;
            } else {
                daysInMonth = FEB_NONLEAP_YEAR_DAY;
            }
        }
        return testDay <= daysInMonth;
    }

    /**
     *  Returns the integer value of year
     */
    public static int getYear(String date) {

        String year = date.substring(YEAR_START_INDEX, YEAR_END_INDEX);

        return Integer.parseInt(year);

    }

    /**
     *  Returns the integer value of month
     */
    public static String getMonth(String date) {

        String month = date.substring(MONTH_START_INDEX, MONTH_END_INDEX);

        return month;

    }

    /**
     *  Returns the integer value of day
     */
    public static int getDay(String date) {

        String day = date.substring(DAY_START_INDEX);

        return Integer.parseInt(day);

    }

    @Override
    public String toString() {
        return date;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Date // instanceof handles nulls
                && this.date.equals(((Date) other).date)); // state check
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    //@@author md-azsa
    /**
     * Negative if argument is smaller
     * Postiive if argument is larger
     */
    public int compareToDate(Date other) {
        if (this.date.equals(other.date)) {
            return 0;
        } else if (this.date.compareTo(other.date) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
