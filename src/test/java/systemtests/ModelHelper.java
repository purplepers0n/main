package systemtests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Contains helper methods to set up {@code Model} for testing.
 */
public class ModelHelper {
    private static final Predicate<Client> PREDICATE_MATCHING_NO_CLIENTS = unused -> false;
    private static final Predicate<VetTechnician> PREDICATE_MATCHING_NO_TECHNICIANS = unused -> false;

    /**
     * Updates {@code model}'s filtered list to display only {@code toDisplay}.
     */
    public static void setFilteredList(Model model,
                                       List<Client> toDisplayClient, List<VetTechnician> toDisplayVetTechnician) {
        Optional<Predicate<Client>> predicateClient = toDisplayClient.stream()
                .map(ModelHelper::getPredicateMatchingClient).reduce(Predicate::or);
        model.updateFilteredClientList(predicateClient.orElse(PREDICATE_MATCHING_NO_CLIENTS));
        if (!toDisplayVetTechnician.isEmpty()) {
            Optional<Predicate<VetTechnician>> predicateTechnician = toDisplayVetTechnician.stream()
                    .map(ModelHelper::getPredicateMatchingTechnician).reduce(Predicate::or);
            model.updateFilteredVetTechnicianList(predicateTechnician.orElse(PREDICATE_MATCHING_NO_TECHNICIANS));
        }
    }

    /**
     * @see ModelHelper#setFilteredList(Model, List)
     */
    public static void setFilteredList(Model model, Person... toDisplay) {
        List<Person> persons = Arrays.asList(toDisplay);
        List<Client> clients = new ArrayList<>();
        List<VetTechnician> technicians = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).isClient()) {
                clients.add((Client) persons.get(i));
            } else {
                technicians.add((VetTechnician) persons.get(i));
            }
        }
        persons = new ArrayList<>();
        setFilteredList(model, clients, technicians);
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<Client> getPredicateMatchingClient(Person other) {
        return person -> person.equals(other);
    }

    /**
     * Returns a predicate that evaluates to true if this {@code Person} equals to {@code other}.
     */
    private static Predicate<VetTechnician> getPredicateMatchingTechnician(Person other) {
        return person -> person.equals(other);
    }

}
