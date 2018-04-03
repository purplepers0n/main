package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddPetToClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author jonathanwj-unused
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Parses input arguments and creates a new AddPetToClientCommand object
 */
public class AddPetToClientCommandParser implements Parser<AddPetToClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPetToClientCommand
     * and returns an AddPetToClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPetToClientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX);

        Index indexPet;
        Index indexClient;

        if (!arePrefixesPresent(argMultimap, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPetToClientCommand.MESSAGE_USAGE));
        }

        try {
            indexPet = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
            indexClient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPetToClientCommand.MESSAGE_USAGE));
        }

        return new AddPetToClientCommand(indexPet, indexClient);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
