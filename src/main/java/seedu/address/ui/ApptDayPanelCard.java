package seedu.address.ui;

import java.text.DateFormatSymbols;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.appointment.Appointment;

/**
 * Panel containing the list of appointments in a day
 */
public class ApptDayPanelCard extends UiPart<Region> {

    public static final String FXML = "ApptDayPanelCard.fxml";
    private static final String[] COLORS = {"red", "yellow", "blue", "orange", "green",
        "pink", "navy", "teal", "purple", "peach", "lightblue", "darkpurple",
        "green2", "wine", "fuchsia", "sea"};

    @FXML
    private ListView<ApptCard> apptDayListView;
    @FXML
    private Label dateDisplay;

    private String year;
    private String month;
    private String day;

    public ApptDayPanelCard(ObservableList<Appointment> apptDay, String date, int startIndex) {
        super(FXML);
        setConnections(apptDay, date, startIndex);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Appointment> apptDayList, String date, int startIndex) {
        ObservableList<ApptCard> mappedList = EasyBind.map(
                apptDayList, (appt) -> new ApptCard(appt, startIndex + apptDayList.indexOf(appt) + 1));
        apptDayListView.setItems(mappedList);
        apptDayListView.setCellFactory(listView -> new ApptListViewCell());

        year = date.substring(0, 4);
        day = date.substring(8, 10);

        int mon = Integer.parseInt(date.substring(5, 7));

        month = new DateFormatSymbols().getMonths()[mon - 1];

        dateDisplay.setText("  " + day + " " + month + " " + year);
        setColorFor(date);
    }

    /**
     * set the color for {@code date}'s label
     */
    private void setColorFor(String date) {

        String color = COLORS[Math.abs(date.hashCode()) % COLORS.length];
        dateDisplay.setId(color);
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
