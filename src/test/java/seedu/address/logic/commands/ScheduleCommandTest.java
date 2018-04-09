package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
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
import seedu.address.testutil.AppointmentBuilder;

//@@author Godxin-test
public class ScheduleCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullAppointment_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ScheduleCommand(null);
    }

    @Test
    public void execute_appointmentAcceptedByModel_scheduleSuccessful() throws Exception {
        ModelStubAcceptingAppointmentScheduled modelStub = new ModelStubAcceptingAppointmentScheduled();
        Appointment validAppointment = new AppointmentBuilder().build();

        CommandResult commandResult = getScheduleCommandForAppointment(validAppointment, modelStub).execute();

        assertEquals(String.format(ScheduleCommand.MESSAGE_SUCCESS, validAppointment), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validAppointment), modelStub.appointmentScheduled);
    }

    @Test
    public void execute_duplicateAppointment_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateAppointmentException();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);

        getScheduleCommandForAppointment(validAppointment, modelStub).execute();
    }

    @Test
    public void equals() {
        Appointment appointment1 = new AppointmentBuilder().withDate("2018-12-12").build();
        Appointment appointment2 = new AppointmentBuilder().withTime("00:00").build();
        Appointment appointment3 = new AppointmentBuilder().withDuration("30").build();
        Appointment appointment4 = new AppointmentBuilder().withDescription("Sterilize Garfield").build();

        ScheduleCommand scheduleAppointment1 = new ScheduleCommand(appointment1);
        ScheduleCommand scheduleAppointment2 = new ScheduleCommand(appointment2);
        ScheduleCommand scheduleAppointment3 = new ScheduleCommand(appointment3);
        ScheduleCommand scheduleAppointment4 = new ScheduleCommand(appointment4);

        // same object -> returns true
        assertTrue(scheduleAppointment1.equals(scheduleAppointment1));
        assertTrue(scheduleAppointment2.equals(scheduleAppointment2));
        assertTrue(scheduleAppointment3.equals(scheduleAppointment3));
        assertTrue(scheduleAppointment4.equals(scheduleAppointment4));

        // same values -> returns true
        ScheduleCommand scheduleAppointment1Copy = new ScheduleCommand(appointment1);
        assertTrue(scheduleAppointment1.equals(scheduleAppointment1Copy));

        // different types -> returns false
        assertFalse(scheduleAppointment1.equals(2));

        // null -> returns false
        assertFalse(scheduleAppointment1.equals(null));

        // different appointment -> returns false
        assertFalse(scheduleAppointment1.equals(scheduleAppointment2));
    }

    /**
     * Generates a new ScheduleCommand with the details of the given appointment.
     */
    private ScheduleCommand getScheduleCommandForAppointment(Appointment appointment, Model model) {
        ScheduleCommand command = new ScheduleCommand(appointment);
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
            return null;
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<VetTechnician> getFilteredVetTechnicianList() {
            fail("This method should not be called.");
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
            return model.getFilteredAppointmentList();
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            fail("This method should not be called.");
        }

        @Override
        public void deletePet(Pet pet) throws PetNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void setCurrentList(int currentList) {
            fail("This method should not be called");
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
     * A Model stub that always throw a DuplicateAppointmentException when trying to schedule an appointment.
     */
    private class ModelStubThrowingDuplicateAppointmentException extends ModelStub {
        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            throw new DuplicateAppointmentException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the appointment being scheduled.
     */
    private class ModelStubAcceptingAppointmentScheduled extends ModelStub {
        final ArrayList<Appointment> appointmentScheduled = new ArrayList<>();

        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            requireNonNull(appointment);
            appointmentScheduled.add(appointment);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
