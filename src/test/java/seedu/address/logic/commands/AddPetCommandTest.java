package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
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
import seedu.address.model.client.UniqueClientList;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.testutil.PetBuilder;

//@@author md-azsa
public class AddPetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPet_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddPetCommand(null, null);
    }

    @Test
    public void execute_petAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPetAdded modelStub = new ModelStubAcceptingPetAdded();
        Pet validPet = new PetBuilder().build();

        CommandResult commandResult = getAddPetCommandForPet(validPet, modelStub).execute();

        assertEquals(String.format(AddPetCommand.MESSAGE_SUCCESS, validPet), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPet), modelStub.petsAdded);
    }

    @Test
    public void execute_duplicatePet_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePetException();
        Pet validPet = new PetBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddPetCommand.MESSAGE_DUPLICATE_PET);

        getAddPetCommandForPet(validPet, modelStub).execute();
    }

    @Test
    public void equals() {
        Pet garfield = new PetBuilder().withPetName("Garfield").build();
        Pet ginger = new PetBuilder().withPetName("Ginger").build();
        AddPetCommand addGarfieldCommand = new AddPetCommand(garfield, INDEX_FIRST_PERSON);
        AddPetCommand addGinger = new AddPetCommand(ginger, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(addGarfieldCommand.equals(addGarfieldCommand));

        // same values -> return true
        AddPetCommand addGarfieldCommandCpy = new AddPetCommand(garfield, INDEX_FIRST_PERSON);
        assertTrue(addGarfieldCommand.equals(addGarfieldCommandCpy));

        // different types -> return false
        assertFalse(addGarfieldCommand.equals(1));

        // null -> returns false
        assertFalse(addGarfieldCommand.equals(null));

        // different pets -> returns false
        assertFalse(addGarfieldCommand.equals(addGinger));
    }

    /**
     * Generates a new AddPetCommand with the details of the given pet
     */
    private AddPetCommand getAddPetCommandForPet(Pet pet, Model model) {
        AddPetCommand command = new AddPetCommand(pet, INDEX_FIRST_PERSON);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Default model stub that has all the methods failing
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
        public ObservableList<ClientOwnPet> getClientPetAssociationList() {
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
            fail("This method should not be called.");
        }

        @Override
        public int getCurrentList() {
            fail("This method should not be called.");
            return -1;
        }

        @Override
        public void addVetTechToAppointment(VetTechnician technician, Appointment appointment) {
            fail("This method should not be called.");
        }

        @Override
        public void removeVetTechFromAppointent(Appointment apptToRemoveVetFrom)
                throws DuplicateAppointmentException, AppointmentNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortClientList() {
            fail("This method should not be called.");
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
     * Model stub that always throws DuplicatePetException
     */
    private class ModelStubThrowingDuplicatePetException extends ModelStub {
        final UniqueClientList clientSample;

        private ModelStubThrowingDuplicatePetException() throws DuplicateClientException {
            clientSample = new UniqueClientList();
            clientSample.add((Client) ALICE);
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            throw new DuplicatePetException();
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.unmodifiableObservableList(clientSample.asObservableList());
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the pet being added.
     */
    private class ModelStubAcceptingPetAdded extends ModelStub {
        final ArrayList<Pet> petsAdded = new ArrayList<>();
        final UniqueClientList clientSample;

        private ModelStubAcceptingPetAdded() throws DuplicateClientException {
            clientSample = new UniqueClientList();
            clientSample.add((Client) ALICE);
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            requireNonNull(pet);
            petsAdded.add(pet);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException {

        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.unmodifiableObservableList(clientSample.asObservableList());
        }

        @Override
        public void updateFilteredPetList(Predicate<Pet> predicate) {
        }

    }
}
