package seedu.address.logic.autocomplete;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CLIENT_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_AGE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_VETTECH_INDEX;

import java.util.ArrayList;
import java.util.Arrays;

import seedu.address.logic.commands.AddAppointmentToPetCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.commands.AddVetTechToAppointmentCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.RemoveAppointmentFromPetCommand;
import seedu.address.logic.commands.RescheduleCommand;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.Prefix;

//@@author jonathanwj

/**
 * Contains Command syntax definitions for multiple commands
 */
public class CommandParameterSyntaxHandler {

    public static final ArrayList<Prefix> ADD_COMMAND_PREFIXES = getListOfPrefix(PREFIX_PERSON_ROLE,
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

    public static final ArrayList<Prefix> ADD_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_CLIENT_INDEX,
            PREFIX_PET_NAME, PREFIX_PET_AGE, PREFIX_PET_GENDER, PREFIX_TAG);

    public static final ArrayList<Prefix> EDIT_COMMAND_PREFIXES = getListOfPrefix(PREFIX_PERSON_ROLE,
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

    public static final ArrayList<Prefix> ADD_APPT_TO_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_APPOINTMENT_INDEX,
            PREFIX_PET_INDEX);

    public static final ArrayList<Prefix>
            REMOVE_APPT_FROM_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_APPOINTMENT_INDEX);

    public static final ArrayList<Prefix> SCHEDULE_COMMAND_PREFIXES = getListOfPrefix(PREFIX_DATE, PREFIX_TIME,
            PREFIX_DURATION, PREFIX_DESCRIPTION);

    public static final ArrayList<Prefix> ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES = getListOfPrefix(PREFIX_VETTECH_INDEX,
            PREFIX_APPOINTMENT_INDEX);

    public static final ArrayList<Prefix> RESCHEDULE_COMMAND_PREFIXES = getListOfPrefix(PREFIX_DATE,
            PREFIX_TIME, PREFIX_DURATION, PREFIX_DESCRIPTION);

    /**
     * Returns ArrayList of prefixes from given prefixes
     */
    private static ArrayList<Prefix> getListOfPrefix(Prefix... prefixes) {
        return new ArrayList<>(Arrays.asList(prefixes));
    }

    /**
     * Returns ArrayList of missing prefixes based on a String command and current user text input
     */
    public ArrayList<Prefix> getMissingPrefixes(String command, String input) {
        ArrayList<Prefix> missingPrefixes = new ArrayList<>();

        switch (command) {

        case AddCommand.COMMAND_WORD:
            ADD_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case EditCommand.COMMAND_WORD:
            EDIT_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case ScheduleCommand.COMMAND_WORD:
            SCHEDULE_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case AddPetCommand.COMMAND_WORD:
            ADD_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;


        case AddAppointmentToPetCommand.COMMAND_WORD:
            ADD_APPT_TO_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case RemoveAppointmentFromPetCommand.COMMAND_WORD:
            REMOVE_APPT_FROM_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case AddVetTechToAppointmentCommand.COMMAND_WORD:
            ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case RescheduleCommand.COMMAND_WORD:
            RESCHEDULE_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        default:
            break;
        }

        return missingPrefixes;
    }
}
