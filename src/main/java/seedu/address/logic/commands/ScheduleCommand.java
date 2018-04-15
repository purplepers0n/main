package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Time;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

//@@author Godxin-functional
/**
 * Schedule the date and time for an appointment as well as the duration of this appointment
 */
public class ScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "schedule";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedule an appointment with "
            + "date, time, duration and description.  "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_DURATION + "DURATION "
            + PREFIX_DESCRIPTION + "DESCRIPTION\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2018-05-01 "
            + PREFIX_TIME + "15:15 "
            + PREFIX_DURATION + "60 "
            + PREFIX_DESCRIPTION + "Sterilize Garfield";


    public static final String MESSAGE_SUCCESS = "New appointment scheduled";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "The date and time are taken ";
    public static final String MESSAGE_CLOSE_APPOINTMENT_PREVIOUS = "The new appointment is within the duration"
            + " of the earlier appointment.\n";
    public static final String MESSAGE_SUGGESTION_TIME = "You may delay the appointment for: ";
    public static final String MESSAGE_CLOSE_APPOINTMENT_NEXT = "The later appointment is within the duration "
            + "of the new appointment.\n";
    public static final String MESSAGE_SUGGESTION_DURATION = "This appointment can last at most: ";
    private static final String MINUTE_SUFFIX = " minutes";
    private static final int MINIMUM_INTERVAL = 1440;
    private static final int CORRECT_DURATION = 120;

    private final Appointment toAdd;

    /**
     * Creates an ScheduleCommand to add the specified {@code appointment}
     */
    public ScheduleCommand(Appointment appointment) {
        requireNonNull(appointment);

        this.toAdd = appointment;
    }

    /**
     * Returns an integer value of duration
     */
    public int getSuggestedDelayDuration(ObservableList<Appointment> existingAppointmentList,
                                              Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = MINIMUM_INTERVAL;
        int correctDuration = CORRECT_DURATION;

        for (Appointment earlierAppointment : existingAppointmentList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (newAppointmentDate.equals(earlierAppointmentDate)) {
                if (earlierAppointmentTime.getHour() < hour
                        || (earlierAppointmentTime.getHour() == hour && earlierAppointmentTime.getMinute() < min)) {
                    interval = appointment.calDurationDifferencePositive(earlierAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = calInterval(earlierAppointment.getDuration().getDurationValue(), minInterval);
                    }
                }
            }
        }
        return correctDuration;
    }

    /**
     * Returns an integer value of duration.
     */
    public int getSuggestedMaxDuration(ObservableList<Appointment> existingAppointmentList,
                                            Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();
        int interval;
        int minInterval = appointment.getDuration().getDurationValue();

        for (Appointment laterAppointment : existingAppointmentList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (newAppointmentDate.equals(laterAppointmentDate)) {
                if (laterAppointmentTime.getHour() > hour
                        || (laterAppointmentTime.getHour() == hour && min < laterAppointmentTime.getMinute())) {

                    interval = appointment.calDurationDifferenceNegative(laterAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                    }
                }
            }
        }
        return minInterval;
    }

    /**
     * Return the interval between two given integer values
     */
    public int calInterval(int first, int second) {
        return first - second;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.scheduleAppointment(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAppointmentException e1) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentCloseToPreviousException e2) {
            int suggestedDelayDuration = getSuggestedDelayDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_PREVIOUS + MESSAGE_SUGGESTION_TIME
                    + Integer.toString(suggestedDelayDuration) + MINUTE_SUFFIX);
        } catch (AppointmentCloseToNextException e3) {
            int suggestedMaxDuration = getSuggestedMaxDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_NEXT + MESSAGE_SUGGESTION_DURATION
                        + Integer.toString(suggestedMaxDuration) + MINUTE_SUFFIX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleCommand // instanceof handles nulls
                && toAdd.equals(((ScheduleCommand) other).toAdd));
    }

}
