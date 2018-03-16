package seedu.address.model.client;

import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Guarantees: details are present and not null, field values are validated, immutable.
 * Represents a Person in the address book.
 */

public class Client extends Person {


    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, PersonRole.CLIENT_ROLE, tags);
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
                && otherClient.getAddress().equals(this.getAddress())
                && otherClient.getRole().equals(this.getRole());

    }

}
