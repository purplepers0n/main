package seedu.address.logic;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     * @throws ParseException If an error occurs during parsing.
     */
    CommandResult execute(String commandText) throws CommandException, ParseException;

    /** Returns an unmodifiable view of the filtered list of persons */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the filtered list of clients */
    ObservableList<Client> getFilteredClientList();

    /** Returns an unmodifiable view of the filtered list of vet technician */
    ObservableList<VetTechnician> getFilteredVetTechnicianList();

    /** Returns an unmodifiable view of the filtered list of pets */
    ObservableList<Pet> getFilteredPetList();

    /** Returns an unmodifiable view of the filtered client pet association list */
    ObservableList<ClientOwnPet> getClientPetAssociationList();

    /** Returns an unmodifiable view of the filtered list of appointments */
    ObservableList<Appointment> getFilteredAppointmentList();

    /** Returns the list of input entered by the user, encapsulated in a {@code ListElementPointer} object */
    ListElementPointer getHistorySnapshot();

    /** Returns the sorted list of autocomplete commands with prefix {@code keyWord},
     *  encapsulated in a {@code List<String>} object
     */
    List<String> autoCompleteCommands(String keyWord);

    /**
     * Sets the index of the current list that is viewed
     */
    void setCurrentList(int currList);

    /** Get the index of the current list that is viewed */
    int getCurrentList();
}
