package seedu.address.storage;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import seedu.address.testutil.TypicalPets;

//@@author md-azsa
public class XmlAdaptedPetTest {

    @Test
    public void toModelType_validPet_returnsModel() throws Exception {
        XmlAdaptedPet pet = new XmlAdaptedPet(TypicalPets.GARFIELD);
        assertEquals(TypicalPets.GARFIELD, pet.toModelType());
    }

    @Test
    public void equals() {
        XmlAdaptedPet petOne = new XmlAdaptedPet(TypicalPets.GARFIELD);

        // Same objects -> returns true
        assertEquals(petOne, petOne);

        // Different objects -> returns false
        assertNotEquals(petOne, new Object());

        // Same calls -> returns true
        XmlAdaptedPet petTwo = new XmlAdaptedPet(TypicalPets.GARFIELD);
        assertEquals(petOne, petTwo);

        // Different calls -> returns false
        XmlAdaptedPet petThree = new XmlAdaptedPet(TypicalPets.LOTSO);
        assertNotEquals(petOne, petThree);
    }
}
