package seedu.address.model.appointment;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents an Appointment in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Appointment {

    private final Date date;
    private final Time time;
    private final Duration duration;


    /**
     * Every field must be present and not null.
     */
    public Appointment(Date date, Time time, Duration duration) {
        requireAllNonNull(date, time);
        this.date = date;
        this.time = time;
        this.duration = duration;

    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return otherAppointment.getDate().equals(this.getDate())
                && otherAppointment.getTime().equals(this.getTime());

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, time, duration);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(" Date: ")
                .append(getDate())
                .append(" Time: ")
                .append(getTime())
                .append(" Duration: ")
                .append(getDuration());


        return builder.toString();
    }

}
