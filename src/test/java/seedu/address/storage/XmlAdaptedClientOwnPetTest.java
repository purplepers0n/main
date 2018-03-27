package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.testutil.TypicalAssociations.FIONA_LOTSO;

import org.junit.Test;

public class XmlAdaptedClientOwnPetTest {

    @Test
    public void toModelType_validAssociation_returnsAssociation() throws Exception {
        XmlAdaptedClientOwnPet association = new XmlAdaptedClientOwnPet(FIONA_LOTSO);
        assertEquals(FIONA_LOTSO, association.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedClientOwnPet associationOne = new XmlAdaptedClientOwnPet(FIONA_LOTSO);
        assertEquals(associationOne, associationOne);
        assertNotEquals(associationOne, new Object());
        XmlAdaptedClientOwnPet associationTwo = new XmlAdaptedClientOwnPet(FIONA_LOTSO);
        assertEquals(associationOne, associationTwo);
    }

}
