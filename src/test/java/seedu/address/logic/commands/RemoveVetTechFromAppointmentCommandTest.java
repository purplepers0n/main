package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.TypicalAddressBook;

//@@author jonathanwj-reused
/**
 * Contains integration tests unit tests for
 * {@code RemoveVetTechFromAppointmentCommand}.
 */
public class RemoveVetTechFromAppointmentCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removeVetTech_invalidAppt_throwAppointmentNotFoundException() throws Exception {
        thrown.expect(AppointmentNotFoundException.class);
        model.removeVetTechFromAppointent(new AppointmentBuilder().withDate("2019-02-01")
                .withTime("14:40")
                .withDuration("80")
                .withDescription("Dummy")
                .build());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Appointment appointmentToRemoveVetTech = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        RemoveVetTechFromAppointmentCommand command = new RemoveVetTechFromAppointmentCommand(INDEX_FIRST);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedMessage = String.format(RemoveVetTechFromAppointmentCommand
                .MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS, appointmentToRemoveVetTech);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.removeVetTechFromAppointent(appointmentToRemoveVetTech);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

}
