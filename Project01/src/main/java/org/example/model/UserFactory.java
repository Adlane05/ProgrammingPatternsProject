package org.example.model;

public class UserFactory {
    public static User createUser(String type, String firstName,String lastName, String Email){
        if (type.equalsIgnoreCase("client")){
            return new Client(firstName,lastName, Email);
        }
        if (type.equalsIgnoreCase("librarian") ){

            return new Librarian(firstName,lastName, Email);
        }
        else{
            throw new IllegalArgumentException("Invalid user type");
        }
    }
}
