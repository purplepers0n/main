# purplepers0n
###### \java\seedu\address\logic\commands\ListAllCommandTest.java
``` java

/**
 * Contains integration tests (interaction with the Model) for {@code ListAllCommand}.
 */
public class ListAllCommandTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_invalidIndex_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndex_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        assertExecutionSuccess(INDEX_FIRST_PERSON);
    }

    @Test
    public void execute_clearData_listAllPanelCleared() {
        assertExecutionSuccess(INDEX_FIRST_PERSON);

        model.resetData(new AddressBook());

        assertEquals(null, model.getClientDetails());
        assertEquals(null, model.getClientPetList());
        assertEquals(null, model.getClientApptList());
    }

    @Test
    public void execute_deleteClient_listAllPanelCleared() {
        assertExecutionSuccess(INDEX_FIRST_PERSON);
        Client clientToDelete = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        try {
            model.deletePerson(clientToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new IllegalArgumentException("Execution of command should not fail.", pnfe);
        }

        assertEquals(null, model.getClientDetails());
        assertEquals(null, model.getClientPetList());
        assertEquals(null, model.getClientApptList());
    }

    @Test
    public void execute_deleteOtherClient_listAllPanelNoChange() {
        assertExecutionSuccess(INDEX_FIRST_PERSON);
        Client clientToDelete = model.getFilteredClientList().get(INDEX_SECOND_PERSON.getZeroBased());

        try {
            model.deletePerson(clientToDelete);
        } catch (PersonNotFoundException pnfe) {
            throw new IllegalArgumentException("Execution of command should not fail.", pnfe);
        }

        assertNotEquals(null, model.getClientDetails());
        assertNotEquals(null, model.getClientPetList());
        assertNotEquals(null, model.getClientApptList());
    }

    /**
     * Executes a {@code ListAllCommand} with the given {@code index}
     */
    private void assertExecutionSuccess(Index index) {
        ListAllCommand listAllCommand = prepareCommand(index);

        try {
            CommandResult commandResult = listAllCommand.execute();
            assertEquals(String.format(ListAllCommand.MESSAGE_SUCCESS,
                    model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased()).getName().fullName),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        Client expectedClient = model.getFilteredClientList().get(index.getZeroBased());

        assertEquals(expectedClient, model.getClientDetails());

        List<ClientOwnPet> allClientOwnPets = model.getFilteredClientPetAssociationList();
        List<Pet> expectedPetList = FXCollections.observableArrayList();

        for (ClientOwnPet clientOwnPet : allClientOwnPets) {
            if (clientOwnPet.getClient().equals(expectedClient)) {
                Pet currPet = clientOwnPet.getPet();
                expectedPetList.add(currPet);
            }
        }
        assertEquals(expectedPetList, model.getClientPetList());

        List<Appointment> allApptList = model.getFilteredAppointmentList();
        List<Appointment> expectedApptList = FXCollections.observableArrayList();

        for (Appointment appt : allApptList) {
            for (Pet pet : expectedPetList) {
                if (appt.getClientOwnPet() != null && appt.getClientOwnPet().getPet().equals(pet)) {
                    expectedApptList.add(appt);
                }
            }
        }
        assertEquals(expectedApptList, model.getClientApptList());
    }

    /**
     * Executes a {@code ListAllCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        ListAllCommand listAllCommand = prepareCommand(index);

        try {
            listAllCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
        }
    }

    /**
     * Returns a {@code ListAllCommand} with parameters {@code index}.
     */
    private ListAllCommand prepareCommand(Index index) {
        ListAllCommand listAllCommand = new ListAllCommand(index);
        listAllCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return listAllCommand;
    }
}
```
