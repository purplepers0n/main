package seedu.address.model.pet;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.appointment.UniqueAppointmentUidList;
import seedu.address.model.client.ClientUid;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Pet in the applications.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Pet {

    private final PetUid uid;
    private final PetName petName;
    private final PetAge petAge;
    private final PetGender petGender;

    // UID References to other objects
    private final UniqueAppointmentUidList petAppointmentUids;
    private final ClientUid clientUid;

    private final UniqueTagList tags;

    /**
     * Every field must be present and not null
     */
    public Pet(PetName petName, PetAge petAge, PetGender petGender,
               UniqueAppointmentUidList petAppointmentUids, ClientUid clientUid, Set<Tag> tags) {
        requireAllNonNull(petName, petAge, petGender, tags);
        this.petName = petName;
        this.petAge = petAge;
        this.petGender = petGender;
        this.petAppointmentUids = petAppointmentUids;
        this.clientUid = clientUid;

        uid = new PetUid();
        //protect internal tags from changes in the arg lis
        this.tags = new UniqueTagList(tags);
    }

    public PetName getPetName() {
        return petName;
    }

    public PetAge getPetAge() {
        return petAge;
    }

    public PetGender getPetGender() {
        return petGender;
    }

    public ClientUid getPetClientUid() {
        return clientUid;
    }

    public UniqueAppointmentUidList getPetAppointmentUids() {
        return petAppointmentUids;
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
                && otherPet.getPetGender().equals(this.getPetGender())
                && otherPet.getPetClientUid().equals(this.getPetClientUid())
                && otherPet.getPetAppointmentUids().equals(this.getPetAppointmentUids());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(petName, petAge, petGender, clientUid, petAppointmentUids);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Pet Name: ")
                .append(getPetName())
                .append(" Pet Age: ")
                .append(getPetAge())
                .append(" Gender: ")
                .append(getPetGender())
                .append(" Pet Owner: ")
                //TODO get client name and appointments dates from addressbook with referenced Uid
                .append(getPetClientUid().toString())
                .append(" Appointment Date: ")
                .append(getPetAppointmentUids().toString());
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     *  Returns the uid of the pet as a {@code PetUid}
     */
    public PetUid getPetUid() {
        return uid;
    }

}
