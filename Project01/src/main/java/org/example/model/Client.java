package org.example.model;

public class Client extends User {

    public Client(String firstName,String lastName, String email) {
        super(firstName,lastName, email, false); // Set isLibrarian to false
    }

}
