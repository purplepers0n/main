package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.UniqueAppointmentList;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFound;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonRole;
import seedu.address.model.person.UniquePersonList;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.UniquePetList;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.address.model.vettechnician.VetTechnician;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList persons;
    private final UniqueTagList tags;
    private final UniqueAppointmentList appointments;
    private final UniquePetList pets;

    private final ObservableList<ClientOwnPet> clientPetAssociations;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        tags = new UniqueTagList();

        appointments = new UniqueAppointmentList();
        pets = new UniquePetList();

        clientPetAssociations = FXCollections.observableArrayList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons and Tags in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setPersons(List<Person> persons) throws DuplicatePersonException {
        this.persons.setPersons(persons);
    }

    public void setTags(Set<Tag> tags) {
        this.tags.setTags(tags);
    }

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException {
        this.appointments.setAppointments(appointments);
    }

    public void setPets(List<Pet> pets) throws DuplicatePetException {
        this.pets.setPets(pets);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setTags(new HashSet<>(newData.getTagList()));
        List<Person> syncedPersonList = newData.getPersonList().stream()
                .map(this::syncWithMasterTagList)
                .collect(Collectors.toList());
        try {
            setPersons(syncedPersonList);
        } catch (DuplicatePersonException e) {
            throw new AssertionError("AddressBooks should not have duplicate persons");
        }

        List< Appointment > syncedAppointmentList = newData.getAppointmentList();
        try {
            setAppointments(syncedAppointmentList);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("Program should not have duplicate appointments");
        }

        List<Pet> syncedPetList = newData.getPetList().stream()
                .map(this::syncWithMasterPetTagList)
                .collect(Collectors.toList());
        try {
            setPets(syncedPetList);
        } catch (DuplicatePetException e) {
            throw new AssertionError("Program should not have duplicate pets");
        }
    }

    //// person-level operations

    /**
     * Adds a person to the address book.
     * Also checks the new person's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the person to point to those in {@link #tags}.
     *
     * @throws DuplicatePersonException if an equivalent person already exists.
     */
    public void addPerson(Person p) throws DuplicatePersonException {
        Person person = syncWithMasterTagList(p);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any person
        // in the person list.
        persons.add(person);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedPerson}.
     *
     * @throws DuplicatePersonException if updating the client's details causes the client to be equivalent to
     *      another existing client in the list.
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     *
     * @see #syncWithMasterTagList(Person)
     */
    public void updatePerson(Person target, Person editedPerson)
            throws DuplicatePersonException, PersonNotFoundException {
        requireNonNull(editedPerson);

        Person syncedEditedPerson = syncWithMasterTagList(editedPerson);
        // TODO: the tags master list will be updated even though the below line fails.
        // This can cause the tags master list to have additional tags that are not tagged to any client
        // in the client list.
        persons.setPerson(target, syncedEditedPerson);
    }

    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private Person syncWithMasterTagList(Person person) {
        Person syncedPerson;

        final UniqueTagList personTags = new UniqueTagList(person.getTags());
        tags.mergeFrom(personTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        personTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));

        if (person.getRole().equals(PersonRole.CLIENT_ROLE)) {
            syncedPerson = new Client(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), correctTagReferences);
        } else {
            syncedPerson = new VetTechnician(person.getName(), person.getPhone(), person.getEmail(),
                    person.getAddress(), correctTagReferences);
        }
        return syncedPerson;
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PersonNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePerson(Person key) throws PersonNotFoundException {
        if (persons.remove(key)) {
            return true;
        } else {
            throw new PersonNotFoundException();
        }
    }

    //// tag-level operations

    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
    }

    //// appointment-level operations

    /**
     * Schedule an appointment to the address book.
     * @throws DuplicateAppointmentException if an equivalent person already exists.
     */
    public void scheduleAppointment(Appointment a) throws DuplicateAppointmentException {
        appointments.add(a);
    }

    //// pet-level operations

    /**
     * Adds a pet to the program.
     * Also checks the new pet's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the pet to point to those in {@link #tags}.
     *
     * @throws DuplicatePetException if an equivalent pet already exists.
     */
    public void addPet(Pet p) throws DuplicatePetException {
        Pet pet = syncWithMasterPetTagList(p);
        pets.add(pet);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PetNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePet(Pet key) throws PetNotFoundException {
        if (pets.remove(key)) {
            return true;
        } else {
            throw new PetNotFoundException();
        }
    }

    /**
     *  Updates the master tag list to include tags in {@code person} that are not in the list.
     *  @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     *  list.
     */
    private Pet syncWithMasterPetTagList(Pet pet) {
        Pet syncedPet;

        final UniqueTagList petTags = new UniqueTagList(pet.getTags());
        tags.mergeFrom(petTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        petTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));

        syncedPet = new Pet(pet.getPetName(), pet.getPetAge(), pet.getPetGender(), correctTagReferences);
        return syncedPet;
    }

    //// Association methods

    /**
     * Associates pet to client
     * @throws ClientAlreadyOwnsPetException
     */
    public void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException {
        ClientOwnPet toAdd = new ClientOwnPet(client, pet);
        if (!clientPetAssociations.contains(toAdd)) {
            clientPetAssociations.add(toAdd);
        } else {
            throw new ClientAlreadyOwnsPetException();
        }
    }


    /**
     * Removes association from pet and client
     * @throws ClientPetAssociationNotFound
     */
    public void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFound {
        ClientOwnPet toRemove = new ClientOwnPet(client, pet);
        if (clientPetAssociations.contains(toRemove)) {
            clientPetAssociations.remove(toRemove);
        } else {
            throw new ClientPetAssociationNotFound();
        }
    }


    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() + " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asObservableList();
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return tags.asObservableList();
    }

    @Override
    public ObservableList<Appointment> getAppointmentList() {
        return appointments.asObservableList();
    }

    @Override
    public ObservableList<Pet> getPetList() {
        return pets.asObservableList();
    }


    public ObservableList<ClientOwnPet> getClientPetAssociations() {
        return clientPetAssociations;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && this.persons.equals(((AddressBook) other).persons)
                && this.tags.equalsOrderInsensitive(((AddressBook) other).tags))
                && this.appointments.equals(((AddressBook) other).appointments)
                && this.pets.equals(((AddressBook) other).pets);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(persons, tags, appointments, pets);
    }
}
