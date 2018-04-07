package seedu.address.logic.autocomplete;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author jonathanwj
public class AutoCompleteManagerTest {

    public static final String NOT_FOUND_KEYWORD = "NotFoundKeyword";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AutoCompleteManager autoCompleteManger = new AutoCompleteManager();

    @Test
    public void autoCompleteCommands_preFixKeyWordInCommandTrie_notEmptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands("add");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_emptyKeyWord_notEmptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands("");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_preFixKeyWordNotInCommandTrie_emptyList() {
        List<String> listOfWords = autoCompleteManger.getAutoCompleteCommands(NOT_FOUND_KEYWORD);
        assertTrue(listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_nullKeyWord_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        autoCompleteManger.getAutoCompleteCommands(null);
    }

    @Test
    public void autoCompleteParameter_emptyInput_emptyString() {
        String result = autoCompleteManger.getAutoCompleteNextMissingParameter("");
        assertTrue(result.isEmpty());
    }

}
