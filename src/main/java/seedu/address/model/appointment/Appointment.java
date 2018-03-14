package seedu.address.model.appointment;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Represents an Appointment in the application.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Appointment {

    private final Date date;
    private final Time time;
    private final Client client; //dummy variable, class to be created
    private final Pet pet; //dummy variable, class to be created
    private final VetTechnician vetTech; //dummy variable, class to be created
    private final AppointmentUid uid;


    /**
     * Every field must be present and not null.
     */
    public Appointment(Date date, Time time, Client client, Pet pet, VetTechnician vetTech) {
        requireAllNonNull(date, time, client, pet, vetTech);
        this.date = date;
        this.time = time;
        this.client = client;
        this.pet = pet;
        this.vetTech = vetTech;
        uid = new AppointmentUid();
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Client getClient() {
        return client;
    }

    public Pet getPet() {
        return pet;
    }

    public VetTechnician getVetTech() {
        return vetTech;
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
                && otherAppointment.getClient().equals(this.getClient())
                && otherAppointment.getPet().equals(this.getPet())
                && otherAppointment.getVetTech().equals(this.getVetTech());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, time, client, pet, vetTech);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(" Date: ")
                .append(getDate())
                .append(" Time: ")
                .append(getTime())
                .append(" Client: ")
                .append(getClient())
                .append(" Pet: ")
                .append(getPet())
                .append(" Vet Tech: ")
                .append(getVetTech());
        return builder.toString();
    }

    /**
     * Returns the uid of the appointment as a {@code AppointmentUid}
     */
    public AppointmentUid getAppointmentUid() {
        return uid;
    }

}
