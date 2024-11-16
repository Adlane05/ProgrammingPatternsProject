package org.example.controller;

import org.example.model.Book;
import org.example.model.Librarian;
import org.example.view.LibrarianView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianController {
    private Librarian model;
    private LibrarianView view; // Placeholder for Librarian UI interactions

    public LibrarianController(Librarian model, LibrarianView view) {
        this.model = model;
        this.view = view;
    }

    public boolean login(String email, String password) {
        String sql = "SELECT * FROM Librarians WHERE Email = ? AND PasswordHash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                model.setId(rs.getInt("LibrarianID"));
                model.setFirstName(rs.getString("FirstName"));
                model.setLastName(rs.getString("LastName"));
                model.setEmail(rs.getString("Email"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public boolean addBook(String name, String author, String genre, int copies) {
        String sql = "INSERT INTO Books (Name, Author, Status, Genre, Copies) VALUES (?, ?, 'AVAILABLE', ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, author);
            pstmt.setString(3, genre);
            pstmt.setInt(4, copies);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Book> searchBooks(String query) {
        query = "%" + query.toLowerCase() + "%";
        String sql = "SELECT * FROM Books WHERE LOWER(Name) LIKE ? OR LOWER(Author) LIKE ? OR LOWER(Genre) LIKE ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, query);
            stmt.setString(2, query);
            stmt.setString(3, query);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(extractBook(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> searchBooksByTitle(String title) {
        return searchBooksByField("Name", title);
    }

    public List<Book> searchBooksByAuthor(String author) {
        return searchBooksByField("Author", author);
    }

    public List<Book> searchBooksByGenre(String genre) {
        return searchBooksByField("Genre", genre);
    }

    private List<Book> searchBooksByField(String field, String value) {
        String sql = "SELECT * FROM Books WHERE LOWER(" + field + ") LIKE ?";
        List<Book> books = new ArrayList<>();
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + value.toLowerCase() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(extractBook(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book extractBook(ResultSet rs) throws Exception {
        return new Book(

                rs.getString("Name"),
                rs.getString("Author"),
                rs.getString("Genre"),
                rs.getInt("Copies")
        );
    }


    public boolean removeBook(int bookId) {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> refreshBookList() {
        String sql = "SELECT * FROM Books";
        List<String> books = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(rs.getString("Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
