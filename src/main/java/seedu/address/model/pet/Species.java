package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Pet's species in the program.
 * Guarantees: immutable; is valid as declared in {@link #isSpeciesValid(String)}
 */
public class Species {

    public static final String MESSAGE_SPECIES_CONSTRAINTS =
            "Species name should only contain alphanumeric characters and spaces, and should not be blank";
    /*
     * The species should only contain character
     */
    public static final String SPECIES_VALIDATION_REGEX = "[a-zA-Z]+";

    public final String fullSpecies;

    /**
     * Constructs a {@code Species}
     *
     * @param species A valid species name
     */
    public Species(String species) {
        requireNonNull(species);
        checkArgument(isSpeciesValid(species), MESSAGE_SPECIES_CONSTRAINTS);
        this.fullSpecies = species;
    }

    /**
     * Returns true if a given string is a valid species format.
     */
    public static boolean isSpeciesValid(String test) {
        return test.matches(SPECIES_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullSpecies;
    }
    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Species
                && this.fullSpecies.equals(((Species) other).fullSpecies));
    }

    @Override
    public int hashCode() {
        return fullSpecies.hashCode();
    }
}
