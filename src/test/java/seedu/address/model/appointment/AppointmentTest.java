package seedu.address.model.appointment;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AppointmentTest {

    @Test
    public void equals() {
        Appointment appointment1 = new Appointment(new Date("2018-01-01"), new Time("14:00"), new Duration("30"));

        // same object -> returns true
        assertTrue(appointment1.equals(appointment1));

        // same values -> returns true
        Appointment appointment1Copy = new Appointment(appointment1.getDate(), appointment1.getTime(),
                appointment1.getDuration());
        assertTrue(appointment1.equals(appointment1Copy));

        // different types -> returns false
        assertFalse(appointment1.equals(10));

        // null -> returns false
        assertFalse(appointment1.equals(null));

        // different appointment -> returns false
        Appointment differentAppointmentWithDifferentDuration = new Appointment(new Date("2018-11-20"),
                new Time("15:15"), new Duration("100"));
        assertFalse(appointment1.equals(differentAppointmentWithDifferentDuration));

        Appointment differentAppointmentWithSameDuration = new Appointment(new Date("2018-11-20"),
                new Time("15:15"), new Duration("30"));
        assertFalse(appointment1.equals(differentAppointmentWithSameDuration));

        //duplicate appointment -> returns true
        Appointment duplicateAppointmentWithDifferentDuration = new Appointment(new Date("2018-01-01"),
                new Time("14:00"), new Duration("100"));
        assertTrue(appointment1.equals(duplicateAppointmentWithDifferentDuration));

        Appointment duplicateAppointmentWithSameDuration = new Appointment(new Date("2018-01-01"),
                new Time("14:00"), new Duration("30"));
        assertTrue(appointment1.equals(duplicateAppointmentWithSameDuration));
    }

}
