package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENT;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;


/**
 * Edits or reschedules the details of an existing appointment in the address book.
 */
public class RescheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "reschedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details, reschedules the time or deletes "
            + "the appointment identified "
            + "by the index number used in the appointment list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_TIME + "TIME] "
            + "[" + PREFIX_DURATION + "DURATION] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "2018-04-01 "
            + PREFIX_TIME + "16:00";

    public static final String MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS = "Reschedules Appointment: %1$s";
    public static final String MESSAGE_NOT_RESCHEDULED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book.";

    private final Index index;
    private final RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor;
    private int currList = 0; //default is on client list upon opening app

    private Appointment appointmentToReschedule;
    private Appointment rescheduledAppointment;

    /**
     * @param index                of the appointment in the filtered appointment list to edit(reschedule)
     * @param rescheduleAppointmentDescriptor details to edit the person with
     */
    public RescheduleCommand(Index index, RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor) {
        requireNonNull(index);
        requireNonNull(rescheduleAppointmentDescriptor);

        this.index = index;
        this.rescheduleAppointmentDescriptor = new RescheduleAppointmentDescriptor(rescheduleAppointmentDescriptor);
    }

    public void setCurrentList() {
        this.currList = model.getCurrentList();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        } catch (DuplicateAppointmentException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException anfe) {
            throw new AssertionError("The target appointment cannot be missing");
        }
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);

        return new CommandResult(String.format(MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS, rescheduledAppointment));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<? extends Appointment> lastShownList;

        setCurrentList();

        if (currList == 0) {
            lastShownList = model.getFilteredAppointmentList();
        } else {
            throw new CommandException("Not currently on a list that 'reschedule' command can change");
        }

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        appointmentToReschedule = lastShownList.get(index.getZeroBased());
        rescheduledAppointment = createRescheduledAppointment(appointmentToReschedule, rescheduleAppointmentDescriptor);
    }

    /**
     * Creates and returns a {@code Appointment} with the details of {@code appointmentToReschedule}
     * edited with {@code rescheduleAppointmentDescriptor}.
     */
    private static Appointment createRescheduledAppointment(
            Appointment appointmentToReschedule, RescheduleAppointmentDescriptor rescheduleAppointmentDescriptor) {
        assert appointmentToReschedule != null;

        Date updatedDate = rescheduleAppointmentDescriptor.getDate().orElse(appointmentToReschedule.getDate());
        Time updatedTime = rescheduleAppointmentDescriptor.getTime().orElse(appointmentToReschedule.getTime());
        Duration updatedDuration = rescheduleAppointmentDescriptor.getDuration().orElse(
                appointmentToReschedule.getDuration());
        Description updatedDescription = rescheduleAppointmentDescriptor.getDescription().orElse(
                appointmentToReschedule.getDescription());

        return new Appointment(updatedDate, updatedTime, updatedDuration, updatedDescription);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ScheduleCommand)) {
            return false;
        }

        // state check
        RescheduleCommand r = (RescheduleCommand) other;
        return index.equals(r.index)
                && rescheduleAppointmentDescriptor.equals(r.rescheduleAppointmentDescriptor)
                && Objects.equals(appointmentToReschedule, r.appointmentToReschedule);
    }

    /**
     * Stores the details to reschedule the appointment with. Each non-empty field value will replace the
     * corresponding field value of the appointment.
     */
    public static class RescheduleAppointmentDescriptor {
        private Date date;
        private Time time;
        private Duration duration;
        private Description description;

        public RescheduleAppointmentDescriptor() {
        }

        /**
         * Copy constructor.
         */
        public RescheduleAppointmentDescriptor(RescheduleAppointmentDescriptor toCopy) {
            setDate(toCopy.date);
            setTime(toCopy.time);
            setDuration(toCopy.duration);
            setDescription(toCopy.description);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.date, this.time, this.duration, this.description);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTime(Time time) {
            this.time = time;
        }

        public Optional<Time> getTime() {
            return Optional.ofNullable(time);
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }

        public Optional<Duration> getDuration() {
            return Optional.ofNullable(duration);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof RescheduleAppointmentDescriptor)) {
                return false;
            }

            // state check
            RescheduleAppointmentDescriptor r = (RescheduleAppointmentDescriptor) other;

            return getDate().equals(r.getDate())
                    && getTime().equals(r.getTime())
                    && getDuration().equals(r.getDuration())
                    && getDescription().equals(r.getDescription());
        }

    }
}
