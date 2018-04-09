package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new listall display is available.
 */
public class NewListAllDisplayAvailableEvent extends BaseEvent {
    public final String message;

    public NewListAllDisplayAvailableEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
