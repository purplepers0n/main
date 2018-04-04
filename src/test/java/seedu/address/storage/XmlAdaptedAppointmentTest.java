package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_2;

import org.junit.Test;

//@@author md-azsa
public class XmlAdaptedAppointmentTest {

    @Test
    public void toModelType_validAppointment_returnsModel() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(APPOINTMENT_1, appointment.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedAppointment apptOne = new XmlAdaptedAppointment(APPOINTMENT_1);

        // Same objects -> returns true
        assertEquals(apptOne, apptOne);

        // Different objects -> returns false
        assertNotEquals(apptOne, new Object());

        // Same calls -> returns true
        XmlAdaptedAppointment apptTwo = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(apptOne, apptTwo);

        // Different calls -> returns false
        XmlAdaptedAppointment apptThree = new XmlAdaptedAppointment(APPOINTMENT_2);
        assertNotEquals(apptOne, apptThree);
    }
}
