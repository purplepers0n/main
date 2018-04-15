package seedu.address.model.appointment;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.vettechnician.VetTechnician;

//@@author Godxin-functional
/**
 * Represents an Appointment in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Appointment {

    private static final int CONVERSION_TIME = 60;
    private final Date date;
    private final Time time;
    private final Duration duration;
    private final Description description;
    private ClientOwnPet clientOwnPet;
    private Optional<VetTechnician> vetTech;

    /**
     * Every field must be present and not null.
     */
    public Appointment(Date date, Time time, Duration duration, Description description) {
        requireAllNonNull(date, time, duration, description);
        this.date = date;
        this.time = time;
        this.duration = duration;
        this.description = description;
        clientOwnPet = null;
        vetTech = Optional.empty();
    }

    public Appointment(Appointment toCopy) {
        date = toCopy.getDate();
        time = toCopy.getTime();
        duration = toCopy.getDuration();
        description = toCopy.getDescription();
        clientOwnPet = toCopy.getClientOwnPet();
        vetTech = toCopy.getOptionalVetTechnician();
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

    public Description getDescription() {
        return description;
    }

    public ClientOwnPet getClientOwnPet() {
        return clientOwnPet;
    }

    public Optional<VetTechnician> getOptionalVetTechnician() {
        return vetTech;
    }
    public VetTechnician getVetTechnician() {
        return vetTech.orElse(null);
    }


    public void setClientOwnPet(ClientOwnPet clientOwnPet) {
        this.clientOwnPet = clientOwnPet;
    }

    public void setClientOwnPetToNull() {
        clientOwnPet = null;
    }

    public void setVetTech(VetTechnician vetTech) {
        this.vetTech = Optional.of(vetTech);
    }

    public void setOptionalVetTech (Optional<VetTechnician> vetTech) {
        this.vetTech = vetTech;
    }

    public void removeVetTech() {
        vetTech = Optional.empty();
    }

    /**
     * Returns the interval in minutes between two appointments
     */
    public int calDurationDifferencePositive(Appointment previous) {
        Time previousTime = previous.getTime();

        return hourDifference(this.time.getHour(), previousTime.getHour())
                + minDifference(this.time.getMinute(), previousTime.getMinute());
    }

    /**
     * Returns the interval in minutes between two appointments
     */
    public int calDurationDifferenceNegative(Appointment next) {
        Time nextTime = next.getTime();

        return hourDifference(nextTime.getHour(), this.time.getHour())
                + minDifference(nextTime.getMinute(), this.time.getMinute());
    }

    /**
     * Returns the difference in Hour of time between two appointments
     */
    public int hourDifference(int hourFirst, int hourSecond) {
        return Math.abs((hourFirst - hourSecond) * CONVERSION_TIME);
    }

    /**
     * Returns the difference in Minute of time between two appointments
     */
    public int minDifference(int minFirst, int minSecond) {
        return minFirst - minSecond;
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
                && otherAppointment.getTime().equals(this.getTime())
                && otherAppointment.getOptionalVetTechnician().equals(this.getOptionalVetTechnician());

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, time, duration, description);

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(" Date: ")
                .append(getDate())
                .append(" Time: ")
                .append(getTime())
                .append(" Duration: ")
                .append(getDuration())
                .append(" Description: ")
                .append(getDescription());

        return builder.toString();
    }

    /**
     * Comparator that compares the date and then time of the appointment
     */
    public int compareTo(Appointment other) {
        if (this.getDate().compareToDate(other.getDate()) == 0) {
            return (this.getTime().compareToTime(other.getTime()));
        } else {
            return this.getDate().compareToDate(other.getDate());
        }
    }

}
