package org.example.model;

public class Client extends User {

    public Client(String name, String email) {
        super(name, email, false); // Set isLibrarian to false
    }

}
