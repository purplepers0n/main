package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_APPT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PET;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAppointmentToPetCommand;

public class AddAppointmentToPetCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE);

    private AddAppointmentToPetCommandParser parser = new AddAppointmentToPetCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "adsdsf", MESSAGE_INVALID_FORMAT);

        // one index specified
        assertParseFailure(parser, " appt/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1", MESSAGE_INVALID_FORMAT);

        // wrong prefixes
        assertParseFailure(parser, " appt/1 sss/1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, " appt/-11 p/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/1 p/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/-1 p/1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, " appt/0 p/0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/0 p/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/1 p/0", MESSAGE_INVALID_FORMAT);

        // characters used
        assertParseFailure(parser, " appt/2 p/#$@$", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/fe!@#$ p/;;;", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validCommand() {
        Index apptIndex = INDEX_FIRST_APPT;
        Index petIndex = INDEX_FIRST_PET;

        String userInput = " appt/" + apptIndex.getZeroBased() + " p/" + petIndex.getZeroBased()
                + " appt/" + apptIndex.getOneBased() + " p/" + petIndex.getOneBased();
        AddAppointmentToPetCommand expected = new AddAppointmentToPetCommand(apptIndex, petIndex);
        // all arguments fulfilled and supplied
        assertParseSuccess(parser, userInput, expected);
    }
}
