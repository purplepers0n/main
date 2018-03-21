package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Appointment's duration in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidDuration(String)}
 */
public class Duration {

    public static final String MESSAGE_DURATION_CONSTRAINTS =
            "Appointment duration should be all integers in format MMM (in minute), "
                    + "and it should not be blank";

    /*
     * The first character of the date must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DURATION_VALIDATION_REGEX = "(1[5-9]|[2-9][0-9]|1[01][0-9]|120)";

    public final String duration;

    /**
     * Constructs a {@code Duration}.
     *
     * @param duration A valid duration.
     */
    public Duration(String duration) {
        requireNonNull(duration);
        checkArgument(isValidDuration(duration), MESSAGE_DURATION_CONSTRAINTS);
        this.duration = duration;
    }

    /**
     * Returns the integer value of duration
     */
    public int getDuration() {
        return Integer.parseInt(this.duration);
    }

    /**
     * Returns true if a given string is a valid duration.
     */
    public static boolean isValidDuration(String test) {
        return test.matches(DURATION_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return duration;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Duration // instanceof handles nulls
                && this.duration.equals(((Duration) other).duration)); // state check
    }

    @Override
    public int hashCode() {
        return duration.hashCode();
    }
}
