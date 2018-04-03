package seedu.address.logic.commands;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.testutil.AppointmentBuilder;
import seedu.address.testutil.TypicalAddressBook;

/**
 * Contains integration tests unit tests for
 * {@code RemoveVetTechFromAppointmentCommand}.
 */
public class RemoveVetTechFromAppointmentCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removeVet_invalidAppt_throwAppointmentNotFoundException() throws Exception {
        thrown.expect(AppointmentNotFoundException.class);
        model.removeVetFromAppointent(new AppointmentBuilder().withDate("2019-02-01")
                .withTime("14:40")
                .withDuration("80")
                .withDescription("Dummy")
                .build());
    }

}
