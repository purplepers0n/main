package seedu.address.model.pet.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate PetUid objects
 */
public class DuplicatePetUidException extends DuplicateDataException {
    public DuplicatePetUidException() {
        super("Operation would result in duplicate pets");
    }
}
