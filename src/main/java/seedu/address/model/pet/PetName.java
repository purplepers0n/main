package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a pet's name in the address book.
 * Guarantees: imumutable; is valid as declared in {@link #isValidPetName(String)}
 */
public class PetName {

    public static final String MESSAGE_PETNAME_CONSTRAINTS =
            "Pet names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
      * The first character must not be whitespace,
      * otherwise " " (a blank string) becomes a valid input.
      */
    public static final String PETNAME_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullPetName;

    /**
     * Constructs a {@code Name}
     *
     * @param petName A valid pet name
     */
    public PetName(String petName) {
        requireNonNull(petName);
        checkArgument(isValidPetName(petName), MESSAGE_PETNAME_CONSTRAINTS);
        this.fullPetName = petName;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidPetName(String test) {
        return test.matches(PETNAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullPetName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PetName //instanceof handles null
                && this.fullPetName.equals(((PetName) other).fullPetName)); //state check
    }

    @Override
    public in hashCode() {
        return fullPetName.hashCode();
    }
}
