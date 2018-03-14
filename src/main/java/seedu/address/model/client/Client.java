package seedu.address.model.client;

import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.UniquePetUidList;
import seedu.address.model.tag.Tag;

/**
 * Guarantees: details are present and not null, field values are validated, immutable.
 * Represents a Person in the address book.
 */

public class Client extends Person {

    private final ClientUid uid;
    private final UniquePetUidList listOfPetUids;

    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  UniquePetUidList listOfPetUids) {
        super(name, phone, email, address, tags);
        uid = new ClientUid();
        this.listOfPetUids = listOfPetUids;
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

    /**
     * Returns the uid of the client as a {@code ClientUid}
     */
    public ClientUid getClientUid() {
        return uid;
    }

    /**
     *  Returns a list of pet uids of client as a {@code UniquePetUidList}
     */
    public UniquePetUidList getListOfpetUids() {
        return listOfPetUids;
    }

}
