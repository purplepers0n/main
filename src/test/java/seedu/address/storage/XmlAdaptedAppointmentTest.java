package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_1;
import static seedu.address.testutil.TypicalAppointments.APPOINTMENT_2;

import org.junit.Test;

public class XmlAdaptedAppointmentTest {

    @Test
    public void toModelType_validAppointment_returnsModel() throws Exception {
        XmlAdaptedAppointment appointment = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(APPOINTMENT_1, appointment.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedAppointment apptOne = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(apptOne, apptOne);
        assertNotEquals(apptOne, new Object());
        XmlAdaptedAppointment apptTwo = new XmlAdaptedAppointment(APPOINTMENT_1);
        assertEquals(apptOne, apptTwo);
        XmlAdaptedAppointment apptThree = new XmlAdaptedAppointment(APPOINTMENT_2);
        assertNotEquals(apptOne, apptThree);
    }
}
