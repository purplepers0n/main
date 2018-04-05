package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

//@@author jonathanwj
/**
 * Represents a Person's role in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPersonRole(Role)}
 */

public class PersonRole {

    public static final String CLIENT_STRING = "client";
    public static final String TECHNICIAN_STRING = "technician";

    /**
     * Person roles that can be used.
     */
    private enum Role {
        CLIENT,
        TECHNICIAN
    }

    public static final PersonRole TECHNICIAN_ROLE = new PersonRole(Role.TECHNICIAN);
    public static final PersonRole CLIENT_ROLE = new PersonRole(Role.CLIENT);

    public static final String MESSAGE_ROLE_CONSTRAINTS =
            "Person role can take only 'client' and 'technician' values, and it should not be blank";

    private final Role role;

    /**
     * Constructs a PersonRole
     *
     * @param role a valid role
     */
    public PersonRole(Role role) {
        requireNonNull(role);
        checkArgument(isValidPersonRole(role), MESSAGE_ROLE_CONSTRAINTS);

        if (role.equals(Role.CLIENT)) {
            this.role = Role.CLIENT;
        } else {
            this.role = Role.TECHNICIAN;
        }
    }

    /**
     * Constructs a PersonRole
     *
     * @param role a valid string representation of {@code PersonRole}
     */
    public PersonRole(String role) {
        requireNonNull(role);
        checkArgument(isValidPersonRole(role), MESSAGE_ROLE_CONSTRAINTS);

        if (role.equalsIgnoreCase("client")) {
            this.role = Role.CLIENT;
        } else {
            this.role = Role.TECHNICIAN;
        }
    }

    /**
     * Returns true if a given Role is a valid person role.
     */
    public static boolean isValidPersonRole(Role test) {
        if (test == null) {
            return false;
        }
        return (test.equals(Role.CLIENT)
                || test.equals(Role.TECHNICIAN));
    }

    /**
     * Returns true if a given String represents a valid person role.
     */
    public static boolean isValidPersonRole(String test) {
        if (test == null) {
            return false;
        }
        return (test.equalsIgnoreCase(CLIENT_STRING)
                || test.equalsIgnoreCase(TECHNICIAN_STRING));
    }

    /**
     * Returns the string representation of PersonRole.
     */
    public String toString() {
        requireNonNull(role);
        if (role.equals(Role.CLIENT)) {
            return CLIENT_STRING;
        } else {
            return TECHNICIAN_STRING;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof PersonRole // instanceof handles nulls
                && this.role.equals(((PersonRole) other).role)); // state check
    }
}
