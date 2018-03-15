package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.PetCard;

/**
 * Represents a selection change in the Pet List Panel
 */
public class PetPanelSelectionChangedEvent extends BaseEvent {


    private final PetCard newSelection;

    public PetPanelSelectionChangedEvent(PetCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public PetCard getNewSelection() {
        return newSelection;
    }
}
