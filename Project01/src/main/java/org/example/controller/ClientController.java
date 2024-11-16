package org.example.controller;

import org.example.model.Book;
import org.example.model.Client;
import org.example.view.ClientView;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientController {
    private Client model;
    private ClientView view; // Placeholder for Client UI interactions

    public ClientController(Client model, ClientView view) {
        this.model = model;
        this.view = view;
    }

    public boolean login(String email, String password) {
        String sql = "SELECT * FROM Clients WHERE Email = ? AND PasswordHash = ?";
        String passwordHash = String.valueOf(password.hashCode());

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, passwordHash);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                model.setId(rs.getInt("ClientID"));
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

    public boolean signUp(Client newClient, String Password) {
        String passwordHash = String.valueOf(Password.hashCode());
        String sql = "INSERT INTO Clients (FirstName, LastName, Email, PasswordHash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newClient.getFirstName());
            pstmt.setString(2, newClient.getLastName());
            pstmt.setString(3, newClient.getEmail());
            pstmt.setString(4, passwordHash);
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

    // Private helper for field-based search
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

    public boolean borrowBook(int bookId) {
        String sql = "INSERT INTO Reservations (Status, BookID, ClientID, ReturnDate, ReservationDate) " +
                "VALUES ('BORROWED', ?, ?, DATE('now', '+7 days'), DATE('now'))";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, model.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean returnBook(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE ReservationID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
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
