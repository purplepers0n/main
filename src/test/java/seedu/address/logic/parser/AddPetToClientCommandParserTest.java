package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddPetToClientCommand;

public class AddPetToClientCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetToClientCommand.MESSAGE_USAGE);

    private AddPetToClientCommandParser parser = new AddPetToClientCommandParser();

    @Test
    public void parse_missingParts_failure() {

        //no index specified
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);

        //only one index specified
        assertParseFailure(parser, " p/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " c/1", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, " 1 2", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, " p/-1 c/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/-1 c/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1 c/-1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, " p/0 c/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1 c/0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/0 c/0", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed
        assertParseFailure(parser, " p/1 wefsef c/0", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index petIndex = INDEX_FIRST_PERSON;
        Index clientIndex = INDEX_FIRST_PERSON;

        String userInput = " p/" + petIndex.getOneBased() + " c/" + clientIndex.getOneBased();

        AddPetToClientCommand expectedCommand = new AddPetToClientCommand(petIndex, clientIndex);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index petIndex = INDEX_SECOND_PERSON;
        Index clientIndex = INDEX_SECOND_PERSON;

        String userInput = " p/" + petIndex.getZeroBased() + " c/" + clientIndex.getZeroBased()
                + " c/" + clientIndex.getOneBased() + " p/" + petIndex.getOneBased();

        AddPetToClientCommand expectedCommand = new AddPetToClientCommand(petIndex, clientIndex);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
