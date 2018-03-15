package seedu.address.model.vettechnician;

import java.util.Set;

import seedu.address.model.appointment.UniqueAppointmentUidList;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Represents a Vet Technician in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class VetTechnician extends Person {

    private final VetTechnicianUid uid;
    private final UniqueAppointmentUidList appointmentUids;

    /**
     * Every field must be present and not null.
     */
    public VetTechnician(Name name, Phone phone, Email email,
                         Address address, Set<Tag> tags, UniqueAppointmentUidList appointmentUids) {
        super(name, phone, email, address, tags);

        uid = new VetTechnicianUid();
        this.appointmentUids = appointmentUids;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof VetTechnician)) {
            return false;
        }

        VetTechnician otherPerson = (VetTechnician) other;
        return otherPerson.getName().equals(this.getName())
                && otherPerson.getPhone().equals(this.getPhone())
                && otherPerson.getEmail().equals(this.getEmail())
                && otherPerson.getAddress().equals(this.getAddress());
    }


    /**
     *  Returns the uid of the vet technician as a {@code VetTechnicianUid}
     */
    public VetTechnicianUid getVetTechUid() {
        return uid;
    }

    /**
     *  Returns a list of appointment uids of vet technician as a {@code UniqueAppointmentUidList}
     */
    public UniqueAppointmentUidList getAppointmentUids() {
        return appointmentUids;
    }
}
