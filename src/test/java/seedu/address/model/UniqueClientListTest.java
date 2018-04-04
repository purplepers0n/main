package seedu.address.model;

import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.client.Client;
import seedu.address.model.client.UniqueClientList;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;

//@@ author jonathanwj
public class UniqueClientListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private UniqueClientList uniqueClientList = new UniqueClientList();


    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueClientList.asObservableList().remove(0);
    }

    @Test
    public void addClient_clientAlreadyExist_throwsDuplicateClientException() throws DuplicateClientException {
        uniqueClientList.add((Client) HOON);
        thrown.expect(DuplicateClientException.class);
        uniqueClientList.add((Client) HOON);
    }

    @Test
    public void removeClient_clientNotInList_throwsClientNotFoundException() throws ClientNotFoundException {
        thrown.expect(ClientNotFoundException.class);
        uniqueClientList.remove((Client) HOON);
    }

    @Test
    public void setClient_clientNotInList_throwsClientNotFoundException()
            throws ClientNotFoundException, DuplicateClientException {
        thrown.expect(ClientNotFoundException.class);
        uniqueClientList.setClient((Client) HOON, (Client) IDA);
    }

    @Test
    public void setClient_clientIsTheSame_throwsDuplicateClientException()
            throws ClientNotFoundException, DuplicateClientException {
        uniqueClientList.add((Client) HOON);
        uniqueClientList.add((Client) IDA);
        thrown.expect(DuplicateClientException.class);
        uniqueClientList.setClient((Client) IDA, (Client) HOON);
    }

    @Test
    public void setClients_duplicateClientsInList_throwsDuplicateClientException()
            throws ClientNotFoundException, DuplicateClientException {
        thrown.expect(DuplicateClientException.class);
        uniqueClientList.setClients(Arrays.asList((Client) HOON, (Client) HOON));
    }
}
