package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.VetTechnicianCard;

/**
 * Represents a selection change in the VetTechnician List Panel
 */
public class VetTechnicianPanelSelectionChangedEvent extends BaseEvent {


    private final VetTechnicianCard newSelection;

    public VetTechnicianPanelSelectionChangedEvent(VetTechnicianCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public VetTechnicianCard getNewSelection() {
        return newSelection;
    }
}
