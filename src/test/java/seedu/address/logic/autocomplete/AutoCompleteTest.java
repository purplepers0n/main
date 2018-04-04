package seedu.address.logic.autocomplete;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author jonathanwj
public class AutoCompleteTest {

    public static final String NOT_FOUND_KEYWORD = "NotFoundKeyword";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private AutoComplete autoComplete;

    @Before
    public void setup() {
        autoComplete = new AutoComplete();
    }

    @Test
    public void autoCompleteCommands_preFixKeyWordInCommandTrie_notEmptyList() {
        List<String> listOfWords = autoComplete.autoCompleteCommands("add");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_emptyKeyWord_notEmptyList() {
        List<String> listOfWords = autoComplete.autoCompleteCommands("");
        assertTrue(!listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_preFixKeyWordNotInCommandTrie_emptyList() {
        List<String> listOfWords = autoComplete.autoCompleteCommands(NOT_FOUND_KEYWORD);
        assertTrue(listOfWords.isEmpty());
    }

    @Test
    public void autoCompleteCommands_nullKeyWord_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        List<String> listOfWords = autoComplete.autoCompleteCommands(null);
    }

    @Test
    public void autoCompleteParameter_emptyInput_emptyString() {
        String result = autoComplete.autoCompleteNextMissingParameter("");
        assertTrue(result.isEmpty());
    }

}
