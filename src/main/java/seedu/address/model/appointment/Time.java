package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Appointment's time in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class Time {

    public static final String MESSAGE_TIME_CONSTRAINTS =
            "Appointment time should be all integers in format HH:MM, and it should not be blank";

    /*
     * The first character of the time must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String TIME_VALIDATION_REGEX = "([01]?[0-9]|2[0-3]):([0-5][0-9])";

    private static final int MINUTE_START_INDEX = 0;
    private static final int MINUTE_END_INDEX = 2;
    private static final int HOUR_START_INDEX = 3;

    public final String time;

    /**
     * Constructs a {@code Time}.
     *
     * @param time A valid time.
     */
    public Time(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_TIME_CONSTRAINTS);
        this.time = time;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidTime(String test) {
        return test.matches(TIME_VALIDATION_REGEX);
    }

    /**
     *  Returns the integer value of the Minute in time
     */
    public int getMinute() {
        String minute = this.toString().substring(MINUTE_START_INDEX, MINUTE_END_INDEX);

        return Integer.parseInt(minute);
    }

    /**
     *  Returns the integer value of Hour in time
     */
    public int getHour() {
        String hour = this.toString().substring(HOUR_START_INDEX);

        return Integer.parseInt(hour);

    }

    @Override
    public String toString() {
        return time;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Time // instanceof handles nulls
                && this.time.equals(((Time) other).time)); // state check
    }

    @Override
    public int hashCode() {
        return time.hashCode();
    }
}
