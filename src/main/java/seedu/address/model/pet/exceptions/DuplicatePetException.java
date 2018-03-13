package seedu.address.model.pet.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Pet objects
 */
public class DuplicatePetException extends DuplicateDataException {
    public DuplicatePetException() {
        super("Operation would result in duplicate pets");
    }
}
