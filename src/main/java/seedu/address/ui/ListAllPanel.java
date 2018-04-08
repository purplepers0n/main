package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@@author purplepers0n
/**
 * Panel containing the list of client details.
 */
public class ListAllPanel extends UiPart<Region>{
    private static final String FXML = "ListAllPanel.fxml";

    public final Client client;
    public final ObservableList<Pet> pets;
    public final ObservableList<Appointment> appts;

    @FXML
    private HBox listAllPane;
    
    public ListAllPanel(Client client, ObservableList<Pet> pets, ObservableList<Appointment> appts) {
        super(FXML);
        
        this.client = client;
        this.pets = pets;
        this.appts = appts;
    }
}
