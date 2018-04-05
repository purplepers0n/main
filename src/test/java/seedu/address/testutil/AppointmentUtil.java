package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.model.appointment.Appointment;

//@@author Godxin-test
/**
 * A utility class for Appointment.
 */
public class AppointmentUtil {

    /**
     * Returns an schedule command string for scheduling the {@code appointment}.
     */
    public static String getScheduleCommand(Appointment appointment) {
        return ScheduleCommand.COMMAND_WORD + " " + getAppointmentDetails(appointment);
    }

    /**
     * Returns the part of command string for the given {@code appointment}'s details.
     */
    public static String getAppointmentDetails(Appointment appointment) {

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DATE + appointment.getDate().toString() + " ");
        sb.append(PREFIX_TIME + appointment.getTime().toString() + " ");
        sb.append(PREFIX_DURATION + appointment.getDuration().toString() + " ");
        sb.append(PREFIX_DESCRIPTION + appointment.getDescription().toString() + " ");

        return sb.toString();
    }
}
