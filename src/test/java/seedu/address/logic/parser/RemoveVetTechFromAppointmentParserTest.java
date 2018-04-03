package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST;

import org.junit.Test;

import seedu.address.logic.commands.RemoveVetTechFromAppointmentCommand;

public class RemoveVetTechFromAppointmentParserTest {

    private RemoveVetTechFromAppointmentCommandParser parser = new RemoveVetTechFromAppointmentCommandParser();

    @Test
    public void parse_validArgs_returnsRemoveCommand() {
        assertParseSuccess(parser, "1", new RemoveVetTechFromAppointmentCommand(INDEX_FIRST));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String
                .format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveVetTechFromAppointmentCommand.MESSAGE_USAGE));
    }
}
