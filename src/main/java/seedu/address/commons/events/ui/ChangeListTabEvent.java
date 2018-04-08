package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author purplepers0n-reused
/**
 * Indicates a request to change the tab to show other list
 */
public class ChangeListTabEvent extends BaseEvent {

    public final int targetList;

    public ChangeListTabEvent(int targetList) {
        this.targetList = targetList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
