package seedu.address.model.appointment.exceptions;

/**
 *Signals that the operation will result the newly scheduled appointment falls
 * within the duration of the next appointment
 */
public class AppointmentCloseToNextException extends Exception {
    public AppointmentCloseToNextException(String message) {
        super(message);
    }
}
