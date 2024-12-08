package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Book;
import org.example.model.Client;
import org.example.model.Library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientController {
    private Client model;
    private Library library;


    /**
     * logs in the user based on params, and sets the model to it
     *
     * @param email    persons email
     * @param password persons password
     * @return if it worked or not
     */
    public boolean login(String email, String password) {
        String sql = "SELECT * FROM Clients WHERE Email = ? AND PasswordHash = ?";
        String passwordHash = String.valueOf(password.hashCode());

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    /**
     * adds the new signed up client to database
     *
     * @param newClient client info
     * @param Password  the password
     * @return wether it worked
     */
    public boolean signUp(Client newClient, String Password) {
        String passwordHash = String.valueOf(Password.hashCode());
        String sql = "INSERT INTO Clients (FirstName, LastName, Email, PasswordHash) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    /**
     * views all borrowed books by the model/user
     *
     * @return a list of all borrowed books
     */
    public List<Book> viewBorrowedBooks() {
        String sql = "SELECT b.Name, b.Author, b.Genre, b.Copies FROM Books b " + "JOIN Reservations r ON b.BookID = r.BookID " + "WHERE r.ClientID = ? AND r.Status = 'BORROWED'";
        List<Book> borrowedBooks = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, model.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    borrowedBooks.add(extractBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }

    /**
     * views all books that were ever borrowed by user/client
     *
     * @return list of books in the history
     */
    public List<Book> viewBorrowingHistory() {
        String sql = "SELECT b.Name, b.Author, b.Genre, b.Copies FROM Books b " + "JOIN Reservations r ON b.BookID = r.BookID " + "WHERE r.ClientID = ?";
        List<Book> history = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, model.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    history.add(extractBook(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * extends the reservation due date an amount of days
     *
     * @param reservationId reservation to be extended
     * @param extraDays     amount of days
     * @return wether it worked or not
     */
    public boolean extendBorrowingPeriod(int reservationId, int extraDays) {
        String sql = "UPDATE Reservations SET ReturnDate = DATE(ReturnDate, ?) " + "WHERE ReservationID = ? AND ClientID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "+" + extraDays + " days");
            stmt.setInt(2, reservationId);
            stmt.setInt(3, model.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * cancels a reservation
     *
     * @param reservationId reservation to be cancelled
     * @return wether it succeeded
     */
    public boolean cancelReservation(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE ReservationID = ? AND ClientID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reservationId);
            stmt.setInt(2, model.getId());
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * searches book table based on a query
     *
     * @param query the query
     * @return books that match the query
     */
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


    /**
     * adds a reservation
     *
     * @param bookId book to be borrowed
     * @return wether it succeeded
     */
    public boolean borrowBook(int bookId) {
        String sql = "INSERT INTO Reservations (Status, BookID, ClientID, ReturnDate, ReservationDate) " + "VALUES ('BORROWED', ?, ?, DATE('now', '+7 days'), DATE('now'))";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, model.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * deletes the reservation
     *
     * @param reservationId reservation to be deleted
     * @return wether it succeded
     */
    public boolean returnBook(int reservationId) {
        String sql = "DELETE FROM Reservations WHERE ReservationID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * selects all books
     *
     * @return list of all books
     */
    public List<Book> refreshBookList() {
        String sql = "SELECT * FROM Books";
        List<Book> books = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(extractBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    /**
     * helper method that extracts a book from the result set
     *
     * @param rs result set to be extracted from
     * @return the book
     * @throws SQLException
     */
    private Book extractBook(ResultSet rs) throws SQLException {
        return new Book(rs.getString("Name"), rs.getString("Author"), rs.getString("Genre"), rs.getInt("Copies"));
    }

}
