package seedu.address.model.pet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author md-azsa
public class PetAgeTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetAge(null));
    }

    @Test
    public void constructor_invalidPetAge_throwsIllegalArgumentException() {
        String invalidPetAge = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetAge(invalidPetAge));
    }

    @Test
    public void isValidPetAge() {
        // null pet age
        Assert.assertThrows(NullPointerException.class, () -> PetAge.isValidPetAge(null));

        // invalid pet age
        assertFalse(PetAge.isValidPetAge("")); // empty string
        assertFalse(PetAge.isValidPetAge("123")); // 3 digits long
        assertFalse(PetAge.isValidPetAge("asv")); // alphabets
        assertFalse(PetAge.isValidPetAge("*(*(123")); // characters and numbers
        assertFalse(PetAge.isValidPetAge("321 2132")); // spaces in between

        // valid pet age
        assertTrue(PetAge.isValidPetAge("2")); // 1 digit
        assertTrue(PetAge.isValidPetAge("21")); // 2 digits

    }
}
