package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author md-azsa
/**
 * Represents a Pet's age in the application.
 * Guarantees: immutable; is valid as declared in {@link #isValidPetAge(String)}
 */
public class PetAge {

    public static final String MESSAGE_PETAGE_CONSTRAINTS =
            "Pet age can only contain numbers, and should be 1-2 digit long";
    public static final String PET_VALIDATIONS_REGEX = "\\d{1,2}";
    public final String value;

    /**
     * Constructs a {@code PetAge}.
     *
     * @param petAge A valid pet age
     */
    public PetAge(String petAge) {
        requireNonNull(petAge);
        checkArgument(isValidPetAge(petAge), MESSAGE_PETAGE_CONSTRAINTS);
        this.value = petAge;
    }

    /**
     * Returns true if a given is a valid pet age number
     */
    public static boolean isValidPetAge(String test) {
        return test.matches(PET_VALIDATIONS_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if object
                || (other instanceof PetAge // instanceof handles nulls
                && this.value.equals(((PetAge) other).value)); //state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
