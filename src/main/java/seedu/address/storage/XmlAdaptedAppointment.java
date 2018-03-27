package seedu.address.storage;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;

/**
 * JAXB-friendly version of the Appointment.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment's %s field is missing!";

    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String time;
    @XmlElement(required = true)
    private String duration;
    @XmlElement(required = true)
    private String description;

    /**
     * Constructs an XmlAdaptedAppointment.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given person details.
     */
    public XmlAdaptedAppointment(String date, String time, String duration, String description) {
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.description = description;
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedAppointment
     */
    public XmlAdaptedAppointment(Appointment source) {
        date = source.getDate().toString();
        time = source.getTime().toString();
        duration = source.getDuration().toString();
        description = source.getDescription().toString();
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted appointment
     */
    public Appointment toModelType() throws IllegalValueException {

        Appointment convertedAppointment;

        if (this.date == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Date.class.getSimpleName()));
        }
        if (!Date.isValidDate(this.date)) {
            throw new IllegalValueException(Date.MESSAGE_DATE_CONSTRAINTS);
        }
        final Date date = new Date(this.date);

        if (this.time == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Time.class.getSimpleName()));
        }
        if (!Time.isValidTime(this.time)) {
            throw new IllegalValueException(Time.MESSAGE_TIME_CONSTRAINTS);
        }
        final Time time = new Time(this.time);

        if (this.duration == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Duration.class.getSimpleName()));
        }
        if (!Duration.isValidDuration(this.duration)) {
            throw new IllegalValueException(Duration.MESSAGE_DURATION_CONSTRAINTS);
        }
        final Duration duration = new Duration(this.duration);

        final Description description = new Description(this.description);

        convertedAppointment = new Appointment(date, time, duration, description);

        return convertedAppointment;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPerson)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(date, otherAppointment.date)
                && Objects.equals(time, otherAppointment.time)
                && Objects.equals(duration, otherAppointment.duration)
                && Objects.equals(description, otherAppointment.description);

    }
}
