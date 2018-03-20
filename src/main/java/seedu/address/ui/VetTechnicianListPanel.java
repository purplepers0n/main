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
import seedu.address.commons.events.ui.VetTechnicianPanelSelectionChangedEvent;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Panel containing the list of vetTechnicians.
 */
public class VetTechnicianListPanel extends UiPart<Region> {
    private static final String FXML = "VetTechnicianListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(VetTechnicianListPanel.class);

    @FXML
    private ListView<VetTechnicianCard> vetTechnicianListView;

    public VetTechnicianListPanel(ObservableList<VetTechnician> vetTechnicianList) {
        super(FXML);
        setConnections(vetTechnicianList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<VetTechnician> vetTechnicianList) {
        ObservableList<VetTechnicianCard> mappedList = EasyBind.map(
                vetTechnicianList, (vetTechnician) ->
                        new VetTechnicianCard(vetTechnician, vetTechnicianList.indexOf(vetTechnician) + 1));
        vetTechnicianListView.setItems(mappedList);
        vetTechnicianListView.setCellFactory(listView -> new VetTechnicianListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        vetTechnicianListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in vetTechnician list panel changed to : '" + newValue + "'");
                        raise(new VetTechnicianPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code VetTechnicianCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            vetTechnicianListView.scrollTo(index);
            vetTechnicianListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code VetTechnicianCard}.
     */
    class VetTechnicianListViewCell extends ListCell<VetTechnicianCard> {

        @Override
        protected void updateItem(VetTechnicianCard vetTechnician, boolean empty) {
            super.updateItem(vetTechnician, empty);

            if (empty || vetTechnician == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(vetTechnician.getRoot());
            }
        }
    }

}
