package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.pet.Pet;

//@@author md-azsa
/**
 * A utility class containing a list of {@code Pet} objects to be used in tests.
 */
public class TypicalPets {

    public static final Pet GARFIELD = new PetBuilder().withPetName("Garfield")
            .withPetAge("10")
            .withPetGender("M")
            .withTags("cat", "tabby")
            .build();
    public static final Pet SCOOBY = new PetBuilder().withPetName("Scooby Doo")
            .withPetAge("10")
            .withPetGender("M")
            .withTags("dog", "greatDane")
            .build();
    public static final Pet PICKLES = new PetBuilder().withPetName("Pickles Tickles")
            .withPetAge("3")
            .withPetGender("F")
            .withTags("hamster", "obesed")
            .build();
    public static final Pet LOTSO = new PetBuilder().withPetName("Lotso Fatso")
            .withPetAge("4")
            .withPetGender("M")
            .withTags("bear", "purple")
            .build();

    private TypicalPets() {} // prevents instantiation


    public static List<Pet> getTypicalPets() {
        return new ArrayList<>(Arrays.asList(GARFIELD, SCOOBY, PICKLES, LOTSO));
    }
}

