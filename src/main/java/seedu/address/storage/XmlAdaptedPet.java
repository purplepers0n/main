package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetAge;
import seedu.address.model.pet.PetGender;
import seedu.address.model.pet.PetName;
import seedu.address.model.tag.Tag;

/**
 * JAXV-friendly version of the Person.
 */
public class XmlAdaptedPet {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Pet's %s field is missing!";

    @XmlElement(required = true)
    private String petName;
    @XmlElement(required = true)
    private String petAge;
    @XmlElement(required = true)
    private String petGender;

    @XmlElement(required = true)
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * Construct an XmlAdaptedPet
     * Thisis the no-arg constructor.
     */
    public XmlAdaptedPet() {}

    /**
     * Constructs the xml with pet details
     */
    public XmlAdaptedPet(String petName, String petAge, String petGender, List<XmlAdaptedTag> tagged) {
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;
        this.tagged = new ArrayList<>(tagged);
    }

    /**
     * Convers a given Pet into a class for JAXB
     *
     * @param source future changes will not affect the created XmladaptedPet
     */
    public XmlAdaptedPet(Pet source) {
        petName = source.getPetName().toString();
        petAge = source.getPetAge().value;
        petGender = source.getPetGender().toString();
        tagged = new ArrayList<>();
        for (Tag tag : source.getTags()) {
            tagged.add(new XmlAdaptedTag(tag));
        }
    }

    /**
     * Convers this to model pet's object
     *
     * @throws IllegalValueException if any constraints
     */
    public Pet toModelType() throws IllegalValueException {
        final List<Tag> petTags = new ArrayList<>();
        Pet convertedPet;

        for (XmlAdaptedTag tag : tagged) {
            petTags.add(tag.toModelType());
        }

        if (this.petName == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PetName.class.getSimpleName()));
        }

        if (!PetName.isValidPetName(this.petName)) {
            throw new IllegalValueException(PetName.MESSAGE_PETNAME_CONSTRAINTS);
        }
        final PetName petName = new PetName(this.petName);

        if (this.petAge == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, PetAge.class.getSimpleName()));
        }

        if (!PetAge.isValidPetAge(this.petAge)) {
            throw new IllegalValueException(PetAge.MESSAGE_PETAGE_CONSTRAINTS);
        }
        final PetAge petAge = new PetAge(this.petAge);

        if (this.petGender == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    PetGender.class.getSimpleName()));
        }

        if (!PetGender.isValidGender(this.petGender)) {
            throw new IllegalValueException(PetGender.MESSAGE_PETGENDER_CONSTRAINTS);
        }
        final PetGender petGender = new PetGender(this.petGender);

        final Set<Tag> tags = new HashSet<>(petTags);

        convertedPet = new Pet(petName, petAge, petGender, tags);

        return convertedPet;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof  XmlAdaptedPet)) {
            return false;
        }

        XmlAdaptedPet otherPet = (XmlAdaptedPet) other;
        return Objects.equals(petName, otherPet.petName)
                && Objects.equals(petAge, otherPet.petAge)
                && Objects.equals(petGender, otherPet.petGender)
                && tagged.equals(otherPet.tagged);
    }
}
