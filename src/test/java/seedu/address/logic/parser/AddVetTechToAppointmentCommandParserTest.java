package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VETTECH_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddVetTechToAppointmentCommand;

public class AddVetTechToAppointmentCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddVetTechToAppointmentCommand.MESSAGE_USAGE);

    private AddVetTechToAppointmentCommandParser parser = new AddVetTechToAppointmentCommandParser();

    @Test
    public void parse_missingParts_failure() {

        //no index specified
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);

        //only one index specified
        assertParseFailure(parser, PREFIX_VETTECH_INDEX + "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "" + PREFIX_APPOINTMENT_INDEX + "1", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, " 1 2", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "-1" + PREFIX_APPOINTMENT_INDEX + "-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "-1" + PREFIX_APPOINTMENT_INDEX + "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "1" + PREFIX_APPOINTMENT_INDEX + "-1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "0" + PREFIX_APPOINTMENT_INDEX + "1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "1" + PREFIX_APPOINTMENT_INDEX + "0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "0" + PREFIX_APPOINTMENT_INDEX + "0", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed
        assertParseFailure(parser, PREFIX_VETTECH_INDEX
                + "1 wePREFIX_APPOINTMENT_INDEX" + PREFIX_APPOINTMENT_INDEX + "0", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index petIndex = INDEX_FIRST_PERSON;
        Index clientIndex = INDEX_FIRST_PERSON;

        String userInput = " " + PREFIX_VETTECH_INDEX + ""
                + petIndex.getOneBased() + " " + PREFIX_APPOINTMENT_INDEX + "" + clientIndex.getOneBased();

        AddVetTechToAppointmentCommand expectedCommand = new AddVetTechToAppointmentCommand(petIndex, clientIndex);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
