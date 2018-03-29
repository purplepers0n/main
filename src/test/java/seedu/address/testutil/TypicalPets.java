package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.exceptions.DuplicatePetException;

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
    public static final Pet NOFONO = new PetBuilder().withPetName("Nofono Not")
            .withPetAge("5")
            .withPetGender("M")
            .withTags("tiger", "green")
            .build();

    private TypicalPets() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical pets.
     */
    public static AddressBook getTypicalAddressBookPet() {
        AddressBook ab = new AddressBook();
        for (Pet pet : getTypicalPets()) {
            try {
                ab.addPet(pet);
            } catch (DuplicatePetException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    public static List<Pet> getTypicalPets() {
        return new ArrayList<>(Arrays.asList(GARFIELD, SCOOBY, PICKLES, LOTSO));
    }
}

