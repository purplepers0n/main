package seedu.address.logic.autocomplete;

import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERSON_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.AddCommand;

//@@author jonathanwj
public class AutoCompleteManagerTest {

    public static final String NOT_FOUND_KEYWORD = "NotFoundKeyword";
    public static final String ADD_COMMAND_SAMPLE = AddCommand.COMMAND_WORD + " "
            + PREFIX_PERSON_ROLE + "client "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends ";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AutoCompleteManager autoCompleteManger = new AutoCompleteManager();

    @Test
    public void getAutoCompleteCommands_preFixKeyWordInCommandTrie_notEmptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands("add");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void getAutoCompleteCommands_emptyKeyWord_notEmptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands("");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void getAutoCompleteCommands_preFixKeyWordNotInCommandTrie_emptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands(NOT_FOUND_KEYWORD);
        assertTrue(listOfWords.isEmpty());
    }

    @Test
    public void getAutoCompleteCommands_nullKeyWord_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        autoCompleteManger.getAutoCompleteCommands(null);
    }

    @Test
    public void getAutoCompleteCommands_emptyInput_emptyString() {
        String result = autoCompleteManger.getAutoCompleteNextMissingParameter("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void getAutoCompleteNextMissingParameter_noMorePrefix_emptyString() {
        String result = autoCompleteManger.getAutoCompleteNextMissingParameter(ADD_COMMAND_SAMPLE);
        assertTrue(result.isEmpty());
    }

}
