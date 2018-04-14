package seedu.address.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;
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
import seedu.address.model.vettechnician.VetTechnician;

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
            new VetTechnician(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("fulltimer")),
            new VetTechnician(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("parttimer")),
            new VetTechnician(new Name("Frank Boo"), new Phone("63830808"), new Email("frank@example.com"),
                    new Address("31 Pandan Road, 609278, Singapore"),
                    getTagSet("parttimer"))
        };
    }

    public static Pet[] getSamplePets() {
        return new Pet[] {
            new Pet(new PetName("Tweety"), new PetAge("2"), new PetGender("M"), getTagSet("Bird")),
            new Pet(new PetName("Sylvester"), new PetAge("1"), new PetGender("M"), getTagSet("Cat")),
            new Pet(new PetName("Mickey"), new PetAge("1"), new PetGender("M"), getTagSet("Mouse")),
            new Pet(new PetName("Goofy"), new PetAge("1"), new PetGender("M"), getTagSet("Dog")),
            new Pet(new PetName("Daisy"), new PetAge("1"), new PetGender("F"), getTagSet("Duck")),
            new Pet(new PetName("Minnie"), new PetAge("1"), new PetGender("F"), getTagSet("Mouse")),
        };
    }


    public static ClientOwnPet[] getSampleClientOwnpetAssociation() {
        return new ClientOwnPet[] {
            new ClientOwnPet((Client) getSamplePersons()[0], getSamplePets()[0]),
            new ClientOwnPet((Client) getSamplePersons()[1], getSamplePets()[1]),
            new ClientOwnPet((Client) getSamplePersons()[2], getSamplePets()[2]),
            new ClientOwnPet((Client) getSamplePersons()[0], getSamplePets()[3]),
            new ClientOwnPet((Client) getSamplePersons()[1], getSamplePets()[4]),
            new ClientOwnPet((Client) getSamplePersons()[2], getSamplePets()[5])
        };
    }

    public static Appointment[] getSampleAppointment() {
        return new Appointment[] {
            new Appointment(new Date("2018-01-02"), new Time("14:30"),
                    new Duration("60"), new Description("Sterilize Garfield now")),
            new Appointment(new Date("2018-02-01"), new Time("15:30"),
                    new Duration("60"), new Description("Give Tweety a shower")),
            new Appointment(new Date("2018-01-02"), new Time("10:30"),
                    new Duration("60"), new Description("Sick")),
            new Appointment(new Date("2018-02-01"), new Time("08:30"),
                    new Duration("60"), new Description("Unwell")),
            new Appointment(new Date("2018-01-02"), new Time("13:30"),
                    new Duration("60"), new Description("Injury")),
            new Appointment(new Date("2018-02-01"), new Time("11:30"),
                    new Duration("60"), new Description("Euthanasia"))
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
            int cycle = 0;
            for (Appointment sampleAppointment : getSampleAppointment()) {
                sampleAppointment.setClientOwnPet(getSampleClientOwnpetAssociation()[cycle]);
                sampleAppointment.setOptionalVetTech(Optional.of((VetTechnician) getSamplePersons()[4 + cycle]));
                sampleAb.scheduleAppointment(sampleAppointment);
                cycle++;
                cycle = cycle % 3;
            }
            sampleAb.setClientPetAssociations(Arrays.asList(getSampleClientOwnpetAssociation()));

            return sampleAb;
        } catch (DuplicatePersonException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        } catch (DuplicatePetException e) {
            throw new AssertionError("sample data cannot contain duplicate pets", e);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("sample data cannot contain duplicate appointments", e);
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
