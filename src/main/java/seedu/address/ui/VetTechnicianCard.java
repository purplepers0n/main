package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * An UI component that displays information of a {@code VetTechnician}.
 */
public class VetTechnicianCard extends UiPart<Region> {

    private static final String FXML = "VetTechnicianListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final VetTechnician vetTechnician;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public VetTechnicianCard(VetTechnician vetTechnician, int displayedIndex) {
        super(FXML);
        this.vetTechnician = vetTechnician;
        id.setText(displayedIndex + ". ");
        name.setText(vetTechnician.getName().fullName);
        phone.setText(vetTechnician.getPhone().value);
        address.setText(vetTechnician.getAddress().value);
        email.setText(vetTechnician.getEmail().value);
        vetTechnician.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VetTechnicianCard)) {
            return false;
        }

        // state check
        VetTechnicianCard card = (VetTechnicianCard) other;
        return id.getText().equals(card.id.getText())
                && vetTechnician.equals(card.vetTechnician);
    }
}
