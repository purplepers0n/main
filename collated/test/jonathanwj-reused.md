# jonathanwj-reused
###### \java\seedu\address\logic\autocomplete\CommandParameterSyntaxHandlerTest.java
``` java
public class CommandParameterSyntaxHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandParameterSyntaxHandler handler;

    @Before
    public void setup() {
        handler = new CommandParameterSyntaxHandler();
    }

    @Test
    public void getMissingPrefix_addCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddCommand.COMMAND_WORD, AddCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_editCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(EditCommand.COMMAND_WORD,
                EditCommand.COMMAND_WORD);
        assertEquals(result, handler.EDIT_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_scheduleCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(ScheduleCommand.COMMAND_WORD,
                ScheduleCommand.COMMAND_WORD);
        assertEquals(result, handler.SCHEDULE_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddPetCommand.COMMAND_WORD,
                AddPetCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addApptToPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddAppointmentToPetCommand.COMMAND_WORD,
                AddAppointmentToPetCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_APPT_TO_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_removeApptFromPetCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(RemoveAppointmentFromPetCommand.COMMAND_WORD,
                RemoveAppointmentFromPetCommand.COMMAND_WORD);
        assertEquals(result, handler.REMOVE_APPT_FROM_PET_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_addVetTechToAppointmentCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(AddVetTechToAppointmentCommand.COMMAND_WORD,
                AddVetTechToAppointmentCommand.COMMAND_WORD);
        assertEquals(result, handler.ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES);
    }

    @Test
    public void getMissingPrefix_rescheduleAppointmentCommand_allPrefixMissing() {
        ArrayList<Prefix> result = handler.getMissingPrefixes(RescheduleCommand.COMMAND_WORD,
                RescheduleCommand.COMMAND_WORD);
        assertEquals(result, handler.RESCHEDULE_COMMAND_PREFIXES);
    }

}
```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
    @Test
    public void execute_vetTechnicianAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validTechnician = new PersonBuilder().buildWithRoleVetTechnician();

        CommandResult commandResult = getAddCommandForPerson(validTechnician, modelStub).execute();

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validTechnician), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validTechnician), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePersonOrClient_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validPerson = new PersonBuilder().buildWithRoleClient();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validPerson, modelStub).execute();
    }

```
###### \java\seedu\address\logic\commands\AddCommandTest.java
``` java
    @Test
    public void execute_duplicateVetTechnician_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicatePersonException();
        Person validVetTechnician = new PersonBuilder().buildWithRoleVetTechnician();

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);

        getAddCommandForPerson(validVetTechnician, modelStub).execute();
    }

```
###### \java\seedu\address\logic\commands\AddVetTechToAppointmentCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand)
 * and unit tests for AddVetTechToAppointmentCommand.
 */
public class AddVetTechToAppointmentCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_vetTechFilteredList_success() throws Exception {
        showVetTechnicianAtIndex(model, INDEX_FIRST);
        showAppointmentAtIndex(model, INDEX_FIRST);

        VetTechnician vetTechInFilteredList = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);

        String expectedMessage = String.format(AddVetTechToAppointmentCommand.MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTechInFilteredList, appointmentInFilteredList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.addVetTechToAppointment(model.getFilteredVetTechnicianList().get(0),
                model.getFilteredAppointmentList().get(0));

        assertCommandSuccess(avttcCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndexVetTechnician = Index.fromOneBased(model.getFilteredVetTechnicianList().size() + 1);
        Index outOfBoundIndexAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(outOfBoundIndexVetTechnician, INDEX_FIRST);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

        avttcCommand = prepareCommand(INDEX_FIRST, outOfBoundIndexAppointment);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndexAppointment = INDEX_SECOND;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexAppointment.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, outOfBoundIndexAppointment);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        showVetTechnicianAtIndex(model, INDEX_FIRST);
        Index outOfBoundIndexVetTechnician = INDEX_SECOND;

        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndexVetTechnician.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        avttcCommand = prepareCommand(outOfBoundIndexVetTechnician, INDEX_FIRST);

        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // add -> vetTech added to appointment
        avttcCommand.execute();
        undoRedoStack.push(avttcCommand);

        // undo -> reverts addressbook back to previous state and filtered lists to show all
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> add vetTech back to appointment
        expectedModel.addVetTechToAppointment(model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased()),
                model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased()));
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndexVetTechnician = Index.fromOneBased(model.getFilteredVetTechnicianList().size() + 1);
        Index outOfBoundIndexAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(outOfBoundIndexVetTechnician,
                outOfBoundIndexAppointment);

        // execution failed -> editCommand not pushed into undoRedoStack
        assertCommandFailure(avttcCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Adss a {@code VetTechnician} To a {@code Appointment} from filtered lists.
     * 2. Undo the command.
     * 3. The unfiltered lists should be shown now. Verify that the index of the previously edited appointment in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the command. This ensures {@code RedoCommand} adds the vetTech to appointment object regardless
     * of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showVetTechnicianAtIndex(model, INDEX_SECOND);
        showAppointmentAtIndex(model, INDEX_SECOND);
        VetTechnician vetTechToAdd = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentToAddVetTechnicianTo = model.getFilteredAppointmentList()
                .get(INDEX_FIRST.getZeroBased());
        /* add -> add first appointment in filtered appointment list
           and the first vetTech in filtered vetTech list
        */
        avttcCommand.execute();
        undoRedoStack.push(avttcCommand);

        /* undo -> reverts addressbook back to previous state and filtered
           person list to show all appointments and vetTechs
        */
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased()), vetTechToAdd);
        assertNotEquals(model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased()),
                appointmentToAddVetTechnicianTo);

        // redo -> add the same vetTech to appointment in unfiltered lists
        expectedModel.addVetTechToAppointment(vetTechToAdd, appointmentToAddVetTechnicianTo);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_addVetTechToAppointment_success() throws Exception {
        VetTechnician vetTechInFilteredList = model.getFilteredVetTechnicianList().get(INDEX_FIRST.getZeroBased());
        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addVetTechToAppointment(vetTechInFilteredList, appointmentInFilteredList);

        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        String expectedMessage = String.format(AddVetTechToAppointmentCommand.MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTechInFilteredList, appointmentInFilteredList);

        assertCommandSuccess(avttcCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appointmentAlreadyHasVetTechnician_throwsCommandException() throws Exception {
        AddVetTechToAppointmentCommand avttcCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);
        avttcCommand.execute();

        assertCommandFailure(avttcCommand, model, AddVetTechToAppointmentCommand.MESSAGE_APPOINTMENT_HAS_TECH);
    }

    @Test
    public void equals() throws Exception {
        final AddVetTechToAppointmentCommand standardCommand = prepareCommand(INDEX_FIRST, INDEX_FIRST);

        // same values -> returns true
        AddVetTechToAppointmentCommand commandWithSameValues = prepareCommand(INDEX_FIRST, INDEX_FIRST);
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
        assertFalse(standardCommand.equals(new AddVetTechToAppointmentCommand(INDEX_SECOND, INDEX_SECOND)));
    }

    /**
     * Returns an {@code AddVetTechToAppointmentCommand} with parameters vetTech {@code index}
     * and appointment {@code index}
     */
    private AddVetTechToAppointmentCommand prepareCommand(Index vetTechIndex, Index appointmentIndex) {
        AddVetTechToAppointmentCommand avttcCommand = new AddVetTechToAppointmentCommand(vetTechIndex,
                appointmentIndex);
        avttcCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return avttcCommand;
    }
}
```
###### \java\seedu\address\logic\commands\EditCommandTest.java
``` java
    @Test
    public void execute_filteredList_success() throws Exception {

        Person clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedClient = new PersonBuilder(clientInFilteredList).withName(VALID_NAME_BOB).buildWithRoleClient();

        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedClient);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

        // edit client to vet technician
        clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(clientInFilteredList)
                .withName(VALID_NAME_BOB).buildWithRoleVetTechnician();
        editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withRole(VALID_ROLE_TECHNICIAN).build());

        expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredClientList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);

        // edit vet technician to client
        model.setCurrentList(2);
        Person technicianInFilteredList = model.getFilteredVetTechnicianList().get(INDEX_FIRST_PERSON.getZeroBased());
        editedPerson = new PersonBuilder(technicianInFilteredList)
                .withName(VALID_NAME_BOB).buildWithRoleClient();
        editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withRole(VALID_ROLE_CLIENT).build());
        editCommand.setCurrentList();

        expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedPerson);

        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredVetTechnicianList().get(0), editedPerson);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

```
###### \java\seedu\address\logic\commands\RemoveVetTechFromAppointmentCommandTest.java
``` java
/**
 * Contains integration tests unit tests for
 * {@code RemoveVetTechFromAppointmentCommand}.
 */
public class RemoveVetTechFromAppointmentCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(TypicalAddressBook.getTypicalAddressBook(), new UserPrefs());

    @Test
    public void removeVetTech_invalidAppt_throwAppointmentNotFoundException() throws Exception {
        thrown.expect(AppointmentNotFoundException.class);
        model.removeVetTechFromAppointent(new AppointmentBuilder().withDate("2019-02-01")
                .withTime("14:40")
                .withDuration("80")
                .withDescription("Dummy")
                .build());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() throws Exception {
        Appointment appointmentToRemoveVetTech = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());
        model.addVetTechToAppointment((VetTechnician) BOON, appointmentToRemoveVetTech);
        appointmentToRemoveVetTech = model.getFilteredAppointmentList().get(INDEX_FIRST.getZeroBased());

        RemoveVetTechFromAppointmentCommand command = new RemoveVetTechFromAppointmentCommand(INDEX_FIRST);
        command.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedMessage = String.format(RemoveVetTechFromAppointmentCommand
                .MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS, appointmentToRemoveVetTech);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.removeVetTechFromAppointent(appointmentToRemoveVetTech);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

}
```
###### \java\seedu\address\storage\XmlAdaptedPersonTest.java
``` java
    @Test
    public void toModelType_invalidRole_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, INVALID_ROLE, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = PersonRole.MESSAGE_ROLE_CONSTRAINTS;
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullRole_throwsIllegalValueException() {
        XmlAdaptedPerson person =
                new XmlAdaptedPerson(VALID_NAME, null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, PersonRole.class.getSimpleName());
        Assert.assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

}
```
###### \java\seedu\address\testutil\TypicalAssociations.java
``` java
/**
 * A utility class containing a list of {@code ClientOwnPet} objects to be used in tests.
 */
public class TypicalAssociations {

    public static final ClientOwnPet FIONA_LOTSO = new ClientOwnPet((Client) TypicalPersons.FIONA,
            TypicalPets.LOTSO);
    public static final ClientOwnPet ELLE_PICKLES = new ClientOwnPet((Client) TypicalPersons.ELLE,
            TypicalPets.PICKLES);


    private TypicalAssociations() {} // prevents instantiation


    public static List<ClientOwnPet> getTypicalAssociations() {
        return new ArrayList<>(Arrays.asList(FIONA_LOTSO, ELLE_PICKLES));
    }
}
```
