# Godxin-test
###### \java\seedu\address\logic\commands\RescheduleCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class RescheduleCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastAppointment = Index.fromOneBased(model.getFilteredAppointmentList().size());
        Appointment lastAppointment = model.getFilteredAppointmentList().get(indexLastAppointment.getZeroBased());

        AppointmentBuilder appointmentInList = new AppointmentBuilder(lastAppointment);
        Appointment rescheduledAppointment = appointmentInList.withDate(VALID_APPOINTMENT_DATE1)
                .withTime(VALID_APPOINTMENT_TIME1).withDuration(VALID_APPOINTMENT_DURATION1).build();

        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).withTime(VALID_APPOINTMENT_TIME1)
                .withDuration(VALID_APPOINTMENT_DURATION1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(indexLastAppointment, descriptor);

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(lastAppointment, rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, new RescheduleAppointmentDescriptor());
        Appointment rescheduledAppointment = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {

        Appointment appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Appointment rescheduledAppointment = new AppointmentBuilder(appointmentInFilteredList)
                .withDate(VALID_APPOINTMENT_DATE1).build();

        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        String expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);

        // reschedule date of an appointment
        appointmentInFilteredList = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        Appointment rescheduledAnotherAppointment = new AppointmentBuilder(appointmentInFilteredList)
                .withDate(VALID_APPOINTMENT_DATE1).build();
        rescheduleCommand = prepareCommand(INDEX_FIRST_APPT,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        expectedMessage = String.format(RescheduleCommand.MESSAGE_RESCHEDULE_APPOINTMENT_SUCCESS,
                rescheduledAnotherAppointment);

        expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updateAppointment(model.getFilteredAppointmentList().get(0), rescheduledAnotherAppointment);

        assertCommandSuccess(rescheduleCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidAppointmentIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidAppointmentIndexFilteredList_failure() {
        showAppointmentAtIndex(model, INDEX_FIRST_APPT);

        Index outOfBoundIndex = INDEX_SECOND_APPT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getAppointmentList().size());

        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex,
                new RescheduleAppointmentDescriptorBuilder().withDate(VALID_APPOINTMENT_DATE1).build());

        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        Appointment appointmentToReschedule = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        // edit -> first appointment rescheduled
        rescheduleCommand.execute();
        undoRedoStack.push(rescheduleCommand);

        // undo -> reverts addressbook back to previous state and filtered appointment list to show all appointments
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first appointment rescheduled again
        expectedModel.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredAppointmentList().size() + 1);
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder()
                .withDate(VALID_APPOINTMENT_DATE1).build();
        RescheduleCommand rescheduleCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> rescheduleCommand not pushed into undoRedoStack
        assertCommandFailure(rescheduleCommand, model, Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Reschedules an {@code Appointment} from a filtered list.
     * 2. Undo the reschedule.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously rescheduled appointment
     * in the unfiltered list is different from the index at the filtered list.
     * 4. Redo the reschedule. This ensures {@code RedoCommand} reschedules the appointment object
     * regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_sameAppointmentEdited() throws Exception {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Appointment rescheduledAppointment = new AppointmentBuilder().build();
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(rescheduledAppointment)
                .build();
        RescheduleCommand rescheduleCommand = prepareCommand(INDEX_FIRST_APPT, descriptor);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        showAppointmentAtIndex(model, INDEX_SECOND_APPT);
        Appointment appointmentToReschedule = model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased());
        // reschedule -> reschedules the second appointment in unfiltered appointment list /
        // first appointment in filtered appointment list
        rescheduleCommand.execute();
        undoRedoStack.push(rescheduleCommand);

        // undo -> reverts addressbook back to previous state and filtered appointment list to show all appointments
        assertCommandSuccess(undoCommand, model, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        expectedModel.updateAppointment(appointmentToReschedule, rescheduledAppointment);
        assertNotEquals(model.getFilteredAppointmentList().get(INDEX_FIRST_APPT.getZeroBased()),
                appointmentToReschedule);
        // redo -> reschedules the same second appointment in unfiltered appointment list
        assertCommandSuccess(redoCommand, model, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() throws Exception {
        final RescheduleCommand standardCommand = prepareCommand(INDEX_FIRST_APPT, DESC_APPT1);

        // same values -> returns true
        RescheduleAppointmentDescriptor copyDescriptor = new RescheduleAppointmentDescriptor(DESC_APPT1);
        RescheduleCommand commandWithSameValues = prepareCommand(INDEX_FIRST_APPT, copyDescriptor);
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
        assertFalse(standardCommand.equals(new RescheduleCommand(INDEX_SECOND_APPT, DESC_APPT1)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new RescheduleCommand(INDEX_FIRST_APPT, DESC_APPT2)));
    }

    /**
     * Returns an {@code RescheduleCommand} with parameters {@code index} and {@code descriptor}
     */
    private RescheduleCommand prepareCommand(Index index, RescheduleAppointmentDescriptor descriptor) {
        RescheduleCommand rescheduleCommand = new RescheduleCommand(index, descriptor);
        rescheduleCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return rescheduleCommand;
    }
}
```
###### \java\seedu\address\logic\commands\ScheduleCommandTest.java
``` java
public class ScheduleCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullAppointment_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new ScheduleCommand(null);
    }

    @Test
    public void execute_appointmentAcceptedByModel_scheduleSuccessful() throws Exception {
        ModelStubAcceptingAppointmentScheduled modelStub = new ModelStubAcceptingAppointmentScheduled();
        Appointment validAppointment = new AppointmentBuilder().build();

        CommandResult commandResult = getScheduleCommandForAppointment(validAppointment, modelStub).execute();

        assertEquals(String.format(ScheduleCommand.MESSAGE_SUCCESS, validAppointment), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validAppointment), modelStub.appointmentScheduled);
    }

    @Test
    public void execute_duplicateAppointment_throwsCommandException() throws Exception {
        ModelStub modelStub = new ModelStubThrowingDuplicateAppointmentException();
        Appointment validAppointment = new AppointmentBuilder().build();

        thrown.expect(CommandException.class);
        thrown.expectMessage(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);

        getScheduleCommandForAppointment(validAppointment, modelStub).execute();
    }

    @Test
    public void equals() {
        Appointment appointment1 = new AppointmentBuilder().withDate("2018-12-12").build();
        Appointment appointment2 = new AppointmentBuilder().withTime("00:00").build();
        Appointment appointment3 = new AppointmentBuilder().withDuration("30").build();
        Appointment appointment4 = new AppointmentBuilder().withDescription("Sterilize Garfield").build();

        ScheduleCommand scheduleAppointment1 = new ScheduleCommand(appointment1);
        ScheduleCommand scheduleAppointment2 = new ScheduleCommand(appointment2);
        ScheduleCommand scheduleAppointment3 = new ScheduleCommand(appointment3);
        ScheduleCommand scheduleAppointment4 = new ScheduleCommand(appointment4);

        // same object -> returns true
        assertTrue(scheduleAppointment1.equals(scheduleAppointment1));
        assertTrue(scheduleAppointment2.equals(scheduleAppointment2));
        assertTrue(scheduleAppointment3.equals(scheduleAppointment3));
        assertTrue(scheduleAppointment4.equals(scheduleAppointment4));

        // same values -> returns true
        ScheduleCommand scheduleAppointment1Copy = new ScheduleCommand(appointment1);
        assertTrue(scheduleAppointment1.equals(scheduleAppointment1Copy));

        // different types -> returns false
        assertFalse(scheduleAppointment1.equals(2));

        // null -> returns false
        assertFalse(scheduleAppointment1.equals(null));

        // different appointment -> returns false
        assertFalse(scheduleAppointment1.equals(scheduleAppointment2));
    }

    /**
     * Generates a new ScheduleCommand with the details of the given appointment.
     */
    private ScheduleCommand getScheduleCommandForAppointment(Appointment appointment, Model model) {
        ScheduleCommand command = new ScheduleCommand(appointment);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
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
            return null;
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<VetTechnician> getFilteredVetTechnicianList() {
            fail("This method should not be called.");
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
            return model.getFilteredAppointmentList();
        }

        @Override
        public void addPet(Pet pet) throws DuplicatePetException {
            fail("This method should not be called.");
        }

        @Override
        public void deletePet(Pet pet) throws PetNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void setCurrentList(int currentList) {
            fail("This method should not be called");
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
     * A Model stub that always throw a DuplicateAppointmentException when trying to schedule an appointment.
     */
    private class ModelStubThrowingDuplicateAppointmentException extends ModelStub {
        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            throw new DuplicateAppointmentException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the appointment being scheduled.
     */
    private class ModelStubAcceptingAppointmentScheduled extends ModelStub {
        final ArrayList<Appointment> appointmentScheduled = new ArrayList<>();

        @Override
        public void scheduleAppointment(Appointment appointment) throws DuplicateAppointmentException {
            requireNonNull(appointment);
            appointmentScheduled.add(appointment);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_schedule() throws Exception {
        Appointment appointment = new AppointmentBuilder().build();
        ScheduleCommand command = (ScheduleCommand) parser.parseCommand(
                                    AppointmentUtil.getScheduleCommand(appointment));
        assertEquals(new ScheduleCommand(appointment), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_reschedule() throws Exception {
        Appointment appointment = new AppointmentBuilder().build();
        RescheduleAppointmentDescriptor descriptor = new RescheduleAppointmentDescriptorBuilder(appointment).build();
        RescheduleCommand command = (RescheduleCommand) parser.parseCommand(RescheduleCommand.COMMAND_WORD + " "
                + INDEX_FIRST_APPT.getOneBased() + " "
                + AppointmentUtil.getAppointmentDetails(appointment));
        assertEquals(new RescheduleCommand(INDEX_FIRST_APPT, descriptor), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_clearAlias() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_ALIAS + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().buildWithRoleClient();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " "
                + PersonUtil.getPersonDetails(person));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_addVetTechToAppointment() throws Exception {
        AddVetTechToAppointmentCommand command = (AddVetTechToAppointmentCommand) parser
                .parseCommand(AddVetTechToAppointmentCommand.COMMAND_WORD + " " + PREFIX_VETTECH_INDEX
                + INDEX_FIRST.getOneBased() + " " + PREFIX_APPOINTMENT_INDEX
                + INDEX_FIRST.getOneBased());
        assertEquals(new AddVetTechToAppointmentCommand(INDEX_FIRST, INDEX_FIRST), command);
    }

    @Test
    public void parseCommand_removeVetTechFromAppointment() throws Exception {
        RemoveVetTechFromAppointmentCommand command = (RemoveVetTechFromAppointmentCommand) parser.parseCommand(
                RemoveVetTechFromAppointmentCommand.COMMAND_WORD + " " + INDEX_FIRST.getOneBased());
        assertEquals(new RemoveVetTechFromAppointmentCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_history() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_WORD + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " client") instanceof ListCommand);
        assertTrue(parser.parseCommand("list client") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listAlias() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " client") instanceof ListCommand);
        assertTrue(parser.parseCommand("ls client") instanceof ListCommand);
    }

    @Test
    public void parseCommand_select() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_redoCommandWord_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_WORD) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("rd 1") instanceof RedoCommand);
    }

    @Test
    public void parseCommand_undoCommandWord_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_WORD) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("ud 3") instanceof UndoCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        parser.parseCommand("");
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() throws Exception {
        thrown.expect(ParseException.class);
        thrown.expectMessage(MESSAGE_UNKNOWN_COMMAND);
        parser.parseCommand("unknownCommand");
    }
}
```
###### \java\seedu\address\model\appointment\AppointmentTest.java
``` java
public class AppointmentTest {

    @Test
    public void equals() {

        Appointment appointment1 = new Appointment(new Date("2018-01-01"), new Time("14:00"),
                new Duration("30"), new Description("Sterilize Garfield"));


        // same object -> returns true
        assertTrue(appointment1.equals(appointment1));

        // same values -> returns true
        Appointment appointment1Copy = new Appointment(appointment1.getDate(), appointment1.getTime(),
                appointment1.getDuration(), appointment1.getDescription());
        assertTrue(appointment1.equals(appointment1Copy));

        // different types -> returns false
        assertFalse(appointment1.equals(10));

        // null -> returns false
        assertFalse(appointment1.equals(null));

        // different appointment -> returns false
        Appointment differentAppointmentWithDifferentDuration = new Appointment(new Date("2018-11-20"),
                    new Time("15:15"), new Duration("100"), new Description("Sterilize Garfield"));
        assertFalse(appointment1.equals(differentAppointmentWithDifferentDuration));

        Appointment differentAppointmentWithSameDuration = new Appointment(new Date("2018-11-20"),
                    new Time("15:15"), new Duration("30"), new Description("Sterilize Garfield"));
        assertFalse(appointment1.equals(differentAppointmentWithSameDuration));

        // same timing and date
        Appointment duplicateAppointmentWithDifferentDuration = new Appointment(new Date("2018-01-01"),
                new Time("14:00"), new Duration("100"), new Description("Sterilize Garfield"));
        assertTrue(appointment1.equals(duplicateAppointmentWithDifferentDuration));

        //duplicate appointment -> returns true

        Appointment duplicateAppointmentWithSameDuration = new Appointment(new Date("2018-01-01"),
                    new Time("14:00"), new Duration("30"), new Description("Sterilize Garfield"));
        assertTrue(appointment1.equals(duplicateAppointmentWithSameDuration));
    }

}
```
###### \java\seedu\address\model\UniqueAppointmentListTest.java
``` java
public class UniqueAppointmentListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAppointmentList.asObservableList().remove(0);
    }
}
```
###### \java\seedu\address\testutil\AppointmentBuilder.java
``` java
/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {
    public static final String DEFAULT_DATE = "2018-01-01";
    public static final String DEFAULT_TIME = "00:00";
    public static final String DEFAULT_DURATION = "30";
    public static final String DEFAULT_DESCRIPTION = "Sterilize garfield";

    private Date date;
    private Time time;
    private Duration duration;
    private Description description;

    public AppointmentBuilder() {
        date = new Date(DEFAULT_DATE);
        time = new Time(DEFAULT_TIME);
        duration = new Duration(DEFAULT_DURATION);
        description = new Description(DEFAULT_DESCRIPTION);
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code appointmentToCopy}.
     */
    public AppointmentBuilder(Appointment appointmentToCopy) {
        date = appointmentToCopy.getDate();
        time = appointmentToCopy.getTime();
        duration = appointmentToCopy.getDuration();
        description = appointmentToCopy.getDescription();

    }

    /**
     * Sets the {@code Date} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withTime(String time) {
        this.time = new Time(time);
        return this;
    }

    /**
     * Sets the {@code Duration} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDuration(String duration) {
        this.duration = new Duration(duration);
        return this;
    }

    /**
     * Sets the {@code Description} of the code {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    public Appointment build() {
        return new Appointment(date, time, duration, description);
    }

}
```
###### \java\seedu\address\testutil\AppointmentUtil.java
``` java
/**
 * A utility class for Appointment.
 */
public class AppointmentUtil {

    /**
     * Returns an schedule command string for scheduling the {@code appointment}.
     */
    public static String getScheduleCommand(Appointment appointment) {
        return ScheduleCommand.COMMAND_WORD + " " + getAppointmentDetails(appointment);
    }

    /**
     * Returns the part of command string for the given {@code appointment}'s details.
     */
    public static String getAppointmentDetails(Appointment appointment) {

        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_DATE + appointment.getDate().toString() + " ");
        sb.append(PREFIX_TIME + appointment.getTime().toString() + " ");
        sb.append(PREFIX_DURATION + appointment.getDuration().toString() + " ");
        sb.append(PREFIX_DESCRIPTION + appointment.getDescription().toString() + " ");

        return sb.toString();
    }
}
```
###### \java\seedu\address\testutil\RescheduleAppointmentDescriptorBuilder.java
``` java
/**
 * A utility class to help with building RescheduleAppointmentBuilder objects.
 */
public class RescheduleAppointmentDescriptorBuilder {

    private RescheduleAppointmentDescriptor descriptor;

    public RescheduleAppointmentDescriptorBuilder() {
        descriptor = new RescheduleAppointmentDescriptor();
    }

    public RescheduleAppointmentDescriptorBuilder(RescheduleAppointmentDescriptor descriptor) {
        this.descriptor = new RescheduleAppointmentDescriptor(descriptor);
    }

    /**
     * Returns an {@code RescheduleAppointmentDescriptorBuilder} with fields containing {@code appointments}'s details
     */
    public RescheduleAppointmentDescriptorBuilder(Appointment appointment) {
        descriptor = new RescheduleAppointmentDescriptor();
        descriptor.setDate(appointment.getDate());
        descriptor.setTime(appointment.getTime());
        descriptor.setDuration(appointment.getDuration());
        descriptor.setDescription(appointment.getDescription());
    }

    /**
     * Sets the {@code Date} of the {@code RescheduleAppointmentDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDate(String date) {
        descriptor.setDate(new Date(date));
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code RescheduleAppointmentDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withTime(String time) {
        descriptor.setTime(new Time(time));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDuration(String duration) {
        descriptor.setDuration(new Duration(duration));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new Description(description));
        return this;
    }

    public RescheduleAppointmentDescriptor build() {
        return descriptor;
    }
}
```
###### \java\seedu\address\testutil\TypicalAppointments.java
``` java
    /**
     * Returns an {@code AddressBook} with all the typical appointments.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Appointment appointment : getTypicalAppointments()) {
            try {
                ab.scheduleAppointment(appointment);
            } catch (DuplicateAppointmentException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }



    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(Arrays.asList(APPOINTMENT_1, APPOINTMENT_2));
    }
}
```
