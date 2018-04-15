package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetAge;
import seedu.address.model.pet.PetGender;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Client(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("premium")),
            new Client(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("premium", "insurance")),
            new Client(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("temporary")),
            new Client(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("premium")),
            new Client(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("fulltimer")),
            new Client(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("parttimer"))
        };
    }

    public static Pet[] getSamplePets() {
        return new Pet[] {
            new Pet(new PetName("Tweety"), new PetAge("2"), new PetGender("M"), getTagSet("Bird")),
            new Pet(new PetName("Sylvester"), new PetAge("1"), new PetGender("M"), getTagSet("Cat")),
        };
    }

    public static ClientOwnPet[] getSampleClientOwnpetAssociation() {
        return new ClientOwnPet[] {
            new ClientOwnPet((Client) getSamplePersons()[0], getSamplePets()[0]),
            new ClientOwnPet((Client) getSamplePersons()[1], getSamplePets()[1])
        };
    }

    public static Appointment[] getSampleAppointment() {
        return new Appointment[] {
            new Appointment(new Date("2018-01-02"), new Time("14:30"),
                    new Duration("60"), new Description("Sterilize Garfield now")),
            new Appointment(new Date("2018-02-01"), new Time("15:30"),
                    new Duration("60"), new Description("Give Tweety a shower"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAb = new AddressBook();
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
            for (Pet samplePet : getSamplePets()) {
                sampleAb.addPet(samplePet);
            }
            for (Appointment sampleAppointment : getSampleAppointment()) {
                sampleAb.scheduleAppointment(sampleAppointment);
            }
            sampleAb.setClientPetAssociations(Arrays.asList(getSampleClientOwnpetAssociation()));

            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        } catch (DuplicatePetException e) {
            throw new AssertionError("sample data cannot contain duplicate pets", e);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("sample data cannot contain duplicate appointments", e);
        } catch (AppointmentCloseToPreviousException ape) {
            throw new AssertionError("sample data cannot schedule appointments close to previous", ape);
        } catch (AppointmentCloseToNextException ape) {
            throw new AssertionError("sample data cannot schedule appointments close to next", ape);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
