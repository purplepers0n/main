# md-azsa
###### \java\seedu\address\logic\commands\AddAppointmentToPetCommand.java
``` java
/**
 * Adds an appointment to the pet object.
 */
public class AddAppointmentToPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addappttopet";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to a pet "
            + "by the index number used in the last pet and last appointment listing.\n"
            + "Parameters: "
            + PREFIX_APPOINTMENT_INDEX + "APPOINTMENT_INDEX "
            + PREFIX_PET_INDEX + "PET_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPOINTMENT_INDEX + "1 "
            + PREFIX_PET_INDEX + "2";

    public static final String MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS = "Added Appointment to Pet:\n%1$s\n>> %2$s";
    public static final String MESSAGE_PET_HAS_APPOINTMENT = "Pet already has an appointment.";
    public static final String MESSAGE_PET_DOES_NOT_HAVE_OWNER = "This pet does not have an owner yet!";

    private final Index appointmentIndex;
    private final Index petIndex;

    private Optional<Appointment> appointment;
    private Optional<Pet> pet;

    /**
     * @parem appointmentIndex of the appointment in the filtered appointment list to add
     * @param petIndex of the pet in the filtered pet list to add to.
     */
    public AddAppointmentToPetCommand(Index appointmentIndex, Index petIndex) {
        requireAllNonNull(appointmentIndex, petIndex);

        this.appointmentIndex = appointmentIndex;
        this.petIndex = petIndex;

        appointment = Optional.empty();
        pet = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(model, pet.get(), appointment.get());
        try {
            model.addAppointmentToPet(appointment.get(), pet.get());
        } catch (PetAlreadyHasAppointmentException e) {
            throw new CommandException(MESSAGE_PET_HAS_APPOINTMENT);
        } catch (ClientPetAssociationNotFoundException e) {
            throw new CommandException(MESSAGE_PET_DOES_NOT_HAVE_OWNER);
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        } catch (AppointmentHasBeenTakenException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_TAKEN);
        }
        return new CommandResult(String.format(MESSAGE_ADD_APPOINTMENT_TO_PET_SUCCESS, appointment.get(), pet.get()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();
        List<Pet> lastShownPetList = model.getFilteredPetList();

        if (appointmentIndex.getZeroBased() >= lastShownAppointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
        if (petIndex.getZeroBased() >= lastShownPetList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_INDEX);
        }

        appointment = Optional.of(lastShownAppointmentList.get(appointmentIndex.getZeroBased()));
        pet = Optional.of(lastShownPetList.get(petIndex.getZeroBased()));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddAppointmentToPetCommand)) {
            return false;
        }

        AddAppointmentToPetCommand aatpc = (AddAppointmentToPetCommand) other;
        return appointmentIndex.equals(aatpc.appointmentIndex)
                && petIndex.equals(aatpc.petIndex)
                && appointment.equals(aatpc.appointment)
                && pet.equals(aatpc.pet);
    }
}
```
###### \java\seedu\address\logic\commands\AddPetCommand.java
``` java
/**
 * Adds support for adding a pet into the program.
 */
public class AddPetCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addp";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a pet to a specified client. "
            + "Parameters: "
            + PREFIX_CLIENT_INDEX + "CLIENT INDEX "
            + PREFIX_PET_NAME + "PET NAME "
            + PREFIX_PET_AGE + "PET AGE "
            + PREFIX_PET_GENDER + "PET GENDER "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_CLIENT_INDEX + "1 "
            + PREFIX_PET_NAME + "Garfield "
            + PREFIX_PET_AGE + "5 "
            + PREFIX_PET_GENDER + "M "
            + PREFIX_TAG + "cat "
            + PREFIX_TAG + "tabby ";

    public static final String MESSAGE_SUCCESS = "New pet added: %1$s";
    public static final String MESSAGE_DUPLICATE_PET = "This pet already exists in the address book";
    public static final String MESSAGE_CLIENT_HAS_PET = "Client already has pet";
    public static final String MESSAGE_PET_HAS_OWNER = "Pet already has an owner";

    private final Pet petToAdd;
    private final Index clientIndex;

    private Optional<Client> client;


    /**
     * Creates a AddPetCommand to add the specified {@code pet}
     */
    public AddPetCommand(Pet pet, Index clientIndex) {
        requireNonNull(pet);
        this.clientIndex = clientIndex;

        this.petToAdd = pet;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        try {
            model.addPet(petToAdd);
            model.addPetToClient(petToAdd, client.get());
            model.updateFilteredPetList(PREDICATE_SHOW_ALL_PETS);
            model.updateFilteredClientOwnPetAssocation(PREDICATE_SHOW_ALL_ASSOCIATION);
        } catch (DuplicatePetException e) {
            throw new CommandException(MESSAGE_DUPLICATE_PET);
        } catch (ClientAlreadyOwnsPetException e) {
            throw new CommandException(MESSAGE_CLIENT_HAS_PET);
        } catch (PetAlreadyHasOwnerException e) {
            throw new CommandException(MESSAGE_PET_HAS_OWNER);
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, petToAdd));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Client> lastShownListClient = model.getFilteredClientList();
        if (clientIndex.getZeroBased() >= lastShownListClient.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        client = Optional.of(lastShownListClient.get(clientIndex.getZeroBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof AddPetCommand
                && petToAdd.equals(((AddPetCommand) other).petToAdd));
    }

}
```
###### \java\seedu\address\logic\commands\DeletePetCommand.java
``` java
/**
 * Deletes a pet identified using it's last displayed index from the program
 */
public class DeletePetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deletep";
    public static final String COMMAND_ALIAS = "delp";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the pet identified by the indexnumber used in the last pet listing\n"
            + "Parameters: INDEX (must be a postive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PET_SUCCESS = "Delete Pet: %1$s";

    private final Index targetIndex;

    private Pet petToDelete;

    public DeletePetCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(petToDelete);
        try {
            model.deletePet(petToDelete);
        } catch (PetNotFoundException pnfe) {
            throw new AssertionError("The target pet cannot be missing");
        } catch (ClientPetAssociationNotFoundException e) {
            throw new AssertionError("Client pet association cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_DELETE_PET_SUCCESS, petToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Pet> lastShownList = model.getFilteredPetList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        petToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof DeletePetCommand
                && this.targetIndex.equals(((DeletePetCommand) other).targetIndex)
                && Objects.equals(this.petToDelete, ((DeletePetCommand) other).petToDelete));
    }
}
```
###### \java\seedu\address\logic\commands\RemoveAppointmentFromPetCommand.java
``` java
/**
 * Removes the appointment from a pet
 */
public class RemoveAppointmentFromPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removeapptfrompet";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes an appointment from a pet "
            + "by the index number used din the last appointment listing.\n "
            + "Parameters: "
            + PREFIX_APPOINTMENT_INDEX + "APPOINTMENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_APPOINTMENT_INDEX + "1";

    public static final String MESSAGE_REMOVE_APPOINTMENT_SUCCESS = "Removed appointment from pet: %1$s\n";

    private final Index appointmentIndex;

    private Optional<Appointment> appointment;

    /**
     * @param appointmentIndex of the appointment in the filtered appointment list
     */
    public RemoveAppointmentFromPetCommand(Index appointmentIndex) {
        requireNonNull(appointmentIndex);

        this.appointmentIndex = appointmentIndex;

        appointment = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireAllNonNull(model, appointment.get());
        try {
            model.removeAppointmentFromPet(appointment.get());
            EventsCenter.getInstance().post(new NewApptAvailableEvent(appointment.toString()));
        } catch (AppointmentNotFoundException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(ScheduleCommand.MESSAGE_DUPLICATE_APPOINTMENT);
        } catch (AppointmentDoesNotHavePetException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_NO_PET);
        }
        return new CommandResult(String.format(MESSAGE_REMOVE_APPOINTMENT_SUCCESS, appointment.get()));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();

        if (appointmentIndex.getZeroBased() >= lastShownAppointmentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        appointment = Optional.of(lastShownAppointmentList.get(appointmentIndex.getZeroBased()));
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof RemoveAppointmentFromPetCommand)) {
            return false;
        }

        RemoveAppointmentFromPetCommand e = (RemoveAppointmentFromPetCommand) other;
        return appointmentIndex.equals(e.appointmentIndex)
                && appointment.equals(e.appointment);
    }

}
```
###### \java\seedu\address\logic\commands\SortAppointmentCommand.java
``` java
/**
 * Sorts the appointment list.
 */
public class SortAppointmentCommand extends  UndoableCommand {

    public static final String COMMAND_WORD = "sortappt";
    public static final String MESSAGE_SUCCESS = "Appointment list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortAppointmentList();
        } catch (AppointmentListIsEmptyException e) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\SortClientCommand.java
``` java
/**
 * Sorts the client list
 */
public class SortClientCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortc";

    public static final String MESSAGE_SUCCESS = "Client list sorted";


    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortClientList();
        } catch (PersonsListIsEmptyException e) {
            throw new CommandException(Messages.MESSAGE_PERSONSLIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\SortPetCommand.java
``` java
/**
 * Sorts the pet list.
 */
public class SortPetCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "sortp";
    public static final String MESSAGE_SUCCESS = "Pet list successfully sorted";

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        try {
            model.sortPetList();
        } catch (ClientPetAssociationListEmptyException e) {
            throw new CommandException(Messages.MESSAGE_CLIENTPETLIST_EMPTY);
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\commands\UnscheduleCommand.java
``` java
/**
 * Deletes an appointment identifiedusing it's last displayed index from the program.
 */
public class UnscheduleCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "unschedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the appointment identified by the index number used in the appointment listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS =
            "Unscheduled appointment: %1$s";

    private final Index targetIndex;

    private Appointment appointmentToDelete;

    public UnscheduleCommand (Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(appointmentToDelete);
        try {
            model.unscheduleAppointment(appointmentToDelete);
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target cannot be missing.");
        } catch (AppointmentListIsEmptyException e) {
            throw new AssertionError("Appointment cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_UNSCHEDULE_APPOINTMENT_SUCCESS, appointmentToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (lastShownList.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_APPOINTMENT_LIST_EMPTY);
        } else if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        appointmentToDelete = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof UnscheduleCommand
                && this.targetIndex.equals(((UnscheduleCommand) other).targetIndex)
                && Objects.equals(this.appointmentToDelete, ((UnscheduleCommand) other).appointmentToDelete));
    }
}
```
###### \java\seedu\address\logic\parser\AddAppointmentToPetCommandParser.java
``` java
/**
 * Parses the input arguments and creates a new AddAppointmentToPet object.
 */
public class AddAppointmentToPetCommandParser implements Parser<AddAppointmentToPetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddAppointmentToPetCommand
     * returns the specified object for execution.
     * @throws ParseException if the user does not conform to expected format.
     */
    public AddAppointmentToPetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultiMap =
                ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_INDEX, PREFIX_PET_INDEX);
        Index appointmentIndex;
        Index petIndex;

        if (!arePrefixesPresent(argMultiMap, PREFIX_APPOINTMENT_INDEX, PREFIX_PET_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE));
        }

        try {
            appointmentIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_APPOINTMENT_INDEX).get());
            petIndex = ParserUtil.parseIndex(argMultiMap.getValue(PREFIX_PET_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentToPetCommand.MESSAGE_USAGE));
        }

        return new AddAppointmentToPetCommand(appointmentIndex, petIndex);
    }

    /**
     * Returns true if the prefixes contain the values
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\logic\parser\AddPetCommandParser.java
``` java
/**
 * Parses the input arguments and create a new AddPetCommand object.
 */
public class AddPetCommandParser implements Parser<AddPetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddPetCommand
     * and returns an AddPetCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public AddPetCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_CLIENT_INDEX,
                        PREFIX_PET_NAME, PREFIX_PET_AGE, PREFIX_PET_GENDER, PREFIX_TAG);

        Index indexClient;

        if (!arePrefixesPresent(argMultimap, PREFIX_CLIENT_INDEX, PREFIX_PET_NAME, PREFIX_PET_AGE,
                PREFIX_PET_GENDER, PREFIX_TAG)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetCommand.MESSAGE_USAGE));
        }

        try {
            indexClient = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_CLIENT_INDEX).get());
            PetName petName = ParserUtil.parsePetName(argMultimap.getValue(PREFIX_PET_NAME).get());
            PetAge petAge = ParserUtil.parsePetAge(argMultimap.getValue(PREFIX_PET_AGE).get());
            PetGender petGender = ParserUtil.parsePetGender(argMultimap.getValue(PREFIX_PET_GENDER).get());
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            Pet pet = new Pet(petName, petAge, petGender, tagList);

            return new AddPetCommand(pet, indexClient);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty values in the argument.
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\logic\parser\DeletePetCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeletePetCommand object
 */
public class DeletePetCommandParser implements Parser<DeletePetCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of DeletePetCommand
     * returns DeletePetCommand object for execution
     * @throws ParseException if user input does not conform to expected format
     */
    @Override
    public DeletePetCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeletePetCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeletePetCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\RemoveAppointmentFromPetParser.java
``` java
/**
 * Parses input arguments and creates a new RemoveAppointmentFromPet object
 */
public class RemoveAppointmentFromPetParser implements
        Parser<RemoveAppointmentFromPetCommand> {

    @Override
    public RemoveAppointmentFromPetCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_APPOINTMENT_INDEX);

        Index appointmentIndex;

        if (!arePrefixesPresent(argMultimap, PREFIX_APPOINTMENT_INDEX)) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveAppointmentFromPetCommand.MESSAGE_USAGE));
        }

        try {
            appointmentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPOINTMENT_INDEX).get());
        } catch (IllegalValueException ie) {
            throw new ParseException(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    RemoveAppointmentFromPetCommand.MESSAGE_USAGE));
        }
        return new RemoveAppointmentFromPetCommand(appointmentIndex);
    }

    /**
     * Returns true if none of the prefixes contains empty
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\logic\parser\UnscheduleCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.UnscheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;

```
###### \java\seedu\address\logic\parser\UnscheduleCommandParser.java
``` java
/**
 * Parses input arguments and creates a new UnscheduleCommand object.
 */
public class UnscheduleCommandParser implements Parser<UnscheduleCommand> {

    /**
     * Parses the given {@code String} of arguments in the context
     * of UnscheduleCOmmand
     * returns UnscheduleCommand object for execution
     * @throws ParseException if user input does not conform to expected format
     */
    @Override
    public UnscheduleCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnscheduleCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnscheduleCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Sorts the persons list lexicographically.
     * @throws PersonsListIsEmptyException
     */
    public void sortClientList() throws PersonsListIsEmptyException {
        if (persons.isEmpty()) {
            throw new PersonsListIsEmptyException();
        } else {
            this.persons.sort();
        }
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Sets the list of pets to contain data
     */
    public void setPets(List<Pet> pets) throws DuplicatePetException {
        this.pets.setPets(pets);
    }

    /**
     * Sorts the pet list lexicographically.
     */
    public void sortPetList() throws ClientPetAssociationListEmptyException {
        if (clientPetAssociations.isEmpty()) {
            throw new ClientPetAssociationListEmptyException();
        } else {
            this.clientPetAssociations.sort((ClientOwnPet a, ClientOwnPet b) ->
                    a.getPet().getPetName().toString().toLowerCase()
                    .compareTo(b.getPet().getPetName().toString().toLowerCase()));
        }
    }

    /**
     * Sorts the appointment internal list.
     *
     * @throws AppointmentListIsEmptyException
     */
    public void sortAppointmentList() throws AppointmentListIsEmptyException {
        if (appointments.isEmpty()) {
            throw new AppointmentListIsEmptyException();
        } else {
            appointments.sort();
        }
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Unschedules an appointment
     */
    public void unscheduleAppointment(Appointment key) throws AppointmentListIsEmptyException,
            AppointmentNotFoundException {
        if (appointments.isEmpty()) {
            throw new AppointmentListIsEmptyException();
        } else {
            appointments.remove(key);
        }
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Adds a pet to the program.
     * Also checks the new pet's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the pet to point to those in {@link #tags}.
     *
     * @throws DuplicatePetException if an equivalent pet already exists.
     */
    public void addPet(Pet p) throws DuplicatePetException {
        Pet pet = syncWithMasterPetTagList(p);
        pets.add(pet);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     *
     * @throws PetNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removePet(Pet key) throws PetNotFoundException, ClientPetAssociationNotFoundException {
        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getClientOwnPet() != null
                    && appointment.getClientOwnPet().getPet().equals(key)) {
                appointment.setClientOwnPetToNull();
            }
        }
        for (ClientOwnPet association : clientPetAssociations) {
            if (association.getPet().equals(key)) {
                clientPetAssociations.remove(association);
                found = true;
                break;
            }
        }
        if (!found) {
            throw new ClientPetAssociationNotFoundException();
        }
        if (pets.remove(key)) {
            return true;
        } else {
            throw new PetNotFoundException();
        }
    }

    /**
     * Updates the master tag list to include tags in {@code person} that are not in the list.
     *
     * @return a copy of this {@code person} such that every tag in this person points to a Tag object in the master
     * list.
     */
    private Pet syncWithMasterPetTagList(Pet pet) {
        Pet syncedPet;

        final UniqueTagList petTags = new UniqueTagList(pet.getTags());
        tags.mergeFrom(petTags);

        // Create map with values = tag object references in the master list
        // used for checking person tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of person tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        petTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));

        syncedPet = new Pet(pet.getPetName(), pet.getPetAge(), pet.getPetGender(), correctTagReferences);
        return syncedPet;
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Finds the pet and adds the appointment
     */
    public void addAppointmentToPet(Appointment appointment, Pet pet) throws PetAlreadyHasAppointmentException,
            ClientPetAssociationNotFoundException, AppointmentNotFoundException, DuplicateAppointmentException,
            AppointmentHasBeenTakenException {

        boolean isAdded = false;
        boolean isPresent = false;

        if (clientPetAssociations.isEmpty()) {
            throw new ClientPetAssociationNotFoundException();
        }
        if (appointment.getClientOwnPet() != null) {
            throw new AppointmentHasBeenTakenException();
        }

        for (ClientOwnPet a : clientPetAssociations) {
            if (a.getPet().equals(pet)) {
                isPresent = true;
                if (appointment.getClientOwnPet() == null) {
                    Appointment appointmentCopy = new Appointment(appointment);
                    appointmentCopy.setClientOwnPet(a);
                    appointments.setAppointment(appointment, appointmentCopy);
                    isAdded = true;
                }
            }
        }

        if (!isPresent) {
            throw new ClientPetAssociationNotFoundException();
        }
        if (isPresent && !isAdded) {
            throw new PetAlreadyHasAppointmentException();
        }
    }

    /**
     * Removes the appointment from a pet
     */
    public void removeAppointmentFromPet(Appointment appointment) throws
            AppointmentNotFoundException, DuplicateAppointmentException, AppointmentDoesNotHavePetException {
        if (!appointments.contains(appointment)) {
            throw new AppointmentNotFoundException();
        } else {
            Appointment appointmentCopy = new Appointment(appointment);

            if (appointmentCopy.getClientOwnPet() == null) {
                throw new AppointmentDoesNotHavePetException();
            } else {
                appointmentCopy.setClientOwnPetToNull();
                appointments.setAppointment(appointment, appointmentCopy);
            }
        }
    }
```
###### \java\seedu\address\model\appointment\Date.java
``` java
    /**
     * Negative if argument is smaller
     * Postiive if argument is larger
     */
    public int compareToDate(Date other) {
        if (this.date.equals(other.date)) {
            return 0;
        } else if (this.date.compareTo(other.date) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
```
###### \java\seedu\address\model\appointment\Description.java
``` java
/**
 * Represents the description of an Appointment
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Description of the appointment cannot be a blank space.";

    // Should be non-empty input
    public static final String DESCRIPTION_VALIDATION_REGEX = "(.*\\S.*)";

    public final String description;

    /**
     * Constructs a {@code Description}
     */
    public Description(String description) {
        requireNonNull(description);
        checkArgument(isValidDescription(description), MESSAGE_DESCRIPTION_CONSTRAINTS);
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Description
                && this.description.equals(((Description) other).description));
    }

    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }
}
```
###### \java\seedu\address\model\appointment\Time.java
``` java
    /**
     * Negative if argument is smaller
     * Postiive if argument is larger
     */
    public int compareToTime(Time other) {
        if (this.time.equals(other.time)) {
            return 0;
        } else if (this.time.compareTo(other.time) < 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
```
###### \java\seedu\address\model\appointment\UniqueAppointmentList.java
``` java
    /**
     * Sorts the internal list
     */
    public void sort() {
        SortedList<Appointment> sortedList = new SortedList<>(internalList, Appointment::compareTo);
        internalList.setAll(sortedList);
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void unscheduleAppointment(Appointment appointment) throws
            AppointmentNotFoundException, AppointmentListIsEmptyException {
        addressBook.unscheduleAppointment(appointment);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void sortClientList() throws PersonsListIsEmptyException {
        addressBook.sortClientList();
        indicateAddressBookChanged();
    }


    // Pet

    @Override
    public synchronized void addPet(Pet pet) throws DuplicatePetException {
        addressBook.addPet(pet);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void deletePet(Pet target) throws PetNotFoundException, ClientPetAssociationNotFoundException {
        addressBook.removePet(target);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void sortPetList() throws ClientPetAssociationListEmptyException {
        addressBook.sortPetList();
        indicateAddressBookChanged();
    }

    @Override
    public void addAppointmentToPet(Appointment appointment, Pet pet)
            throws PetAlreadyHasAppointmentException, ClientPetAssociationNotFoundException,
            AppointmentNotFoundException, DuplicateAppointmentException, AppointmentHasBeenTakenException {
        requireAllNonNull(appointment, pet);
        addressBook.addAppointmentToPet(appointment, pet);
        indicateAddressBookChanged();
    }

    @Override
    public void removeAppointmentFromPet(Appointment appointment)
            throws AppointmentNotFoundException, DuplicateAppointmentException, AppointmentDoesNotHavePetException {
        requireNonNull(appointment);
        addressBook.removeAppointmentFromPet(appointment);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void sortAppointmentList() throws AppointmentListIsEmptyException {
        addressBook.sortAppointmentList();
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\person\UniquePersonList.java
``` java
    /**
     * Sorts the internal list
     */
    public void sort() {
        internalList.sort((Person one, Person two) -> one.getName().toString().toLowerCase()
                .compareTo(two.getName().toString().toLowerCase()));
    }
```
###### \java\seedu\address\model\pet\Pet.java
``` java
/**
 * Represents a Pet in the applications.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Pet {

    private final PetName petName;
    private final PetAge petAge;
    private final PetGender petGender;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null
     */
    public Pet(PetName petName, PetAge petAge, PetGender petGender, Set<Tag> tags) {
        requireAllNonNull(petName, petAge, petGender, tags);
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;

        //protect internal tags from changes in the arg lis
        this.tags = new UniqueTagList(tags);
    }

    public PetName getPetName() {
        return petName;
    }

    public PetAge getPetAge() {
        return petAge;
    }

    public PetGender getPetGender() {
        return petGender;
    }


    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Pet)) {
            return false;
        }

        Pet otherPet = (Pet) other;
        return otherPet.getPetName().equals(this.getPetName())
                && otherPet.getPetAge().equals(this.getPetAge())
                && otherPet.getPetGender().equals(this.getPetGender());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(petName, petAge, petGender);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Pet Name: ")
                .append(getPetName())
                .append(" Pet Age: ")
                .append(getPetAge())
                .append(" Gender: ")
                .append(getPetGender())
                //.append(" Pet Owner: ")
                .append(" Species/Breed: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }



}
```
###### \java\seedu\address\model\pet\PetAge.java
``` java
/**
 * Represents a Pet's age in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidPetAge(String)}
 */
public class PetAge {

    public static final String MESSAGE_PETAGE_CONSTRAINTS =
            "Pet age can only contain numbers, and should be 1-2 digit long";
    public static final String PET_VALIDATIONS_REGEX = "\\d{1,2}";
    public final String value;

    /**
     * Constructs a {@code PetAge}.
     *
     * @param petAge A valid pet age
     */
    public PetAge(String petAge) {
        requireNonNull(petAge);
        checkArgument(isValidPetAge(petAge), MESSAGE_PETAGE_CONSTRAINTS);
        this.value = petAge;
    }

    /**
     * Returns true if a given is a valid pet age number
     */
    public static boolean isValidPetAge(String test) {
        return test.matches(PET_VALIDATIONS_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if object
                || (other instanceof PetAge // instanceof handles nulls
                && this.value.equals(((PetAge) other).value)); //state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### \java\seedu\address\model\pet\PetGender.java
``` java
/**
 * Represents a Pet's gender in the program.
 * Guarantees: immutable; is valid as declared in {@link #isValidGender(String)}
 */
public class PetGender {

    public static final String MESSAGE_PETGENDER_CONSTRAINTS =
            "Pet gender can only be m, f, M or F.";

    /*
     * The string can only be m,f,M,F
     */
    public static final String GENDER_VALIDATION_REGEX = "[m,f,M,F]";

    public final String fullGender;

    /**
     * Constructs a {@code PetGender}
     *
     * @param petGender A valid pet gender
     */
    public PetGender(String petGender) {
        requireNonNull(petGender);
        checkArgument(isValidGender(petGender), MESSAGE_PETGENDER_CONSTRAINTS);
        this.fullGender = petGender.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid gender
     */
    public static boolean isValidGender(String test) {
        return test.matches(GENDER_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullGender;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PetGender)
                && this.fullGender.equals(((PetGender) other).fullGender);
    }

    @Override
    public int hashCode() {
        return fullGender.hashCode();
    }
}
```
###### \java\seedu\address\model\pet\PetName.java
``` java
/**
 * Represents a pet's name in the address book.
 * Guarantees: imumutable; is valid as declared in {@link #isValidPetName(String)}
 */
public class PetName {

    public static final String MESSAGE_PETNAME_CONSTRAINTS =
            "Pet names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
      * The first character must not be whitespace,
      * otherwise " " (a blank string) becomes a valid input.
      */
    public static final String PETNAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullPetName;

    /**
     * Constructs a {@code Name}
     *
     * @param petName A valid pet name
     */
    public PetName(String petName) {
        requireNonNull(petName);
        checkArgument(isValidPetName(petName), MESSAGE_PETNAME_CONSTRAINTS);
        this.fullPetName = petName;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidPetName(String test) {
        return test.matches(PETNAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullPetName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PetName //instanceof handles null
                && this.fullPetName.equals(((PetName) other).fullPetName)); //state check
    }

    @Override
    public int hashCode() {
        return fullPetName.hashCode();
    }
}
```
###### \java\seedu\address\model\pet\UniquePetList.java
``` java
/**
 * A list of pets that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Pet#equals(Object)
 */
public class UniquePetList implements Iterable<Pet> {

    private final ObservableList<Pet> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent pet as the given argument.
     */
    public boolean contains(Pet toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a pet to the list.
     *
     * @throws DuplicatePetException if the pet to add is a duplicate of an existing pet in the list.
     */
    public void add(Pet toAdd) throws DuplicatePetException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePetException();
        }
        internalList.add(toAdd);
    }


    /**
     * Removes the equivalent pet from the list.
     *
     * @throws PetNotFoundException if no such pet could be found in the list.
     */
    public boolean remove(Pet toRemove) throws PetNotFoundException {
        requireNonNull(toRemove);
        final boolean petFoundAndDeleted = internalList.remove(toRemove);
        if (!petFoundAndDeleted) {
            throw new PetNotFoundException();
        }
        return petFoundAndDeleted;
    }

    public void setPets(UniquePetList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPets(List<Pet> pets) throws DuplicatePetException {
        requireAllNonNull(pets);
        final UniquePetList replacement = new UniquePetList();
        for (final Pet pet : pets) {
            replacement.add(pet);
        }
        setPets(replacement);
    }


    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Pet> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public ObservableList<Pet> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Pet> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePetList // instanceof handles nulls
                && this.internalList.equals(((UniquePetList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\model\util\SampleDataUtilPet.java
``` java
/**
 * Contains util for populating {@code AddressBook} with sampledata.
 */
public class SampleDataUtilPet {
    public static Pet[] getSamplePets() {
        return new Pet[] {
            new Pet(new PetName("Garfield"), new PetAge("5"), new PetGender("M"),
                    getTagSet("Cat", "tabby")),
            new Pet(new PetName("Scooby Doo"), new PetAge("10"), new PetGender("M"),
                    getTagSet("Dog", "great_dane"))
        };
    }


    /**
     * Returns a tag set containg list of strings given
     */
    public static Set<Tag> getTagSet(String... strings) {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedPet.java
``` java
/**
 * JAXV-friendly version of the Person.
 */
public class XmlAdaptedPet {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pet's %s field is missing!";

    @XmlElement(required = true)
    private String petName;
    @XmlElement(required = true)
    private String petAge;
    @XmlElement(required = true)
    private String petGender;

    @XmlElement(required = true)
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Construct an XmlAdaptedPet
     * Thisis the no-arg constructor.
     */
    public XmlAdaptedPet() {}

    /**
     * Constructs the xml with pet details
     */
    public XmlAdaptedPet(String petName, String petAge, String petGender, List<XmlAdaptedTag> tagged) {
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;
        this.tagged = new ArrayList<>(tagged);
    }

    /**
     * Convers a given Pet into a class for JAXB
     *
     * @param source future changes will not affect the created XmladaptedPet
     */
    public XmlAdaptedPet(Pet source) {
        petName = source.getPetName().toString();
        petAge = source.getPetAge().value;
        petGender = source.getPetGender().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Convers this to model pet's object
     *
     * @throws IllegalValueException if any constraints
     */
    public Pet toModelType() throws IllegalValueException {
        final List<Tag> petTags = new ArrayList<>();
        Pet convertedPet;

        for (XmlAdaptedTag tag : tagged) {
            petTags.add(tag.toModelType());
        }

        if (this.petName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PetName.class.getSimpleName()));
        }

        if (!PetName.isValidPetName(this.petName)) {
            throw new IllegalValueException(PetName.MESSAGE_PETNAME_CONSTRAINTS);
        }
        final PetName petName = new PetName(this.petName);

        if (this.petAge == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PetAge.class.getSimpleName()));
        }

        if (!PetAge.isValidPetAge(this.petAge)) {
            throw new IllegalValueException(PetAge.MESSAGE_PETAGE_CONSTRAINTS);
        }
        final PetAge petAge = new PetAge(this.petAge);

        if (this.petGender == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PetGender.class.getSimpleName()));
        }

        if (!PetGender.isValidGender(this.petGender)) {
            throw new IllegalValueException(PetGender.MESSAGE_PETGENDER_CONSTRAINTS);
        }
        final PetGender petGender = new PetGender(this.petGender);

        final Set<Tag> tags = new HashSet<>(petTags);

        convertedPet = new Pet(petName, petAge, petGender, tags);

        return convertedPet;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof  XmlAdaptedPet)) {
            return false;
        }

        XmlAdaptedPet otherPet = (XmlAdaptedPet) other;
        return Objects.equals(petName, otherPet.petName)
                && Objects.equals(petAge, otherPet.petAge)
                && Objects.equals(petGender, otherPet.petGender)
                && tagged.equals(otherPet.tagged);
    }
}
```
