package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.vettechnician.VetTechnician;

//@@author purplepers0n-reused
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
