package org.example.view;

import org.example.controller.ClientController;
import org.example.model.Book;
import org.example.model.Client;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryApp {
    private final ClientController clientController;

    public LibraryApp() {
        // Initialize a dummy Client model
        Client clientModel = new Client("", "", "");
        clientController = new ClientController(clientModel);

        createRoleSelectionScreen();
    }

    private void createRoleSelectionScreen() {
        JFrame frame = new JFrame("Library App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        JButton librarianButton = new JButton("Librarian");
        JButton clientButton = new JButton("Client");

        panel.add(librarianButton);
        panel.add(clientButton);

        librarianButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Librarian feature not implemented yet!"));
        clientButton.addActionListener(e -> createLoginScreen(frame));

        frame.add(panel);
        frame.setVisible(true);
    }

    private void createLoginScreen(JFrame parentFrame) {
        parentFrame.getContentPane().removeAll();
        parentFrame.setTitle("Client Login");

        JPanel panel = new JPanel(new GridLayout(3, 2));
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (clientController.login(email, password)) {
                JOptionPane.showMessageDialog(parentFrame, "Login successful!");
                createClientMenu(parentFrame);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Invalid credentials. Please try again.");
            }
        });

        parentFrame.add(panel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void createClientMenu(JFrame parentFrame) {
        parentFrame.getContentPane().removeAll();
        parentFrame.setTitle("Client Menu");

        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton searchBooksButton = new JButton("Search Books");
        JButton borrowBookButton = new JButton("Borrow a Book");
        JButton returnBookButton = new JButton("Return a Book");
        JButton viewAvailableBooksButton = new JButton("View All Available Books");
        JButton logoutButton = new JButton("Logout");

        panel.add(searchBooksButton);
        panel.add(borrowBookButton);
        panel.add(returnBookButton);
        panel.add(viewAvailableBooksButton);
        panel.add(logoutButton);

        // Search Books Action
        searchBooksButton.addActionListener(e -> {
            String query = JOptionPane.showInputDialog(parentFrame, "Enter keyword for search (title/author/genre):");
            if (query != null && !query.trim().isEmpty()) {
                List<Book> books = clientController.searchBooks(query);
                displayBooks(parentFrame, books);
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Search query cannot be empty.");
            }
        });

        // Borrow Book Action
        borrowBookButton.addActionListener(e -> {
            String bookIdInput = JOptionPane.showInputDialog(parentFrame, "Enter the Book ID to borrow:");
            try {
                int bookId = Integer.parseInt(bookIdInput);
                if (clientController.borrowBook(bookId)) {
                    JOptionPane.showMessageDialog(parentFrame, "Book borrowed successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to borrow the book. Please try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid Book ID. Please enter a number.");
            }
        });

        // Return Book Action
        returnBookButton.addActionListener(e -> {
            String reservationIdInput = JOptionPane.showInputDialog(parentFrame, "Enter the Reservation ID to return:");
            try {
                int reservationId = Integer.parseInt(reservationIdInput);
                if (clientController.returnBook(reservationId)) {
                    JOptionPane.showMessageDialog(parentFrame, "Book returned successfully!");
                } else {
                    JOptionPane.showMessageDialog(parentFrame, "Failed to return the book. Please try again.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(parentFrame, "Invalid Reservation ID. Please enter a number.");
            }
        });

        // View Available Books Action
        viewAvailableBooksButton.addActionListener(e -> {
            List<String> books = clientController.refreshBookList();
            displayBooks(parentFrame, books);
        });

        // Logout Action
        logoutButton.addActionListener(e -> createRoleSelectionScreen());

        parentFrame.add(panel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void displayBooks(JFrame parentFrame, List<?> books) {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "No books found.");
        } else {
            StringBuilder bookList = new StringBuilder("Books:\n");
            for (Object book : books) {
                bookList.append(book.toString()).append("\n");
            }
            JOptionPane.showMessageDialog(parentFrame, bookList.toString());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryApp::new);
    }
}
