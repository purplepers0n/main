package seedu.address.logic.commands;

import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PET;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ChangeListTabEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_ALIAS = "ls";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists the specified type\n"
            + "Parameters: TYPE (must be either client, pet or vettech)\n"
            + "Example: " + COMMAND_WORD + " pet";

    public static final String MESSAGE_SUCCESS = "Listed all %1$s" + "s";

    private final String targetType;

    public ListCommand(String targetType) {
        this.targetType = targetType;
    }


    @Override
    public CommandResult execute() throws CommandException {

        switch (targetType) {
        case "client":
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            EventsCenter.getInstance().post(new ChangeListTabEvent(0));
            break;

        case "pet":
            model.updateFilteredPetList(PREDICATE_SHOW_ALL_PET);
            EventsCenter.getInstance().post(new ChangeListTabEvent(1));
            break;

        case "vettech":
            //model.updateFilteredVetTechList(PREDICATE_SHOW_ALL_VETTECH);
            EventsCenter.getInstance().post(new ChangeListTabEvent(2));
            break;

        default:
            throw new CommandException(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, targetType));
    }
}
