package org.example;


import org.example.model.User;
import org.example.model.UserFactory;

public class Main {
    public static void main(String[] args) {
        User librarian1 = UserFactory.createUser("Librarian", "Jonathan sims", "BLAHBLA@GMAIL,COM");
    }
    }