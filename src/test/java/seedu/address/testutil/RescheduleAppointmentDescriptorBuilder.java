package seedu.address.testutil;

import seedu.address.logic.commands.RescheduleCommand.RescheduleAppointmentDescriptor;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.Date;
import seedu.address.model.appointment.Description;
import seedu.address.model.appointment.Duration;
import seedu.address.model.appointment.Time;

//@@author Godxin-test
/**
 * A utility class to help with building RescheduleAppointmentBuilder objects.
 */
public class RescheduleAppointmentDescriptorBuilder {

    private RescheduleAppointmentDescriptor descriptor;

    public RescheduleAppointmentDescriptorBuilder() {
        descriptor = new RescheduleAppointmentDescriptor();
    }

    public RescheduleAppointmentDescriptorBuilder(RescheduleAppointmentDescriptor descriptor) {
        this.descriptor = new RescheduleAppointmentDescriptor(descriptor);
    }

    /**
     * Returns an {@code RescheduleAppointmentDescriptorBuilder} with fields containing {@code appointments}'s details
     */
    public RescheduleAppointmentDescriptorBuilder(Appointment appointment) {
        descriptor = new RescheduleAppointmentDescriptor();
        descriptor.setDate(appointment.getDate());
        descriptor.setTime(appointment.getTime());
        descriptor.setDuration(appointment.getDuration());
        descriptor.setDescription(appointment.getDescription());
    }

    /**
     * Sets the {@code Date} of the {@code RescheduleAppointmentDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDate(String date) {
        descriptor.setDate(new Date(date));
        return this;
    }

    /**
     * Sets the {@code Time} of the {@code RescheduleAppointmentDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withTime(String time) {
        descriptor.setTime(new Time(time));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditPersonDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDuration(String duration) {
        descriptor.setDuration(new Duration(duration));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditPersonDescriptor} that we are building.
     */
    public RescheduleAppointmentDescriptorBuilder withDescription(String description) {
        descriptor.setDescription(new Description(description));
        return this;
    }

    public RescheduleAppointmentDescriptor build() {
        return descriptor;
    }
}
