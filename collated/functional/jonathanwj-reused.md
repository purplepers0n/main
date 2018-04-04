# jonathanwj-reused
###### \java\seedu\address\logic\commands\AddVetTechToAppointmentCommand.java
``` java
/**
 * Adds a vet technician to an appointment in the address book.
 */
public class AddVetTechToAppointmentCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addvettechtoappointment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Add a vet technician to a appointment "
            + "by the index number used in the last vet technician and appointment listing.\n"
            + "Parameters: "
            + PREFIX_VETTECH_INDEX + "VETTECH_INDEX "
            + PREFIX_APPOINTMENT_INDEX + "APPOINTMENT_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_VETTECH_INDEX + "1 " + PREFIX_APPOINTMENT_INDEX + "2";

    public static final String MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS = "Added vet technician To"
            + " appointment:\n%1$s\n>> %2$s";

    public static final String MESSAGE_APPOINTMENT_HAS_TECH = "Appointment already has a vet technician";

    private final Index vetTechIndex;
    private final Index appointmentIndex;

    private Optional<VetTechnician> vetTech;
    private Optional<Appointment> appointment;

    /**
     * @param vetTechIndex     of the vet tech in the filtered list to add
     * @param appointmentIndex of the appointment in the filtered list to add vet tech to
     */
    public AddVetTechToAppointmentCommand(Index vetTechIndex, Index appointmentIndex) {
        requireNonNull(vetTechIndex);
        requireNonNull(appointmentIndex);

        this.vetTechIndex = vetTechIndex;
        this.appointmentIndex = appointmentIndex;

        vetTech = Optional.empty();
        appointment = Optional.empty();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        requireNonNull(vetTech.get());
        requireNonNull(appointment.get());
        try {
            model.addVetTechToAppointment(vetTech.get(), appointment.get());
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("The target appointment cannot be a duplicate");
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target appointment cannot be missing");
        } catch (AppointmentAlreadyHasVetTechnicianException e) {
            throw new CommandException(MESSAGE_APPOINTMENT_HAS_TECH);
        }

        return new CommandResult(String.format(MESSAGE_ADD_TECH_TO_APPOINTMENT_SUCCESS,
                vetTech.get(), appointment.get()));

    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<VetTechnician> lastShownListVetTech = model.getFilteredVetTechnicianList();
        List<Appointment> lastShownListAppointment = model.getFilteredAppointmentList();

        if (appointmentIndex.getZeroBased() >= lastShownListAppointment.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }
        if (vetTechIndex.getZeroBased() >= lastShownListVetTech.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        vetTech = Optional.of(lastShownListVetTech.get(vetTechIndex.getZeroBased()));
        appointment = Optional.of(lastShownListAppointment.get(appointmentIndex.getZeroBased()));
        model.updateFilteredVetTechnicianList(PREDICATE_SHOW_ALL_TECHNICIAN);
        model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENT);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddVetTechToAppointmentCommand)) {
            return false;
        }

        // state check
        AddVetTechToAppointmentCommand e = (AddVetTechToAppointmentCommand) other;
        return vetTechIndex.equals(e.vetTechIndex)
                && appointmentIndex.equals(e.appointmentIndex)
                && vetTech.equals(e.vetTech)
                && appointment.equals(e.appointment);
    }

}
```
###### \java\seedu\address\logic\commands\RemoveVetTechFromAppointmentCommand.java
``` java
/**
 * removes the vet from appointment identified using it's last displayed index from the program
 */
public class RemoveVetTechFromAppointmentCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "removevettechfromappt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": removes the vet from appointment identified by the index number"
            + " used in the last appointment listing\n"
            + "Parameters: INDEX (must be a postive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS = "Removed Vet from: %1$s";

    private final Index targetIndex;

    private Appointment apptToRemoveVetFrom;

    public RemoveVetTechFromAppointmentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(apptToRemoveVetFrom);
        try {
            model.removeVetTechFromAppointent(apptToRemoveVetFrom);
        } catch (DuplicateAppointmentException e) {
            throw new AssertionError("The target appointment cannot be a duplicate");
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target appointment cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_REMOVE_VET_FROM_APPT_SUCCESS, apptToRemoveVetFrom));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_INDEX);
        }

        apptToRemoveVetFrom = lastShownList.get(targetIndex.getZeroBased());
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof RemoveVetTechFromAppointmentCommand
                && this.targetIndex.equals(((RemoveVetTechFromAppointmentCommand) other).targetIndex)
                && Objects.equals(this.apptToRemoveVetFrom, ((RemoveVetTechFromAppointmentCommand) other)
                .apptToRemoveVetFrom));
    }
}
```
###### \java\seedu\address\logic\parser\AddVetTechToAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddVetTechToAppointmentCommand object
 */
public class AddVetTechToAppointmentCommandParser implements Parser<AddVetTechToAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddVetTechToAppointmentCommand
     * and returns an AddVetTechToAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddVetTechToAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_VETTECH_INDEX, PREFIX_APPOINTMENT_INDEX);

        Index indexVetTech;
        Index indexAppointment;

        if (!arePrefixesPresent(argMultimap, PREFIX_VETTECH_INDEX, PREFIX_APPOINTMENT_INDEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddVetTechToAppointmentCommand.MESSAGE_USAGE));
        }

        try {
            indexVetTech = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_VETTECH_INDEX).get());
            indexAppointment = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_APPOINTMENT_INDEX).get());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddVetTechToAppointmentCommand.MESSAGE_USAGE));
        }

        return new AddVetTechToAppointmentCommand(indexVetTech, indexAppointment);
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
###### \java\seedu\address\logic\parser\RemoveVetTechFromAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RemoveVetTechFromAppointmentCommand object
 */
public class RemoveVetTechFromAppointmentCommandParser implements Parser<RemoveVetTechFromAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of RemoveVetTechFromAppointmentCommand
     * returns RemoveVetTechFromAppointmentCommand object for execution
     * @throws ParseException if user input does not conform to expected format
     */
    @Override
    public RemoveVetTechFromAppointmentCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new RemoveVetTechFromAppointmentCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemoveVetTechFromAppointmentCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\model\vettechnician\exceptions\DuplicateVetTechnicianException.java
``` java
/**
 * Signals that the operation will result in duplicate VetTechnician objects
 */
public class DuplicateVetTechnicianException extends DuplicateDataException {
    public DuplicateVetTechnicianException() {
        super("Operation would result in duplicate vet technicians");
    }
}
```
###### \java\seedu\address\model\vettechnician\exceptions\VetTechnicianNotFoundException.java
``` java
/**
 * Signals the operation is unable to find the specified pet.
 */
public class VetTechnicianNotFoundException extends Exception {
}
```
###### \java\seedu\address\model\vettechnician\UniqueVetTechnicianList.java
``` java
/**
 * A list of vetTechnicians that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see VetTechnician#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueVetTechnicianList implements Iterable<VetTechnician> {

    private final ObservableList<VetTechnician> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent vetTechnician as the given argument.
     */
    public boolean contains(VetTechnician toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a vetTechnician to the list.
     *
     * @throws DuplicateVetTechnicianException if the vetTechnician
     * to add is a duplicate of an existing vetTechnician in the list.
     */
    public void add(VetTechnician toAdd) throws DuplicateVetTechnicianException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateVetTechnicianException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the vetTechnician {@code target} in the list with {@code editedvetTechnician}.
     *
     * @throws DuplicateVetTechnicianException if the replacement
     * is equivalent to another existing vetTechnician in the list.
     * @throws VetTechnicianNotFoundException if {@code target} could not be found in the list.
     */
    public void setVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
            throws DuplicateVetTechnicianException, VetTechnicianNotFoundException {
        requireNonNull(editedVetTechnician);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new VetTechnicianNotFoundException();
        }

        if (!target.equals(editedVetTechnician) && internalList.contains(editedVetTechnician)) {
            throw new DuplicateVetTechnicianException();
        }

        internalList.set(index, editedVetTechnician);
    }

    /**
     * Removes the equivalent vetTechnician from the list.
     *
     * @throws VetTechnicianNotFoundException if no such vetTechnician could be found in the list.
     */
    public boolean remove(VetTechnician toRemove) throws VetTechnicianNotFoundException {
        requireNonNull(toRemove);
        final boolean vetTechnicianFoundAndDeleted = internalList.remove(toRemove);
        if (!vetTechnicianFoundAndDeleted) {
            throw new VetTechnicianNotFoundException();
        }
        return vetTechnicianFoundAndDeleted;
    }

    public void setVetTechnicians(UniqueVetTechnicianList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setVetTechnicians(List<VetTechnician> vetTechnicians) throws DuplicateVetTechnicianException {
        requireAllNonNull(vetTechnicians);
        final UniqueVetTechnicianList replacement = new UniqueVetTechnicianList();
        for (final VetTechnician vetTechnician : vetTechnicians) {
            replacement.add(vetTechnician);
        }
        setVetTechnicians(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<VetTechnician> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<VetTechnician> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueVetTechnicianList // instanceof handles nulls
                && this.internalList.equals(((UniqueVetTechnicianList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\model\vettechnician\VetTechnician.java
``` java
/**
 * Represents a Vet Technician in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class VetTechnician extends Person {

    /**
     * Every field must be present and not null.
     */
    public VetTechnician(Name name, Phone phone, Email email,
                         Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);

    }

    @Override
    public PersonRole getRole() {
        return PersonRole.TECHNICIAN_ROLE;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VetTechnician)) {
            return false;
        }

        VetTechnician otherTechnician = (VetTechnician) other;
        return otherTechnician.getName().equals(this.getName())
                && otherTechnician.getPhone().equals(this.getPhone())
                && otherTechnician.getEmail().equals(this.getEmail())
                && otherTechnician.getAddress().equals(this.getAddress());
    }


}
```
###### \java\seedu\address\storage\XmlAdaptedClientOwnPet.java
``` java
/**
 * JAXV-friendly version of the ClientOwnPet.
 */
public class XmlAdaptedClientOwnPet {

    @XmlElement(required = true)
    private XmlAdaptedPet pet;
    @XmlElement(required = true)
    private XmlAdaptedPerson client;

    /**
     * Constructs an XmlAdaptedClientOwnPet.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedClientOwnPet() {}

    /**
     * Constructs an {@code XmlAdaptedClientOwnPet} with the given {@code ClientOwnPet}
     */
    public XmlAdaptedClientOwnPet(ClientOwnPet association) {
        this.pet = new XmlAdaptedPet(association.getPet());
        this.client = new XmlAdaptedPerson(association.getClient());
    }

    /**
     * Converts this jaxb-friendly adapted association object into the model's ClientOwnPet object.
     */
    public ClientOwnPet toModelType() throws IllegalValueException {
        return new ClientOwnPet((Client) client.toModelType(), pet.toModelType());
    }

    public Pet getPet() throws IllegalValueException {
        return pet.toModelType();
    }

    public Client getClient() throws IllegalValueException {
        return (Client) client.toModelType();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedClientOwnPet)) {
            return false;
        }

        XmlAdaptedClientOwnPet otherAdapted = (XmlAdaptedClientOwnPet) other;
        return pet.equals(otherAdapted.pet) && client.equals(otherAdapted.client);
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    /**
     * Converts this jaxb-friendly adapted person object into the model's Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        Person convertedPerson;

        for (XmlAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }

        if (this.name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(this.name)) {
            throw new IllegalValueException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        final Name name = new Name(this.name);

        if (this.phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(this.phone)) {
            throw new IllegalValueException(Phone.MESSAGE_PHONE_CONSTRAINTS);
        }
        final Phone phone = new Phone(this.phone);

        if (this.email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(this.email)) {
            throw new IllegalValueException(Email.MESSAGE_EMAIL_CONSTRAINTS);
        }
        final Email email = new Email(this.email);

        if (this.address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(this.address)) {
            throw new IllegalValueException(Address.MESSAGE_ADDRESS_CONSTRAINTS);
        }
        final Address address = new Address(this.address);

        final Set<Tag> tags = new HashSet<>(personTags);

        if (this.role == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PersonRole.class.getSimpleName()));
        }
        if (!PersonRole.isValidPersonRole(this.role)) {
            throw new IllegalValueException(PersonRole.MESSAGE_ROLE_CONSTRAINTS);
        }
        final PersonRole role = new PersonRole(this.role);

        if (role.equals(PersonRole.CLIENT_ROLE)) {
            convertedPerson = new Client(name, phone, email, address, tags);
        } else {
            convertedPerson = new VetTechnician(name, phone, email, address, tags);
        }
        return convertedPerson;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedPerson otherPerson = (XmlAdaptedPerson) other;
        return Objects.equals(name, otherPerson.name)
                && Objects.equals(phone, otherPerson.phone)
                && Objects.equals(email, otherPerson.email)
                && Objects.equals(address, otherPerson.address)
                && tagged.equals(otherPerson.tagged);
    }
}
```
