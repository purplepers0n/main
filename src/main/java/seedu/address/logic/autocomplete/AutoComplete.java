package seedu.address.logic.autocomplete;

import java.util.List;

/**
 * API of AutoComplete component
 */
public interface AutoComplete {
    /**
     * Returns an {@code ArrayList<String>} of auto-completed words
     */
    List<String> getAutoCompleteWords(String keyWord);
}
