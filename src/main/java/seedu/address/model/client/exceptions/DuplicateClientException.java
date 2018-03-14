package seedu.address.model.client.exceptions;

import seedu.address.commons.exceptions.DuplicateDataException;

/**
 * Signals that the operation will result in duplicate Client objects.
 */
public class DuplicateClientException extends DuplicateDataException {
    public DuplicateClientException() {
        super("Operation would result in duplicate clients");
    }
}
