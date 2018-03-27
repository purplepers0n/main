package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.appointment.UniqueAppointmentList;

public class UniqueAppointmentListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueAppointmentList.asObservableList().remove(0);
    }
}
