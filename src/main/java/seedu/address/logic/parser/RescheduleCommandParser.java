package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RescheduleCommand;
import seedu.address.logic.commands.RescheduleCommand.RescheduleAppointmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class RescheduleCommandParser implements Parser<RescheduleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RescheduleCommand
     * and returns an RescheduleCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RescheduleCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME, PREFIX_DURATION, PREFIX_DESCRIPTION);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RescheduleCommand.MESSAGE_USAGE));
        }

        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptor();
        try {
            ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).ifPresent(descriptor::setDate);
            ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME)).ifPresent(descriptor::setTime);
            ParserUtil.parseDuration(argMultimap.getValue(PREFIX_DURATION)).ifPresent(descriptor::setDuration);
            ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).ifPresent(descriptor::setDescription);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!descriptor.isAnyFieldEdited()) {
            throw new ParseException(RescheduleCommand.MESSAGE_NOT_RESCHEDULED);
        }

        return new RescheduleCommand(index, descriptor);
    }

}
