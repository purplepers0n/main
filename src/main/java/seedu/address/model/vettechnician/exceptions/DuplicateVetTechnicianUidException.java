package seedu.address.model.vettechnician.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate VetTechniciansUid objects
 */
public class DuplicateVetTechnicianUidException extends DuplicateDataException {
    public DuplicateVetTechnicianUidException() {
        super("Operation would result in duplicate vetTechnicianUids");
    }
}
