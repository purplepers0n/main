package seedu.address.model.vettechnician.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate VetTechnician objects
 */
public class DuplicateVetTechnicianException extends DuplicateDataException {
    public DuplicateVetTechnicianException() {
        super("Operation would result in duplicate vet technicians");
    }
}
