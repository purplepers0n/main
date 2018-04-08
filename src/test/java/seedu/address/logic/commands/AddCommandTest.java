package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentListIsEmptyException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.ClientPetAssociationNotFoundException;
import seedu.address.model.association.exceptions.PetAlreadyHasAppointmentException;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.testutil.PersonBuilder;

public class AddCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personOrClientAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().buildWithRoleClient();

        CommandResult commandResult = getAddCommandForPerson(validPerson, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validPerson), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);

    }

    //@@author jonathanwj-reused
    @Test
    public void execute_vetTechnicianAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validTechnician = new PersonBuilder().buildWithRoleVetTechnician();

        CommandResult commandResult = getAddCommandForPerson(validTechnician, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validTechnician), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTechnician), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePersonOrClient_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validPerson = new PersonBuilder().buildWithRoleClient();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

    //@@author jonathanwj-reused
    @Test
    public void execute_duplicateVetTechnician_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validVetTechnician = new PersonBuilder().buildWithRoleVetTechnician();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validVetTechnician, modelStub).execute();
    }

    //@@author
    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").buildWithRoleClient();
        Person bob = new PersonBuilder().withName("Bob").buildWithRoleClient();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * Generates a new AddCommand with the details of the given person.
     */
    private AddCommand getAddCommandForPerson(Person person, Model model) {
        AddCommand command = new AddCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Pet> getFilteredPetList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<VetTechnician> getFilteredVetTechnicianList() {
            return null;
        }

        @Override
        public void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate) {
            fail("This method should not be called.");
        }

        public void updateFilteredPetList(Predicate<Pet> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredClientOwnPetAssocation(Predicate<ClientOwnPet> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ClientOwnPet> getFilteredClientPetAssociationList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException {
            fail("This method should not be called.");
        }

        @Override
        public void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public void updateAppointment(Appointment target, Appointment rescheduledAppointment)
                throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Appointment> getFilteredAppointmentList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            fail("This method should not be called");
        }

        @Override
        public void deletePet(Pet pet) throws PetNotFoundException {
            fail("This method should not be called");
        }

        @Override
        public void setCurrentList(int currentList) {
            fail("This method should not be called");
        }

        @Override
        public int getCurrentList() {
            fail("This method should not be called");
            return -1;
        }

        @Override
        public void addVetTechToAppointment(VetTechnician technician, Appointment appointment) {
            fail("This method should not be called");
        }

        @Override
        public void removeVetTechFromAppointent(Appointment apptToRemoveVetFrom)
                throws DuplicateAppointmentException, AppointmentNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortClientList() {
            fail("This method should not be called");
        }

        @Override
        public void sortPetList() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void addAppointmentToPet(Appointment appointment, Pet pet) throws PetAlreadyHasAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public void removeAppointmentFromPet(Appointment appointment) throws AppointmentNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortAppointmentList() throws AppointmentListIsEmptyException {
            fail("This method should not be called.");
        }

        @Override
        public void updateDetailsList(Client client, ObservableList<Pet> pets,
                                      ObservableList<Appointment> appointments) {
            fail("This method should not be called.");
        }

        @Override
        public Client getClientDetails() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Pet> getClientPetList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Appointment> getClientApptList() {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person.
     */
    private class ModelStubThrowingDuplicatePersonException extends ModelStub {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            throw new DuplicatePersonException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            requireNonNull(person);
            personsAdded.add(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
        }

        @Override
        public void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate) {
        }

        @Override
        public void updateFilteredPetList(Predicate<Pet> predicate) {
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
        }
    }

}
