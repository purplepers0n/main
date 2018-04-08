package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author md-azsa
/**
 * Represents a Pet's gender in the program.
 * Guarantees: immutable; is valid as declared in {@link #isValidGender(String)}
 */
public class PetGender {

    public static final String MESSAGE_PETGENDER_CONSTRAINTS =
            "Pet gender can only be m, f, M or F.";

    /*
     * The string can only be m,f,M,F
     */
    public static final String GENDER_VALIDATION_REGEX = "[m,f,M,F]";

    public final String fullGender;

    /**
     * Constructs a {@code PetGender}
     *
     * @param petGender A valid pet gender
     */
    public PetGender(String petGender) {
        requireNonNull(petGender);
        checkArgument(isValidGender(petGender), MESSAGE_PETGENDER_CONSTRAINTS);
        this.fullGender = petGender.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid gender
     */
    public static boolean isValidGender(String test) {
        return test.matches(GENDER_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullGender;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PetGender)
                && this.fullGender.equals(((PetGender) other).fullGender);
    }

    @Override
    public int hashCode() {
        return fullGender.hashCode();
    }
}
