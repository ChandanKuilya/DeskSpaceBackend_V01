
The following diagram illustrates the interaction between the User, Client (Frontend), and the Backend layers (Controller, Service, Repository, Database).

```mermaid
graph TD
    %% Actors and Client
    User[User]
    Client[Frontend Client<br>Vanilla JS / Browser]
    
    %% Backend Boundary
    subgraph Backend [Backend Server - Spring Boot]
        AuthCtrl[AuthController]
        WSCtrl[WorkSpaceController]
        BookCtrl[BookingController]
        
        Security[Security Filter Chain<br>JWT & Basic Auth]
        
        AuthSvc[AuthService]
        WSSvc[WorkSpaceService]
        BookSvc[BookingService]
        
        RepoUser[UserRepository]
        RepoWS[WorkSpaceRepository]
        RepoBook[BookingRepository]
    end
    
    %% Database Boundary
    subgraph Data [Data Persistence]
        DB[(MySQL Database)]
    end
    
    %% Relationships
    User -->|Interacts| Client
    
    %% Frontend to Backend (API Calls)
    Client -->|POST /auth/login| Security
    Client -->|GET /workspaces| Security
    Client -->|POST /bookings| Security
    
    %% Security Layer Routing
    Security -->|Public Access| AuthCtrl
    Security -->|Authenticated| WSCtrl
    Security -->|Authenticated| BookCtrl
    
    %% Controllers to Services
    AuthCtrl -->|Login/Register| AuthSvc
    WSCtrl -->|CRUD Operations| WSSvc
    BookCtrl -->|Create/View| BookSvc
    
    %% Services to Repositories
    AuthSvc -->|Find/Save User| RepoUser
    WSSvc -->|Fetch Workspaces| RepoWS
    BookSvc -->|Save Booking| RepoBook
    BookSvc -->|Fetch User| RepoUser
    BookSvc -->|Fetch Workspace| RepoWS
    
    %% Repositories to Database
    RepoUser -->|SQL| DB
    RepoWS -->|SQL| DB
    RepoBook -->|SQL| DB
```



<img width="1919" height="1018" alt="image" src="https://github.com/user-attachments/assets/523ec126-e000-4965-bc9e-8d4ca42ba1dd" />

<img width="1823" height="627" alt="image" src="https://github.com/user-attachments/assets/40742647-459b-4b02-af46-4216e68fe992" />

<img width="1919" height="1044" alt="image" src="https://github.com/user-attachments/assets/041953ac-d18f-4ec4-9cc6-7a220d0cd056" />

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/4e8b5c48-0c2a-4dcc-8800-e3cc7443578a" />

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/46faad96-93df-447c-b5a9-a4c6113544c9" />

<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/2e2283d3-6259-4b74-9406-1322602505cc" />

<img width="1827" height="894" alt="image" src="https://github.com/user-attachments/assets/252f5625-7c56-4a4b-959a-7cd6d11cfe7f" />




## End-to-End Program Flow:

### 1.1 User Authentication Flow
The entry point for all users. The application is secured, requiring a valid JWT for any operation beyond login/registration.

1.  **Registration**:
    -   **Frontend**: User fills `First Name`, `Last Name`, `Email`, `Password`, `Role` on `#register-form`.
    -   **Action**: `POST /api/auth/register` with JSON payload.
    -   **Backend**: [AuthController](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/controllers/AuthController.java#12-33) receives request -> [AuthService](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/services/AuthService.java#13-63) checks for existing email -> Hashes password (BCrypt) -> Saves `User` entity to DB.
    -   **Response**: 200 OK with success message.

2.  **Login**:
    -   **Frontend**: User enters `Email`, `Password`.
    -   **Action**: `POST /api/auth/login`.
    -   **Backend**: `AuthenticationManager` validates credentials -> `JwtService` creates a signed JWT token.
    -   **Response**: 200 OK with `{ token: "..." }`.
    -   **Frontend**: Stores token in `localStorage` -> Redirects to Dashboard.

### 1.2 Workspace Browsing Flow
Once logged in, the user encounters the main dashboard.

1.  **Dashboard Load**:
    -   **Frontend**: [showDashboard()](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/deskSpace-FrontEnd/app.js#157-164) triggers [fetchWorkspaces()](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/deskSpace-FrontEnd/app.js#165-185).
    -   **Action**: `GET /api/workspaces` (Authorization: Bearer Token).
    -   **Backend**: [WorkSpaceController](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/controllers/WorkSpaceController.java#12-38) calls `WorkSpaceService` -> Fetches all [WorkSpace](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/controllers/WorkSpaceController.java#12-38) entities -> Returns DTO list.
    -   **Frontend**: Maps the JSON array to HTML cards displaying Name, Capacity, Price, and Amenities.

### 1.3 Booking Flow
The core business logic.

1.  **Initiation**: User clicks "Book Now" on a workspace card -> Opens Modal.
2.  **Submission**:
    -   **Frontend**: User selects Start/End time. Validator checks `Start < End`.
    -   **Action**: `POST /api/bookings` with `{ workspaceId, startTime, endTime }`.
    -   **Backend**: 
        -   [SecurityConfig](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/config/SecurityConfig.java#23-125) extracts User Email from JWT.
        -   [BookingController](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/controllers/BookingController.java#14-38) passes user email + request to `BookingService`.
        -   Service creates [Booking](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/DeskSpace/src/main/java/com/ck/deskspace/controllers/BookingController.java#21-30) entity associated with User and Workspace.
    -   **Response**: 201 Created.
    -   **Frontend**: Alerts success -> Closes Modal -> Refreshes "My Bookings".

### 1.4 User History Flow
1.  **View History**: User clicks "My Bookings" tab.
2.  **Action**: `GET /api/bookings/my-bookings`.
3.  **Backend**: Uses the JWT subject (email) to find bookings for that specific user only.
4.  **Frontend**: Renders list showing Workspace Name, Dates, and Status (CONFIRMED).

---

## 2. High-Level Design (HLD) Diagram

The following diagram illustrates the interaction between the User, Client (Frontend), and the Backend layers (Controller, Service, Repository, Database).

```mermaid
graph TD
    %% Actors and Client
    User[User]
    Client[Frontend Client<br>Vanilla JS / Browser]
    
    %% Backend Boundary
    subgraph Backend [Backend Server - Spring Boot]
        AuthCtrl[AuthController]
        WSCtrl[WorkSpaceController]
        BookCtrl[BookingController]
        
        Security[Security Filter Chain<br>JWT & Basic Auth]
        
        AuthSvc[AuthService]
        WSSvc[WorkSpaceService]
        BookSvc[BookingService]
        
        RepoUser[UserRepository]
        RepoWS[WorkSpaceRepository]
        RepoBook[BookingRepository]
    end
    
    %% Database Boundary
    subgraph Data [Data Persistence]
        DB[(MySQL Database)]
    end
    
    %% Relationships
    User -->|Interacts| Client
    
    %% Frontend to Backend (API Calls)
    Client -->|POST /auth/login| Security
    Client -->|GET /workspaces| Security
    Client -->|POST /bookings| Security
    
    %% Security Layer Routing
    Security -->|Public Access| AuthCtrl
    Security -->|Authenticated| WSCtrl
    Security -->|Authenticated| BookCtrl
    
    %% Controllers to Services
    AuthCtrl -->|Login/Register| AuthSvc
    WSCtrl -->|CRUD Operations| WSSvc
    BookCtrl -->|Create/View| BookSvc
    
    %% Services to Repositories
    AuthSvc -->|Find/Save User| RepoUser
    WSSvc -->|Fetch Workspaces| RepoWS
    BookSvc -->|Save Booking| RepoBook
    BookSvc -->|Fetch User| RepoUser
    BookSvc -->|Fetch Workspace| RepoWS
    
    %% Repositories to Database
    RepoUser -->|SQL| DB
    RepoWS -->|SQL| DB
    RepoBook -->|SQL| DB
```

## 3. Technical Stack Breakdown

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Frontend** | HTML5, CSS3, Vanilla JS | Lightweight, no frameworks. Uses [fetch](file:///c:/Users/CHANDAN%20KUILYA/Music/FOLDER_DISC/NOW%20Phase%20Dev/Java_Backend/IntelliJ%20Ultimate/WorkSpace06/deskSpace-FrontEnd/app.js#186-215) API. |
| **Backend Framework** | Spring Boot | MVC Architecture, Dependency Injection. |
| **Language** | Java 21 | Modern Java features. |
| **Security** | Spring Security + JWT | Stateless authentication. |
| **Database Access** | Spring Data JPA | interface-based repositories (Hibernate). |
| **Metrics** | Actuator + Micrometer | Prometheus-ready metrics exposure. |
