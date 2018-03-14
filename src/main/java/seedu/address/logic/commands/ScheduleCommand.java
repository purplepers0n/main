package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Schedule the date and time for an appointment
 */
public class ScheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "schedule";

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        return null;
    }
}
