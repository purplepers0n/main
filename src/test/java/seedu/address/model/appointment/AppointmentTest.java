package seedu.address.model.appointment;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

//@@author Godxin-test
public class AppointmentTest {

    @Test
    public void equals() {

        Appointment appointment1 = new Appointment(new Date("2018-01-01"), new Time("14:00"),
                new Duration("30"), new Description("Sterilize Garfield"));


        // same object -> returns true
        assertTrue(appointment1.equals(appointment1));

        // same values -> returns true
        Appointment appointment1Copy = new Appointment(appointment1.getDate(), appointment1.getTime(),
                appointment1.getDuration(), appointment1.getDescription());
        assertTrue(appointment1.equals(appointment1Copy));

        // different types -> returns false
        assertFalse(appointment1.equals(10));

        // null -> returns false
        assertFalse(appointment1.equals(null));

        // different appointment -> returns false
        Appointment differentAppointmentWithDifferentDuration = new Appointment(new Date("2018-11-20"),
                    new Time("15:15"), new Duration("100"), new Description("Sterilize Garfield"));
        assertFalse(appointment1.equals(differentAppointmentWithDifferentDuration));

        Appointment differentAppointmentWithSameDuration = new Appointment(new Date("2018-11-20"),
                    new Time("15:15"), new Duration("30"), new Description("Sterilize Garfield"));
        assertFalse(appointment1.equals(differentAppointmentWithSameDuration));

        // same timing and date
        Appointment duplicateAppointmentWithDifferentDuration = new Appointment(new Date("2018-01-01"),
                new Time("14:00"), new Duration("100"), new Description("Sterilize Garfield"));
        assertTrue(appointment1.equals(duplicateAppointmentWithDifferentDuration));

        //duplicate appointment -> returns true

        Appointment duplicateAppointmentWithSameDuration = new Appointment(new Date("2018-01-01"),
                    new Time("14:00"), new Duration("30"), new Description("Sterilize Garfield"));
        assertTrue(appointment1.equals(duplicateAppointmentWithSameDuration));
    }

}
