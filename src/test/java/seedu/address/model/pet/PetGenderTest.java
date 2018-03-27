package seedu.address.model.pet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class PetGenderTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetGender(null));
    }

    @Test
    public void constructor_invalidPetGender_throwsIllegalArgumentException() {
        String invalidGender = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetGender(invalidGender));
    }

    @Test
    public void isValidGender() {
        // null pet gender
        Assert.assertThrows(NullPointerException.class, () -> PetGender.isValidGender(null));

        // invalid gender
        assertFalse(PetGender.isValidGender("")); // empty string
        assertFalse(PetGender.isValidGender(" ")); // just spaces
        assertFalse(PetGender.isValidGender("123")); // numbers
        assertFalse(PetGender.isValidGender("asd")); // alphabets
        assertFalse(PetGender.isValidGender("12sda12")); // mix of alphabets
        assertFalse(PetGender.isValidGender("asd assd")); // spaces in between

        // valid gender
        assertTrue(PetGender.isValidGender("m")); // small m
        assertTrue(PetGender.isValidGender("M")); // capital
        assertTrue(PetGender.isValidGender("f")); // small f
        assertTrue(PetGender.isValidGender("F")); // capital f

    }
}
