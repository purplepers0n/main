package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;

/**
 * A list of pets that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Pet#equals(Object)
 */
public class UniquePetList implements Iterable<Pet> {

    private final ObservableList<Pet> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent pet as the given argument.
     */
    public boolean contains(Pet toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a pet to the list.
     *
     * @throws DuplicatePetException if the pet to add is a duplicate of an existing pet in the list.
     */
    public void add(Pet toAdd) throws DuplicatePetException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePetException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the pet {@code target} in the list with {@code editedpet}.
     *
     * @throws DuplicatePetException if the replacement is equivalent to another existing pet in the list.
     * @throws PetNotFoundException if {@code target} could not be found in the list.
     */
    public void setPet(Pet target, Pet editedPet)
            throws DuplicatePetException, PetNotFoundException {
        requireNonNull(editedPet);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PetNotFoundException();
        }

        if (!target.equals(editedPet) && internalList.contains(editedPet)) {
            throw new DuplicatePetException();
        }

        internalList.set(index, editedPet);
    }

    /**
     * Removes the equivalent pet from the list.
     *
     * @throws PetNotFoundException if no such pet could be found in the list.
     */
    public boolean remove(Pet toRemove) throws PetNotFoundException {
        requireNonNull(toRemove);
        final boolean petFoundAndDeleted = internalList.remove(toRemove);
        if (!petFoundAndDeleted) {
            throw new PetNotFoundException();
        }
        return petFoundAndDeleted;
    }

    public void setPets(UniquePetList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPets(List<Pet> pets) throws DuplicatePetException {
        requireAllNonNull(pets);
        final UniquePetList replacement = new UniquePetList();
        for (final Pet pet : pets) {
            replacement.add(pet);
        }
        setPets(replacement);
    }

    /**
     * Sorts the internal list.
     */
    public void sort() {
        internalList.sort((Pet one, Pet two) -> one.getPetName().toString()
                .compareTo(two.getPetName().toString()));
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Pet> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Pet> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePetList // instanceof handles nulls
                && this.internalList.equals(((UniquePetList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
