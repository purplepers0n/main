package seedu.address.model.client;

import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Guarantees: details are present and not null, field values are validated, immutable.
 *Represents a Person in the address book.
 */

public class Client extends Person {

    //private final Pet pet;
    private int clientId;

    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, int cid) {
        super(name, phone, email, address, tags);
        this.clientId = cid;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int setId) {
        clientId = setId;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Client)) {
            return false;
        }

        Client otherClient = (Client) other;
        return otherClient.getName().equals(this.getName())
                && otherClient.getPhone().equals(this.getPhone())
                && otherClient.getEmail().equals(this.getEmail())
                && otherClient.getAddress().equals(this.getAddress());
    }

}
