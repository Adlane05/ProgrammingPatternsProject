package org.example.model;

public class UserFactory {
    /**
     * all args constructor
     *
     * @param type
     * @param firstName
     * @param lastName
     * @param Email
     * @return the created user
     */
    public static User createUser(String type, String firstName, String lastName, String Email) {
        if (type.equalsIgnoreCase("client")) {
            return new Client(firstName, lastName, Email);
        }
        if (type.equalsIgnoreCase("librarian")) {

            return new Librarian(firstName, lastName, Email);
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }

    /**
     * empty args constructor
     *
     * @param type
     * @return the user
     */
    public static User createUser(String type) {
        if (type.equalsIgnoreCase("client")) {
            return new Client();
        }
        if (type.equalsIgnoreCase("librarian")) {

            return new Librarian();
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}
