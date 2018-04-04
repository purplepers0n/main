package seedu.address.logic.autocomplete;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

//@@author jonathanwj
public class TrieTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void insert_duplicateWord_sameSize() {
        Trie trie = new Trie();
        assertEquals(trie.size(), 0);
        trie.insertWord("word1");
        assertEquals(trie.size(), 1);
        trie.insertWord("word1");
        assertEquals(trie.size(), 1);
    }

    @Test
    public void insert_nullWord_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        Trie trie = new Trie();
        trie.insertWord(null);
    }
}
