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
    private String name;
    private String email;
    private Boolean isLibrarian;

    protected User(String name, String email, Boolean isLibrarian) {
        this.id = idCounter++;
        this.name = name;
        this.email = email;
        this.isLibrarian = isLibrarian;
    }
}