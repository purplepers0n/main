package seedu.address.model.vettechnician;

import java.util.UUID;

/**
 * Represents a VetTechnician identifier in the address book.
 */
public class VetTechnicianUid {

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

        if (!(other instanceof VetTechnicianUid)) {
            return false;
        }

        return other.toString().equalsIgnoreCase(id.toString());
    }

}
