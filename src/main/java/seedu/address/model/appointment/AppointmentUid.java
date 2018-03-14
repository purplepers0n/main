package seedu.address.model.appointment;

import java.util.UUID;

/**
 * Represents a Appointment identifier in the address book.
 */
public class AppointmentUid {

    private UUID id = UUID.randomUUID();

    @Override
    public String toString() {
        return id.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AppointmentUid)) {
            return false;
        }

        return other.toString().equalsIgnoreCase(id.toString());
    }

}
