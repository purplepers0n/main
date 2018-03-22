package guitests.guihandles;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.ListView;
import seedu.address.model.client.Client;
import seedu.address.ui.ClientCard;

/**
 * Provides a handle for {@code ClientListPanel} containing the list of {@code ClientCard}.
 */
public class ClientListPanelHandle extends NodeHandle<ListView<ClientCard>> {
    public static final String CLIENT_LIST_VIEW_ID = "#clientListView";

    private Optional<ClientCard> lastRememberedSelectedClientCard;

    public ClientListPanelHandle(ListView<ClientCard> clientListPanelNode) {
        super(clientListPanelNode);
    }

    /**
     * Returns a handle to the selected {@code ClientCardHandle}.
     * A maximum of 1 item can be selected at any time.
     * @throws AssertionError if no card is selected, or more than 1 card is selected.
     */
    public ClientCardHandle getHandleToSelectedCard() {
        List<ClientCard> clientList = getRootNode().getSelectionModel().getSelectedItems();

        if (clientList.size() != 1) {
            throw new AssertionError("Client list size expected 1.");
        }

        return new ClientCardHandle(clientList.get(0).getRoot());
    }

    /**
     * Returns the index of the selected card.
     */
    public int getSelectedCardIndex() {
        return getRootNode().getSelectionModel().getSelectedIndex();
    }

    /**
     * Returns true if a card is currently selected.
     */
    public boolean isAnyCardSelected() {
        List<ClientCard> selectedCardsList = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedCardsList.size() > 1) {
            throw new AssertionError("Card list size expected 0 or 1.");
        }

        return !selectedCardsList.isEmpty();
    }

    /**
     * Navigates the listview to display and select the client.
     */
    public void navigateToCard(Client client) {
        List<ClientCard> cards = getRootNode().getItems();
        Optional<ClientCard> matchingCard = cards.stream().filter(card -> card.client.equals(client)).findFirst();

        if (!matchingCard.isPresent()) {
            throw new IllegalArgumentException("Client does not exist.");
        }

        guiRobot.interact(() -> {
            getRootNode().scrollTo(matchingCard.get());
            getRootNode().getSelectionModel().select(matchingCard.get());
        });
        guiRobot.pauseForHuman();
    }

    /**
     * Returns the client card handle of a client associated with the {@code index} in the list.
     */
    public ClientCardHandle getClientCardHandle(int index) {
        return getClientCardHandle(getRootNode().getItems().get(index).client);
    }

    /**
     * Returns the {@code ClientCardHandle} of the specified {@code client} in the list.
     */
    public ClientCardHandle getClientCardHandle(Client client) {
        Optional<ClientCardHandle> handle = getRootNode().getItems().stream()
                .filter(card -> card.client.equals(client))
                .map(card -> new ClientCardHandle(card.getRoot()))
                .findFirst();
        return handle.orElseThrow(() -> new IllegalArgumentException("Client does not exist."));
    }

    /**
     * Selects the {@code ClientCard} at {@code index} in the list.
     */
    public void select(int index) {
        getRootNode().getSelectionModel().select(index);
    }

    /**
     * Remembers the selected {@code ClientCard} in the list.
     */
    public void rememberSelectedClientCard() {
        List<ClientCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            lastRememberedSelectedClientCard = Optional.empty();
        } else {
            lastRememberedSelectedClientCard = Optional.of(selectedItems.get(0));
        }
    }

    /**
     * Returns true if the selected {@code ClientCard} is different from the value remembered by the most recent
     * {@code rememberSelectedClientCard()} call.
     */
    public boolean isSelectedClientCardChanged() {
        List<ClientCard> selectedItems = getRootNode().getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            return lastRememberedSelectedClientCard.isPresent();
        } else {
            return !lastRememberedSelectedClientCard.isPresent()
                    || !lastRememberedSelectedClientCard.get().equals(selectedItems.get(0));
        }
    }

    /**
     * Returns the size of the list.
     */
    public int getListSize() {
        return getRootNode().getItems().size();
    }
}
