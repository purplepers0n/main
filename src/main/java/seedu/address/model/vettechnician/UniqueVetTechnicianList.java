package seedu.address.model.vettechnician;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;

/**
 * A list of vetTechnicians that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see VetTechnician#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueVetTechnicianList implements Iterable<VetTechnician> {

    private final ObservableList<VetTechnician> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent vetTechnician as the given argument.
     */
    public boolean contains(VetTechnician toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a vetTechnician to the list.
     *
     * @throws DuplicateVetTechnicianException if the vetTechnician
     * to add is a duplicate of an existing vetTechnician in the list.
     */
    public void add(VetTechnician toAdd) throws DuplicateVetTechnicianException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateVetTechnicianException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the vetTechnician {@code target} in the list with {@code editedvetTechnician}.
     *
     * @throws DuplicateVetTechnicianException if the replacement
     * is equivalent to another existing vetTechnician in the list.
     * @throws VetTechnicianNotFoundException if {@code target} could not be found in the list.
     */
    public void setVetTechnician(VetTechnician target, VetTechnician editedVetTechnician)
            throws DuplicateVetTechnicianException, VetTechnicianNotFoundException {
        requireNonNull(editedVetTechnician);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new VetTechnicianNotFoundException();
        }

        if (!target.equals(editedVetTechnician) && internalList.contains(editedVetTechnician)) {
            throw new DuplicateVetTechnicianException();
        }

        internalList.set(index, editedVetTechnician);
    }

    /**
     * Removes the equivalent vetTechnician from the list.
     *
     * @throws VetTechnicianNotFoundException if no such vetTechnician could be found in the list.
     */
    public boolean remove(VetTechnician toRemove) throws VetTechnicianNotFoundException {
        requireNonNull(toRemove);
        final boolean vetTechnicianFoundAndDeleted = internalList.remove(toRemove);
        if (!vetTechnicianFoundAndDeleted) {
            throw new VetTechnicianNotFoundException();
        }
        return vetTechnicianFoundAndDeleted;
    }

    public void setVetTechnicians(UniqueVetTechnicianList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setVetTechnicians(List<VetTechnician> vetTechnicians) throws DuplicateVetTechnicianException {
        requireAllNonNull(vetTechnicians);
        final UniqueVetTechnicianList replacement = new UniqueVetTechnicianList();
        for (final VetTechnician vetTechnician : vetTechnicians) {
            replacement.add(vetTechnician);
        }
        setVetTechnicians(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<VetTechnician> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<VetTechnician> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueVetTechnicianList // instanceof handles nulls
                && this.internalList.equals(((UniqueVetTechnicianList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
