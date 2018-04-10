# md-azsa-reused
###### \java\seedu\address\model\UniquePetListTest.java
``` java
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
```
