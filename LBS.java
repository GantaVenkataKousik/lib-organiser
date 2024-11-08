import java.io.*;
import java.util.*;
/**
 * The Queue class is a basic implementation of a queue data structure in Java.
 */
class Queue {
    public int top;
    private int[] queue_array = new int[20];

    /**
     */
    public Queue() {
        top = -1;
    }

    /**
     * The function checks if the queue is full by comparing the top index to the maximum index value.
     * Queue size is set to 20 items (0-19).
     * 
     * @return a boolean value, which indicates whether the queue is full or not.
     */
    public boolean isFull() {
        return top == 19;
    }

    /**
     * The function checks if the top of the stack is equal to -1 and returns true if it is, indicating
     * that the stack is empty.
     * 
     * @return a boolean value, which indicates whether the stack is empty or not.
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * The enQueue function adds an element to the queue if it is not full, otherwise it displays a
     * message indicating that the queue is full.
     * 
     * @param value The parameter "value" is an integer that represents the value to be added to the
     * queue.
     */
    public void enQueue(int value) {
        if (!isFull()) {
            top++;
            queue_array[top] = value;
        } else {
            System.out.println("Queue is full!");
        }
    }

    /**
     * The deQueue function removes and returns the first element in the queue by shifting all elements
     * to the left.
     * 
     * @return The value being returned is the element that was removed from the front of the queue.
     */
    public int deQueue() {
        int value = queue_array[0];
        // Shift left
        for (int i = 0; i < top; ++i) {
            queue_array[i] = queue_array[i + 1];
        }
        top--;
        return value;
    }

    /**
     * The function clears the queue by setting the value of the variable "top" to -1.
     */
    public void reset() {
        top = -1;
    }

    /**
     * The function returns the value of the top element in the stack.
     * 
     * @return The value of the variable "top".
     */
    public int getTop() {
        return top;
    }
}

/**
 * The Book class has variables for name, author, id, amount, rack, and a reference to the next Book.
 */
class Book {
    String name;
    String author;
    int id, amount;
    int rack;
    Book next;

    /**
     * The Book constructor initializes the name, author, id, rack, next, and amount variables.
     */
    public Book(String new_name, String new_author, int new_id, int new_rack) {
        name = new_name;
        author = new_author;
        id = new_id;
        rack = new_rack;
        next = null;
        amount = 1;
    }
}

/**
 * The BST class represents a Binary Search Tree node with an additional reference to a Book instance.
 */
class BST {
    int id;
    BST left, right;
    Book next;

    /**
     * The BST constructor initializes the id, left, right, and next references.
     */
    public BST(int new_id) {
        id = new_id;
        left = null;
        right = null;
        next = null;
    }
}

/**
 * The User class has a name, a reference to a Book, and a reference to the next User.
 */
class User {
    String name;
    Book book;
    User next;

    /**
     * The User constructor initializes the name, book, and next references.
     */
    public User(String new_name) {
        name = new_name;
        next = null;
        book = null;
    }
}

public class Main {
    
    static int quantity = 0;
    
    static int[] queue_array = new int[20];
    
    static BST[][] rack = new BST[10][5];
    
    static User start = null;

    static Queue queue = new Queue();
    /**
     * The function generates a book ID by converting a string into a numerical value based on the
     * alphabetical values of its characters.
     *
     * @param str The parameter `str` is a string that represents the title of a book.
     * @return an integer value, which is the generated book ID based on the input string.
     */
    public static int generateBookID(String str) {
        queue.reset();
        int value = 0;

        // Check each character in str
        for (int i = 0; i < str.length(); ++i) {
            // If str[i] is a space
            if (str.charAt(i) == ' ') {
                queue.enQueue(value);
                value = 0;
            } else {
                value += generateAlphabetID(str.charAt(i));
            }
        }
        queue.enQueue(value);

        StringBuilder stringBuilder = new StringBuilder();

        // Add all queue values to stringBuilder
        while (!queue.isEmpty()) {
            stringBuilder.append(queue.deQueue());
        }

        // Convert the combined values to int
        try {
            value = Integer.parseInt(stringBuilder.toString());
        } catch (NumberFormatException e) {
            System.err.println("Error: Cannot parse book ID from the generated string.");
            value = 0;
        }

        return value;
    }

    /**
     * The function returns an integer value representing the alphabetical ID for a given character.
     * 
     * @param ch The character to convert to an alphabetical ID.
     * @return The alphabetical ID for the character.
     */
    public static int generateAlphabetID(char ch) {
        // Assuming 'A'/'a' = 1, 'B'/'b' = 2, ..., 'Z'/'z' = 26
        ch = Character.toUpperCase(ch);
        return (ch >= 'A' && ch <= 'Z') ? ch - 'A' + 1 : 0;
    }

    public static BST searchBST(BST root, int id) {
        while (root != null) {
            if (root.id > id) {
                root = root.left;
            } else if (root.id < id) {
                root = root.right;
            } else {
                return root; // Node found
            }
        }
        return null; // Node not found
    }

    public static void addBook(int shelf, String bookName, String authorName) {
        int id = generateBookID(bookName);
        int row = id % 10;
        Book newBook = new Book(bookName, authorName, id, row);

        if (rack[row][shelf] != null) {
            BST root = rack[row][shelf];
            BST searched = searchBST(root, id);

            if (searched == null) {
                BST newBST = new BST(id);
                newBST.next = newBook;
            } else {
                Book searchedBook = searchBook(searched, bookName, authorName);
                if (searchedBook != null) {
                    searchedBook.amount++;
                } else {
                    newBook.next = searched.next;
                    searched.next = newBook;
                }
            }
        } else {
            rack[row][shelf] = new BST(id);
            rack[row][shelf].next = newBook;
        }
    }
    public static BST createBST(int id, BST root) {
        BST current = new BST(id);
        BST p = root;
        BST k = p;

        if (p == null) {
            root = current;
        } else {
            while (p != null) {
                k = p;
                if (p.id > id)
                    p = p.left;
                else
                    p = p.right;
            }

            if (k.id > id)
                k.left = current;
            else
                k.right = current;
        }
        return current;
    }

    public static void displayLibrary(BST root) {
        if (root != null) {
            displayLibrary(root.left);

            // Displaying book linked list
            Book book = root.next;
            while (book != null) {
                System.out.printf("Book ID = %-10d Amount = %-10d Book Name = %-30s Author Name = %10s\n", book.id, book.amount,
                        book.name, book.author);
                book = book.next;
            }

            displayLibrary(root.right);
        }
    }

    public static Book searchBook(BST bst, String name, String authorName) {
    int id = generateBookID(name);
    Book book = bst.next;

    while (book != null) {
        if (book.name.equals(name) && book.author.equals(authorName)) {
            return book;
        }
        book = book.next;
    }

    return null;
    }

    public static boolean searchingSearchBook(BST bst, String name, int rack, int shelve) {
        int id = generateBookID(name);
        Book book = bst.next;
        boolean found = false;

        while (book != null) {
            if (book.name.equals(name)) {
                found = true;
                System.out.println("\nBook name: " + book.name + "       ID: " + book.id + "   Rack: " + (rack + 1)
                        + "  Shelve: " + (shelve + 1) + "  Author Name: " + book.author);
            }
            book = book.next;
        }

        return found;
    }

    public static boolean deleteBook(String name, String authorName) {
    int id = generateBookID(name);

    // For each rack
    for (int c = 0; c < 5; ++c) {
        // For shelve
        int shelve = id % 10;

        // Search BST
        BST bst = searchBST(rack[shelve][c], id);

        // If BST found
        if (bst != null) {
            // Searching book
            if (searchBookCheck(bst, name, authorName)) {
                Book book = searchBookMain(bst, name, authorName);
                Book prevBook = searchPreviousBook(bst, name, authorName);
                if (book.name.equals(name) && book.author.equals(authorName)) {
                    if (book.amount > 1) {
                        book.amount--;
                    } else {
                        if (book.next == null) {
                            bst.next = null;
                        } else {
                            bst.next = book.next;
                        }
                    }
                    return true;
                } else if (prevBook != null) {
                    Book delBook = book.next;
                    if (book.next.name.equals(name) && book.next.author.equals(authorName)) {
                        // If amount is greater than 1
                        if (book.next.amount > 1) {
                            book.next.amount--;
                            return true;
                        }
                        if (book.next.next != null) {
                            book.next = book.next.next;
                        } else {
                            book.next = null;
                        }
                        deleteBookNode(delBook);
                        return true;
                    }
                }
            }
        }
    }

        return false;
    }

    public static Book searchPreviousBook(BST root, String name, String authorName) {
        int id = generateBookID(name);
        Book book = root.next;
        Book prevBook = book;

        while (book != null) {
            if (book.name.equals(name) && book.author.equals(authorName)) {
                return prevBook;
            }
            prevBook = book;
            book = book.next;
        }

        return null;
    }

    public static void deleteBookNode(Book delBook) {
        // Assuming the deletion logic for Book nodes (freeing memory)
        delBook = null;
    }

        /**
     * The function searches for a book in a binary search tree based on its name and author name.
     * 
     * @param root The root parameter is a pointer to the root node of a binary search tree (BST).
     * @param name The name parameter is a string that represents the name of the book you are searching for.
     * @param authorName The parameter "authorName" is a string that represents the name of the author of the book.
     * 
     * @return a Book object.
     */
    public static Book searchBookMain(BST root, String name, String authorName) {
        int id = generateBookID(name);
        Book book = root.next;

        while (book != null) {
            if (book.name.equals(name) && book.author.equals(authorName)) {
                return root.next;
            }
            book = book.next;
        }
        return null;
    }

    /**
     * The function searches for a book in a binary search tree based on its name and author name.
     * 
     * @param root A pointer to the root of a binary search tree (BST) structure.
     * @param name The name parameter is a string that represents the name of the book.
     * @param authorName The parameter "authorName" is a string that represents the name of the author of the book.
     * 
     * @return a boolean value indicating whether the book exists in the BST.
     */
    public static boolean searchBookCheck(BST root, String name, String authorName) {
        int id = generateBookID(name);
        Book book = root.next;

        while (book != null) {
            if (book.name.equals(name) && book.author.equals(authorName)) {
                return true;
            }
            book = book.next;
        }
        return false;
    }

        public static void generateLibraryData() {
        BufferedReader inFile = null;

        // File path (replace with actual path)
        String filePath = ""; // Provide the correct file path

        if (filePath.isEmpty()) {
            System.err.println("Please provide file path to store and extract data");
            return;
        }

        try {
            inFile = new BufferedReader(new FileReader(filePath));

            String shelveStr;
            while ((shelveStr = inFile.readLine()) != null) {
                int shelve = Integer.parseInt(shelveStr);  // Converting shelveStr to int
                String bookName = inFile.readLine();
                String authorName = inFile.readLine();

                addBook(shelve, bookName, authorName);
            }
        } catch (IOException e) {
            System.err.println("Unable to open the file");
            e.printStackTrace();
        } finally {
            try {
                if (inFile != null) {
                    inFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        /**
     * The function `issueBook` checks if a user exists, searches for a book in the library, and if found,
     * assigns the book to the user and removes it from the library.
     * 
     * @param userName The name of the user who wants to issue the book.
     * @param bookName The name of the book that the user wants to issue.
     * @param authorName The author's name of the book that the user wants to issue.
     * 
     * @return a boolean value indicating if the book was successfully issued.
     */
    public static boolean issueBook(String userName, String bookName, String authorName) {
        boolean userExists = false;
        User u = start;
        User user1 = null;
        User user = null;

        // Check if user exists
        while (u != null) {
            if (u.name.equals(userName)) {
                userExists = true;
                break;
            }
            user1 = u;
            u = u.next;
        }

        if (!userExists) {
            // If user doesn't exist, create a new user
            user = new User(userName);

            int id = generateBookID(bookName);
            int shelve = id % 10;
            Book book = null;
            Book tempBook = null;
            BST bst = null;

            // Loop through rack for search
            for (int i = 0; i < 5; ++i) {
                bst = searchBST(rack[shelve][i], id);
                if (bst != null) {
                    book = searchBook(bst, bookName, authorName);
                    if (book != null) {
                        tempBook = new Book(bookName, authorName, id, i);
                        tempBook.author = book.author;

                        // Deleting the book from the library
                        deleteBook(bookName, authorName);

                        // Link user to the issued book
                        user.book = tempBook;
                        if (start == null) {
                            start = user;
                        } else {
                            user1.next = user;
                        }

                        return true;
                    }
                }
            }
            System.out.println("Book Not Found");
        } else {
            System.out.println("\nOne User can Issue Only One Book");
        }
        return false;
    }

       /**
     * The function `returnBook` takes a user name and a book name as input, searches for the user in a
     * linked list, checks if the user has the specified book, and if so, removes the book from the user's
     * possession and returns it to the library.
     * 
     * @param userName The username of the user who wants to return the book.
     * @param bookName The book name parameter is a string that represents the name of the book that the
     * user wants to return.
     * 
     * @return a boolean value. It returns true if the book is successfully returned by the user, and false
     * otherwise.
     */
    public static boolean returnBook(String userName, String bookName) {
        // Searching for user in list
        User user = start;
        User prevUser = start;

        while (user != null) {
            prevUser = user;
            if (user.name.equals(userName)) {
                break;
            }
            user = user.next;
        }

        // If user exists
        if (user != null) {
            // Searching for book
            Book book = user.book;

            // If book exists
            if (book != null && book.name.equals(bookName)) {
                // If there is only one book with the user
                if (user.book == book) {
                    addBook(book.rack, bookName, book.author);
                    user.book = null;
                    // Assuming Book class handles deleting itself, if needed
                    book = null;

                    // If the user is the root
                    if (user == start) {
                        if (start.next != null) {
                            start = start.next;
                        } else {
                            start = null;
                        }
                    } else {
                        prevUser.next = user.next;
                        user = null;
                    }
                    return true;
                }
            } else {
                System.out.println("\nBook not Found");
            }
        } else {
            System.out.println("User Doesn't Exist");
        }
        return false;
    }

        public static void displayIssuedBooks() {
        User user = start;
        while (user != null) {
            Book book = user.book;

            // Display Books
            while (book != null) {
                System.out.printf("Username: %-15s Book name: %-15s Author Name: %-15s\n", user.name, book.name, book.author);
                book = book.next;
            }

            user = user.next;
        }
    }

    public static void main(String[] args) {
        // generating auto library data from file
        generateLibraryData();

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("\nLibrary Management System\n\n");

        while (true) {
            System.out.println("\n\n\n++++++++++     M E N U     ++++++++++");
            System.out.println("0. Exit");
            System.out.println("1. Display All Books");
            System.out.println("2. Insert a Book");
            System.out.println("3. Delete a Book");
            System.out.println("4. Search Book By Name");
            System.out.println("5. Issue Book");
            System.out.println("6. Return Book");
            System.out.println("7. Display Issues");
            System.out.print("\nSelect an option : ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 0: System.exit(0);
                
                case 1: {
                    System.out.println("\n\n\n++++++++++     D I S P L A Y     ++++++++++");
                    for (int c = 0; c < 5; ++c) {
                        System.out.println("\n+++++   R A C K - " + (c + 1) + "   +++++");
                        for (int r = 0; r < 10; ++r) {
                            System.out.println("\n+++ S H E L V E - " + (r + 1) + " +++");
                            displayLibrary(rack[r][c]);
                        }
                    }
                }
                
                case 2: {
                    System.out.println("\n\n\n++++++++++     I N S E R T     ++++++++++");
                    System.out.println("0. Back");
                    System.out.println("1. Mathematics");
                    System.out.println("2. Computer");
                    System.out.println("3. Physics");
                    System.out.println("4. Others");
                    System.out.print("\nSelect an option : ");
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice != 0) {
                        System.out.print("\nBook Name : ");
                        String book_name = scanner.nextLine();

                        System.out.print("\nAuthor Name : ");
                        String author_name = scanner.nextLine();

                        addBook(choice - 1, book_name, author_name);
                    }
                }
                
                case 3: {
                    System.out.println("\n\n\n++++++++++     D E L E T E     ++++++++++");
                    System.out.print("\nEnter Book Name : ");
                    String name = scanner.nextLine();
                    System.out.print("\nEnter Author Name : ");
                    String author_name = scanner.nextLine();
                    boolean deleted = deleteBook(name, author_name);
                    System.out.println(deleted ? "\n\nSuccessfully Deleted" : "\n\nBook not found");
                }
                
                case 4: {
                    System.out.println("\n\n\n++++++++++     S E A R C H     ++++++++++");
                    System.out.print("\nEnter Book Name : ");
                    String name = scanner.nextLine();

                    int id = generateBookID(name);
                    boolean found = false;

                    for (int c = 0; c < 5; ++c) {
                        int shelve = id % 10;
                        BST bst = searchBST(rack[shelve][c], id);

                        if (bst != null) {
                            found = searchingSearchBook(bst, name, c, shelve);
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("\n\nBook not found");
                    }
                }
                
                case 5: {
                    System.out.println("\n\n\n++++++++++     I S S U E     ++++++++++");
                    System.out.print("\nEnter User Name : ");
                    String username = scanner.nextLine();
                    System.out.print("\nEnter Book Name : ");
                    String book_name = scanner.nextLine();
                    System.out.print("\nEnter Author Name : ");
                    String author_name = scanner.nextLine();

                    boolean issue = issueBook(username, book_name, author_name);
                    System.out.println(issue ? "\n\nBook issued successfully!\n" : "\n\nIssue failed.\n");
                }
                
                case 6: {
                    System.out.println("\n\n\n++++++++++     R E T U R N     ++++++++++");
                    System.out.print("\nEnter User Name : ");
                    String username = scanner.nextLine();
                    System.out.print("\nEnter Book Name : ");
                    String book_name = scanner.nextLine();

                    boolean issue = returnBook(username, book_name);
                    System.out.println(issue ? "\n\nBook returned successfully!\n" : "\n\nReturn failed.\n");
                }
                
                case 7: {
                    System.out.println("\n\n\n++++++++++     I S S U E D   B O O K S     ++++++++++");
                    displayIssuedBooks();
                }
                
                default: System.out.println("\nInvalid input!");
            }
        }
    }
}