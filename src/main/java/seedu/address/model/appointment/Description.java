package seedu.address.model.appointment;


/**
 * Represents the description of an Appointment
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Description of the appointment cannot be a blank space.";

    public final String description;

    /**
     * Constructs a {@code Description}
     */
    public Description(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description.trim();
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Description
                && this.description.equals(((Description) other).description));
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
