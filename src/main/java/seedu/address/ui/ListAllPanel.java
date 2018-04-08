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

//@@author purplepers0n-reused
/**
 * Panel containing the list of client details.
 */
public class ListAllPanel extends UiPart<Region> {
    private static final String FXML = "ListAllPanel.fxml";

    @FXML
    private HBox listAllPane;
    @FXML
    private StackPane clientPane;
    @FXML
    private ListView<PetDisplayCard> petListView;
    @FXML
    private ListView<ApptDisplayCard> apptListView;

    public ListAllPanel(Client client, ObservableList<Pet> pets, ObservableList<Appointment> appts) {
        super(FXML);

        setClient(client);
        setPets(pets);
        setAppts(appts);
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
        petListView.setPrefHeight(pets.size() * 105);
    }

    private void setAppts(ObservableList<Appointment> appts) {
        ObservableList<ApptDisplayCard> mappedList = EasyBind.map(appts, (appt) -> new ApptDisplayCard(appt,
                appts.indexOf(appt) + 1));
        apptListView.setItems(mappedList);
        apptListView.setCellFactory(listView -> new ApptListViewCell());
        apptListView.setPrefHeight(appts.size() * 105);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetDisplayCard}.
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

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptDisplayCard}.
     */
    class ApptListViewCell extends ListCell<ApptDisplayCard> {

        @Override
        protected void updateItem(ApptDisplayCard appt, boolean empty) {
            super.updateItem(appt, empty);

            if (empty || appt == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(appt.getRoot());
            }
        }
    }
}
