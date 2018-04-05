package seedu.address.model.appointment.exceptions;

//@@author Godxin-functional
/**
 *Signals that the operation will result the newly scheduled appointment falls
 * within the duration of previous appointment
 */
public class AppointmentCloseToPreviousException extends Exception {
    public AppointmentCloseToPreviousException(String message) {
        super(message);
    }
}
