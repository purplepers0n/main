# md-azsa
###### \java\seedu\address\logic\commands\AddAppointmentToPetCommandTest.java
``` java
/**
 * Contains tests for AddAppointmentToPetCommand
 */
public class AddAppointmentToPetCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        Index outOfBoundIndexPet = Index.fromOneBased(model.getFilteredPetList().size() + 1);
        Index outOfBoundIndexAppt = Index.fromOneBased(model.getFilteredClientList().size() + 1);

        AddAppointmentToPetCommand aptcCommand = prepareCommand(outOfBoundIndexPet, outOfBoundIndexAppt);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(aptcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    @Test
    public void equals() throws Exception {
        final AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);

        // Same values -> true
        AddAppointmentToPetCommand commandWithSameValues = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        assertTrue(command.equals(commandWithSameValues));

        // Same objects -> true
        assertTrue(command.equals(command));

        // One command being preprocessed -> false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(command.equals(commandWithSameValues));

        // null -> false
        assertFalse(command.equals(null));

        // different commands -> false
        assertFalse(command.equals(new ClearCommand()));

        // different index -> false
        AddAppointmentToPetCommand differentIndexCommand = prepareCommand(INDEX_SECOND_APPT, INDEX_SECOND_PET);
        assertFalse(command.equals(differentIndexCommand));
    }

    @Test
    public void execute_appointmentHasBeenBooked_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        command.execute();
        command = prepareCommand(INDEX_FIRST_APPT, INDEX_SECOND_PET);





        assertCommandFailure(command, model, Messages.MESSAGE_APPOINTMENT_TAKEN);

    }

    @Test
    public void execute_clientPetAssociationNotFound_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_SECOND_PET);
        assertCommandFailure(command, model, AddAppointmentToPetCommand.MESSAGE_PET_DOES_NOT_HAVE_OWNER);
    }

    @Test
    public void execute_appointmentNotFound_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_THIRD_APPT, INDEX_FIRST_PET);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void execute_petNotFound_throwsCommandException() throws Exception {
        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_OOB_PET);
        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_addAppointmentToPet_success() throws Exception {
        Appointment appointmentInList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Pet petInList = model.getFilteredPetList().get(INDEX_FIRST_PET.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addAppointmentToPet(appointmentInList, petInList);

        AddAppointmentToPetCommand command = prepareCommand(INDEX_FIRST_APPT, INDEX_FIRST_PET);
        String expectedMessage = String.format(AddAppointmentToPetCommand.MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS,
                appointmentInList, petInList);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }


    /**
     * Returns an {@code AddAppointmentToPetCommand} wth parameters
     * of appointment {@code index} and of pet {@code index}
     */
    private AddAppointmentToPetCommand prepareCommand(Index appointmentIndex, Index petIndex) {
        AddAppointmentToPetCommand addappt = new AddAppointmentToPetCommand(appointmentIndex, petIndex);
        addappt.setData(model, new CommandHistory(), new UndoRedoStack());
        return addappt;
    }
}
```
###### \java\seedu\address\logic\commands\AddPetCommandIntegrationTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code AddPetCommand}
 */
public class AddPetCommandIntegrationTest {

    private Model model;

    @Before
    public void setup() {
        model = new ModelManager(getTypicalAddressBook(),
                new UserPrefs());
    }

    @Test
    public void execute_newPet_success() throws Exception {
        Pet validPet = new PetBuilder().build();
        Client validClient = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(),
                new UserPrefs());
        expectedModel.addPet(validPet);
        expectedModel.addPetToClient(validPet, validClient);

        assertCommandSuccess(prepareCommand(validPet, model), model,
                String.format(AddPetCommand.MESSAGE_SUCCESS, validPet), expectedModel);
    }

    @Test
    public void execute_duplicatePet_throwsCommandException() {
        Pet petInList = model.getAddressBook().getPetList().get(0);
        assertCommandFailure(prepareCommand(petInList, model), model, AddPetCommand.MESSAGE_DUPLICATE_PET);
    }

    /**
     * Generates a new {@code AddPetCommand} which upon execution,
     * adds {@code Pet} into {@code model}
     */
    private AddPetCommand prepareCommand(Pet pet, Model model) {
        AddPetCommand command = new AddPetCommand(pet, INDEX_FIRST_PERSON);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\AddPetCommandTest.java
``` java
public class AddPetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPet_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddPetCommand(null, null);
    }

    @Test
    public void execute_petAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPetAdded modelStub = new ModelStubAcceptingPetAdded();
        Pet validPet = new PetBuilder().build();

        CommandResult commandResult = getAddPetCommandForPet(validPet, modelStub).execute();

        assertEquals(String.format(AddPetCommand.MESSAGE_SUCCESS, validPet), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validPet), modelStub.petsAdded);
    }

    @Test
    public void execute_duplicatePet_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePetException();
        Pet validPet = new PetBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddPetCommand.MESSAGE_DUPLICATE_PET);

        getAddPetCommandForPet(validPet, modelStub).execute();
    }

    @Test
    public void equals() {
        Pet garfield = new PetBuilder().withPetName("Garfield").build();
        Pet ginger = new PetBuilder().withPetName("Ginger").build();
        AddPetCommand addGarfieldCommand = new AddPetCommand(garfield, INDEX_FIRST_PERSON);
        AddPetCommand addGinger = new AddPetCommand(ginger, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(addGarfieldCommand.equals(addGarfieldCommand));

        // same values -> return true
        AddPetCommand addGarfieldCommandCpy = new AddPetCommand(garfield, INDEX_FIRST_PERSON);
        assertTrue(addGarfieldCommand.equals(addGarfieldCommandCpy));

        // different types -> return false
        assertFalse(addGarfieldCommand.equals(1));

        // null -> returns false
        assertFalse(addGarfieldCommand.equals(null));

        // different pets -> returns false
        assertFalse(addGarfieldCommand.equals(addGinger));
    }

    /**
     * Generates a new AddPetCommand with the details of the given pet
     */
    private AddPetCommand getAddPetCommandForPet(Pet pet, Model model) {
        AddPetCommand command = new AddPetCommand(pet, INDEX_FIRST_PERSON);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * Default model stub that has all the methods failing
     */
    private class ModelStub implements Model {
        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Pet> getFilteredPetList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<VetTechnician> getFilteredVetTechnicianList() {
            return null;
        }

        @Override
        public void updateFilteredVetTechnicianList(Predicate<VetTechnician> predicate) {
            fail("This method should not be called.");
        }

        public void updateFilteredPetList(Predicate<Pet> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredClientOwnPetAssocation(Predicate<ClientOwnPet> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<ClientOwnPet> getFilteredClientPetAssociationList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException {
            fail("This method should not be called.");
        }

        @Override
        public void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public void updateAppointment(Appointment target, Appointment rescheduledAppointment)
                throws DuplicateAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Appointment> getFilteredAppointmentList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            fail("This method should not be called");
        }

        @Override
        public void deletePet(Pet pet) throws PetNotFoundException {
            fail("This method should not be called");
        }

        @Override
        public void setCurrentList(int currentList) {
            fail("This method should not be called.");
        }

        @Override
        public int getCurrentList() {
            fail("This method should not be called.");
            return -1;
        }

        @Override
        public void addVetTechToAppointment(VetTechnician technician, Appointment appointment) {
            fail("This method should not be called.");
        }

        @Override
        public void removeVetTechFromAppointent(Appointment apptToRemoveVetFrom)
                throws DuplicateAppointmentException, AppointmentNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortClientList() {
            fail("This method should not be called.");
        }

        @Override
        public void sortPetList() {
            fail("This method should not be called.");
        }

        @Override
        public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void addAppointmentToPet(Appointment appointment, Pet pet) throws PetAlreadyHasAppointmentException {
            fail("This method should not be called.");
        }

        @Override
        public void removeAppointmentFromPet(Appointment appointment) throws AppointmentNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortAppointmentList() throws AppointmentListIsEmptyException {
            fail("This method should not be called.");
        }

        @Override
        public void unscheduleAppointment(Appointment appointment) {
            fail("This method should not be called.");
        }

        public void updateDetailsList(Client client, ObservableList<Pet> pets,
                                      ObservableList<Appointment> appointments) {
            fail("This method should not be called.");
        }

        @Override
        public Client getClientDetails() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Pet> getClientPetList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public ObservableList<Appointment> getClientApptList() {
            fail("This method should not be called.");
            return null;
        }
    }

    /**
     * Model stub that always throws DuplicatePetException
     */
    private class ModelStubThrowingDuplicatePetException extends ModelStub {
        final UniqueClientList clientSample;

        private ModelStubThrowingDuplicatePetException() throws DuplicateClientException {
            clientSample = new UniqueClientList();
            clientSample.add((Client) ALICE);
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            throw new DuplicatePetException();
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.unmodifiableObservableList(clientSample.asObservableList());
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the pet being added.
     */
    private class ModelStubAcceptingPetAdded extends ModelStub {
        final ArrayList<Pet> petsAdded = new ArrayList<>();
        final UniqueClientList clientSample;

        private ModelStubAcceptingPetAdded() throws DuplicateClientException {
            clientSample = new UniqueClientList();
            clientSample.add((Client) ALICE);
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            requireNonNull(pet);
            petsAdded.add(pet);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public void addPetToClient(Pet pet, Client client) throws ClientAlreadyOwnsPetException {

        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.unmodifiableObservableList(clientSample.asObservableList());
        }

        @Override
        public void updateFilteredPetList(Predicate<Pet> predicate) {
        }

        @Override
        public void updateFilteredClientOwnPetAssocation(Predicate<ClientOwnPet> predicate) {
        }

    }
}
```
###### \java\seedu\address\logic\commands\DeletePetCommandTest.java
``` java

/**
 * Contains integration tests unit tests for
 * {@code DeletePetCommand}.
 */
public class DeletePetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removePet_invalidAssociation_throwClientPetAssociationNotFoundException() throws Exception {
        thrown.expect(ClientPetAssociationNotFoundException.class);
        model.deletePet(GARFIELD);
    }

    @Test
    public void removePet_petHasAppointment_success() throws Exception {
        model.addPetToClient(GARFIELD, (Client) FIONA);
        model.addAppointmentToPet(APPOINTMENT_1, GARFIELD);

        Pet petToDelete = model.getFilteredPetList().get(INDEX_FIRST.getZeroBased());
        DeletePetCommand deletePetCommand = new DeletePetCommand(INDEX_FIRST);

        String expectedMessage = String.format(DeletePetCommand.MESSAGE_DELETE_PET_SUCCESS, petToDelete);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePet(petToDelete);
        deletePetCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        assertCommandSuccess(deletePetCommand, model, expectedMessage, expectedModel);
    }

}
```
###### \java\seedu\address\logic\commands\RemoveAppointmentFromPetCommandTest.java
``` java
/**
 * Contains integration test and unit tests for RemoveAppointmentFromPetCommand
 */
public class RemoveAppointmentFromPetCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Before
    public void setup() throws PetAlreadyHasAppointmentException, ClientPetAssociationNotFoundException,
            DuplicateAppointmentException, AppointmentHasBeenTakenException,
            AppointmentNotFoundException, ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {

        // set up association
        model.addPetToClient(model.getAddressBook().getPetList().get(0),
                model.getAddressBook().getClientList().get(0));
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);

        //adds appointment to association
        model.addAppointmentToPet(model.getAddressBook().getAppointmentList().get(0),
                model.getAddressBook().getPetList().get(0));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PET);
    }

    @Test
    public void execute_removeAppointmentFromPet_throwsCommandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        prepareCommand(INDEX_THIRD_PERSON).execute();
    }

    @Test
    public void execute_removeAppointmentFromPet_success() throws Exception {
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        RemoveAppointmentFromPetCommand command = prepareCommand(INDEX_FIRST_APPT);

        String expectedMessage = String.format(RemoveAppointmentFromPetCommand.MESSAGE_REMOVE_APPOINTMENT_SUCCESS,
                appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new UserPrefs());
        expectedModel.removeAppointmentFromPet(model.getFilteredAppointmentList().get(0));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);
        Index outOfBounds = INDEX_SECOND_APPT;

        assertTrue(outOfBounds.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        RemoveAppointmentFromPetCommand command = prepareCommand(outOfBounds);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final RemoveAppointmentFromPetCommand standardCommand = prepareCommand(INDEX_FIRST_APPT);

        // same values -> true
        RemoveAppointmentFromPetCommand sameValueCommand = prepareCommand(INDEX_FIRST_APPT);
        assertTrue(standardCommand.equals(sameValueCommand));
        assertTrue(standardCommand.equals(standardCommand));

        //preprocessed with undoable
        sameValueCommand.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(sameValueCommand));

        // null
        assertFalse(standardCommand.equals(null));

        // different types
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new RemoveAppointmentFromPetCommand(INDEX_SECOND_APPT)));
    }

    /**
     * Returns an {@code RemoveAppointmentFromPetCommand} object with param
     */
    private RemoveAppointmentFromPetCommand prepareCommand(Index appointmentIndex) {
        RemoveAppointmentFromPetCommand command = new RemoveAppointmentFromPetCommand(appointmentIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\SortAppointmentCommandTest.java
``` java
/**
 * Contains integration tests for sorting the appointment list.
 */
public class SortAppointmentCommandTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model modelWithNoAppointments = new ModelManager(getTypicalAddressBookEmpty(),
            new UserPrefs());

    @Test
    public void sortEmptyAppointments() throws Exception {
        error.expect(CommandException.class);
        prepareCommand(modelWithNoAppointments).execute();
    }

    @Test
    public void sortAppointment_success() throws Exception {
        Model modelSorted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelSorted.sortAppointmentList();
        SortAppointmentCommand command = prepareCommand(model);
        String expectedMessage = SortAppointmentCommand.MESSAGE_SUCCESS;
        assertCommandSuccess(command, model, expectedMessage, modelSorted);
    }

    @Test
    public void sortEmptyAppointment_fail() throws Exception {
        SortAppointmentCommand command = prepareCommand(modelWithNoAppointments);
        assertCommandFailure(command, modelWithNoAppointments, Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
    }

    @Test
    public void equals() throws Exception {
        SortAppointmentCommand command = prepareCommand(model);
        command.execute();

        // Same objects -> return true
        assertEquals(command, command);
        // Different types -> return false
        assertFalse(command.equals(new ClearCommand()));
    }

    private SortAppointmentCommand prepareCommand(Model model) {
        SortAppointmentCommand command = new SortAppointmentCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
```
###### \java\seedu\address\logic\commands\SortClientCommandTest.java
``` java
/**
 * Adds integrations test methods for {@code SortClientCommand}
 */
public class SortClientCommandTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model modelWithNoPersonsList =
            new ModelManager(getTypicalAddressBookEmpty(), new UserPrefs());

    @Test
    public void sortEmptyList() throws Exception {
        error.expect(CommandException.class);
        prepareCommand(modelWithNoPersonsList).execute();
    }

    @Test
    public void sortPersonsList_success() throws Exception {
        Model modelSorted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelSorted.sortClientList();

        SortClientCommand command = prepareCommand(model);
        String expectedMessage = SortClientCommand.MESSAGE_SUCCESS;

        assertCommandSuccess(command, model, expectedMessage, modelSorted);
    }

    @Test
    public void sortEmptyList_fail() throws Exception {
        SortClientCommand command = prepareCommand(modelWithNoPersonsList);
        assertCommandFailure(command, modelWithNoPersonsList, Messages.MESSAGE_PERSONSLIST_EMPTY);
    }

    @Test
    public void equal() throws Exception {
        SortClientCommand command = prepareCommand(model);
        command.execute();

        // Same objects -> return true
        assertEquals(command, command);

        // Different types -> return false
        assertFalse(command.equals(new ClearCommand()));

        // Different references -> return false
        SortClientCommand commandDiff = prepareCommand(model);
        commandDiff.execute();
        assertFalse(command.equals(commandDiff));
    }

    private SortClientCommand prepareCommand(Model model) {
        SortClientCommand command = new SortClientCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\SortPetCommandTest.java
``` java
/**
 * Contains integration tests for sorting the client association list.
 */
public class SortPetCommandTest {

    @Rule
    public ExpectedException error = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model modelWithNoClientPetAssociationList =
            new ModelManager(getTypicalAddressBookEmpty(), new UserPrefs());

    @Test
    public void sortEmptyList() throws Exception {
        error.expect(CommandException.class);
        prepareCommand(modelWithNoClientPetAssociationList).execute();
    }

    @Test
    public void sortClientPetList_success() throws Exception {
        Model modelSorted = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        modelSorted.sortPetList();

        SortPetCommand command = prepareCommand(model);
        String expectedMessage = SortPetCommand.MESSAGE_SUCCESS;

        assertCommandSuccess(command, model, expectedMessage, modelSorted);
    }

    @Test
    public void sortEmptyList_fail() throws Exception {
        SortPetCommand command = prepareCommand(modelWithNoClientPetAssociationList);
        assertCommandFailure(command, modelWithNoClientPetAssociationList, Messages.MESSAGE_CLIENTPETLIST_EMPTY);
    }

    @Test
    public void equal() throws Exception {
        SortPetCommand command = prepareCommand(model);
        command.execute();

        // Same objects -> return true
        assertEquals(command, command);

        // Different types -> return false
        assertFalse(command.equals(new ClearCommand()));

        // Different references -> return false
        SortPetCommand commandDiff = prepareCommand(model);
        commandDiff.execute();
        assertFalse(command.equals(commandDiff));

    }

    private SortPetCommand prepareCommand(Model model) {
        SortPetCommand command = new SortPetCommand();
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\UnscheduleCommandTest.java
``` java
/**
 * Contains integration tests unit for {@code UnscheduleCommand}.
 */
public class UnscheduleCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(),
            new UserPrefs());

    @Test
    public void execute_unscheduleCommand_throwsCOmmandException() throws Exception {
        thrown.expect(CommandException.class);
        thrown.expectMessage(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        prepareCommand(INDEX_THIRD_APPT).execute();
    }

    @Test
    public void execute_unscheduleCommand_success() throws Exception {
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        UnscheduleCommand command = prepareCommand(INDEX_FIRST_APPT);

        String expectedMessage = String.format(UnscheduleCommand.MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS,
                appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new UserPrefs());
        expectedModel.unscheduleAppointment(model.getFilteredAppointmentList().get(0));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndex_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);
        Index outOfBOunds = INDEX_SECOND_APPT;

        assertTrue(outOfBOunds.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        UnscheduleCommand command = prepareCommand(outOfBOunds);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfileteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment appointmentToDelete = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        UnscheduleCommand command = prepareCommand(INDEX_FIRST_APPT);
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // delete -> first appt unschedule
        command.execute();
        undoRedoStack.push(command);

        // undo -> reverts
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo
        expectedModel.unscheduleAppointment(appointmentToDelete);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }


    @Test
    public void equals() throws Exception {
        final UnscheduleCommand standardCommand = prepareCommand(INDEX_FIRST_APPT);

        // same values -> true
        UnscheduleCommand sameValueCommand = prepareCommand(INDEX_FIRST_APPT);
        assertTrue(standardCommand.equals(standardCommand));
        assertTrue(standardCommand.equals(sameValueCommand));

        // preprocessed
        sameValueCommand.preprocessUndoableCommand();
        assertFalse(sameValueCommand.equals(standardCommand));

        // null -> false
        assertFalse(standardCommand.equals(null));

        // different type -> false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index
        assertFalse(standardCommand.equals(new UnscheduleCommand(INDEX_SECOND_APPT)));
    }

    /**
     * Returns an {@code UnscheduleCommand} object with param.
     */
    private UnscheduleCommand prepareCommand(Index appointmentIndex) {
        UnscheduleCommand command = new UnscheduleCommand(appointmentIndex);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

}
```
###### \java\seedu\address\logic\parser\AddAppointmentToPetCommandParserTest.java
``` java
public class AddAppointmentToPetCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE);

    private AddAppointmentToPetCommandParser parser = new AddAppointmentToPetCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "adsdsf", MESSAGE_INVALID_FORMAT);

        // one index specified
        assertParseFailure(parser, " appt/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " p/1", MESSAGE_INVALID_FORMAT);

        // wrong prefixes
        assertParseFailure(parser, " appt/1 sss/1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidIndex_failure() {
        // negative index
        assertParseFailure(parser, " appt/-11 p/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/1 p/-1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/-1 p/1", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, " appt/0 p/0", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/0 p/1", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/1 p/0", MESSAGE_INVALID_FORMAT);

        // characters used
        assertParseFailure(parser, " appt/2 p/#$@$", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, " appt/fe!@#$ p/;;;", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validCommand() {
        Index apptIndex = INDEX_FIRST_APPT;
        Index petIndex = INDEX_FIRST_PET;

        String userInput = " appt/" + apptIndex.getZeroBased() + " p/" + petIndex.getZeroBased()
                + " appt/" + apptIndex.getOneBased() + " p/" + petIndex.getOneBased();
        AddAppointmentToPetCommand expected = new AddAppointmentToPetCommand(apptIndex, petIndex);
        // all arguments fulfilled and supplied
        assertParseSuccess(parser, userInput, expected);
    }
}
```
###### \java\seedu\address\logic\parser\UnscheduleCommandParserTest.java
``` java
/**
 * Testing for UnschedulCommandParser
 */
public class UnscheduleCommandParserTest {

    private UnscheduleCommandParser parser = new UnscheduleCommandParser();

    @Test
    public void parse_validArgs_returnsUnscheduleCommand() {
        assertParseSuccess(parser, "1", new UnscheduleCommand(INDEX_FIRST_APPT));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnscheduleCommand.MESSAGE_USAGE));
    }

}
```
###### \java\seedu\address\model\pet\PetAgeTest.java
``` java
public class PetAgeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetAge(null));
    }

    @Test
    public void constructor_invalidPetAge_throwsIllegalArgumentException() {
        String invalidPetAge = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetAge(invalidPetAge));
    }

    @Test
    public void isValidPetAge() {
        // null pet age
        Assert.assertThrows(NullPointerException.class, () -> PetAge.isValidPetAge(null));

        // invalid pet age
        assertFalse(PetAge.isValidPetAge("")); // empty string
        assertFalse(PetAge.isValidPetAge("123")); // 3 digits long
        assertFalse(PetAge.isValidPetAge("asv")); // alphabets
        assertFalse(PetAge.isValidPetAge("*(*(123")); // characters and numbers
        assertFalse(PetAge.isValidPetAge("321 2132")); // spaces in between

        // valid pet age
        assertTrue(PetAge.isValidPetAge("2")); // 1 digit
        assertTrue(PetAge.isValidPetAge("21")); // 2 digits

    }
}
```
###### \java\seedu\address\model\pet\PetGenderTest.java
``` java
public class PetGenderTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetGender(null));
    }

    @Test
    public void constructor_invalidPetGender_throwsIllegalArgumentException() {
        String invalidGender = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetGender(invalidGender));
    }

    @Test
    public void isValidGender() {
        // null pet gender
        Assert.assertThrows(NullPointerException.class, () -> PetGender.isValidGender(null));

        // invalid gender
        assertFalse(PetGender.isValidGender("")); // empty string
        assertFalse(PetGender.isValidGender(" ")); // just spaces
        assertFalse(PetGender.isValidGender("123")); // numbers
        assertFalse(PetGender.isValidGender("asd")); // alphabets
        assertFalse(PetGender.isValidGender("12sda12")); // mix of alphabets
        assertFalse(PetGender.isValidGender("asd assd")); // spaces in between

        // valid gender
        assertTrue(PetGender.isValidGender("m")); // small m
        assertTrue(PetGender.isValidGender("M")); // capital
        assertTrue(PetGender.isValidGender("f")); // small f
        assertTrue(PetGender.isValidGender("F")); // capital f

    }
}
```
###### \java\seedu\address\model\pet\PetNameTest.java
``` java
public class PetNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetName(null));
    }

    @Test
    public void constructor_invalidPetName_throwsIllegalArgumentException() {
        String invalidPetName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetName(invalidPetName));
    }

    @Test
    public void isValidName() {
        // null pet name
        Assert.assertThrows(NullPointerException.class, () -> PetName.isValidPetName(null));

        // invalid name
        assertFalse(PetName.isValidPetName("")); // empty string
        assertFalse(PetName.isValidPetName(" ")); // spaces string
        assertFalse(PetName.isValidPetName(" *")); // non-alphanumeric
        assertFalse(PetName.isValidPetName("garfield* ")); // contains non-alpha char

        // valid names
        assertTrue(PetName.isValidPetName("Garfield Jokes")); // alphabets only
        assertTrue(PetName.isValidPetName("Garfield 123"));
        assertTrue(PetName.isValidPetName("Garfield II King 3"));
        assertTrue(PetName.isValidPetName("Garfield Pop starrr"));



    }
}
```
###### \java\seedu\address\storage\XmlAdaptedAppointmentTest.java
``` java
public class XmlAdaptedAppointmentTest {

    @Test
    public void toModelType_validAppointment_returnsModel() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(APPOINTMENT_1, appointment.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedAppointment apptOne = new XmlAdaptedAppointment(APPOINTMENT_1);

        // Same objects -> returns true
        assertEquals(apptOne, apptOne);

        // Different objects -> returns false
        assertNotEquals(apptOne, new Object());

        // Same calls -> returns true
        XmlAdaptedAppointment apptTwo = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(apptOne, apptTwo);

        // Different calls -> returns false
        XmlAdaptedAppointment apptThree = new XmlAdaptedAppointment(APPOINTMENT_2);
        assertNotEquals(apptOne, apptThree);
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedPetTest.java
``` java
public class XmlAdaptedPetTest {

    @Test
    public void toModelType_validPet_returnsModel() throws Exception {
        XmlAdaptedPet pet = new XmlAdaptedPet(TypicalPets.GARFIELD);
        assertEquals(TypicalPets.GARFIELD, pet.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedPet petOne = new XmlAdaptedPet(TypicalPets.GARFIELD);

        // Same objects -> returns true
        assertEquals(petOne, petOne);

        // Different objects -> returns false
        assertNotEquals(petOne, new Object());

        // Same calls -> returns true
        XmlAdaptedPet petTwo = new XmlAdaptedPet(TypicalPets.GARFIELD);
        assertEquals(petOne, petTwo);

        // Different calls -> returns false
        XmlAdaptedPet petThree = new XmlAdaptedPet(TypicalPets.LOTSO);
        assertNotEquals(petOne, petThree);
    }
}
```
###### \java\seedu\address\testutil\PetBuilder.java
``` java
/**
 * Util class to help with building Pet objects.
 */
public class PetBuilder {
    public static final String DEFAULT_PET_NAME = "Garfield";
    public static final String DEFAULT_PET_AGE = "5";
    public static final String DEFAULT_PET_GENDER = "M";
    public static final String DEFAULT_TAGS = "Cat";

    private PetName petName;
    private PetAge petAge;
    private PetGender petGender;
    private Set<Tag> tags;

    public PetBuilder() {
        petName = new PetName(DEFAULT_PET_NAME);
        petAge = new PetAge(DEFAULT_PET_AGE);
        petGender = new PetGender(DEFAULT_PET_GENDER);
        tags = SampleDataUtilPet.getTagSet(DEFAULT_TAGS);
    }


    /**
     * Sets the {@code PetName} of the {@code Pet} we are building
     */
    public PetBuilder withPetName(String petName) {
        this.petName = new PetName(petName);
        return this;
    }

    /**
     * Sets the {@code PetAge} of the {@code Pet} we are building
     */
    public PetBuilder withPetAge(String petAge) {
        this.petAge = new PetAge(petAge);
        return this;
    }

    /**
     * Sets the {@code PetGender} of {@code Pet} we are building
     */
    public PetBuilder withPetGender(String petGender) {
        this.petGender = new PetGender(petGender);
        return this;
    }

    /**
     * Sets the tag
     */
    public PetBuilder withTags(String ... tags) {
        this.tags = SampleDataUtilPet.getTagSet(tags);
        return this;
    }

    /**
     * Builts the pet object
     */
    public Pet build() {
        return new Pet(petName, petAge, petGender, tags);
    }
}
```
###### \java\seedu\address\testutil\PetUtil.java
``` java
/**
 * Util class for pet.
 */
public class PetUtil {

    /**
     * Returns add command string for adding the {@code pet}
     */
    public static String getAddPetCommand(Pet pet) {
        return AddPetCommand.COMMAND_WORD + " " + getPetDetails(pet);
    }

    /**
     * Returns the details of the pet
     */
    public static String getPetDetails(Pet pet) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_PET_NAME + pet.getPetName().toString());
        sb.append(PREFIX_PET_AGE + pet.getPetAge().value);
        sb.append(PREFIX_PET_GENDER + pet.getPetGender().toString());
        pet.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }
}
```
###### \java\seedu\address\testutil\TypicalAppointments.java
``` java
/**
 * A utility class containing a list of {@code Appointment} objects
 * to be used in tests.
 */
public class TypicalAppointments {

    public static final Appointment APPOINTMENT_1 = new AppointmentBuilder().withDate("2018-02-01")
            .withTime("14:30")
            .withDuration("60")
            .withDescription("Sterilize Garfield")
            .build();
    public static final Appointment APPOINTMENT_2 = new AppointmentBuilder().withDate("2018-03-02")
            .withTime("16:30")
            .withDuration("45")
            .withDescription("Give Tweety Bird the showering service")
            .build();

    private TypicalAppointments() {}

```
###### \java\seedu\address\testutil\TypicalPets.java
``` java
/**
 * A utility class containing a list of {@code Pet} objects to be used in tests.
 */
public class TypicalPets {

    public static final Pet GARFIELD = new PetBuilder().withPetName("Garfield")
            .withPetAge("10")
            .withPetGender("M")
            .withTags("cat", "tabby")
            .build();
    public static final Pet SCOOBY = new PetBuilder().withPetName("Scooby Doo")
            .withPetAge("10")
            .withPetGender("M")
            .withTags("dog", "greatDane")
            .build();
    public static final Pet PICKLES = new PetBuilder().withPetName("Pickles Tickles")
            .withPetAge("3")
            .withPetGender("F")
            .withTags("hamster", "obesed")
            .build();
    public static final Pet LOTSO = new PetBuilder().withPetName("Lotso Fatso")
            .withPetAge("4")
            .withPetGender("M")
            .withTags("bear", "purple")
            .build();

    private TypicalPets() {} // prevents instantiation


    public static List<Pet> getTypicalPets() {
        return new ArrayList<>(Arrays.asList(GARFIELD, SCOOBY, PICKLES, LOTSO));
    }
}

```
