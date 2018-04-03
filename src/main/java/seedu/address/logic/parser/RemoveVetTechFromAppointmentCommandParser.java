package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemoveVetTechFromAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new RemoveVetTechFromAppointmentCommand object
 */
public class RemoveVetTechFromAppointmentCommandParser implements Parser<RemoveVetTechFromAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of RemoveVetTechFromAppointmentCommand
     * returns RemoveVetTechFromAppointmentCommand object for execution
     * @throws ParseException if user input does not conform to expected format
     */
    @Override
    public RemoveVetTechFromAppointmentCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RemoveVetTechFromAppointmentCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveVetTechFromAppointmentCommand.MESSAGE_USAGE));
        }
    }
}
