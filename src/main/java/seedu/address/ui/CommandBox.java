package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.ChangeListTabEvent;
import seedu.address.commons.events.ui.NewResultAvailableEvent;
import seedu.address.logic.ListElementPointer;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final int DOUBLE_PRESS_DELAY = 300;
    private static final String MESSAGE_AVAILABLE_AUTOCOMPLETE = "Command suggestions: ";
    private static final String MESSAGE_NO_MORE_AVAILABLE_COMMANDS = "No more available commands suggestions";
    private static final String SPACING = " ";
    private static final String EMPTY_STRING = "";
    private static final int NUMBER_OF_LIST_PANELS = 3;


    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    private final Logic logic;
    private ListElementPointer historySnapshot;

    private long previousTabPressTime;

    @FXML
    private TextField commandTextField;

    public CommandBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        historySnapshot = logic.getHistorySnapshot();
        previousTabPressTime = 0;
    }

    //@@author jonathanwj
    /**
     * Handles the key press event, {@code keyEvent}.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (keyEvent.isControlDown()) {
            handleControlDownKeyPress(keyEvent);
            return;
        }
        if (keyEvent.isShiftDown()) {
            handleShiftDownKeyPress(keyEvent);
            return;
        }

        switch (keyEvent.getCode()) {
        case UP:
            // As up and down buttons will alter the position of the caret,
            // consuming it causes the caret's position to remain unchanged
            keyEvent.consume();
            navigateToPreviousInput();
            break;
        case DOWN:
            keyEvent.consume();
            navigateToNextInput();
            break;
        case TAB:
            keyEvent.consume();
            autoCompleteUserInput();
            break;
        case ESCAPE:
            keyEvent.consume();
            clearCommandBox();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Handles the key press on Control Down event, {@code keyEvent}.
     */
    private void handleControlDownKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case TAB:
            keyEvent.consume();
            changeSelectionPanelForward();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Handles the key press on Control Down event, {@code keyEvent}.
     */
    private void handleShiftDownKeyPress(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
        case TAB:
            keyEvent.consume();
            changeSelectionPanelBackwards();
            break;
        default:
            // let JavaFx handle the keypress
        }
    }

    /**
     * Change the selection panel forward on the UI
     */
    private void changeSelectionPanelForward() {
        int listToChange = logic.getCurrentList();
        listToChange = ++listToChange % NUMBER_OF_LIST_PANELS;
        EventsCenter.getInstance().post(new ChangeListTabEvent(listToChange));
    }

    /**
     * Change the selection panel forward on the UI
     */
    private void changeSelectionPanelBackwards() {
        int listToChange = logic.getCurrentList();
        listToChange = (--listToChange + NUMBER_OF_LIST_PANELS) % NUMBER_OF_LIST_PANELS;
        EventsCenter.getInstance().post(new ChangeListTabEvent(listToChange));
    }

    /**
     * Clears the command box
     */
    private void clearCommandBox() {
        replaceText(EMPTY_STRING);
    }

    /**
     * Shows auto completed text or suggestions on the UI
     */
    private void autoCompleteUserInput() {
        if (commandTextField.getText().isEmpty()) {
            return;
        }

        if (getCurrentText().endsWith(SPACING)) {
            autoCompleteNextCommandParameter();
            return;
        }

        List<String> listOfAutoComplete = logic.getAutoCompleteCommands(getCurrentText());
        if (listOfAutoComplete.isEmpty()) {
            return;
        }

        if (listOfAutoComplete.size() == 1) {
            replaceText(listOfAutoComplete.get(0));
        }

        if (isTabDoubleTap()) {
            showSuggestionsOnUi(listOfAutoComplete);
        }

    }

    /**
     * Shows autocomplete suggestions on the UI given the list of string suggestions
     */
    private void showSuggestionsOnUi(List<String> listOfAutoComplete) {
        logger.info(MESSAGE_AVAILABLE_AUTOCOMPLETE
                + commandTextField.getText() + " >> " + getStringFromList(listOfAutoComplete));
        if (listOfAutoComplete.size() == 1) {
            raise(new NewResultAvailableEvent(MESSAGE_NO_MORE_AVAILABLE_COMMANDS));
        } else {
            raise(new NewResultAvailableEvent(MESSAGE_AVAILABLE_AUTOCOMPLETE + getStringFromList(listOfAutoComplete)));
        }
    }

    /**
     * Shows auto completed next prefix parameter for completed command on UI
     */
    private void autoCompleteNextCommandParameter() {
        String textToShow = getCurrentText()
                + logic.getAutoCompleteNextParameter(getCurrentText());
        replaceText(textToShow);
    }

    /**
     * Returns the {@code String} representative of given the list of Strings.
     */
    private String getStringFromList(List<String> listOfAutoComplete) {
        String toString = listOfAutoComplete.toString();
        toString = toString.substring(1, toString.length() - 1).trim();
        return toString;
    }

    /**
     * Returns true if TAB is pressed in quick succession
     */
    private boolean isTabDoubleTap() {
        if (System.currentTimeMillis() - previousTabPressTime < DOUBLE_PRESS_DELAY) {
            return true;
        }
        previousTabPressTime = System.currentTimeMillis();
        return false;
    }

    /**
     * Returns the current text in the command box
     */
    private String getCurrentText() {
        return commandTextField.getText();
    }

    //@@author
    /**
     * Updates the text field with the previous input in {@code historySnapshot},
     * if there exists a previous input in {@code historySnapshot}
     */
    private void navigateToPreviousInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasPrevious()) {
            return;
        }

        replaceText(historySnapshot.previous());
    }

    /**
     * Updates the text field with the next input in {@code historySnapshot},
     * if there exists a next input in {@code historySnapshot}
     */
    private void navigateToNextInput() {
        assert historySnapshot != null;
        if (!historySnapshot.hasNext()) {
            return;
        }

        replaceText(historySnapshot.next());
    }

    /**
     * Sets {@code CommandBox}'s text field with {@code text} and
     * positions the caret to the end of the {@code text}.
     */
    private void replaceText(String text) {
        commandTextField.setText(text);
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());
            initHistory();
            historySnapshot.next();
            // process result of the command
            commandTextField.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException | ParseException e) {
            initHistory();
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Initializes the history snapshot.
     */
    private void initHistory() {
        historySnapshot = logic.getHistorySnapshot();
        // add an empty string to represent the most-recent end of historySnapshot, to be shown to
        // the user if she tries to navigate past the most-recent end of the historySnapshot.
        historySnapshot.add("");
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

}
