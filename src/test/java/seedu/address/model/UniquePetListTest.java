package seedu.address.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.pet.UniquePetList;
import seedu.address.model.pet.exceptions.DuplicatePetException;
import seedu.address.model.pet.exceptions.PetNotFoundException;
import seedu.address.testutil.TypicalPets;

//@@author md-azsa-reused
public class UniquePetListTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private UniquePetList uniquePetList = new UniquePetList();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniquePetList.asObservableList().remove(0);
    }

    @Test
    public void addPet_petAlreadyExists_throwsDuplicatePetException() throws
            DuplicatePetException {
        uniquePetList.add(TypicalPets.GARFIELD);
        thrown.expect(DuplicatePetException.class);
        uniquePetList.add(TypicalPets.GARFIELD);
    }

    @Test
    public void removePet_petDoesNotExist_throwsPetNotFoundException() throws
            PetNotFoundException {
        thrown.expect(PetNotFoundException.class);
        uniquePetList.remove(TypicalPets.SCOOBY);
    }
}
