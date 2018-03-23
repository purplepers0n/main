package seedu.address.model.association;

import seedu.address.model.client.Client;
import seedu.address.model.pet.Pet;

/**
 * Represents a pet to client association
 */
public class ClientOwnPet {
    private final Client client;
    private final Pet pet;

    public ClientOwnPet(Client client, Pet pet) {
        this.client = client;
        this.pet = pet;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ClientOwnPet)) {
            return false;
        }

        ClientOwnPet otherClientOwnPet = (ClientOwnPet) other;
        return otherClientOwnPet.getClient().equals(this.getClient())
                && otherClientOwnPet.getPet().equals(this.getPet());
    }

    public Client getClient() {
        return client;
    }

    public Pet getPet() {
        return pet;
    }
}
