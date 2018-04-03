package seedu.address.ui;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.appointment.Appointment;

/**
 * Panel containing the list of appointments in a day
 */
public class ApptDayPanelCard extends UiPart<Region> {

    public static final String FXML = "ApptDayPanelCard.fxml";

    @FXML
    private ListView<ApptCard> apptDayListView;

    public ApptDayPanelCard(ObservableList<Appointment> apptDay) {
        super(FXML);
        setConnections(apptDay);
    }

    private void setConnections(ObservableList<Appointment> apptDayList) {
        ObservableList<ApptCard> mappedList = EasyBind.map(
                apptDayList, (appt) -> new ApptCard(appt));
        apptDayListView.setItems(mappedList);
        apptDayListView.setCellFactory(listView -> new ApptListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ApptCard}.
     */
    class ApptListViewCell extends ListCell<ApptCard> {

        @Override
        protected void updateItem(ApptCard appt, boolean empty) {
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
