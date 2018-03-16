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
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.client.Client;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;
import seedu.address.testutil.AppointmentBuilder;

public class ScheduleCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        Appointment appointment1 = new AppointmentBuilder().withDate("12/12/2018").build();
        Appointment appointment2 = new AppointmentBuilder().withTime("0000").build();
        ScheduleCommand scheduleAppointment1 = new ScheduleCommand(appointment1);
        ScheduleCommand scheduleAppointment2 = new ScheduleCommand(appointment2);

        // same object -> returns true
        assertTrue(scheduleAppointment1.equals(scheduleAppointment1));

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
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteClient(Client target) throws ClientNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void addClient(Client client) throws DuplicateClientException {
            fail("This method should not be called.");
        }

        @Override
        public void updateClient(Client target, Client editedClient)
                throws ClientNotFoundException {
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
        public void deleteVetTechnician(VetTechnician target) throws VetTechnicianNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void addVetTechnician(VetTechnician vetTechnician) throws DuplicateVetTechnicianException {
            fail("This method should not be called.");
        }

        @Override
        public void updateVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
                throws DuplicateVetTechnicianException, VetTechnicianNotFoundException {
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

        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            fail("This method should not be called.");
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
