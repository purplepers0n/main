package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.commons.events.ui.PetPanelSelectionChangedEvent;
import seedu.address.model.pet.Pet;

/**
 * Panel containing the list of pets.
 */
public class PetListPanel extends UiPart<Region> {
    private static final String FXML = "PetListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(PetListPanel.class);

    @FXML
    private ListView<PetCard> petListView;

    public PetListPanel(ObservableList<Pet> petList) {
        super(FXML);
        setConnections(petList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Pet> petList) {
        ObservableList<PetCard> mappedList = EasyBind.map(
                petList, (pet) -> new PetCard(pet, petList.indexOf(pet) + 1));
        petListView.setItems(mappedList);
        petListView.setCellFactory(listView -> new PetListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        petListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in pet list panel changed to : '" + newValue + "'");
                        raise(new PetPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PetCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            petListView.scrollTo(index);
            petListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code PetCard}.
     */
    class PetListViewCell extends ListCell<PetCard> {

        @Override
        protected void updateItem(PetCard pet, boolean empty) {
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