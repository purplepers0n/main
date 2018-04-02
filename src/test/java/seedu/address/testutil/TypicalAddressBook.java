package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;


/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalAddressBook {

    private TypicalAddressBook() {
    } // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            try {
                ab.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Pet pet : getTypicalPets()) {
            try {
                ab.addPet(pet);
            } catch (DuplicatePetException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Appointment appt : getTypicalAppointments()) {
            try {
                ab.scheduleAppointment(appt);
            } catch (DuplicateAppointmentException e) {
                throw new AssertionError("not possible");
            }
        }
        for (ClientOwnPet cop : getTypicalAssociations()) {
            try {
                ab.addPetToClient(cop.getPet(), cop.getClient());
            } catch (ClientAlreadyOwnsPetException | PetAlreadyHasOwnerException e) {
                throw new AssertionError("not possible");
            }
        }

        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(TypicalPersons.getTypicalPersons());
    }

    public static List<Pet> getTypicalPets() {
        return new ArrayList<>(TypicalPets.getTypicalPets());
    }

    public static List<ClientOwnPet> getTypicalAssociations() {
        return new ArrayList<>(TypicalAssociations.getTypicalAssociations());
    }

    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(TypicalAppointments.getTypicalAppointments());
    }

}
