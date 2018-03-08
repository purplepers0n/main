package seedu.address.model.client;

import java.util.Collection;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;


/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * Supports a minimal set of list operations.
 * @see Person#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueClientList extends UniquePersonList {
}
