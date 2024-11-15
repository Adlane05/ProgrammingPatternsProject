package org.example.model;

public class UserFactory {
    public static User createUser(String type, String name, String Email){
        if (type.equalsIgnoreCase("client")){
            return new Client(name, Email);
        }
        if (type.equalsIgnoreCase("librarian") ){

            return new Librarian(name, Email);
        }
        else{
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}
