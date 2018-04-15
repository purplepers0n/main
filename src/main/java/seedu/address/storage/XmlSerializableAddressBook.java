package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.appointment.exceptions.AppointmentCloseToNextException;
import seedu.address.model.appointment.exceptions.AppointmentCloseToPreviousException;
import seedu.address.model.association.exceptions.ClientAlreadyOwnsPetException;
import seedu.address.model.association.exceptions.PetAlreadyHasOwnerException;


/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableAddressBook {

    private static final String CLIENT_ALREADY_OWNS_PET = "Client already owns pet";
    private static final String PET_ALREADY_HAS_OWNER = "Pet already has owner";
    private static final String APPOINTMENT_CLOSE_PREVIOUS = "New appointment is too close to previous one";
    private static final String APPOINTMENT_CLOSE_NEXT = "New appointment is too close to next one";
    @XmlElement
    private List<XmlAdaptedPerson> persons;
    @XmlElement
    private List<XmlAdaptedTag> tags;
    @XmlElement
    private List<XmlAdaptedPet> pets;
    @XmlElement
    private List<XmlAdaptedAppointment> appointments;
    @XmlElement
    private List<XmlAdaptedClientOwnPet> clientPetAssociations;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAddressBook() {
        persons = new ArrayList<>();
        tags = new ArrayList<>();
        pets = new ArrayList<>();
        appointments = new ArrayList<>();
        clientPetAssociations = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        this();
        persons.addAll(src.getPersonList().stream().map(XmlAdaptedPerson::new).collect(Collectors.toList()));
        tags.addAll(src.getTagList().stream().map(XmlAdaptedTag::new).collect(Collectors.toList()));
        pets.addAll(src.getPetList().stream().map(XmlAdaptedPet::new).collect(Collectors.toList()));
        appointments.addAll(src.getAppointmentList().stream().map(XmlAdaptedAppointment::new).collect(
                Collectors.toList()));
        clientPetAssociations.addAll(src.getClientPetAssociations().stream().map(XmlAdaptedClientOwnPet::new).collect(
                Collectors.toList()));
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedPerson} or {@code XmlAdaptedTag}.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        for (XmlAdaptedTag t : tags) {
            addressBook.addTag(t.toModelType());
        }

        for (XmlAdaptedPerson p : persons) {
            addressBook.addPerson(p.toModelType());
        }

        for (XmlAdaptedPet pet : pets) {
            addressBook.addPet(pet.toModelType());
        }
        for (XmlAdaptedAppointment appointment : appointments) {
            try {
                addressBook.scheduleAppointment(appointment.toModelType());
            } catch (AppointmentCloseToPreviousException ape) {
                throw new IllegalValueException(APPOINTMENT_CLOSE_PREVIOUS);
            } catch (AppointmentCloseToNextException ape) {
                throw new IllegalValueException(APPOINTMENT_CLOSE_NEXT);
            }
        }
        for (XmlAdaptedClientOwnPet association : clientPetAssociations) {
            try {
                addressBook.addPetToClient(association.getPet(), association.getClient());
            } catch (ClientAlreadyOwnsPetException e) {
                throw new IllegalValueException(CLIENT_ALREADY_OWNS_PET);
            } catch (PetAlreadyHasOwnerException e) {
                throw new IllegalValueException(PET_ALREADY_HAS_OWNER);
            }
        }
        return addressBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableAddressBook)) {
            return false;
        }

        XmlSerializableAddressBook otherAb = (XmlSerializableAddressBook) other;
        return persons.equals(otherAb.persons)
                && tags.equals(otherAb.tags)
                && pets.equals(otherAb.pets)
                && appointments.equals(otherAb.appointments)
                && clientPetAssociations.equals(otherAb.clientPetAssociations);
    }
}
