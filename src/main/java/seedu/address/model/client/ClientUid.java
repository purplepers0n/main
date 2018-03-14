package seedu.address.model.client;

import java.util.UUID;

/**
 * Represents a Client identifier in the address book.
 */
public class ClientUid {

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

        if (!(other instanceof ClientUid)) {
            return false;
        }

        return other.toString().equalsIgnoreCase(id.toString());
    }
}
