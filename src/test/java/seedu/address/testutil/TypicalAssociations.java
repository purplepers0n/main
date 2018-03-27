package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.association.ClientOwnPet;
import seedu.address.model.client.Client;


/**
 * A utility class containing a list of {@code ClientOwnPet} objects to be used in tests.
 */
public class TypicalAssociations {

    public static final ClientOwnPet FIONA_LOTSO = new ClientOwnPet((Client) TypicalPersons.FIONA,
            TypicalPets.LOTSO);
    public static final ClientOwnPet ELLE_PICKLES = new ClientOwnPet((Client) TypicalPersons.ELLE,
            TypicalPets.PICKLES);


    private TypicalAssociations() {} // prevents instantiation


    public static List<ClientOwnPet> getTypicalAssociations() {
        return new ArrayList<>(Arrays.asList(FIONA_LOTSO, ELLE_PICKLES));
    }
}
