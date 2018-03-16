package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPersonRole(String)}
 */

public class PersonRole {

    /**
     * Person roles that can be used.
     */
    private enum Role {
        CLIENT,
        TECHNICIAN
    }

    public static final String MESSAGE_ROLE_CONSTRAINTS =
            "Person role can take only 'client' and 'technician' values, and it should not be blank";

    private final Role role;

    PersonRole(String role) {

        requireNonNull(role);
        checkArgument(isValidPersonRole(role), MESSAGE_ROLE_CONSTRAINTS);

        if (role.equalsIgnoreCase("client")) {
            this.role = Role.CLIENT;
        } else {
            this.role = Role.TECHNICIAN;
        }
    }

    /**
     * Returns if a given string is a valid person role.
     */
    public static boolean isValidPersonRole(String test) {
        if (test == null) {
            return false;
        }
        return (test.equalsIgnoreCase(Role.CLIENT.toString())
                || test.equalsIgnoreCase(Role.TECHNICIAN.toString()));
    }

    /**
     * Returns string of PersonRole type.
     */
    public String toString() {
        requireNonNull(role);
        if (role.equals(Role.CLIENT)) {
            return "client";
        } else {
            return "technician";
        }

    }
}
