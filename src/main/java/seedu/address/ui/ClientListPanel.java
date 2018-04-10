package seedu.address.ui;

import java.util.logging.Logger;

import org.fxmisc.easybind.EasyBind;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.client.Client;

//@@author purplepers0n-reused
/**
 * Panel containing the list of clients.
 */
public class ClientListPanel extends UiPart<Region> {
    private static final String FXML = "ClientListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ClientListPanel.class);

    @FXML
    private ListView<ClientCard> clientListView;

    public ClientListPanel(ObservableList<Client> clientList) {
        super(FXML);
        setConnections(clientList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Client> clientList) {
        ObservableList<ClientCard> mappedList = EasyBind.map(
                clientList, (client) -> new ClientCard(client, clientList.indexOf(client) + 1));
        clientListView.setItems(mappedList);
        clientListView.setCellFactory(listView -> new ClientListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code ClientCard}.
     */
    class ClientListViewCell extends ListCell<ClientCard> {

        @Override
        protected void updateItem(ClientCard client, boolean empty) {
            super.updateItem(client, empty);

            if (empty || client == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(client.getRoot());
            }
        }
    }

}
