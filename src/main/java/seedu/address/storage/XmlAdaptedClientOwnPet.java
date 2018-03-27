package seedu.address.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

/**
 * JAXV-friendly version of the ClientOwnPet.
 */
public class XmlAdaptedClientOwnPet {

    @XmlElement(required = true)
    private XmlAdaptedPet pet;
    @XmlElement(required = true)
    private XmlAdaptedPerson client;

    /**
     * Constructs an XmlAdaptedClientOwnPet.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedClientOwnPet() {}

    /**
     * Constructs an {@code XmlAdaptedClientOwnPet} with the given {@code ClientOwnPet}
     */
    public XmlAdaptedClientOwnPet(ClientOwnPet association) {
        this.pet = new XmlAdaptedPet(association.getPet());
        this.client = new XmlAdaptedPerson(association.getClient());
    }

    /**
     * Converts this jaxb-friendly adapted association object into the model's ClientOwnPet object.
     */
    public ClientOwnPet toModelType() throws IllegalValueException {
        return new ClientOwnPet((Client) client.toModelType(), pet.toModelType());
    }

    public Pet getPet() throws IllegalValueException {
        return pet.toModelType();
    }

    public Client getClient() throws IllegalValueException {
        return (Client) client.toModelType();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedClientOwnPet)) {
            return false;
        }

        XmlAdaptedClientOwnPet otherAdapted = (XmlAdaptedClientOwnPet) other;
        return pet.equals(otherAdapted.pet) && client.equals(otherAdapted.client);
    }
}
