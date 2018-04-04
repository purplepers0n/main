package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddAppointmentToPetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author md-azsa
/**
 * Parses the input arguments and creates a new AddAppointmentToPet object.
 */
public class AddAppointmentToPetCommandParser implements Parser<AddAppointmentToPetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentToPetCommand
     * returns the specified object for execution.
     * @throws ParseException if the user does not conform to expected format.
     */
    public AddAppointmentToPetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_INDEX, PREFIX_PET_INDEX);
        Index appointmentIndex;
        Index petIndex;

        if (!arePrefixesPresent(argMultiMap, PREFIX_APPOINTMENT_INDEX, PREFIX_PET_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE));
        }

        try {
            appointmentIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_APPOINTMENT_INDEX).get());
            petIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_PET_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE));
        }

        return new AddAppointmentToPetCommand(appointmentIndex, petIndex);
    }

    /**
     * Returns true if the prefixes contain the values
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
