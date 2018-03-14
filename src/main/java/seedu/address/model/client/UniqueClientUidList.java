package seedu.address.model.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.client.ClientUid;
import seedu.address.model.client.exceptions.DuplicateClientUidException;
import seedu.address.model.client.exceptions.ClientUidNotFoundException;

/**
 * A list of clientUids that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see ClientUid#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueClientUidList implements Iterable<ClientUid> {

    private final ObservableList<ClientUid> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent clientUid as the given argument.
     */
    public boolean contains(ClientUid toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a clientUid to the list.
     *
     * @throws DuplicateClientUidException if the clientUid to add is a duplicate of an existing clientUid in the list.
     */
    public void add(ClientUid toAdd) throws DuplicateClientUidException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateClientUidException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the clientUid {@code target} in the list with {@code editedclient}.
     *
     * @throws DuplicateClientUidException if the replacement is equivalent to another existing clientUid in the list.
     * @throws ClientUidNotFoundException if {@code target} could not be found in the list.
     */
    public void setClientUid(ClientUid target, ClientUid editedClientUid)
            throws DuplicateClientUidException, ClientUidNotFoundException {
        requireNonNull(editedClientUid);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ClientUidNotFoundException();
        }

        if (!target.equals(editedClientUid) && internalList.contains(editedClientUid)) {
            throw new DuplicateClientUidException();
        }

        internalList.set(index, editedClientUid);
    }

    /**
     * Removes the equivalent clientUid from the list.
     *
     * @throws ClientUidNotFoundException if no such clientUid could be found in the list.
     */
    public boolean remove(ClientUid toRemove) throws ClientUidNotFoundException {
        requireNonNull(toRemove);
        final boolean clientUidFoundAndDeleted = internalList.remove(toRemove);
        if (!clientUidFoundAndDeleted) {
            throw new ClientUidNotFoundException();
        }
        return clientUidFoundAndDeleted;
    }

    public void setClientUids(UniqueClientUidList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setClientUids(List<ClientUid> clientUids) throws DuplicateClientUidException {
        requireAllNonNull(clientUids);
        final UniqueClientUidList replacement = new UniqueClientUidList();
        for (final ClientUid clientUid : clientUids) {
            replacement.add(clientUid);
        }
        setClientUids(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ClientUid> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<ClientUid> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueClientUidList // instanceof handles nulls
                && this.internalList.equals(((UniqueClientUidList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
