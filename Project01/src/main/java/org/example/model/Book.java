package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class Book {
    private static int idCounter = 1;
    private int id;
    private String name;
    private String author;
    private String genre;
    private int copiesAvailable;
    private boolean isAvailable;

    protected Book(String name, String author, String genre, int copiesAvailable) {
        this.id = idCounter++;
        this.name = name;
        this.genre = genre;
        this.copiesAvailable = copiesAvailable;
        if (copiesAvailable == 0) {
            this.isAvailable = false;
        } else {
            this.isAvailable = true;
        }


    }
}
