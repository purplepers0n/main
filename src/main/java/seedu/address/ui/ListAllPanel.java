package seedu.address.ui;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

//@@author purplepers0n
/**
 * Panel containing the list of client details.
 */
public class ListAllPanel extends UiPart<Region>{
    private static final String FXML = "ListAllPanel.fxml";

    @FXML
    private HBox listAllPane;
    @FXML
    private StackPane clientPane;
    @FXML
    private ListView<PetDisplayCard> petListView;
    @FXML
    private ListView<ApptDisplayCard> apptDisplayCardListView;
    
    public ListAllPanel(Client client, ObservableList<Pet> pets, ObservableList<Appointment> appts) {
        super(FXML);
        
        setClient(client);
        setPets(pets);
    }
    
    private void setClient(Client client) {
        ClientDisplayCard clientDisplayCard = new ClientDisplayCard(client);
        clientPane.getChildren().add(clientDisplayCard.getRoot());
    }
    
    private void setPets(ObservableList<Pet> pets) {
        ObservableList<PetDisplayCard> mappedList = EasyBind.map(pets, (pet) -> new PetDisplayCard(pet,
                pets.indexOf(pet) + 1));
        petListView.setItems(mappedList);
        petListView.setCellFactory(listView -> new PetListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetCard}.
     */
    class PetListViewCell extends ListCell<PetDisplayCard> {

        @Override
        protected void updateItem(PetDisplayCard pet, boolean empty) {
            super.updateItem(pet, empty);

            if (empty || pet == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(pet.getRoot());
            }
        }
    }
}
