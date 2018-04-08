package seedu.address.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.appointment.Appointment;

//@@author purplepers0n-reused
/**
 * An UI component that displays information of an {@code Appointment} for the {@code ListAllPanel} display.
 */
public class ApptDisplayCard extends UiPart<Region> {

    private static final String FXML = "ApptDisplayCard.fxml";

    public final Appointment appointment;
    private String startTime;
    private String endTime;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label time;
    @FXML
    private Label petName;
    @FXML
    private Label vetTechName;
    @FXML
    private Label desc;

    public ApptDisplayCard(Appointment appointment, int startIndex) {
        super(FXML);

        this.appointment = appointment;
        id.setText(startIndex + ". ");
        startTime = appointment.getTime().toString();
        getTimeFrame(startTime, appointment.getDuration().toString());
        time.setText(startTime + " - " + endTime);
        petName.setText("Pet: " + appointment.getClientOwnPet().getPet().getPetName().fullPetName);
        if (appointment.getVetTechnician() == null) {
            vetTechName.setText("V.Tech: -");
        } else {
            vetTechName.setText("V.Tech: " + appointment.getVetTechnician().getName().fullName);
        }
        desc.setText("Description:" + appointment.getDescription().description);
        desc.setWrapText(true);
    }

    private void getTimeFrame(String time, String duration) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            Date date = df.parse(time);
            cal.setTime(date);
            cal.add(Calendar.MINUTE, Integer.parseInt(duration));
            endTime = df.format(cal.getTime());
        } catch (Exception e) {
            System.out.println("time cannot be parsed");
        }

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ApptCard)) {
            return false;
        }

        // state check
        ApptDisplayCard card = (ApptDisplayCard) other;
        return startTime.equals(card.startTime)
                && appointment.equals(card.appointment);
    }
}
