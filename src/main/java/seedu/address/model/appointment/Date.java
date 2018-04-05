package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author Godxin-functional
/**
 * Represents an Appointment's date in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Appointment date should be all integers in format YYYY-MM-DD, and it should not be blank";

    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DATE_VALIDATION_REGEX =
            "([2-9][0-9][1-9][89])-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])";

    public final String date;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        this.date = date;
    }

    /**
     * Returns true if a given string is a valid date.
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
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
