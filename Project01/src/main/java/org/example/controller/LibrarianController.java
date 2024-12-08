package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.model.Book;
import org.example.model.Librarian;
import org.example.model.Library;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LibrarianController {
    private Librarian model;
    private Library library;

    /**
     * logs in the user based on params, and sets the model to it
     *
     * @param email    persons email
     * @param password persons password
     * @return if it worked or not
     */
    public boolean login(String email, String password) {
        String sql = "SELECT * FROM Librarians WHERE Email = ? AND PasswordHash = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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

    /**
     * adds a book into the table
     *
     * @param book book to be added
     * @return wether it worked or not
     */
    public static boolean addBook(Book book) {
        String sql = "INSERT INTO Books (Name, Author, Genre, Copies) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getCopiesAvailable());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * adds a book using the parameters instead of an object
     *
     * @param name   book info
     * @param author book info
     * @param genre  book iinfo
     * @param copies book info
     * @return wether it worked or not
     */
    public static boolean addBook(String name, String author, String genre, int copies) {
        String sql = "INSERT INTO Books (Name, Author, Status, Genre, Copies) VALUES (?, ?, 'AVAILABLE', ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
     * removes a book based on id from the table
     *
     * @param bookId id of book
     * @return wether it worked
     */
    public boolean removeBook(int bookId) {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
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
