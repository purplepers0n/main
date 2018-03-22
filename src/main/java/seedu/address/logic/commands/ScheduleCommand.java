package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
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

/**
 * Schedule the date and time for an appointment as well as the duration of this appointment
 */
public class ScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "schedule";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Schedule an appointment with "
            + "date, time and duration.  "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + PREFIX_DURATION + "DURATION\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2018-05-01 "
            + PREFIX_TIME + "15:15 "
            + PREFIX_DURATION + "60 ";

    public static final String MESSAGE_SUCCESS = "New appointment scheduled";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "The date and time are taken ";
    public static final String MESSAGE_CLOSE_APPOINTMENT_PREVIOUS = "The appointment is to close to previous one";
    public static final String MESSAGE_CLOSE_APPOINTMENT_NEXT = "The appointment is to close to next one";

    private final Appointment toAdd;

    /**
     * Creates an ScheduleCommand to add the specified {@code appointment}
     */
    public ScheduleCommand(Appointment appointment) {
        requireNonNull(appointment);

        this.toAdd = appointment;
    }

    /**
     * Check that there is no earlier existing appointment too close
     */
    public void durationCheckPrevoius(ObservableList<Appointment> existingAppointmentList)
            throws AppointmentCloseToPreviousException {

        Date newAppointmentDate = this.toAdd.getDate();
        Time newAppointmentTime = this.toAdd.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = 1440;
        int correctDuration = 120;

        for (Appointment earlierAppointment: existingAppointmentList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (earlierAppointmentDate.equals(newAppointmentDate)) {
                if ((earlierAppointmentTime.getHour() <= hour) & (min != earlierAppointmentTime.getMinute())) {
                    interval = (hour - earlierAppointmentTime.getHour()) * 60
                            + (min - earlierAppointmentTime.getMinute());
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = earlierAppointment.getDuration().getDurationValue();
                    }
                }
            }
        }
        if (minInterval <= correctDuration) {
            throw new AppointmentCloseToPreviousException(" Appointment is too close to earlier one");
        }
    }

    /**
     * Check that there is no later existing appointment too close
     */
    public void durationCheckNext(ObservableList<Appointment> existingAppointmentList)
            throws AppointmentCloseToNextException {

        Date newAppointmentDate = this.toAdd.getDate();
        Time newAppointmentTime = this.toAdd.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = this.toAdd.getDuration().getDurationValue();

        for (Appointment laterAppointment: existingAppointmentList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (laterAppointmentDate.equals(newAppointmentDate)) {
                if ((laterAppointmentTime.getHour() >= hour) & (min != laterAppointmentTime.getMinute())) {
                    interval = (laterAppointmentTime.getHour() - hour) * 60
                            + (laterAppointmentTime.getMinute() - min);
                    if (interval <= minInterval) {
                        throw new AppointmentCloseToNextException(" Appointment is too close to later one");
                    }
                }
            }
        }
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            durationCheckPrevoius(model.getFilteredAppointmentList());
            durationCheckNext(model.getFilteredAppointmentList());
            model.scheduleAppointment(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAppointmentException e1) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentCloseToPreviousException e2) {
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_PREVIOUS);
        } catch (AppointmentCloseToNextException e3) {
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_NEXT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleCommand // instanceof handles nulls
                && toAdd.equals(((ScheduleCommand) other).toAdd));
    }

}
