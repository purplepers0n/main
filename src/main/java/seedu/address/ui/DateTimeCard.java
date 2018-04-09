package seedu.address.ui;

import java.time.LocalDateTime;
import java.time.Month;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;

//@@author purplepers0n
/**
 * Card displaying the current date and time
 */
public class DateTimeCard extends UiPart<Region> {

    private static final String FXML = "DateTimeCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label date;

    private int year;
    private String month;
    private int day;
    private int hour;
    private String minute;
    private boolean morning = true;

    public DateTimeCard() {
        super(FXML);
        setDateTime();
    }

    /**
     * Sets the current date and time
     */
    public void setDateTime() {

        final Timeline timeline = new Timeline(
                new KeyFrame(
                    Duration.millis(500),
                    event -> {
                        LocalDateTime now = LocalDateTime.now();

                        year = now.getYear();
                        Month currMonth = now.getMonth();
                        month = currMonth.toString();
                        day = now.getDayOfMonth();
                        if (now.getHour() >= 12) {
                            morning = false;
                        }
                        hour = now.getHour() % 12;
                        if (hour == 0) {
                            hour = 12;
                        }
                        int min = now.getMinute();

                        if (min < 10) {
                            minute = "0" + String.valueOf(min);
                        } else {
                            minute = String.valueOf(min);
                        }

                        if (morning) {
                            date.setText(hour + ":" + minute + " AM, " + day + " " + month + " " + year);
                        } else {
                            date.setText(hour + ":" + minute + " PM, " + day + " " + month + " " + year);
                        }
                    }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
