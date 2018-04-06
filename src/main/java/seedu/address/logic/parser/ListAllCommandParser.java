package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ListAllCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@author purplepers0n
/**
 * Parses the input arguments and creates a new ListAllCommand object.
 */
public class ListAllCommandParser implements Parser<ListAllCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the ListAllCommand
     * returns the specified object for execution.
     *
     * @throws ParseException if the user does not conform to expected format.
     */
    public ListAllCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new ListAllCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAllCommand.MESSAGE_USAGE));
        }
    }
}
