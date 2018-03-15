package seedu.address.model.person;

import seedu.address.model.client.Client;

/**
 * Person roles that can be used.
 */
public enum PersonRole {

    CLIENT("client"),
    TECHNICIAN("technician");

    public static final String MESSAGE_ROLE_CONSTRAINTS =
            "Person role can take only 'client' and 'technician' values, and it should not be blank";

    private String name;

    PersonRole(String name) {
        this.name = name;
    }

    /**
     * Returns if a given string is a valid person role.
     */
    public static boolean isValidPersonRole(String test) {
        if (test == null) {
            return false;
        }
        return (test.equalsIgnoreCase(PersonRole.CLIENT.toString())
                || test.equalsIgnoreCase(PersonRole.TECHNICIAN.toString()));
    }

    /**
     * Returns true if a given person is a client.
     */
    public static boolean isClient(Person person) {
        if (person instanceof Client) {
            return true;
        }
        return false;
    }

    /**
     * Returns name of PersonRole type.
     */
    public String toString() {
        return name;
    }
}
