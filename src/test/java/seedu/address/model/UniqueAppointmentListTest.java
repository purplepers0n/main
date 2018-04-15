package seedu.address.model;

import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1_1;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_2;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_2_2;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.appointment.UniqueAppointmentList;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

//@@author Godxin-test
public class UniqueAppointmentListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAppointmentList.asObservableList().remove(0);
    }

    @Test
    public void addAppointment_appointmentAlreadyExist_throwsDuplicateAppointmentException() throws
            DuplicateAppointmentException, AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_1);
        thrown.expect(DuplicateAppointmentException.class);
        uniqueAppointmentList.add(APPOINTMENT_1);
    }

    @Test
    public void addAppointment_appointmentCloseToPrevious_throwsAppointmentCloseToPreviousException() throws
            DuplicateAppointmentException, AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_1);
        thrown.expect(AppointmentCloseToPreviousException.class);
        uniqueAppointmentList.add(APPOINTMENT_1_1);
    }

    @Test
    public void addAppointment_noPrevious_addSuccessful() throws
            DuplicateAppointmentException, AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_1_1);
    }

    @Test
    public void addAppointment_appointmentCloseToNext_throwsAppointmentCloseToNextException() throws
            DuplicateAppointmentException, AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_2);
        thrown.expect(AppointmentCloseToNextException.class);
        uniqueAppointmentList.add(APPOINTMENT_2_2);
    }

    @Test
    public void addAppointment_noNext_addSuccessful() throws
            DuplicateAppointmentException, AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_2_2);
    }

    @Test
    public void removeAppointment_appointmentNotInList_throwsAppointmentNotFoundException()
            throws AppointmentNotFoundException {
        thrown.expect(AppointmentNotFoundException.class);
        uniqueAppointmentList.remove(APPOINTMENT_1);
    }

    @Test
    public void setAppointment_appointmentNotInList_throwsAppointmentNotFoundException()
            throws AppointmentNotFoundException, DuplicateAppointmentException {
        thrown.expect(AppointmentNotFoundException.class);
        uniqueAppointmentList.setAppointment(APPOINTMENT_1, APPOINTMENT_2);
    }

    @Test
    public void setAppointment_appointmentIsTheSame_throwsDuplicateAppointmentException()
            throws AppointmentNotFoundException, DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        uniqueAppointmentList.add(APPOINTMENT_1);
        uniqueAppointmentList.add(APPOINTMENT_2);
        thrown.expect(DuplicateAppointmentException.class);
        uniqueAppointmentList.setAppointment(APPOINTMENT_1, APPOINTMENT_2);
    }

    @Test
    public void setAppointments_duplicateAppointmentsInList_throwsDuplicateAppointmentException()
            throws DuplicateAppointmentException,
            AppointmentCloseToPreviousException, AppointmentCloseToNextException {
        thrown.expect(DuplicateAppointmentException.class);
        uniqueAppointmentList.setAppointments(Arrays.asList(APPOINTMENT_1, APPOINTMENT_1));
    }
}
