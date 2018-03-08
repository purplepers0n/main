package seedu.address.model.pet;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

//TODO: Link Client and Appointment class
/**
 * Represents a Pet in the applications.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Pet {

    private final PetName petName;
    private final PetAge petAge;
    private final Species species;
    private final PetGender petGender;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null
     */
    public Pet(PetName petName, PetAge petAge, Species species, PetGender petGender,
               Set<Tag> tags) {
        requireAllNonNull(petName, petAge, species, petGender, tags);
        this.petName = petName;
        this.petAge = petAge;
        this.species = species;
        this.petGender = petGender;

        //protect internal tags from changes in the arg lis
        this.tags = new UniqueTagList(tags);
    }

    public PetName getPetName() {
        return petName;
    }

    public PetAge getPetAge() {
        return petAge;
    }

    public Species getSpecies() {
        return species;
    }

    public PetGender getPetGender() {
        return petGender;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags.toSet());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Pet)) {
            return false;
        }

        Pet otherPet = (Pet) other;
        return otherPet.getPetName().equals(this.getPetName())
                && otherPet.getPetAge().equals(this.getPetAge())
                && otherPet.getSpecies().equals(this.getSpecies())
                && otherPet.getPetGender().equals(this.getPetGender());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(petName, petAge, species, petGender);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Pet Name: ")
                .append(getPetName())
                .append(" Pet Age: ")
                .append(getPetAge())
                .append(" Species: ")
                .append(getSpecies())
                .append(" Gender: ")
                .append(getPetGender());
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
