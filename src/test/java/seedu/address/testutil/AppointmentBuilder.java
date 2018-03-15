package seedu.address.testutil;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Time;

/**
 * A utility class to help with building Appointment objects.
 */
public class AppointmentBuilder {
    public static final String DEFAULT_DATE = "11/11/2018";
    public static final String DEFAULT_TIME = "1111";

    private Date date;
    private Time time;

    public AppointmentBuilder() {
        date = new Date(DEFAULT_DATE);
        time = new Time(DEFAULT_TIME);
    }

    /**
     * Initializes the AppointmentBuilder with the data of {@code appointmentToCopy}.
     */
    public AppointmentBuilder(Appointment appointmentToCopy) {
        date = appointmentToCopy.getDate();
        time = appointmentToCopy.getTime();
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

    public Appointment build() {
        return new Appointment(date, time);
    }

}
