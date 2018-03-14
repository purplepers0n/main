package seedu.address.model.appointment.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate AppointmentUid objects
 */
public class DuplicateAppointmentUidException extends DuplicateDataException {
    public DuplicateAppointmentUidException() {
        super("Operation would result in duplicate appointmentUids");
    }
}
