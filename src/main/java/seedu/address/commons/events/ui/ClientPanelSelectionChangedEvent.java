package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.ClientCard;

/**
 * Represents a selection change in the Client List Panel
 */
public class ClientPanelSelectionChangedEvent extends BaseEvent {


    private final ClientCard newSelection;

    public ClientPanelSelectionChangedEvent(ClientCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ClientCard getNewSelection() {
        return newSelection;
    }
}
