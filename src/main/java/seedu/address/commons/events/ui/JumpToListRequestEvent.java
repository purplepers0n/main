package seedu.address.commons.events.ui;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of persons
 */
public class JumpToListRequestEvent extends BaseEvent {

    public final int targetIndex;
    public final int targetList;

    public JumpToListRequestEvent(Index targetIndex, int targetList) {
        this.targetIndex = targetIndex.getZeroBased();
        this.targetList = targetList;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
