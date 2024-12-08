package org.example.model;

public class Librarian extends User {
    public Librarian(String firstName, String lastName, String email) {
        super(firstName, lastName, email, true); // Set isLibrarian to true
    }

    public Librarian() {
        super(true);

    }
}
