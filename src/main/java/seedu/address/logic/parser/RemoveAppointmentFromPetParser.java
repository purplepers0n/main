package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemoveAppointmentFromPetCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author md-azsa
/**
 * Parses input arguments and creates a new RemoveAppointmentFromPet object
 */
public class RemoveAppointmentFromPetParser implements
        Parser<RemoveAppointmentFromPetCommand> {

    @Override
    public RemoveAppointmentFromPetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_INDEX);

        Index appointmentIndex;

        if (!arePrefixesPresent(argMultimap, PREFIX_APPOINTMENT_INDEX)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveAppointmentFromPetCommand.MESSAGE_USAGE));
        }

        try {
            appointmentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPOINTMENT_INDEX).get());
        } catch (IllegalValueException ie) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveAppointmentFromPetCommand.MESSAGE_USAGE));
        }
        return new RemoveAppointmentFromPetCommand(appointmentIndex);
    }

    /**
     * Returns true if none of the prefixes contains empty
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
