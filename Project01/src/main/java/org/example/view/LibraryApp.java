package org.example.view;

import org.example.controller.ClientController;
import org.example.controller.LibrarianController;
import org.example.model.*;

import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    /**
     * runs the app and prompts the user to choose an option
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Library System!");
            System.out.println("1. Login as Client");
            System.out.println("2. Login as Librarian");
            System.out.println("3. Sign Up as Client");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> loginAsClient(scanner);
                case 2 -> loginAsLibrarian(scanner);
                case 3 -> signUpAsClient(scanner);
                case 4 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }

        System.out.println("Goodbye!");
    }

    /**
     * if you choose to log in as client this method handles it
     *
     * @param scanner
     */
    private void loginAsClient(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        Client newClient = (Client) UserFactory.createUser("client");
        ClientController clientController = new ClientController();
        clientController.setModel(newClient);


        if (clientController.login(email, password)) {
            System.out.println("Login successful! Welcome, " + clientController.getModel().getFirstName());
            handleClientActions(clientController);
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    /**
     * if you choose to log in as librarian this method handles it
     *
     * @param scanner
     */
    private void loginAsLibrarian(Scanner scanner) {
        System.out.print("Enter librarian username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Librarian newLibrarian = (Librarian) UserFactory.createUser("librarian");
        LibrarianController librarianController = new LibrarianController();
        librarianController.setModel(newLibrarian);
        if (librarianController.login(username, password)) {
            System.out.println("Login successful! Welcome, Librarian.");
            handleLibrarianActions(librarianController);
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    /**
     * if you choose to sign up as a new client this methold handles it
     *
     * @param scanner
     */
    private void signUpAsClient(Scanner scanner) {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Client newClient = (Client) UserFactory.createUser("client", firstName, lastName, email);
        ClientController clientController = new ClientController();
        clientController.signUp(newClient, password);


        if (clientController.signUp(newClient, password)) {
            System.out.println("Sign-up successful! You can now log in.");
        } else {
            System.out.println("Sign-up failed. Please try again.");
        }
    }

    /**
     * prompts the user for what action they want to do and does it
     * re-prompts the user after its completed
     *
     * @param clientController
     */
    private void handleClientActions(ClientController clientController) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nClient Menu:");
            System.out.println("1. Search Books");
            System.out.println("2. View Borrowed Books");
            System.out.println("3. View Borrowing History");
            System.out.println("4. Borrow a Book");
            System.out.println("5. Return a Book");
            System.out.println("6. Extend Borrowing Period");
            System.out.println("7. Cancel a Reservation");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> searchBooks(clientController, scanner);
                case 2 -> viewBorrowedBooks(clientController);
                case 3 -> viewBorrowingHistory(clientController);
                case 4 -> borrowBook(clientController, scanner);
                case 5 -> returnBook(clientController, scanner);
                case 6 -> extendBorrowingPeriod(clientController, scanner);
                case 7 -> cancelReservation(clientController, scanner);
                case 8 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * prompts the user for what action they want to do and does it
     * re-prompts the user after its completed
     *
     * @param librarianController
     */
    private void handleLibrarianActions(LibrarianController librarianController) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nLibrarian Menu:");
            System.out.println("1. Add a New Book");
            System.out.println("2. Remove a Book");
            System.out.println("3. View All Books");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addBook(librarianController, scanner);
                case 2 -> removeBook(librarianController, scanner);
                case 3 -> viewAllBooks(librarianController);
                case 4 -> running = false;
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /**
     * runs the searchBooks method in client controller
     *
     * @param clientController
     * @param scanner
     */
    private void searchBooks(ClientController clientController, Scanner scanner) {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine();

        List<Book> books = clientController.searchBooks(query);
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            books.forEach(book -> System.out.println(book));
        }
    }

    /**
     * runs the viewBorrowedBooks method
     *
     * @param clientController
     */
    private void viewBorrowedBooks(ClientController clientController) {
        List<Book> books = clientController.viewBorrowedBooks();
        if (books.isEmpty()) {
            System.out.println("No borrowed books.");
        } else {
            System.out.println("Your Borrowed Books:");
            books.forEach(book -> System.out.println(book));
        }
    }

    /**
     * runs the viewBorrowingHistory method from client controller
     *
     * @param clientController
     */
    private void viewBorrowingHistory(ClientController clientController) {
        List<Book> history = clientController.viewBorrowingHistory();
        if (history.isEmpty()) {
            System.out.println("No borrowing history.");
        } else {
            System.out.println("Your Borrowing History:");
            history.forEach(book -> System.out.println(book));
        }
    }

    /**
     * runs the borrow book method from the client controller
     *
     * @param clientController
     * @param scanner
     */
    private void borrowBook(ClientController clientController, Scanner scanner) {
        System.out.print("Enter the ID of the book to borrow: ");
        int bookId = scanner.nextInt();
        boolean success = clientController.borrowBook(bookId);
        if (success) {
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Failed to borrow the book. It might not be available.");
        }
    }

    /**
     * runs the returnbook method from the client controller
     *
     * @param clientController
     * @param scanner
     */
    private void returnBook(ClientController clientController, Scanner scanner) {
        System.out.print("Enter the reservation ID of the book to return: ");
        int reservationId = scanner.nextInt();
        boolean success = clientController.returnBook(reservationId);
        if (success) {
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Failed to return the book. Please check the reservation ID.");
        }
    }

    /**
     * runs the extend borrowing period from client controller
     *
     * @param clientController
     * @param scanner
     */
    private void extendBorrowingPeriod(ClientController clientController, Scanner scanner) {
        System.out.print("Enter the reservation ID of the book to extend: ");
        int reservationId = scanner.nextInt();
        System.out.print("Enter the number of extra days: ");
        int extraDays = scanner.nextInt();
        boolean success = clientController.extendBorrowingPeriod(reservationId, extraDays);
        if (success) {
            System.out.println("Borrowing period extended successfully.");
        } else {
            System.out.println("Failed to extend the borrowing period.");
        }
    }

    /**
     * runs the cancel reservation from client controller
     *
     * @param clientController
     * @param scanner
     */
    private void cancelReservation(ClientController clientController, Scanner scanner) {
        System.out.print("Enter the reservation ID to cancel: ");
        int reservationId = scanner.nextInt();
        boolean success = clientController.cancelReservation(reservationId);
        if (success) {
            System.out.println("Reservation canceled successfully.");
        } else {
            System.out.println("Failed to cancel the reservation.");
        }
    }

    /**
     * runs the addbook method from librarian controller
     *
     * @param librarianController
     * @param scanner
     */
    private void addBook(LibrarianController librarianController, Scanner scanner) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author name: ");
        String author = scanner.nextLine();
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine();
        System.out.print("Enter number of copies: ");
        int copies = scanner.nextInt();

        Book book = new Book(title, author, genre, copies);
        boolean success = librarianController.addBook(book);
        if (success) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add the book.");
        }
    }

    /**
     * runs the method remove book from librarian controller
     *
     * @param librarianController
     * @param scanner
     */
    private void removeBook(LibrarianController librarianController, Scanner scanner) {
        System.out.print("Enter the ID of the book to remove: ");
        int bookId = scanner.nextInt();
        boolean success = librarianController.removeBook(bookId);
        if (success) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Failed to remove the book. Please check the book ID.");
        }
    }

    /**
     * runs the refreshBookList method from the librarian controller
     *
     * @param librarianController
     */
    private void viewAllBooks(LibrarianController librarianController) {
        List<Book> books = librarianController.refreshBookList();
        librarianController.getLibrary().setBooks(books);
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            books.forEach(book -> System.out.println(book));
        }
    }
}
