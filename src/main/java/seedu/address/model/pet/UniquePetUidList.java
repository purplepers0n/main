package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.pet.exceptions.DuplicatePetUidException;
import seedu.address.model.pet.exceptions.PetUidNotFoundException;

/**
 * A list of petUids that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see PetUid#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniquePetUidList implements Iterable<PetUid> {

    private final ObservableList<PetUid> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent petUid as the given argument.
     */
    public boolean contains(PetUid toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a petUid to the list.
     *
     * @throws DuplicatePetUidException if the petUid to add is a duplicate of an existing petUid in the list.
     */
    public void add(PetUid toAdd) throws DuplicatePetUidException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePetUidException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the petUid {@code target} in the list with {@code editedpet}.
     *
     * @throws DuplicatePetUidException if the replacement is equivalent to another existing petUid in the list.
     * @throws PetUidNotFoundException if {@code target} could not be found in the list.
     */
    public void setPetUid(PetUid target, PetUid editedPetUid)
            throws DuplicatePetUidException, PetUidNotFoundException {
        requireNonNull(editedPetUid);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PetUidNotFoundException();
        }

        if (!target.equals(editedPetUid) && internalList.contains(editedPetUid)) {
            throw new DuplicatePetUidException();
        }

        internalList.set(index, editedPetUid);
    }

    /**
     * Removes the equivalent petUid from the list.
     *
     * @throws PetUidNotFoundException if no such petUid could be found in the list.
     */
    public boolean remove(PetUid toRemove) throws PetUidNotFoundException {
        requireNonNull(toRemove);
        final boolean petUidFoundAndDeleted = internalList.remove(toRemove);
        if (!petUidFoundAndDeleted) {
            throw new PetUidNotFoundException();
        }
        return petUidFoundAndDeleted;
    }

    public void setPetUids(UniquePetUidList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPetUids(List<PetUid> petUids) throws DuplicatePetUidException {
        requireAllNonNull(petUids);
        final UniquePetUidList replacement = new UniquePetUidList();
        for (final PetUid petUid : petUids) {
            replacement.add(petUid);
        }
        setPetUids(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<PetUid> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<PetUid> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePetUidList // instanceof handles nulls
                && this.internalList.equals(((UniquePetUidList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
