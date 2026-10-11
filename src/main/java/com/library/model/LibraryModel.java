package com.library.model;

import java.time.LocalDate;
import java.util.*;

/**
 * LibraryModel - Single file containing all model classes as static nested classes.
 * This maintains clean separation of concerns and keeps the model layer focused on data representation.
 */
public class LibraryModel {

    /**
     * Enum: BookType
     * Represents the type of book in the library.
     */
    public enum BookType {
        PHYSICAL, DIGITAL, AUDIO
    }

    /**
     * Enum: UserStatus
     * Represents the status of a library user.
     */
    public enum UserStatus {
        ACTIVE, INACTIVE, BLOCKED
    }

    /**
     * Enum: CopyStatus
     * Represents the status of a specific book copy.
     */
    public enum CopyStatus {
        AVAILABLE, ISSUED, LOST, DAMAGED, REMOVED
    }

    /**
     * Enum: IssueStatus
     * Represents the status of an issue record.
     */
    public enum IssueStatus {
        ISSUED, RETURNED, OVERDUE, LOST
    }

    /**
     * Library - Represents a library instance.
     * Each library is independent and cannot share users, books, or issue records with another library.
     */
    public static class Library {
        private String libraryId;
        private String name;
        private List<Floor> floors;
        private List<Rack> racks;
        private Map<String, Book> books;
        private Map<String, User> users;
        private List<IssueRecord> issueRecords;
        private LibraryConfig libraryConfig;

        public Library(String libraryId, String name, LibraryConfig libraryConfig) {
            this.libraryId = libraryId;
            this.name = name;
            this.libraryConfig = libraryConfig;
            this.floors = new ArrayList<>();
            this.racks = new ArrayList<>();
            this.books = new HashMap<>();
            this.users = new HashMap<>();
            this.issueRecords = new ArrayList<>();
        }

        // Getters and Setters
        public String getLibraryId() { return libraryId; }
        public void setLibraryId(String libraryId) { this.libraryId = libraryId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<Floor> getFloors() { return floors; }
        public void setFloors(List<Floor> floors) { this.floors = floors; }

        public List<Rack> getRacks() { return racks; }
        public void setRacks(List<Rack> racks) { this.racks = racks; }

        public Map<String, Book> getBooks() { return books; }
        public void setBooks(Map<String, Book> books) { this.books = books; }

        public Map<String, User> getUsers() { return users; }
        public void setUsers(Map<String, User> users) { this.users = users; }

        public List<IssueRecord> getIssueRecords() { return issueRecords; }
        public void setIssueRecords(List<IssueRecord> issueRecords) { this.issueRecords = issueRecords; }

        public LibraryConfig getLibraryConfig() { return libraryConfig; }
        public void setLibraryConfig(LibraryConfig libraryConfig) { this.libraryConfig = libraryConfig; }

        @Override
        public String toString() {
            return "Library{" +
                    "libraryId='" + libraryId + '\'' +
                    ", name='" + name + '\'' +
                    ", floorsCount=" + floors.size() +
                    ", racksCount=" + racks.size() +
                    ", booksCount=" + books.size() +
                    ", usersCount=" + users.size() +
                    ", issueRecordsCount=" + issueRecords.size() +
                    '}';
        }
    }

    /**
     * User - Represents a registered library user.
     */
    public static class User {
        private String userId;
        private String name;
        private String email;
        private UserStatus status;
        private LocalDate registrationDate;
        private Set<String> issuedCopies;

        public User(String userId, String name, String email) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.status = UserStatus.ACTIVE;
            this.registrationDate = LocalDate.now();
            this.issuedCopies = new HashSet<>();
        }

        // Getters and Setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public UserStatus getStatus() { return status; }
        public void setStatus(UserStatus status) { this.status = status; }

        public LocalDate getRegistrationDate() { return registrationDate; }
        public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

        public Set<String> getIssuedCopies() { return issuedCopies; }
        public void setIssuedCopies(Set<String> issuedCopies) { this.issuedCopies = issuedCopies; }

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", name='" + name + '\'' +
                    ", email='" + email + '\'' +
                    ", status=" + status +
                    ", registrationDate=" + registrationDate +
                    ", issuedCopies=" + issuedCopies.size() +
                    '}';
        }
    }

    /**
     * Book - Represents a book title/edition with multiple copies.
     */
    public static class Book {
        private String bookId;
        private String title;
        private String author;
        private String isbn;
        private String category;
        private BookType bookType;
        private Integer publicationYear;
        private String publisher;
        private Map<String, BookCopy> copies;

        public Book(String bookId, String title, String author, String isbn, BookType bookType) {
            this.bookId = bookId;
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.bookType = bookType;
            this.copies = new HashMap<>();
        }

        // Getters and Setters
        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }

        public BookType getBookType() { return bookType; }
        public void setBookType(BookType bookType) { this.bookType = bookType; }

        public Integer getPublicationYear() { return publicationYear; }
        public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

        public String getPublisher() { return publisher; }
        public void setPublisher(String publisher) { this.publisher = publisher; }

        public Map<String, BookCopy> getCopies() { return copies; }
        public void setCopies(Map<String, BookCopy> copies) { this.copies = copies; }

        @Override
        public String toString() {
            return "Book{" +
                    "bookId='" + bookId + '\'' +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", bookType=" + bookType +
                    ", copiesCount=" + copies.size() +
                    '}';
        }
    }

    /**
     * BookCopy - Represents an individual copy of a book.
     */
    public static class BookCopy {
        private String copyId;
        private String bookId;
        private CopyStatus status;
        private Location location;
        private Library library;
        private LocalDate acquiredDate;

        public BookCopy(String copyId, String bookId, CopyStatus status, Library library) {
            this.copyId = copyId;
            this.bookId = bookId;
            this.status = status;
            this.library = library;
            this.acquiredDate = LocalDate.now();
            this.location = null;
        }

        // Getters and Setters
        public String getCopyId() { return copyId; }
        public void setCopyId(String copyId) { this.copyId = copyId; }

        public String getBookId() { return bookId; }
        public void setBookId(String bookId) { this.bookId = bookId; }

        public CopyStatus getStatus() { return status; }
        public void setStatus(CopyStatus status) { this.status = status; }

        public Location getLocation() { return location; }
        public void setLocation(Location location) { this.location = location; }

        public Library getLibrary() { return library; }
        public void setLibrary(Library library) { this.library = library; }

        public LocalDate getAcquiredDate() { return acquiredDate; }
        public void setAcquiredDate(LocalDate acquiredDate) { this.acquiredDate = acquiredDate; }

        @Override
        public String toString() {
            return "BookCopy{" +
                    "copyId='" + copyId + '\'' +
                    ", bookId='" + bookId + '\'' +
                    ", status=" + status +
                    ", hasLocation=" + (location != null) +
                    ", acquiredDate=" + acquiredDate +
                    '}';
        }
    }

    /**
     * Location - Represents the physical location of a book copy.
     * Used only for PHYSICAL books.
     */
    public static class Location {
        private Floor floor;
        private Rack rack;

        public Location(Floor floor, Rack rack) {
            this.floor = floor;
            this.rack = rack;
        }

        public Floor getFloor() { return floor; }
        public void setFloor(Floor floor) { this.floor = floor; }

        public Rack getRack() { return rack; }
        public void setRack(Rack rack) { this.rack = rack; }

        @Override
        public String toString() {
            return "Location{" +
                    "floor=" + (floor != null ? floor.getFloorNumber() : "null") +
                    ", rack=" + (rack != null ? rack.getRackNumber() : "null") +
                    '}';
        }
    }

    /**
     * Floor - Represents a floor in the library.
     */
    public static class Floor {
        private String floorId;
        private int floorNumber;
        private Library library;
        private List<Rack> racks;

        public Floor(String floorId, int floorNumber, Library library) {
            this.floorId = floorId;
            this.floorNumber = floorNumber;
            this.library = library;
            this.racks = new ArrayList<>();
        }

        public String getFloorId() { return floorId; }
        public void setFloorId(String floorId) { this.floorId = floorId; }

        public int getFloorNumber() { return floorNumber; }
        public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

        public Library getLibrary() { return library; }
        public void setLibrary(Library library) { this.library = library; }

        public List<Rack> getRacks() { return racks; }
        public void setRacks(List<Rack> racks) { this.racks = racks; }

        @Override
        public String toString() {
            return "Floor{" +
                    "floorId='" + floorId + '\'' +
                    ", floorNumber=" + floorNumber +
                    ", racksCount=" + racks.size() +
                    '}';
        }
    }

    /**
     * Rack - Represents a rack on a floor.
     */
    public static class Rack {
        private String rackId;
        private String rackNumber;
        private Floor floor;
        private Library library;
        private List<String> bookCopyIds;

        public Rack(String rackId, String rackNumber, Floor floor, Library library) {
            this.rackId = rackId;
            this.rackNumber = rackNumber;
            this.floor = floor;
            this.library = library;
            this.bookCopyIds = new ArrayList<>();
        }

        public String getRackId() { return rackId; }
        public void setRackId(String rackId) { this.rackId = rackId; }

        public String getRackNumber() { return rackNumber; }
        public void setRackNumber(String rackNumber) { this.rackNumber = rackNumber; }

        public Floor getFloor() { return floor; }
        public void setFloor(Floor floor) { this.floor = floor; }

        public Library getLibrary() { return library; }
        public void setLibrary(Library library) { this.library = library; }

        public List<String> getBookCopyIds() { return bookCopyIds; }
        public void setBookCopyIds(List<String> bookCopyIds) { this.bookCopyIds = bookCopyIds; }

        @Override
        public String toString() {
            return "Rack{" +
                    "rackId='" + rackId + '\'' +
                    ", rackNumber='" + rackNumber + '\'' +
                    ", copiesCount=" + bookCopyIds.size() +
                    '}';
        }
    }

    /**
     * IssueRecord - Represents the lifecycle of an issued book copy.
     */
    public static class IssueRecord {
        private String issueId;
        private String userId;
        private String copyId;
        private LocalDate issueDate;
        private LocalDate dueDate;
        private LocalDate returnDate;
        private IssueStatus status;

        public IssueRecord(String issueId, String userId, String copyId, LocalDate issueDate, LocalDate dueDate) {
            this.issueId = issueId;
            this.userId = userId;
            this.copyId = copyId;
            this.issueDate = issueDate;
            this.dueDate = dueDate;
            this.returnDate = null;
            this.status = IssueStatus.ISSUED;
        }

        public String getIssueId() { return issueId; }
        public void setIssueId(String issueId) { this.issueId = issueId; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getCopyId() { return copyId; }
        public void setCopyId(String copyId) { this.copyId = copyId; }

        public LocalDate getIssueDate() { return issueDate; }
        public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

        public LocalDate getDueDate() { return dueDate; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

        public LocalDate getReturnDate() { return returnDate; }
        public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

        public IssueStatus getStatus() { return status; }
        public void setStatus(IssueStatus status) { this.status = status; }

        @Override
        public String toString() {
            return "IssueRecord{" +
                    "issueId='" + issueId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", copyId='" + copyId + '\'' +
                    ", issueDate=" + issueDate +
                    ", dueDate=" + dueDate +
                    ", returnDate=" + returnDate +
                    ", status=" + status +
                    '}';
        }
    }

    /**
     * LibraryConfig - Controls library-level settings.
     */
    public static class LibraryConfig {
        private String libraryId;
        private int dueDays;
        private int maxBooksPerUser;

        public LibraryConfig(String libraryId, int dueDays, int maxBooksPerUser) {
            this.libraryId = libraryId;
            this.dueDays = dueDays;
            this.maxBooksPerUser = maxBooksPerUser;
        }

        public String getLibraryId() { return libraryId; }
        public void setLibraryId(String libraryId) { this.libraryId = libraryId; }

        public int getDueDays() { return dueDays; }
        public void setDueDays(int dueDays) { this.dueDays = dueDays; }

        public int getMaxBooksPerUser() { return maxBooksPerUser; }
        public void setMaxBooksPerUser(int maxBooksPerUser) { this.maxBooksPerUser = maxBooksPerUser; }

        @Override
        public String toString() {
            return "LibraryConfig{" +
                    "libraryId='" + libraryId + '\'' +
                    ", dueDays=" + dueDays +
                    ", maxBooksPerUser=" + maxBooksPerUser +
                    '}';
        }
    }

    /**
     * BookSearchCriteria - Encapsulates search parameters for multi-criteria book search.
     */
    public static class BookSearchCriteria {
        private String title;
        private String author;
        private String isbn;
        private String category;
        private Integer year;
        private BookType bookType;

        public BookSearchCriteria() {
        }

        public BookSearchCriteria title(String title) {
            this.title = title;
            return this;
        }

        public BookSearchCriteria author(String author) {
            this.author = author;
            return this;
        }

        public BookSearchCriteria isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public BookSearchCriteria category(String category) {
            this.category = category;
            return this;
        }

        public BookSearchCriteria year(Integer year) {
            this.year = year;
            return this;
        }

        public BookSearchCriteria bookType(BookType bookType) {
            this.bookType = bookType;
            return this;
        }

        // Getters
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getIsbn() { return isbn; }
        public String getCategory() { return category; }
        public Integer getYear() { return year; }
        public BookType getBookType() { return bookType; }

        public boolean isValidSearch() {
            return title != null || author != null || isbn != null || category != null || year != null || bookType != null;
        }

        @Override
        public String toString() {
            return "BookSearchCriteria{" +
                    "title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", isbn='" + isbn + '\'' +
                    ", category='" + category + '\'' +
                    ", year=" + year +
                    ", bookType=" + bookType +
                    '}';
        }
    }
}
