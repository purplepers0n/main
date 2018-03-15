package seedu.address.logic.commands;

import static org.junit.Assert.*;

import org.junit.Test;

import seedu.address.model.appointment.Appointment;
import seedu.address.testutil.AppointmentBuilder;

public class ScheduleCommandTest {

    @Test
    public void executeUndoableCommand() throws Exception{
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
    }
}