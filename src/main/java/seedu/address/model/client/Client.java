package seedu.address.model.client;

import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.UniquePetList;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Guarantees: details are present and not null, field values are validated, immutable.
 * Represents a Person in the address book.
 */

public class Client extends Person {

    private UniquePetList pets;

    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        pets = new UniquePetList();
    }

    @Override
    public PersonRole getRole() {
        return PersonRole.CLIENT_ROLE;
    }

    /**
     * @return pets as a {@code UniquePetList}
     */
    public UniquePetList getPets() {
        return pets;
    }

    /**
     * Adds new pet to client
     * @param toAdd new pet
     * @throws DuplicatePetException
     */
    public void addPet(Pet toAdd) throws DuplicatePetException {
        pets.add(toAdd);
    }

    /**
     * Remove old pet from client
     * @param toRemove old pet
     * @throws PetNotFoundException
     */
    public void removePet(Pet toRemove) throws PetNotFoundException {
        pets.remove(toRemove);
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
