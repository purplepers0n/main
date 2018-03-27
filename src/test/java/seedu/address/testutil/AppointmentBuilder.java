package seedu.address.testutil;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;

/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {
    public static final String DEFAULT_DATE = "2018-01-01";
    public static final String DEFAULT_TIME = "00:00";
    public static final String DEFAULT_DURATION = "30";
    public static final String DEFAULT_DESCRIPTION = "Sterilize garfield";

    private Date date;
    private Time time;
    private Duration duration;
    private Description description;

    public AppointmentBuilder() {
        date = new Date(DEFAULT_DATE);
        time = new Time(DEFAULT_TIME);
        duration = new Duration(DEFAULT_DURATION);
        description = new Description(DEFAULT_DESCRIPTION);
    }

    /**
     * Sets the {@code Date} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withTime(String time) {
        this.time = new Time(time);
        return this;
    }

    /**
     * Sets the {@code Duration} of the {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDuration(String duration) {
        this.duration = new Duration(duration);
        return this;
    }

    /**
     * Sets the {@code Description} of the code {@code Appointment} that we are building.
     */
    public AppointmentBuilder withDescription(String description) {
        this.description = new Description(description);
        return this;
    }

    public Appointment build() {
        return new Appointment(date, time, duration, description);
    }

}
