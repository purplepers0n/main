package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.appointment.exceptions.AppointmentUidNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentUidException;

/**
 * A list of appointmentUids that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see AppointmentUid#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueAppointmentUidList implements Iterable<AppointmentUid> {

    private final ObservableList<AppointmentUid> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent appointmentUid as the given argument.
     */
    public boolean contains(AppointmentUid toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a appointmentUid to the list.
     *
     * @throws DuplicateAppointmentUidException if the appointmentUid
     * to add is a duplicate of an existing appointmentUid in the list.
     */
    public void add(AppointmentUid toAdd) throws DuplicateAppointmentUidException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentUidException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the appointmentUid {@code target} in the list with {@code editedappointment}.
     *
     * @throws DuplicateAppointmentUidException if the replacement
     * is equivalent to another existing appointmentUid in the list.
     * @throws AppointmentUidNotFoundException if {@code target} could not be found in the list.
     */
    public void setAppointmentUid(AppointmentUid target, AppointmentUid editedAppointmentUid)
            throws DuplicateAppointmentUidException, AppointmentUidNotFoundException {
        requireNonNull(editedAppointmentUid);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentUidNotFoundException();
        }

        if (!target.equals(editedAppointmentUid) && internalList.contains(editedAppointmentUid)) {
            throw new DuplicateAppointmentUidException();
        }

        internalList.set(index, editedAppointmentUid);
    }

    /**
     * Removes the equivalent appointmentUid from the list.
     *
     * @throws AppointmentUidNotFoundException if no such appointmentUid could be found in the list.
     */
    public boolean remove(AppointmentUid toRemove) throws AppointmentUidNotFoundException {
        requireNonNull(toRemove);
        final boolean appointmentUidFoundAndDeleted = internalList.remove(toRemove);
        if (!appointmentUidFoundAndDeleted) {
            throw new AppointmentUidNotFoundException();
        }
        return appointmentUidFoundAndDeleted;
    }

    public void setAppointmentUids(UniqueAppointmentUidList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAppointmentUids(List<AppointmentUid> appointmentUids) throws DuplicateAppointmentUidException {
        requireAllNonNull(appointmentUids);
        final UniqueAppointmentUidList replacement = new UniqueAppointmentUidList();
        for (final AppointmentUid appointmentUid : appointmentUids) {
            replacement.add(appointmentUid);
        }
        setAppointmentUids(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<AppointmentUid> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<AppointmentUid> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueAppointmentUidList // instanceof handles nulls
                && this.internalList.equals(((UniqueAppointmentUidList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
