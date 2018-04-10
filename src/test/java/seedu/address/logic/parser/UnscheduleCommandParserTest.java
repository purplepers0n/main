package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;

import org.junit.Test;

import seedu.address.logic.commands.UnscheduleCommand;

//@@author md-azsa
/**
 * Testing for UnschedulCommandParser
 */
public class UnscheduleCommandParserTest {

    private UnscheduleCommandParser parser = new UnscheduleCommandParser();

    @Test
    public void parse_validArgs_returnsUnscheduleCommand() {
        assertParseSuccess(parser, "1", new UnscheduleCommand(INDEX_FIRST_APPT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnscheduleCommand.MESSAGE_USAGE));
    }

}
