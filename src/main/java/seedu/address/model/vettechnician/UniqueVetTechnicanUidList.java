package seedu.address.model.vettechnician;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianUidException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianUidNotFoundException;

/**
 * A list of vetTechUids that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see VetTechnicianUid#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueVetTechnicanUidList implements Iterable<VetTechnicianUid> {

    private final ObservableList<VetTechnicianUid> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent vetTechUid as the given argument.
     */
    public boolean contains(VetTechnicianUid toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a vetTechUid to the list.
     *
     * @throws DuplicateVetTechnicianUidException if the vetTechUid
     * to add is a duplicate of an existing vetTechUid in the list.
     */
    public void add(VetTechnicianUid toAdd) throws DuplicateVetTechnicianUidException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateVetTechnicianUidException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the vetTechUid {@code target} in the list with {@code editedvetTech}.
     *
     * @throws DuplicateVetTechnicianUidException if the replacement
     * is equivalent to another existing vetTechUid in the list.
     * @throws VetTechnicianUidNotFoundException if {@code target} could not be found in the list.
     */
    public void setVetTechUid(VetTechnicianUid target, VetTechnicianUid editedVetTechnicianUid)
            throws DuplicateVetTechnicianUidException, VetTechnicianUidNotFoundException {
        requireNonNull(editedVetTechnicianUid);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new VetTechnicianUidNotFoundException();
        }

        if (!target.equals(editedVetTechnicianUid) && internalList.contains(editedVetTechnicianUid)) {
            throw new DuplicateVetTechnicianUidException();
        }

        internalList.set(index, editedVetTechnicianUid);
    }

    /**
     * Removes the equivalent vetTechUid from the list.
     *
     * @throws VetTechnicianUidNotFoundException if no such vetTechUid could be found in the list.
     */
    public boolean remove(VetTechnicianUid toRemove) throws VetTechnicianUidNotFoundException {
        requireNonNull(toRemove);
        final boolean vetTechUidFoundAndDeleted = internalList.remove(toRemove);
        if (!vetTechUidFoundAndDeleted) {
            throw new VetTechnicianUidNotFoundException();
        }
        return vetTechUidFoundAndDeleted;
    }

    public void setVetTechnicianUids(UniqueVetTechnicanUidList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setVetTechnicianUids(List<VetTechnicianUid> vetTechUids) throws DuplicateVetTechnicianUidException {
        requireAllNonNull(vetTechUids);
        final UniqueVetTechnicanUidList replacement = new UniqueVetTechnicanUidList();
        for (final VetTechnicianUid vetTechUid : vetTechUids) {
            replacement.add(vetTechUid);
        }
        setVetTechnicianUids(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<VetTechnicianUid> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<VetTechnicianUid> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueVetTechnicanUidList // instanceof handles nulls
                && this.internalList.equals(((UniqueVetTechnicanUidList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
