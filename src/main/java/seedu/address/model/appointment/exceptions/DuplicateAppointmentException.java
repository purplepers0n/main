package seedu.address.model.appointment.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

//@@author Godxin-functional
/**
 * Signals that the operation will result in duplicate(clashed) appointment objects.
 */
public class DuplicateAppointmentException extends DuplicateDataException {
    public DuplicateAppointmentException() {
        super("Operation would result in appointments clashes");
    }
}
