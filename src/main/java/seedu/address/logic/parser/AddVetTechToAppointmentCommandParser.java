package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VETTECH_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddVetTechToAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author jonathanwj-reused
/**
 * Parses input arguments and creates a new AddVetTechToAppointmentCommand object
 */
public class AddVetTechToAppointmentCommandParser implements Parser<AddVetTechToAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddVetTechToAppointmentCommand
     * and returns an AddVetTechToAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddVetTechToAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_VETTECH_INDEX, PREFIX_APPOINTMENT_INDEX);

        Index indexVetTech;
        Index indexAppointment;

        if (!arePrefixesPresent(argMultimap, PREFIX_VETTECH_INDEX, PREFIX_APPOINTMENT_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddVetTechToAppointmentCommand.MESSAGE_USAGE));
        }

        try {
            indexVetTech = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_VETTECH_INDEX).get());
            indexAppointment = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPOINTMENT_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddVetTechToAppointmentCommand.MESSAGE_USAGE));
        }

        return new AddVetTechToAppointmentCommand(indexVetTech, indexAppointment);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
