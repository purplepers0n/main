package seedu.address.logic.commands;

import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.model.client.Client;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.pet.Pet;
import seedu.address.model.vettechnician.VetTechnician;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons whose names contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice bob charlie";

    private final NameContainsKeywordsPredicate predicate;
    private int currList = 0; //default is on client list upon opening app

    public FindCommand(NameContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public void setCurrentList() {
        this.currList = model.getCurrentList();
    }

    @Override
    public CommandResult execute() {
        setCurrentList();

        if (currList == 0) {
            model.updateFilteredClientList(new Predicate<Client>() {
                @Override
                public boolean test(Client client) {
                    return predicate.getKeywords().stream().anyMatch(keyword ->
                            StringUtil.containsWordIgnoreCase(client.getName().fullName, keyword));
                }
            });
            return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredClientList().size()));
        } else if (currList == 1) {
            model.updateFilteredPetList(new Predicate<Pet>() {
                @Override
                public boolean test(Pet pet) {
                    return predicate.getKeywords().stream().anyMatch(keyword ->
                            StringUtil.containsWordIgnoreCase(pet.getPetName().fullPetName, keyword));
                }
            });
            return new CommandResult(getMessageForPetListShownSummary(model.getFilteredPetList().size()));
        } else if (currList == 2) {
            model.updateFilteredVetTechnicianList(new Predicate<VetTechnician>() {
                @Override
                public boolean test(VetTechnician vetTechnician) {
                    return predicate.getKeywords().stream().anyMatch(keyword ->
                            StringUtil.containsWordIgnoreCase(vetTechnician.getName().fullName, keyword));
                }
            });
            return new CommandResult(getMessageForPersonListShownSummary(model.getFilteredVetTechnicianList().size()));
        }

        return new CommandResult(getMessageForPersonListShownSummary(0));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindCommand // instanceof handles nulls
                && this.predicate.equals(((FindCommand) other).predicate)); // state check
    }
}
