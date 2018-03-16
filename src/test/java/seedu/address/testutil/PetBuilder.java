package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetAge;
import seedu.address.model.pet.PetGender;
import seedu.address.model.pet.PetName;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtilPet;

/**
 * Util class to help with building Pet objects.
 */
public class PetBuilder {
    public static final String DEFAULT_PET_NAME = "Garfield";
    public static final String DEFAULT_PET_AGE = "5";
    public static final String DEFAULT_PET_GENDER = "M";
    public static final String DEFAULT_TAGS = "Cat";

    private PetName petName;
    private PetAge petAge;
    private PetGender petGender;
    private Set<Tag> tags;

    public PetBuilder() {
        petName = new PetName(DEFAULT_PET_NAME);
        petAge = new PetAge(DEFAULT_PET_AGE);
        petGender = new PetGender(DEFAULT_PET_GENDER);
        tags = SampleDataUtilPet.getTagSet(DEFAULT_TAGS);
    }

    /**
     * Inits the PetBuilder with the data of {@code petToCopy}
     */
    public PetBuilder(Pet petToCopy) {
        petName = petToCopy.getPetName();
        petAge = petToCopy.getPetAge();
        petGender = petToCopy.getPetGender();
        tags = new HashSet<>(petToCopy.getTags());
    }

    /**
     * Sets the {@code PetName} of the {@code Pet} we are building
     */
    public PetBuilder withPetName(String petName) {
        this.petName = new PetName(petName);
        return this;
    }

    /**
     * Sets the {@code PetAge} of the {@code Pet} we are building
     */
    public PetBuilder withPetAge(String petAge) {
        this.petAge = new PetAge(petAge);
        return this;
    }

    /**
     * Sets the {@code PetGender} of {@code Pet} we are building
     */
    public PetBuilder withPetGender(String petGender) {
        this.petGender = new PetGender(petGender);
        return this;
    }

    /**
     * Sets the tag
     */
    public PetBuilder withTags(String ... tags) {
        this.tags = SampleDataUtilPet.getTagSet(tags);
        return this;
    }

    /**
     * Builts the pet object
     */
    public Pet build() {
        return new Pet(petName, petAge, petGender, tags);
    }
}
