package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

//@@author Godxin-functional
/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Appointment#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueAppointmentList implements Iterable<Appointment> {
    public static final String MESSAGE_DURATION_PREVIOUS = " Appointment is too close to previous one.";
    public static final String MESSAGE_DURATION_NEXT = " Appointment is too close to next one.";
    private static final int MINIMUM_INTERVAL = 1440;
    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();
    private Appointment previous;
    private Appointment next;

    /**
     * Returns true if the list contains an appointment with the same date and time as the given argument.
     */
    public boolean contains(Appointment toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds an appointment to the list.
     *
     * @throws DuplicateAppointmentException if the appointment to add is a duplicate(same date and time)
     * of an existing appointment in the list.
     */
    public void add(Appointment toAdd) throws DuplicateAppointmentException,
        AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        if (hasDurationClosePrevious(toAdd)) {
            throw new AppointmentCloseToPreviousException(MESSAGE_DURATION_PREVIOUS);
        }
        if (hasDurationCloseNext(toAdd)) {
            throw new AppointmentCloseToNextException(MESSAGE_DURATION_NEXT);
        }
        internalList.add(toAdd);
    }

    /**
     * Reschedule the appointment {@code target} in the list with {@code editedAppointment}.
     *
     * @throws DuplicateAppointmentException if the reschedule clashes to another existing appointment in the list.
     * @throws AppointmentNotFoundException if {@code target} could not be found in the list.
     */
    public void setAppointment(Appointment target, Appointment editedAppointment)
            throws DuplicateAppointmentException, AppointmentNotFoundException {
        requireNonNull(editedAppointment);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new AppointmentNotFoundException();
        }
        if (!target.equals(editedAppointment) && internalList.contains(editedAppointment)) {
            throw new AppointmentNotFoundException();
        }
        internalList.set(index, editedAppointment);
    }

    /**
     * Removes the appointment from the list.
     *
     * @throws AppointmentNotFoundException if no such appointment could be found in the list.
     */
    public boolean remove(Appointment toRemove) throws AppointmentNotFoundException {
        requireNonNull(toRemove);
        final boolean appointmentFoundAndDeleted = internalList.remove(toRemove);
        if (!appointmentFoundAndDeleted) {
            throw new AppointmentNotFoundException();
        }
        return appointmentFoundAndDeleted;
    }

    public void setAppointments(UniqueAppointmentList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        requireAllNonNull(appointments);
        final UniqueAppointmentList replacement = new UniqueAppointmentList();
        for (final Appointment appointment : appointments) {
            replacement.add(appointment);
        }
        setAppointments(replacement);
    }

    //@@author md-azsa
    /**
     * Sorts the internal list
     */
    public void sort() {
        SortedList<Appointment> sortedList = new SortedList<>(internalList, Appointment::compareTo);
        internalList.setAll(sortedList);
    }
    //@@author

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Appointment> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    /**
     * Check that there is no earlier existing appointment too close
     * too close: the new appointment start time is within the duration
     * of previous appointment
     */
    public boolean hasDurationClosePrevious(Appointment toAdd) {
        int minInterval = MINIMUM_INTERVAL;
        int interval = MINIMUM_INTERVAL;
        if (hasPrevious(toAdd)) {
            minInterval = previous.getDuration().getDurationValue();
            interval = toAdd.calDurationDifferencePositive(previous);
        }
        return interval < minInterval;
    }

    /**
     * Check that there is no later existing appointment too close
     * too close: Later appointment is within the duration of new appointment
     */
    public boolean hasDurationCloseNext(Appointment toAdd) {
        int minInterval = MINIMUM_INTERVAL;
        int interval = MINIMUM_INTERVAL;
        if (hasNext(toAdd)) {
            minInterval = toAdd.getDuration().getDurationValue();
            interval = toAdd.calDurationDifferenceNegative(next);
        }
        return interval < minInterval;
    }

    /**
     * Return true if there is a previous appointment
     */
    public boolean hasPrevious(Appointment currentAppointment) {
        boolean hasPreviousAppointment = false;
        Date newAppointmentDate = currentAppointment.getDate();
        Time newAppointmentTime = currentAppointment.getTime();
        int currentMin = newAppointmentTime.getMinute();
        int currentHour = newAppointmentTime.getHour();

        int interval;
        int minInterval = MINIMUM_INTERVAL;

        for (Appointment earlierAppointment : internalList) {
            Date earlierAppointmentDate = earlierAppointment.getDate();
            Time earlierAppointmentTime = earlierAppointment.getTime();

            if (newAppointmentDate.equals(earlierAppointmentDate)) {
                if (earlierAppointmentTime.getHour() < currentHour
                        || (earlierAppointmentTime.getHour() == currentHour
                        && earlierAppointmentTime.getMinute() < currentMin)) {

                    interval = currentAppointment.calDurationDifferencePositive(earlierAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                        previous = earlierAppointment;
                        hasPreviousAppointment = true;
                    }
                }
            }
        }
        return hasPreviousAppointment;
    }

    /**
     * Returns true if there is next appointment
     */
    public boolean hasNext(Appointment currentAppointment) {
        boolean hasNextAppointment = false;
        Date newAppointmentDate = currentAppointment.getDate();
        Time newAppointmentTime = currentAppointment.getTime();
        int currentMin = newAppointmentTime.getMinute();
        int currentHour = newAppointmentTime.getHour();

        int interval;
        int minInterval = currentAppointment.getDuration().getDurationValue();

        for (Appointment laterAppointment : internalList) {
            Date laterAppointmentDate = laterAppointment.getDate();
            Time laterAppointmentTime = laterAppointment.getTime();

            if (newAppointmentDate.equals(laterAppointmentDate)) {
                if (laterAppointmentTime.getHour() > currentHour
                        || (laterAppointmentTime.getHour() == currentHour
                        && currentMin < laterAppointmentTime.getMinute())) {
                    interval = currentAppointment.calDurationDifferenceNegative(laterAppointment);
                    if (interval < minInterval) {
                        minInterval = interval;
                        next = laterAppointment;
                        hasNextAppointment = true;
                    }
                }
            }
        }
        return  hasNextAppointment;
    }
    @Override
    public Iterator<Appointment> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.address.model.appointment.UniqueAppointmentList // instanceof handles nulls
                && this.internalList.equals(((UniqueAppointmentList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
