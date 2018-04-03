package seedu.address.logic.autocomplete;

import static junit.framework.TestCase.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddAppointmentToPetCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.commands.AddVetTechToAppointmentCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RemoveAppointmentFromPetCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.Prefix;

public class CommandParameterSyntaxHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandParameterSyntaxHandler handler;

    @Before
    public void setup() {
        handler = new CommandParameterSyntaxHandler();
    }

    @Test
    public void getMissingPrefix_addCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddCommand.COMMAND_WORD, AddCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_editCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(EditCommand.COMMAND_WORD,
                EditCommand.COMMAND_WORD);
        assertEquals(result, handler.EDIT_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_scheduleCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(ScheduleCommand.COMMAND_WORD,
                ScheduleCommand.COMMAND_WORD);
        assertEquals(result, handler.SCHEDULE_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddPetCommand.COMMAND_WORD,
                AddPetCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addApptToPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddAppointmentToPetCommand.COMMAND_WORD,
                AddAppointmentToPetCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_APPT_TO_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_removeApptFromPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(RemoveAppointmentFromPetCommand.COMMAND_WORD,
                RemoveAppointmentFromPetCommand.COMMAND_WORD);
        assertEquals(result, handler.REMOVE_APPT_FROM_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addVetTechToAppointmentCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddVetTechToAppointmentCommand.COMMAND_WORD,
                AddVetTechToAppointmentCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES);
    }

}
