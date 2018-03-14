package seedu.address.model.client.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate ClientUid objects
 */
public class DuplicateClientUidException extends DuplicateDataException {
    public DuplicateClientUidException() {
        super("Operation would result in duplicate clientUids");
    }
}
