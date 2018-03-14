package seedu.address.model.pet;

import java.util.UUID;

/**
 * Represents a Pet identifier in the address book.
 */
public class PetUid {

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

        if (!(other instanceof PetUid)) {
            return false;
        }

        return other.toString().equalsIgnoreCase(id.toString());
    }
}
