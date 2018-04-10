package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates that a new appointment is available.
 */
public class NewApptAvailableEvent extends BaseEvent {

    public final String message;

    public NewApptAvailableEvent(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
