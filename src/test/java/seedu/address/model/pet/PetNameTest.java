package seedu.address.model.pet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.testutil.Assert;

//@@author md-azsa
public class PetNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new PetName(null));
    }

    @Test
    public void constructor_invalidPetName_throwsIllegalArgumentException() {
        String invalidPetName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new PetName(invalidPetName));
    }

    @Test
    public void isValidName() {
        // null pet name
        Assert.assertThrows(NullPointerException.class, () -> PetName.isValidPetName(null));

        // invalid name
        assertFalse(PetName.isValidPetName("")); // empty string
        assertFalse(PetName.isValidPetName(" ")); // spaces string
        assertFalse(PetName.isValidPetName(" *")); // non-alphanumeric
        assertFalse(PetName.isValidPetName("garfield* ")); // contains non-alpha char

        // valid names
        assertTrue(PetName.isValidPetName("Garfield Jokes")); // alphabets only
        assertTrue(PetName.isValidPetName("Garfield 123"));
        assertTrue(PetName.isValidPetName("Garfield II King 3"));
        assertTrue(PetName.isValidPetName("Garfield Pop starrr"));



    }
}
