package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Duration;
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
    private static final int CONVERSION_TIME = 60;
    private static final Time DEFAULT_TIME = new Time("23:59");

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
    public void durationCheckPrevious(ObservableList<Appointment> existingAppointmentList)
            throws AppointmentCloseToPreviousException {

        Date newAppointmentDate = this.toAdd.getDate();
        Time newAppointmentTime = this.toAdd.getTime();
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
                    interval = (hour - earlierAppointmentTime.getHour()) * CONVERSION_TIME
                            + (min - earlierAppointmentTime.getMinute());
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = earlierAppointment.getDuration().getDurationValue();
                    }
                }
            }
        }
        if (minInterval < correctDuration) {
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

        for (Appointment laterAppointment : existingAppointmentList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (newAppointmentDate.equals(laterAppointmentDate)) {
                if (laterAppointmentTime.getHour() > hour
                        || (laterAppointmentTime.getHour() == hour && min < laterAppointmentTime.getMinute())) {
                    interval = (laterAppointmentTime.getHour() - hour) * CONVERSION_TIME
                            + (laterAppointmentTime.getMinute() - min);
                    if (interval < minInterval) {
                        throw new AppointmentCloseToNextException(" Appointment is too close to later one");
                    }
                }
            }
        }
    }

    /**
     * Returns a duration
     */
    public Duration getSuggestedDelayDuration(ObservableList<Appointment> existingAppointmentList,
                                              Appointment appointment) {
        Date newAppointmentDate = appointment.getDate();
        Time newAppointmentTime = appointment.getTime();
        int min = newAppointmentTime.getMinute();
        int hour = newAppointmentTime.getHour();

        int interval;
        int minInterval = MINIMUM_INTERVAL;
        int correctDuration = CORRECT_DURATION;

        Time previous = DEFAULT_TIME;

        for (Appointment earlierAppointment : existingAppointmentList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (newAppointmentDate.equals(earlierAppointmentDate)) {
                if (earlierAppointmentTime.getHour() < hour
                        || (earlierAppointmentTime.getHour() == hour && earlierAppointmentTime.getMinute() < min)) {
                    interval = (hour - earlierAppointmentTime.getHour()) * CONVERSION_TIME
                            + (min - earlierAppointmentTime.getMinute());
                    if (interval < minInterval) {
                        minInterval = interval;
                        correctDuration = earlierAppointment.getDuration().getDurationValue() - minInterval;
                        previous = earlierAppointmentTime;
                    }
                }
            }
        }

        return new Duration(correctDuration);
    }

    /**
     * Returns a duration.
     */
    public Duration getSuggestedMaxDuration(ObservableList<Appointment> existingAppointmentList,
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
                    interval = (laterAppointmentTime.getHour() - hour) * CONVERSION_TIME
                            + (laterAppointmentTime.getMinute() - min);
                    if (interval < minInterval) {
                        minInterval = interval;
                    }
                }
            }
        }
        return new Duration(minInterval);
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            durationCheckPrevious(model.getFilteredAppointmentList());
            durationCheckNext(model.getFilteredAppointmentList());
            model.scheduleAppointment(toAdd);
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateAppointmentException e1) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentCloseToPreviousException e2) {
            Duration suggestedDelayDuration = getSuggestedDelayDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_PREVIOUS + MESSAGE_SUGGESTION_TIME
                    + suggestedDelayDuration.toString() + MINUTE_SUFFIX);
        } catch (AppointmentCloseToNextException e3) {
            Duration suggestedMaxDuration = getSuggestedMaxDuration(model.getFilteredAppointmentList(), toAdd);
            throw new CommandException(MESSAGE_CLOSE_APPOINTMENT_NEXT + MESSAGE_SUGGESTION_DURATION
                    + suggestedMaxDuration.toString() + MINUTE_SUFFIX);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ScheduleCommand // instanceof handles nulls
                && toAdd.equals(((ScheduleCommand) other).toAdd));
    }

}
