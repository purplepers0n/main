# jonathanwj-unused
###### \AddPetToClientCommand.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Edits the details of an existing person in the address book.
 */
public class AddPetToClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addpettoclient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a owner to a pet "
            + "by the index number used in the last client and pet listing.\n"
            + "Parameters: "
            + PREFIX_PET_INDEX + "PET_INDEX "
            + PREFIX_CLIENT_INDEX + "CLIENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PET_INDEX + "1 " + PREFIX_CLIENT_INDEX + "2";

    public static final String MESSAGE_ADD_PET_TO_CLIENT_SUCCESS = "Added Pet To client:\n%1$s\n>> %2$s";
    public static final String MESSAGE_CLIENT_HAS_PET = "Client already has pet";
    public static final String MESSAGE_PET_HAS_OWNER = "Pet already has an owner";

    private final Index petIndex;
    private final Index clientIndex;

    private Optional<Pet> pet;
    private Optional<Client> client;

    /**
     * @param petIndex of the pet in the filtered pet list to add
     * @param clientIndex of the person in the filtered client list to add pet to
     */
    public AddPetToClientCommand(Index petIndex, Index clientIndex) {
        requireNonNull(petIndex);
        requireNonNull(clientIndex);

        this.petIndex = petIndex;
        this.clientIndex = clientIndex;

        pet = Optional.empty();
        client = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(pet.get());
        requireNonNull(client.get());
        try {
            model.addPetToClient(pet.get(), client.get());
        } catch (ClientAlreadyOwnsPetException e) {
            throw new CommandException(MESSAGE_CLIENT_HAS_PET);
        } catch (PetAlreadyHasOwnerException e) {
            throw new CommandException(MESSAGE_PET_HAS_OWNER);
        }
        return new CommandResult(String.format(MESSAGE_ADD_PET_TO_CLIENT_SUCCESS, pet.get(), client.get()));

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Pet> lastShownListPet = model.getFilteredPetList();
        List<Client> lastShownListClient = model.getFilteredClientList();

        if (clientIndex.getZeroBased() >= lastShownListClient.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        if (petIndex.getZeroBased() >= lastShownListPet.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
        }

        pet = Optional.of(lastShownListPet.get(petIndex.getZeroBased()));
        client = Optional.of(lastShownListClient.get(clientIndex.getZeroBased()));
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPetToClientCommand)) {
            return false;
        }

        // state check
        AddPetToClientCommand e = (AddPetToClientCommand) other;
        return petIndex.equals(e.petIndex)
                && clientIndex.equals(e.clientIndex)
                && pet.equals(e.pet)
                && client.equals(e.client);
    }

}
```
###### \AddPetToClientCommandParser.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Parses input arguments and creates a new AddPetToClientCommand object
 */
public class AddPetToClientCommandParser implements Parser<AddPetToClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPetToClientCommand
     * and returns an AddPetToClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPetToClientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX);

        Index indexPet;
        Index indexClient;

        if (!arePrefixesPresent(argMultimap, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPetToClientCommand.MESSAGE_USAGE));
        }

        try {
            indexPet = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
            indexClient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddPetToClientCommand.MESSAGE_USAGE));
        }

        return new AddPetToClientCommand(indexPet, indexClient);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \AddPetToClientCommandParserTest.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
public class AddPetToClientCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetToClientCommand.MESSAGE_USAGE);

    private AddPetToClientCommandParser parser = new AddPetToClientCommandParser();

    @Test
    public void parse_missingParts_failure() {

        //no index specified
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);

        //only one index specified
        assertParseFailure(parser, " p/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " c/1", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, " 1 2", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, " p/-1 c/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/-1 c/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1 c/-1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, " p/0 c/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1 c/0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/0 c/0", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed
        assertParseFailure(parser, " p/1 wefsef c/0", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index petIndex = INDEX_FIRST_PERSON;
        Index clientIndex = INDEX_FIRST_PERSON;

        String userInput = " p/" + petIndex.getOneBased() + " c/" + clientIndex.getOneBased();

        AddPetToClientCommand expectedCommand = new AddPetToClientCommand(petIndex, clientIndex);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index petIndex = INDEX_SECOND_PERSON;
        Index clientIndex = INDEX_SECOND_PERSON;

        String userInput = " p/" + petIndex.getZeroBased() + " c/" + clientIndex.getZeroBased()
                + " c/" + clientIndex.getOneBased() + " p/" + petIndex.getOneBased();

        AddPetToClientCommand expectedCommand = new AddPetToClientCommand(petIndex, clientIndex);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

}
```
###### \AddPetToClientCommandTest.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for AddPetToClientCommand.
 */
public class AddPetToClientCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_petFilteredList_success() throws Exception {
        showPetAtIndex(model, INDEX_FIRST_PET);
        showClientAtIndex(model, INDEX_FIRST_PERSON);

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(AddPetToClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addPetToClient(model.getFilteredPetList().get(0), model.getFilteredClientList().get(0));

        assertCommandSuccess(aptcCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexClient = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddPetToClientCommand aptcCommand = prepareCommand(outOfBoundIndexPet, INDEX_FIRST_PERSON);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);

        aptcCommand = prepareCommand(INDEX_FIRST_PET, outOfBoundIndexClient);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndexClient = INDEX_SECOND_PERSON;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexClient.getZeroBased() < model.getAddressBook().getClientList().size());

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, outOfBoundIndexClient);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        showPetAtIndex(model, INDEX_FIRST_PET);
        Index outOfBoundIndexPet = INDEX_SECOND_PET;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexPet.getZeroBased() < model.getAddressBook().getClientList().size());

        aptcCommand = prepareCommand(outOfBoundIndexPet, INDEX_FIRST_PERSON);

        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // add -> pet added to client
        aptcCommand.execute();
        undoRedoStack.push(aptcCommand);

        // undo -> reverts addressbook back to previous state and filtered lists to show all
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> add pet back to client
        expectedModel.addPetToClient(model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased()),
                model.getFilteredClientList().get(INDEX_FIRST_PET.getZeroBased()));
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexClient = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddPetToClientCommand aptcCommand = prepareCommand(outOfBoundIndexPet, outOfBoundIndexClient);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Adss a {@code Pet} To a {@code Client} from filtered lists.
     * 2. Undo the command.
     * 3. The unfiltered lists should be shown now. Verify that the index of the previously edited person in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the command. This ensures {@code RedoCommand} adds the pet to client object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showPetAtIndex(model, INDEX_SECOND_PET);
        showClientAtIndex(model, INDEX_SECOND_PERSON);
        Pet petToAdd = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientToAddPetTo = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        // add -> add first client in filtered client list and the first pet in filtered pet list
        aptcCommand.execute();
        undoRedoStack.push(aptcCommand);

        // undo -> reverts addressbook back to previous state and filtered person list to show all clients and pets
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased()), petToAdd);
        assertNotEquals(model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased()), clientToAddPetTo);

        // redo -> add the same pet to client in unfiltered lists
        expectedModel.addPetToClient(petToAdd, clientToAddPetTo);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_addPetToClient_success() throws Exception {
        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPetToClient(petInFilteredList, clientInFilteredList);

        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(AddPetToClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        assertCommandSuccess(aptcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clientAlreadyOwnsPet_throwsCommandException() throws Exception {
        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        aptcCommand.execute();

        assertCommandFailure(aptcCommand, model, AddPetToClientCommand.MESSAGE_CLIENT_HAS_PET);
    }

    @Test
    public void execute_petAlreadyHasOwner_throwsCommandException() throws Exception {
        AddPetToClientCommand aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        aptcCommand.execute();
        aptcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_SECOND_PERSON);

        assertCommandFailure(aptcCommand, model, AddPetToClientCommand.MESSAGE_PET_HAS_OWNER);
    }

    @Test
    public void equals() throws Exception {
        final AddPetToClientCommand standardCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        // same values -> returns true
        AddPetToClientCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddPetToClientCommand(INDEX_SECOND_PET, INDEX_SECOND_PERSON)));

    }

    /**
     * Returns an {@code AddPetToClientCommand} with parameters pet {@code index} and client {@code index}
     */
    private AddPetToClientCommand prepareCommand(Index petIndex, Index clientIndex) {
        AddPetToClientCommand aptcCommand = new AddPetToClientCommand(petIndex, clientIndex);
        aptcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return aptcCommand;
    }
}
```
###### \RemovePetFromClientCommand.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Edits the details of an existing person in the address book.
 */
public class RemovePetFromClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removepetfromclient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Remove a pet from an owner "
            + "by the index number used in the last client and pet listing.\n"
            + "Parameters: "
            + PREFIX_PET_INDEX + "PET_INDEX "
            + PREFIX_CLIENT_INDEX + "CLIENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PET_INDEX + "1 " + PREFIX_CLIENT_INDEX + "2";

    public static final String MESSAGE_ADD_PET_TO_CLIENT_SUCCESS = "Removed Pet from client:\n%1$s\n>> %2$s";
    public static final String MESSAGE_CLIENT_PET_NOT_ASSOCIATED = "Client is not the owner of the indicated pet";

    private final Index petIndex;
    private final Index clientIndex;

    private Optional<Pet> pet;
    private Optional<Client> client;

    /**
     * @param petIndex of the pet in the filtered pet list to add
     * @param clientIndex of the person in the filtered client list to add pet to
     */
    public RemovePetFromClientCommand(Index petIndex, Index clientIndex) {
        requireNonNull(petIndex);
        requireNonNull(clientIndex);

        this.petIndex = petIndex;
        this.clientIndex = clientIndex;

        pet = Optional.empty();
        client = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(pet.get());
        requireNonNull(client.get());
        try {
            model.removePetFromClient(pet.get(), client.get());
        } catch (ClientPetAssociationNotFoundException e) {
            throw new CommandException(MESSAGE_CLIENT_PET_NOT_ASSOCIATED);
        }
        return new CommandResult(String.format(MESSAGE_ADD_PET_TO_CLIENT_SUCCESS, pet.get(), client.get()));

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Pet> lastShownListPet = model.getFilteredPetList();
        List<Client> lastShownListClient = model.getFilteredClientList();

        if (clientIndex.getZeroBased() >= lastShownListClient.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        if (petIndex.getZeroBased() >= lastShownListPet.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
        }

        pet = Optional.of(lastShownListPet.get(petIndex.getZeroBased()));
        client = Optional.of(lastShownListClient.get(clientIndex.getZeroBased()));
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemovePetFromClientCommand)) {
            return false;
        }

        // state check
        RemovePetFromClientCommand e = (RemovePetFromClientCommand) other;
        return petIndex.equals(e.petIndex)
                && clientIndex.equals(e.clientIndex)
                && pet.equals(e.pet)
                && client.equals(e.client);
    }

}
```
###### \RemovePetFromClientCommandParser.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Parses input arguments and creates a new AddPetToClientCommand object
 */
public class RemovePetFromClientCommandParser implements Parser<RemovePetFromClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPetToClientCommand
     * and returns an AddPetToClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemovePetFromClientCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX);

        Index indexPet;
        Index indexClient;

        if (!arePrefixesPresent(argMultimap, PREFIX_PET_INDEX, PREFIX_CLIENT_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemovePetFromClientCommand.MESSAGE_USAGE));
        }

        try {
            indexPet = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
            indexClient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemovePetFromClientCommand.MESSAGE_USAGE));
        }

        return new RemovePetFromClientCommand(indexPet, indexClient);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
```
###### \RemovePetFromClientCommandTest.java
``` java
// Entire command was merged into AddPetCommand that
// currently creates a pet and adds that pet to the client in one command.
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for RemovePetFromClient.
 */
public class RemovePetFromClientCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Test
    public void execute_removePetToClient_success() throws Exception {

        Pet petInFilteredList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());
        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        RemovePetFromClientCommand rpfcCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        String expectedMessage = String.format(RemovePetFromClientCommand.MESSAGE_ADD_PET_TO_CLIENT_SUCCESS,
                petInFilteredList, clientInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.removePetFromClient(model.getFilteredPetList().get(0), model.getFilteredClientList().get(0));

        assertCommandSuccess(rpfcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removePetFromClient_throwsCommandExeception() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(RemovePetFromClientCommand.MESSAGE_CLIENT_PET_NOT_ASSOCIATED);
        prepareCommand(INDEX_FIRST_PET, INDEX_SECOND_PERSON).execute();
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidIndex_failure() {
        showClientAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndexClient = INDEX_SECOND_PERSON;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexClient.getZeroBased() < model.getAddressBook().getClientList().size());

        RemovePetFromClientCommand rpfcCommand = prepareCommand(INDEX_FIRST_PET, outOfBoundIndexClient);

        assertCommandFailure(rpfcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        showPetAtIndex(model, INDEX_FIRST_PET);
        Index outOfBoundIndexPet = INDEX_SECOND_PET;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexPet.getZeroBased() < model.getAddressBook().getClientList().size());

        rpfcCommand = prepareCommand(outOfBoundIndexPet, INDEX_FIRST_PERSON);

        assertCommandFailure(rpfcCommand, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final RemovePetFromClientCommand standardCommand = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);

        // same values -> returns true
        RemovePetFromClientCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PET, INDEX_FIRST_PERSON);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RemovePetFromClientCommand(INDEX_SECOND_PET, INDEX_SECOND_PERSON)));

    }

    /**
     * Returns an {@code RemovePetFromClientCommand} with parameters pet {@code index} and client {@code index}
     */
    private RemovePetFromClientCommand prepareCommand(Index petIndex, Index clientIndex) {
        RemovePetFromClientCommand rpfcCommand = new RemovePetFromClientCommand(petIndex, clientIndex);
        rpfcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return rpfcCommand;
    }
}
```
