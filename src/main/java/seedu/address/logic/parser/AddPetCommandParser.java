package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetAge;
import seedu.address.model.pet.PetGender;
import seedu.address.model.pet.PetName;
import seedu.address.model.tag.Tag;

//@@author md-azsa
/**
 * Parses the input arguments and create a new AddPetCommand object.
 */
public class AddPetCommandParser implements Parser<AddPetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPetCommand
     * and returns an AddPetCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddPetCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLIENT_INDEX,
                        PREFIX_PET_NAME, PREFIX_PET_AGE, PREFIX_PET_GENDER, PREFIX_TAG);

        Index indexClient;

        if (!arePrefixesPresent(argMultimap, PREFIX_CLIENT_INDEX, PREFIX_PET_NAME, PREFIX_PET_AGE,
                PREFIX_PET_GENDER, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetCommand.MESSAGE_USAGE));
        }

        try {
            indexClient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT_INDEX).get());
            PetName petName = ParserUtil.parsePetName(argMultimap.getValue(PREFIX_PET_NAME).get());
            PetAge petAge = ParserUtil.parsePetAge(argMultimap.getValue(PREFIX_PET_AGE).get());
            PetGender petGender = ParserUtil.parsePetGender(argMultimap.getValue(PREFIX_PET_GENDER).get());
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Pet pet = new Pet(petName, petAge, petGender, tagList);

            return new AddPetCommand(pet, indexClient);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty values in the argument.
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
