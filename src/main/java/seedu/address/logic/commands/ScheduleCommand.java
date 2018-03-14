package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Time;

/**
 * Schedule the date and time for an appointment
 */
public class ScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "schedule";

    private final Time time;
    private final Date date;

    /**
     * @param time of the appointment to be scheduled
     * @param date of the appointment to be scheduled
     */
    public ScheduleCommand(Time time, Date date) {
        requireNonNull(time);
        requireNonNull(date);

        this.time = time;
        this.date = date;
    }
    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        return null;
    }
}
