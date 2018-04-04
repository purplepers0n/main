package seedu.address.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.JumpToListRequestEvent;
import seedu.address.model.appointment.Appointment;

/**
 * Panel containing the list of all appointments
 */
public class ApptListPanel extends UiPart<Region> {

    private static final String FXML = "ApptListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ApptListPanel.class);

    @FXML
    private ListView<ApptDayPanelCard> apptListView;

    public ApptListPanel(ObservableList<Appointment> apptList) {
        super(FXML);
        setConnections(apptList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Appointment> apptList) {
        ObservableList<ApptDayPanelCard> mappedList = FXCollections.observableArrayList();
        Appointment lastAppt = null;
        int startIndex = 0;
        int endIndex;

        for (Appointment currAppt : apptList) {
            if (lastAppt != null && !currAppt.getDate().equals(lastAppt.getDate())) {
                endIndex = apptList.indexOf(currAppt);

                ObservableList<Appointment> apptDayList =
                        FXCollections.observableList(apptList.subList(startIndex, endIndex));
                mappedList.add(new ApptDayPanelCard(apptDayList, lastAppt.getDate().toString(), startIndex));

                startIndex = endIndex;
                lastAppt = currAppt;
            } else {
                lastAppt = currAppt;
            }
        }
        endIndex = apptList.size();
        ObservableList<Appointment> apptDayList = FXCollections.observableList(apptList.subList(startIndex, endIndex));
        mappedList.add(new ApptDayPanelCard(apptDayList, apptList.get(endIndex - 1).getDate().toString(), startIndex));

        apptListView.setItems(mappedList);
        apptListView.setCellFactory(listView -> new ApptListViewCell());
    }

    /**
     * Scrolls to the {@code ClientCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            apptListView.scrollTo(index);
            apptListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        if (event.targetList == 0) {
            scrollTo(event.targetIndex);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptDayPanelCard}.
     */
    class ApptListViewCell extends ListCell<ApptDayPanelCard> {

        @Override
        protected void updateItem(ApptDayPanelCard appt, boolean empty) {
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
