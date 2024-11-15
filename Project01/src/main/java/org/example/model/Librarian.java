package org.example.model;

public class Librarian extends User {
    public Librarian(String name, String email) {
        super(name, email, true); // Set isLibrarian to true
    }
}
