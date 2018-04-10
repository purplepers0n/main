# jonathanwj
###### \java\seedu\address\logic\autocomplete\AutoCompleteManagerTest.java
``` java
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
###### \java\seedu\address\model\association\AssociatePetToClientTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for associating pet to client.
 */
public class AssociatePetToClientTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removePetFromClient_alreadyAssociated_success() throws ClientAlreadyOwnsPetException,
            PetAlreadyHasOwnerException,
            ClientPetAssociationNotFoundException {
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddressBook expectedAddressBook = getTypicalAddressBook();
        expectedAddressBook.removePetFromClient(petInFilteredList, clientInFilteredList);

        model.removePetFromClient(petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(expectedAddressBook), new UserPrefs());

        model.updateFilteredPetList(Model.PREDICATE_SHOW_ALL_PET);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_CLIENTS);

        assertEquals(expectedModel, model);
    }

    @Test
    public void removePetFromClient_noAssociated_throwsClientPetAssociationNotFoundException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(ClientPetAssociationNotFoundException.class);
        showPetAtIndex(model, Index.fromOneBased(3));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.removePetFromClient(petInFilteredList, clientInFilteredList);

    }

    @Test
    public void addPettoClient_noAssociation_success() throws ClientAlreadyOwnsPetException,
            PetAlreadyHasOwnerException,
            ClientPetAssociationNotFoundException {
        showPetAtIndex(model, Index.fromOneBased(2));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddressBook expectedAddressBook = getTypicalAddressBook();
        expectedAddressBook.addPetToClient(petInFilteredList, clientInFilteredList);

        model.addPetToClient(petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(expectedAddressBook), new UserPrefs());

        model.updateFilteredPetList(Model.PREDICATE_SHOW_ALL_PET);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_CLIENTS);

        assertEquals(expectedModel, model);
    }

    @Test
    public void addPettoClient_alreadyAssociated_throwsClientAlreadyOwnsPetException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(ClientAlreadyOwnsPetException.class);
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(6));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.addPetToClient(petInFilteredList, clientInFilteredList);

    }

    @Test
    public void addPettoClient_petAlreadyHasOwner_throwsPetAlreadyHasOwnerException()
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException, ClientPetAssociationNotFoundException {
        thrown.expect(PetAlreadyHasOwnerException.class);
        showPetAtIndex(model, Index.fromOneBased(4));
        showClientAtIndex(model, Index.fromOneBased(4));

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        model.addPetToClient(petInFilteredList, clientInFilteredList);

    }

}
```
