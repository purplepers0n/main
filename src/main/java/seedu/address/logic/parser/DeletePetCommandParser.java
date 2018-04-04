package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeletePetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author md-azsa
/**
 * Parses input arguments and creates a new DeletePetCommand object
 */
public class DeletePetCommandParser implements Parser<DeletePetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of DeletePetCommand
     * returns DeletePetCommand object for execution
     * @throws ParseException if user input does not conform to expected format
     */
    @Override
    public DeletePetCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeletePetCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePetCommand.MESSAGE_USAGE));
        }
    }
}
