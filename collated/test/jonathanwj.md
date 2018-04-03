# jonathanwj
###### \java\seedu\address\logic\autocomplete\AutoCompleteTest.java
``` java
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
```
###### \java\seedu\address\logic\autocomplete\TrieTest.java
``` java
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
```
