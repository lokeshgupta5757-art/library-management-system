# Library Management System - High Level Design Plan

This document defines the first-step architecture for the Java library management project. It aims for a balanced design: practical enough for interview-style implementation, without over-engineering production concerns.

## 1. Project Goal
Build a library management system with:
- multiple independent library instances
- users who can register and borrow books
- physical and digital/audio books
- rack and floor support for physical books only
- configurable due dates that are fixed at issue time
- clear separation between model classes and business logic

## 2. Core Design Principles
- Single responsibility: model classes hold data only; business logic handles rules.
- Encapsulation: all validations and state transitions remain inside service/business classes.
- Instance isolation: each Library instance owns its own users, books, copies, floors, racks, and issue records.
- Enum-first design: use enums for fixed categories such as BookType, CopyStatus, UserStatus, and LibraryEntityType.
- Minimal but realistic design: no persistence, no frameworks, no external dependencies.

## 3. Recommended Folder Structure

```text
library-management-system/
├── README.md
├── DESIGN_PLAN.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── library/
│                   ├── app/
│                   │   └── LibraryDemo.java
│                   ├── config/
│                   │   └── LibraryConfig.java
│                   ├── model/
│                   │   └── LibraryModel.java
│                   │       # Single file containing all model classes as nested classes
│                   ├── service/
│                   │   └── LibraryBusinessLogic.java
│                   │       # Single file containing all business logic inner classes
│                   ├── exception/
│                   │   ├── LibraryException.java
│                   │   ├── UserNotFoundException.java
│                   │   ├── BookNotAvailableException.java
│                   │   └── InvalidLibraryOperationException.java
│                   └── util/
│                       └── IdGenerator.java
└── src/test/java/com/library/
    ├── LibraryBusinessLogicTest.java
    └── IntegrationTest.java
```

## 4. Model Design

The requirement states that model classes should be in one file with required classes as inner classes. The main class will be `LibraryModel` containing static nested model classes.

### 4.1 Library
Represents a library instance. Each library is independent and cannot share users, racks, books, or issue history with another library.

**Responsibilities:**
- own its list of users
- own its list of floors
- own its list of racks
- own its list of books and copies
- own its list of issue records
- maintain its own library config

**Fields:**
- `libraryId` (String) - unique identifier
- `name` (String) - library name
- `floors` (List<Floor>)
- `racks` (List<Rack>)
- `books` (Map<String, Book>) - book ID to Book
- `users` (Map<String, User>) - user ID to User
- `issueRecords` (List<IssueRecord>)
- `libraryConfig` (LibraryConfig)

---

### 4.2 User
Represents a registered library user.

**Fields:**
- `userId` (String) - unique identifier
- `name` (String)
- `email` (String)
- `status` (UserStatus) - ACTIVE, INACTIVE, BLOCKED
- `registrationDate` (LocalDate)
- `issuedCopies` (Set<String>) - set of currently issued copy IDs

**Enum: UserStatus**
```java
enum UserStatus { ACTIVE, INACTIVE, BLOCKED }
```

**Rules:**
- Only active registered users may issue a book
- A user can return an issued copy
- Status can change by admin action

---

### 4.3 Book
Represents a book title/edition. A book can have multiple copies.

**Fields:**
- `bookId` (String) - unique book identifier
- `title` (String)
- `author` (String)
- `isbn` (String)
- `category` (String)
- `bookType` (BookType) - PHYSICAL, DIGITAL, AUDIO
- `copies` (Map<String, BookCopy>) - copy ID to BookCopy

**Enum: BookType**
```java
enum BookType { PHYSICAL, DIGITAL, AUDIO }
```

**Behavior:**
- Physical books have location information (floor + rack)
- Digital/audio books do not belong to floors/racks
- Each Book object manages multiple BookCopy entities
- Book itself is just a catalog entry; actual borrowing works on copies

---

### 4.4 BookCopy
Represents an individual copy of a book. This is the item issued to a user.

**Fields:**
- `copyId` (String) - universal unique ID for this copy
- `bookId` (String) - reference to parent book
- `status` (CopyStatus) - AVAILABLE, ISSUED, LOST, DAMAGED, REMOVED
- `location` (Location) - for physical books only (can be null for digital/audio)
- `library` (Library) - which library owns this copy

**Nested Class: Location**
- `floor` (Floor)
- `rack` (Rack)

**Enum: CopyStatus**
```java
enum CopyStatus { AVAILABLE, ISSUED, LOST, DAMAGED, REMOVED }
```

**Rules:**
- Physical copies must have floor + rack + library location
- Digital/audio copies do not have floor/rack/location
- Issue operation works on copy, not book title
- Status determines if copy can be issued

---

### 4.5 Floor
Represents a floor in the library.

**Fields:**
- `floorId` (String) - unique identifier
- `floorNumber` (int)
- `library` (Library) - parent library reference
- `racks` (List<Rack>) - racks on this floor

**Rules:**
- Floor knows which racks are attached to it
- Floor is owned by a particular library instance
- Cannot be shared across libraries

---

### 4.6 Rack
Represents a rack within a library floor.

**Fields:**
- `rackId` (String) - unique identifier
- `rackNumber` (String)
- `floor` (Floor) - parent floor reference
- `library` (Library) - parent library reference
- `bookCopies` (List<String>) - list of copy IDs stored on this rack

**Rules:**
- Rack is aware of its parent floor
- Rack belongs to exactly one library
- Rack can hold multiple book copies (different books or same book)

---

### 4.7 IssueRecord
Represents the lifecycle of an issued book copy.

**Fields:**
- `issueId` (String) - unique identifier
- `userId` (String) - reference to user who issued
- `copyId` (String) - reference to copy being issued
- `issueDate` (LocalDate)
- `dueDate` (LocalDate)
- `returnDate` (LocalDate) - null if not yet returned
- `status` (IssueStatus) - ISSUED, RETURNED, OVERDUE, LOST

**Enum: IssueStatus**
```java
enum IssueStatus { ISSUED, RETURNED, OVERDUE, LOST }
```

**Rules:**
- Due date is captured when issue happens using current LibraryConfig
- If admin changes config later, existing issue records keep original due date
- Return updates status and copy availability
- Overdue status is calculated based on current date vs due date

---

### 4.8 LibraryConfig
Controls library-level settings (owned by a library instance).

**Fields:**
- `libraryId` (String) - reference to owning library
- `dueDays` (int) - default number of days for borrowing (e.g., 14)
- `maxBooksPerUser` (int) - max copies user can borrow at once (optional)

**Rules:**
- Admin may change `dueDays` at any time
- Change applies only for future issues
- Previously issued books keep their original due date
- Can be modified via AdminService

---

## 5. Business Logic Design

The requirement states that business logic will be implemented in a single class with required classes as inner classes. A single `LibraryBusinessLogic` class will contain static nested service helper classes.

### 5.1 LibraryService
Main library orchestration layer.

**Responsibilities:**
- Create library instance with initial config
- Add/remove floor to library
- Add/remove rack to floor
- Add book to library
- Add copies to existing book
- Remove copies by copy ID
- Register/deactivate users
- Issue a book copy to a user (delegates to CirculationService)
- Return a book copy (delegates to CirculationService)

**Key Methods:**
```java
Library createLibrary(String libraryId, String name, LibraryConfig config)
void addFloor(Library library, Floor floor)
void removeFloor(Library library, String floorId)
void addRack(Library library, String floorId, Rack rack)
void addBook(Library library, Book book)
void addCopyToBook(Library library, String bookId, BookCopy copy)
void removeCopy(Library library, String copyId)
void registerUser(Library library, User user)
void issueBook(Library library, String userId, String copyId)
void returnBook(Library library, String userId, String copyId)
```

---

### 5.2 UserService
Manages user lifecycle and eligibility.

**Responsibilities:**
- Register new user with validation
- Deactivate/block user
- Retrieve user details
- Check if user is eligible to issue (active + not blocked)
- Track issued copies per user

**Key Methods:**
```java
void registerUser(Library library, User user)
void deactivateUser(Library library, String userId)
User getUserById(Library library, String userId)
boolean isUserEligibleToIssue(User user)
Set<String> getIssuedCopies(Library library, String userId)
```

---

### 5.3 InventoryService
Manages books and copies.

**Responsibilities:**
- Add book to library
- Add copy of existing book
- Remove copy by copy ID
- Check if a copy is available for issue
- Locate physical copies by floor/rack
- Get book details

**Key Methods:**
```java
void addBook(Library library, Book book)
void addCopyToBook(Library library, String bookId, BookCopy copy)
void removeCopy(Library library, String copyId)
boolean isCopyAvailable(Library library, String copyId)
BookCopy getCopyById(Library library, String copyId)
Book getBookById(Library library, String bookId)
List<BookCopy> getCopiesOnRack(Library library, String rackId)
```

---

### 5.4 CirculationService
Handles issue and return workflows.

**Responsibilities:**
- Issue a copy to a user with full validation
- Validate user registration and status
- Validate copy availability and status
- Calculate due date from library config at issue time
- Create IssueRecord with proper dates
- Return a copy with validation
- Mark copy available again
- Track issue history

**Key Methods:**
```java
void issueCopy(Library library, String userId, String copyId)
void returnCopy(Library library, String userId, String copyId)
IssueRecord getIssueRecord(Library library, String issueId)
List<IssueRecord> getUserIssueHistory(Library library, String userId)
boolean isOverdue(IssueRecord issueRecord)
LocalDate calculateDueDate(LocalDate issueDate, int dueDays)
```

---

### 5.5 AdminService
Manages library configuration and administrative tasks.

**Responsibilities:**
- Update due days in library config
- Update max books per user limit
- Read current library config
- Ensure config changes do not affect active issues

**Key Methods:**
```java
void updateDueDays(Library library, int newDueDays)
void updateMaxBooksPerUser(Library library, int maxBooks)
LibraryConfig getLibraryConfig(Library library)
```

---

## 6. Business Rules to Enforce

1. **User Registration:** Only registered users can issue books.
2. **User Status:** User must be ACTIVE to borrow; INACTIVE or BLOCKED users cannot issue.
3. **Copy Availability:** A copy can be issued only if status is `AVAILABLE`.
4. **Issue by Copy:** Book issue always happens at copy level, not book title level.
5. **Physical vs Digital:** Digital and audio books do not sit on racks/floors; they have no location.
6. **Location Requirement:** Floor and rack are required for physical copies only.
7. **Due Date Capture:** Due date is calculated using the current admin config at issue time.
8. **Config Isolation:** Once an issue record is created, future config changes do not modify it.
9. **Return Workflow:** A returned copy goes back to `AVAILABLE` status.
10. **Library Isolation:** Each library instance has completely independent users, books, copies, floors, racks, and issue records.

---

## 7. Data and State Flow

### Issue Flow
1. Validate library exists
2. Validate user exists in library and is registered + ACTIVE
3. Validate copy exists in library and status is AVAILABLE
4. Check if book is digital/audio (no location needed) or physical (location required)
5. Determine due date from current `LibraryConfig.dueDays`
6. Create `IssueRecord` with today's date and calculated due date
7. Update copy status to ISSUED
8. Add copy ID to user's issued set
9. Store issue record in library's issue history

### Return Flow
1. Validate library exists
2. Validate user exists in library
3. Validate issue record exists for user + copy combination
4. Mark issue record status as RETURNED
5. Set copy status back to AVAILABLE
6. Remove copy ID from user's issued set
7. Update return date in issue record

---

## 8. Exception Handling

Create custom exceptions to handle validation failures:

```java
- LibraryException (parent exception)
  ├── UserNotFoundException
  ├── BookNotFoundException
  ├── CopyNotFoundException
  ├── BookNotAvailableException
  ├── InvalidLibraryOperationException
  └── UserNotEligibleException
```

---

## 9. Enum Usage

Use enums wherever fixed categories exist:

- **BookType**: PHYSICAL, DIGITAL, AUDIO
- **UserStatus**: ACTIVE, INACTIVE, BLOCKED
- **CopyStatus**: AVAILABLE, ISSUED, LOST, DAMAGED, REMOVED
- **IssueStatus**: ISSUED, RETURNED, OVERDUE, LOST

This keeps the model readable, supports type-safe comparisons, and prevents invalid state assignments.

---

## 10. Scope Boundaries for This Exercise

This project will **NOT** include:
- Database persistence / SQL
- REST API or Spring Boot framework
- Frontend/UI
- Multithreading or highly concurrent access
- Advanced reporting or analytics
- Production-grade validation frameworks
- Dependency injection containers

**Focus is on:**
- Clean class modeling
- Object-oriented design principles
- Relationship between classes
- Interview-style business logic implementation
- Clear separation of concerns

---

## 11. Design Review Checklist

Before proceeding to Step 2 (Model Implementation), verify:

- [ ] High-level structure is clear and balanced (not too simple, not over-engineered)
- [ ] All 12 requirements are addressed in the design
- [ ] Model classes and business logic classes are properly separated
- [ ] Enums are used for all fixed categories
- [ ] Library instance isolation is clear
- [ ] Issue and return workflows make sense
- [ ] Exception hierarchy is reasonable
- [ ] Folder structure is organized logically

---

## 12. Next Steps

1. **Step 2:** Review this plan and provide feedback or approval
2. **Step 3:** Implement all model classes in `LibraryModel.java` with nested classes
3. **Step 4:** Review model implementation
4. **Step 5:** Implement all business logic in `LibraryBusinessLogic.java` with nested service classes
5. **Step 6:** Review business logic implementation
6. **Step 7:** Write comprehensive unit tests in `LibraryBusinessLogicTest.java`
7. **Step 8:** Final review and approval

---

**Document Version:** 1.0  
**Last Updated:** 2026-10-11  
**Status:** Pending Review
