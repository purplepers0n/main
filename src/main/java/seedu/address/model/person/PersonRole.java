package seedu.address.model.person;

/**
 * Person roles that can be used.
 */
public enum PersonRole {

    CLIENT("client"),
    TECHNICIAN("technician");

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
     * Returns name of PersonRole type.
     */
    public String toString() {
        return name;
    }
}
