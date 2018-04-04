# jonathanwj
###### \java\seedu\address\logic\autocomplete\AutoComplete.java
``` java
/**
 * the main AutoCompleteManager of the application
 */
public class AutoComplete {

    private static final String LIST_CLIENT_PREFIX = " client";
    private static final String LIST_VETTECH_PREFIX = " vettech";
    private static final String LIST_PET_PREFIX = " pet";
    private Trie commandTrie;
    private CommandParameterSyntaxHandler commandParameterSyntaxHandler;

    public AutoComplete() {
        commandTrie = new Trie();
        commandParameterSyntaxHandler = new CommandParameterSyntaxHandler();
        initCommandKeyWords();
    }

    /**
     * Initialises command keywords in commandTrie
     */
    private void initCommandKeyWords() {
        commandTrie.insertWord(AddCommand.COMMAND_WORD);
        commandTrie.insertWord(AddPetCommand.COMMAND_WORD);
        commandTrie.insertWord(ClearCommand.COMMAND_WORD);
        commandTrie.insertWord(DeleteCommand.COMMAND_WORD);
        commandTrie.insertWord(DeletePetCommand.COMMAND_WORD);
        commandTrie.insertWord(EditCommand.COMMAND_WORD);
        commandTrie.insertWord(ExitCommand.COMMAND_WORD);
        commandTrie.insertWord(FindCommand.COMMAND_WORD);
        commandTrie.insertWord(HelpCommand.COMMAND_WORD);
        commandTrie.insertWord(HistoryCommand.COMMAND_WORD);
        commandTrie.insertWord(RedoCommand.COMMAND_WORD);
        commandTrie.insertWord(ScheduleCommand.COMMAND_WORD);
        commandTrie.insertWord(SelectCommand.COMMAND_WORD);
        commandTrie.insertWord(UndoCommand.COMMAND_WORD);

        commandTrie.insertWord(AddAppointmentToPetCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveAppointmentFromPetCommand.COMMAND_WORD);
        commandTrie.insertWord(AddVetTechToAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(RemoveVetTechFromAppointmentCommand.COMMAND_WORD);

        commandTrie.insertWord(SortClientCommand.COMMAND_WORD);
        commandTrie.insertWord(SortAppointmentCommand.COMMAND_WORD);
        commandTrie.insertWord(SortPetCommand.COMMAND_WORD);

        commandTrie.insertWord(ListCommand.COMMAND_WORD);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_CLIENT_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_VETTECH_PREFIX);
        commandTrie.insertWord(ListCommand.COMMAND_WORD + LIST_PET_PREFIX);


    }

    /**
     * Returns a sorted list of auto completed commands with prefix {@code keyWord}
     *
     */
    public List<String> autoCompleteCommands(String keyWord) {
        requireNonNull(keyWord);
        return commandTrie.autoComplete(keyWord).stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }

    /**
     * Returns the concatenation String of the next missing prefix parameter with the input string.
     */
    public String autoCompleteNextMissingParameter(String input) {
        requireNonNull(input);
        if (input.isEmpty()) {
            return input;
        }
        String command = input.split(" ")[0];

        ArrayList<Prefix> missingPrefixes = commandParameterSyntaxHandler.getMissingPrefixes(command, input);
        String completedText = input;

        if (!missingPrefixes.isEmpty()) {
            completedText = completedText + missingPrefixes.get(0);
        }

        return completedText;
    }
}
```
###### \java\seedu\address\logic\autocomplete\CommandParameterSyntaxHandler.java
``` java
/**
 * Contains Command syntax definitions for multiple commands
 */
public class CommandParameterSyntaxHandler {

    public static final ArrayList<Prefix> ADD_COMMAND_PREFIXES = getListOfPrefix(PREFIX_PERSON_ROLE,
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

    public static final ArrayList<Prefix> ADD_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_CLIENT_INDEX,
            PREFIX_PET_NAME, PREFIX_PET_AGE, PREFIX_PET_GENDER, PREFIX_TAG);

    public static final ArrayList<Prefix> EDIT_COMMAND_PREFIXES = getListOfPrefix(PREFIX_PERSON_ROLE,
            PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

    public static final ArrayList<Prefix> ADD_APPT_TO_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_APPOINTMENT_INDEX,
            PREFIX_PET_INDEX);

    public static final ArrayList<Prefix>
            REMOVE_APPT_FROM_PET_COMMAND_PREFIXES = getListOfPrefix(PREFIX_APPOINTMENT_INDEX);

    public static final ArrayList<Prefix> SCHEDULE_COMMAND_PREFIXES = getListOfPrefix(PREFIX_DATE, PREFIX_TIME,
            PREFIX_DURATION, PREFIX_DESCRIPTION);

    public static final ArrayList<Prefix> ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES = getListOfPrefix(PREFIX_VETTECH_INDEX,
            PREFIX_APPOINTMENT_INDEX);


    /**
     * Returns ArrayList of prefixes from given prefixes
     */
    private static ArrayList<Prefix> getListOfPrefix(Prefix... prefixes) {
        ArrayList<Prefix> list = new ArrayList<>();
        for (int i = 0; i < prefixes.length; i++) {
            list.add(prefixes[i]);
        }
        return list;
    }

    /**
     * Returns ArrayList of missing prefixes based on a String command and current user text input
     */
    public ArrayList<Prefix> getMissingPrefixes(String command, String input) {
        ArrayList<Prefix> missingPrefixes = new ArrayList<>();

        switch (command) {

        case AddCommand.COMMAND_WORD:
            ADD_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case EditCommand.COMMAND_WORD:
            EDIT_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case ScheduleCommand.COMMAND_WORD:
            SCHEDULE_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case AddPetCommand.COMMAND_WORD:
            ADD_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;


        case AddAppointmentToPetCommand.COMMAND_WORD:
            ADD_APPT_TO_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case RemoveAppointmentFromPetCommand.COMMAND_WORD:
            REMOVE_APPT_FROM_PET_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        case AddVetTechToAppointmentCommand.COMMAND_WORD:
            ADD_VET_TECH_TO_APPT_COMMAND_PREFIXES.forEach(prefix -> {
                if (!input.contains(prefix.getPrefix())) {
                    missingPrefixes.add(prefix);
                }
            });
            break;

        default:
            break;
        }

        return missingPrefixes;
    }
}
```
###### \java\seedu\address\logic\autocomplete\Trie.java
``` java
/**
 * Trie data structure for word auto-complete
 */
public class Trie {

    private Node root;
    private int size = 0;

    /**
     * Represents node a Trie
     */
    private class Node {
        private HashMap<Character, Node> children = new HashMap<>();
        private boolean isCompleteWord = false;
    }

    /**
     * Creates a Trie
     */
    public Trie() {
        root = new Node();
    }

    /**
     * Insert a word into Trie
     */
    public void insertWord(String word) {
        requireNonNull(word);
        insert(root, word);
    }

    /**
     * Recursive insert to insert part of key into Trie
     */
    private void insert(Node currNode, String key) {
        if (!key.isEmpty()) {
            if (!currNode.children.containsKey(key.charAt(0))) {
                currNode.children.put(key.charAt(0), new Node());
            }
            insert(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            if (currNode.isCompleteWord == false) {
                size++;
            }
            currNode.isCompleteWord = true;
        }
    }

    /**
     * Auto-complete strings
     * <p>
     * Returns an {@code ArrayList<String>} of auto-completed words
     */
    public List<String> autoComplete(String prefix) {
        List<String> result = new ArrayList<>();
        if (search(root, prefix) == null) {
            return result;
        }
        for (String s : getAllPostFix(search(root, prefix))) {
            result.add(prefix + s);
        }
        return result;
    }

    /**
     * Recursive search for end node
     */
    private Node search(Node currNode, String key) {
        if (!key.isEmpty() && currNode != null) {
            return search(currNode.children.get(key.charAt(0)), key.substring(1));
        } else {
            return currNode;
        }
    }


    /**
     * Returns arraylist of all postfix from node
     */
    private List<String> getAllPostFix(Node node) {
        ArrayList<String> listOfPostFix = new ArrayList<>();
        return getAllPostFix(node, "", null, listOfPostFix);
    }

    /**
     * Recursive method to get all postfix string
     */
    private List<String> getAllPostFix(Node node, String s, Character next, List<String> listOfPostFix) {
        if (next != null) {
            s += next;
        }
        for (Map.Entry<Character, Node> entry : node.children.entrySet()) {
            listOfPostFix = getAllPostFix(entry.getValue(), s, entry.getKey(), listOfPostFix);
        }
        if (node.isCompleteWord) {
            listOfPostFix.add(s);
        }
        return listOfPostFix;
    }

    /**
     * @return size of Trie
     */
    public int size() {
        return size;
    }

}

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Associates pet to client
     *
     * @throws ClientAlreadyOwnsPetException
     * @throws PetAlreadyHasOwnerException
     */
    public void addPetToClient(Pet pet, Client client)
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        ClientOwnPet toAdd = new ClientOwnPet(client, pet);

        if (!clientPetAssociations.contains(toAdd)) {
            if (hasOwner(pet)) {
                throw new PetAlreadyHasOwnerException();
            }
            clientPetAssociations.add(toAdd);
        } else {
            throw new ClientAlreadyOwnsPetException();
        }

    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Returns true if specified pet has an owner
     */
    private boolean hasOwner(Pet pet) {
        for (ClientOwnPet a : clientPetAssociations) {
            if (a.getPet().equals(pet)) {
                return true;
            }
        }
        return false;
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes association from pet and client
     *
     * @throws ClientPetAssociationNotFoundException
     */
    public void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFoundException {
        ClientOwnPet toRemove = new ClientOwnPet(client, pet);
        if (clientPetAssociations.contains(toRemove)) {
            clientPetAssociations.remove(toRemove);
        } else {
            throw new ClientPetAssociationNotFoundException();
        }
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Adds vet technician to appointment
     */
    public void addVetTechToAppointment(VetTechnician technician, Appointment appointment)
            throws AppointmentNotFoundException, AppointmentAlreadyHasVetTechnicianException,
            DuplicateAppointmentException {
        if (!appointments.contains(appointment)) {
            throw new AppointmentNotFoundException();
        }
        if (appointment.getOptionalVetTechnician().isPresent()) {
            throw new AppointmentAlreadyHasVetTechnicianException();
        }
        Appointment appointmentCopy = new Appointment(appointment);
        appointmentCopy.setVetTech(technician);
        appointments.setAppointment(appointment, appointmentCopy);
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes a vet technician from the given appointment
     */
    public void removeVetFromAppointment(Appointment apptToRemoveVetFrom)
            throws AppointmentNotFoundException, DuplicateAppointmentException {
        if (!appointments.contains(apptToRemoveVetFrom)) {
            throw new AppointmentNotFoundException();
        }
        Appointment appointmentCopy = new Appointment(apptToRemoveVetFrom);
        appointmentCopy.removeVetTech();
        appointments.setAppointment(apptToRemoveVetFrom, appointmentCopy);
    }

    //// util methods
```
###### \java\seedu\address\model\AddressBook.java
``` java
    @Override
    public ObservableList<Client> getClientList() {
        ObservableList<Client> clientList = EasyBind.map(getPersonList(), (person) -> {
            if (person.isClient()) {
                return (Client) person;
            } else {
                return null;
            }
        });
        clientList = FXCollections.unmodifiableObservableList(clientList).filtered(Objects::nonNull);
        return clientList;
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    @Override
    public ObservableList<VetTechnician> getVetTechnicianList() {
        ObservableList<VetTechnician> technicianList = EasyBind.map(getPersonList(), (person) -> {
            if (!person.isClient()) {
                return (VetTechnician) person;
            } else {
                return null;
            }
        });
        technicianList = FXCollections.unmodifiableObservableList(technicianList).filtered(Objects::nonNull);
        return technicianList;
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void addPetToClient(Pet pet, Client client)
            throws ClientAlreadyOwnsPetException, PetAlreadyHasOwnerException {
        requireAllNonNull(pet, client);
        addressBook.addPetToClient(pet, client);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void removePetFromClient(Pet pet, Client client) throws ClientPetAssociationNotFoundException {
        requireAllNonNull(pet, client);
        addressBook.removePetFromClient(pet, client);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void addVetTechToAppointment(VetTechnician technician, Appointment appointment)
            throws AppointmentAlreadyHasVetTechnicianException,
            DuplicateAppointmentException, AppointmentNotFoundException {
        requireAllNonNull(technician, appointment);
        addressBook.addVetTechToAppointment(technician, appointment);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void removeVetTechFromAppointent(Appointment apptToRemoveVetTechFrom)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(apptToRemoveVetTechFrom);
        addressBook.removeVetFromAppointment(apptToRemoveVetTechFrom);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================
```
###### \java\seedu\address\model\person\PersonRole.java
``` java
/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPersonRole(Role)}
 */

public class PersonRole {

    public static final String CLIENT_STRING = "client";
    public static final String TECHNICIAN_STRING = "technician";

    /**
     * Person roles that can be used.
     */
    private enum Role {
        CLIENT,
        TECHNICIAN
    }

    public static final PersonRole TECHNICIAN_ROLE = new PersonRole(Role.TECHNICIAN);
    public static final PersonRole CLIENT_ROLE = new PersonRole(Role.CLIENT);

    public static final String MESSAGE_ROLE_CONSTRAINTS =
            "Person role can take only 'client' and 'technician' values, and it should not be blank";

    private final Role role;

    /**
     * Constructs a PersonRole
     *
     * @param role a valid role
     */
    public PersonRole(Role role) {
        requireNonNull(role);
        checkArgument(isValidPersonRole(role), MESSAGE_ROLE_CONSTRAINTS);

        if (role.equals(Role.CLIENT)) {
            this.role = Role.CLIENT;
        } else {
            this.role = Role.TECHNICIAN;
        }
    }

    /**
     * Constructs a PersonRole
     *
     * @param role a valid string representation of {@code PersonRole}
     */
    public PersonRole(String role) {
        requireNonNull(role);
        checkArgument(isValidPersonRole(role), MESSAGE_ROLE_CONSTRAINTS);

        if (role.equalsIgnoreCase("client")) {
            this.role = Role.CLIENT;
        } else {
            this.role = Role.TECHNICIAN;
        }
    }

    /**
     * Returns true if a given Role is a valid person role.
     */
    public static boolean isValidPersonRole(Role test) {
        if (test == null) {
            return false;
        }
        return (test.equals(Role.CLIENT)
                || test.equals(Role.TECHNICIAN));
    }

    /**
     * Returns true if a given String represents a valid person role.
     */
    public static boolean isValidPersonRole(String test) {
        if (test == null) {
            return false;
        }
        return (test.equalsIgnoreCase(CLIENT_STRING)
                || test.equalsIgnoreCase(TECHNICIAN_STRING));
    }

    /**
     * Returns the string representation of PersonRole.
     */
    public String toString() {
        requireNonNull(role);
        if (role.equals(Role.CLIENT)) {
            return CLIENT_STRING;
        } else {
            return TECHNICIAN_STRING;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonRole // instanceof handles nulls
                && this.role.equals(((PersonRole) other).role)); // state check
    }
}
```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Shows auto completed text on the UI
     */
    private void autoCompleteInput() {
        if (commandTextField.getText().isEmpty()) {
            return;
        }

        String currUserInput = commandTextField.getText();
        if (currUserInput.endsWith(" ")) {
            autoCompleteCommandParameters();
            return;
        }

        List<String> listOfAutoComplete = logic.autoCompleteCommands(currUserInput);
        if (listOfAutoComplete.isEmpty()) {
            return;
        }

        if (listOfAutoComplete.size() == 1) {
            commandTextField.setText(listOfAutoComplete.get(0));
            commandTextField.positionCaret(commandTextField.getText().length());
        }
        logger.info("Auto Complete Suggestions '"
                + currUserInput + "' : " + listToString(listOfAutoComplete));

        if (isTabDoubleTap()) {
            if (listOfAutoComplete.size() == 1) {
                raise(new NewResultAvailableEvent(MESSAGE_NO_MORE_AVAILABLE_COMMANDS));
            } else {
                raise(new NewResultAvailableEvent(MESSAGE_AVAILABLE_AUTOCOMPLETE + listToString(listOfAutoComplete)));
            }
        }

    }

```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Shows auto completed text with next prefix parameter on UI
     */
    private void autoCompleteCommandParameters() {
        String autoCompletedText = logic.autoCompleteNextParameter(commandTextField.getText());
        commandTextField.setText(autoCompletedText);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Returns the {@code String} representative of given the list of Strings.
     */
    private String listToString(List<String> listOfAutoComplete) {
        String toString = listOfAutoComplete.toString();
        toString = toString.substring(1, toString.length() - 1).trim();
        return toString;
    }

```
###### \java\seedu\address\ui\CommandBox.java
``` java
    /**
     * Returns true if TAB is pressed in quick succession
     */
    private boolean isTabDoubleTap() {
        if (System.currentTimeMillis() - previousTabPressTime < DOUBLE_PRESS_DELAY) {
            return true;
        } else {
            previousTabPressTime = System.currentTimeMillis();
        }
        return false;
    }

```
