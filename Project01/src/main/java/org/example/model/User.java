package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;


@EqualsAndHashCode
@ToString
@Getter
@Setter
public class User {
    private static int idCounter = 1;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isLibrarian;

    protected User(String firstName, String lastName, String email, Boolean isLibrarian) {
        this.id = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isLibrarian = isLibrarian;
    }
    protected User(Boolean isLibrarian) {
        this.id = idCounter++;
        this.isLibrarian = isLibrarian;
    }

}