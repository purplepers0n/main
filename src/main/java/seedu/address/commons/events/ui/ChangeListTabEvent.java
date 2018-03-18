package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to change the tab to show other list
 */
public class ChangeListTabEvent extends BaseEvent {

    public final int targetIndex;

    public ChangeListTabEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
