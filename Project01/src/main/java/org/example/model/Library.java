package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Library {
    private List<Book> books = new ArrayList<Book>();
    private List<User> users = new ArrayList<User>();
    public static Library instance;

    private Library(List<Book> books, List<User> users) {
        this.books = books;
        this.users = users;
    }
    /**
     * Singleton method that ensures only one instance exists
     *
     * @param books parameters for the constructor
     * @param users  parameters for the constructor
     * @return the instance of the singleton
     */
    public static Library getInstance(List<Book> books, List<User> users) {
        if (instance == null) {
            synchronized (Library.class) {
                if (instance == null) {
                    instance = new Library(books, users);
                }
            }
        }
        return instance;
    }
}