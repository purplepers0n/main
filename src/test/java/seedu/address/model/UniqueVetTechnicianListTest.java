package seedu.address.model;

import static seedu.address.testutil.TypicalPersons.BOON;
import static seedu.address.testutil.TypicalPersons.SWEE;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.vettechnician.UniqueVetTechnicianList;
import seedu.address.model.vettechnician.VetTechnician;
import seedu.address.model.vettechnician.exceptions.DuplicateVetTechnicianException;
import seedu.address.model.vettechnician.exceptions.VetTechnicianNotFoundException;

//@@ author jonathanwj
public class UniqueVetTechnicianListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private UniqueVetTechnicianList uniqueVetTechnicianList = new UniqueVetTechnicianList();


    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueVetTechnicianList.asObservableList().remove(0);
    }

    @Test
    public void addVetTechnician_technicianAlreadyExist_throwsDuplicateVetTechnicianException()
            throws DuplicateVetTechnicianException {
        uniqueVetTechnicianList.add((VetTechnician) BOON);
        thrown.expect(DuplicateVetTechnicianException.class);
        uniqueVetTechnicianList.add((VetTechnician) BOON);
    }

    @Test
    public void removeVetTechnician_technicianNotInList_throwsVetTechnicianNotFoundException()
            throws VetTechnicianNotFoundException {
        thrown.expect(VetTechnicianNotFoundException.class);
        uniqueVetTechnicianList.remove((VetTechnician) BOON);
    }

    @Test
    public void setVetTechnician_technicianNotInList_throwsVetTechnicianNotFoundException()
            throws VetTechnicianNotFoundException, DuplicateVetTechnicianException {
        thrown.expect(VetTechnicianNotFoundException.class);
        uniqueVetTechnicianList.setVetTechnician((VetTechnician) BOON, (VetTechnician) SWEE);
    }

    @Test
    public void setVetTechnician_technicianIsTheSame_throwsDuplicateVetTechnicianException()
            throws VetTechnicianNotFoundException, DuplicateVetTechnicianException {
        uniqueVetTechnicianList.add((VetTechnician) BOON);
        uniqueVetTechnicianList.add((VetTechnician) SWEE);
        thrown.expect(DuplicateVetTechnicianException.class);
        uniqueVetTechnicianList.setVetTechnician((VetTechnician) SWEE, (VetTechnician) BOON);
    }

    @Test
    public void setVetTechnicians_duplicateVetTechniciansInList_throwsDuplicateVetTechnicianException()
            throws VetTechnicianNotFoundException, DuplicateVetTechnicianException {
        thrown.expect(DuplicateVetTechnicianException.class);
        uniqueVetTechnicianList.setVetTechnicians(Arrays.asList((VetTechnician) BOON, (VetTechnician) BOON));
    }
}
