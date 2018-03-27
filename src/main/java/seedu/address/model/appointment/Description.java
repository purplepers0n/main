package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents the description of an Appointment
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Description of the appointment cannot be a blank space.";

    // Should be non-empty input
    public static final String DESCRIPTION_VALIDATION_REGEX = "(.*\\S.*)";

    public final String description;

    /**
     * Constructs a {@code Description}
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Description
                && this.description.equals(((Description) other).description));
    }

    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
