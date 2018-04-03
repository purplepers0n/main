package seedu.address.model.vettechnician;

import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

//@@author jonathanwj-reused
/**
 * Represents a Vet Technician in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class VetTechnician extends Person {

    /**
     * Every field must be present and not null.
     */
    public VetTechnician(Name name, Phone phone, Email email,
                         Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);

    }

    @Override
    public PersonRole getRole() {
        return PersonRole.TECHNICIAN_ROLE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VetTechnician)) {
            return false;
        }

        VetTechnician otherTechnician = (VetTechnician) other;
        return otherTechnician.getName().equals(this.getName())
                && otherTechnician.getPhone().equals(this.getPhone())
                && otherTechnician.getEmail().equals(this.getEmail())
                && otherTechnician.getAddress().equals(this.getAddress());
    }


}
