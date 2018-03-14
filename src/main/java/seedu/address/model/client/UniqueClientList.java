package seedu.address.model.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;

/**
 * A list of clients that enforces uniqueness between its elements and does not allow nulls.
 * Supports a minimal set of list operations.
 *
 * @see Client#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueClientList implements Iterable<Client> {

    private final ObservableList<Client> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent client as the given argument.
     */
    public boolean contains(Client toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a client to the list.
     *
     * @throws DuplicateClientException if the client to add is a duplicate of an existing client in the list.
     */
    public void add(Client toAdd) throws DuplicateClientException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateClientException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the client {@code target} in the list with {@code editedClient}.
     *
     * @throws DuplicateClientException if the replacement is equivalent to another existing client in the list.
     * @throws ClientNotFoundException if {@code target} could not be found in the list.
     */
    public void setClient(Client target, Client editedClient)
            throws DuplicateClientException, ClientNotFoundException {
        requireNonNull(editedClient);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ClientNotFoundException();
        }

        if (!target.equals(editedClient) && internalList.contains(editedClient)) {
            throw new DuplicateClientException();
        }

        internalList.set(index, editedClient);
    }

    /**
     * Removes the equivalent client from the list.
     *
     * @throws ClientNotFoundException if no such client could be found in the list.
     */
    public boolean remove(Client toRemove) throws ClientNotFoundException {
        requireNonNull(toRemove);
        final boolean clientFoundAndDeleted = internalList.remove(toRemove);
        if (!clientFoundAndDeleted) {
            throw new ClientNotFoundException();
        }
        return clientFoundAndDeleted;
    }

    public void setClients(UniqueClientList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setClients(List<Client> clients) throws DuplicateClientException {
        requireAllNonNull(clients);
        final UniqueClientList replacement = new UniqueClientList();
        for (final Client client : clients) {
            replacement.add(client);
        }
        setClients(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Client> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Client> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueClientList // instanceof handles nulls
                && this.internalList.equals(((UniqueClientList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
