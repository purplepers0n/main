package seedu.address.model.appointment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppointmentTest {

    @Test
    public void equals() {
        Appointment appointment1 = new Appointment(new Date("01/01/2018"), new Time("1400"));

        // same object -> returns true
        assertTrue(appointment1.equals(appointment1));

        // same values -> returns true
        Appointment appointment1Copy = new Appointment(appointment1.getDate(), appointment1.getTime());
        assertTrue(appointment1.equals(appointment1Copy));

        // different types -> returns false
        assertFalse(appointment1.equals(10));

        // null -> returns false
        assertFalse(appointment1.equals(null));

        // different remark -> returns false
        Appointment differentAppointment = new Appointment(new Date("10/10/2018"), new Time("1515"));
        assertFalse(appointment1.equals(differentAppointment));
    }
}
