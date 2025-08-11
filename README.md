# Blog Backend API

A RESTful API for a blog system built with Spring Boot, featuring JWT authentication, post management, and comment functionality with comprehensive input validation, extensive testing, and API documentation.

## 🚀 Features

- **JWT Authentication & Authorization** - Secure login and registration with JWT tokens
- **Post Management** - Full CRUD operations for blog posts using CreatePostDto for all post-related endpoints
- **Comment System** - Add and retrieve comments on posts
- **Category Management** - Organize posts by categories
- **Search Functionality** - Search posts by title and author
- **User Management** - User registration and authentication
- **Input Validation** - Comprehensive validation for all endpoints
- **DTO Pattern** - Secure data transfer with validation (using CreatePostDto for posts)
- **Global Exception Handling** - Structured error responses
- **Consistent API Responses** - Unified response structure for post operations
- **API Documentation** - OpenAPI/Swagger UI integration for interactive API exploration
- **Comprehensive Testing** - Unit tests, integration tests, and JWT authentication tests
- **Database Auditing** - Automatic timestamp management with @CreationTimestamp

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Database (with H2 for testing)
- **JWT** - JSON Web Tokens for authentication
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management
- **SpringDoc OpenAPI** - API documentation and Swagger UI
- **JUnit 5** - Testing framework
- **AssertJ** - Fluent assertions for testing
- **MockMvc** - Web layer testing

## 📋 Prerequisites

- Java 21 or higher
- MySQL 8.0 or higher
- Maven 3.6+

## ⚙️ Installation & Setup

### 1. Clone the repository
```bash
git clone <repository-url>
cd blog-backend-springboot
```

### 2. Database Setup
Create a MySQL database named `blog`:
```sql
CREATE DATABASE blog;
```

### 3. Configure Database
Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

### 5. Access API Documentation
Once the application is running, access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## 📚 API Endpoints

### Authentication
- `POST /api/register` - Register a new user
- `POST /api/login` - Login and get JWT token

### Posts (using CreatePostDto)
- `GET /api/posts` - Get all posts (returns List<CreatePostDto>)
- `POST /api/posts` - Create a new post (accepts CreatePostDto)
- `GET /api/posts/{id}` - Get post by ID (returns CreatePostDto)
- `PUT /api/posts/{id}` - Update a post (accepts CreatePostDto)
- `DELETE /api/posts/{id}` - Delete a post
- `GET /api/posts/search?title={title}` - Search posts by title (returns List<CreatePostDto>)
- `GET /api/posts/author/{id}` - Get posts by author ID (returns List<CreatePostDto>)

### Comments
- `POST /api/posts/{postId}/comments` - Add comment to a post
- `GET /api/posts/{postId}/comments` - Get comments for a post

## 📝 Example Usage

### Register a User
```json
POST /api/register
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123"
}
```

### Login and Get JWT Token
```json
POST /api/login
{
  "username": "johndoe",
  "password": "password123"
}
```

### Create a Post
```json
POST /api/posts
{
  "title": "My First Post",
  "content": "This is the content of my first post.",
  "authorId": 1,
  "categoryId": 1
}
```

### Update a Post
```json
PUT /api/posts/1
{
  "title": "Updated Post Title",
  "content": "This is the updated content.",
  "categoryId": 2
}
```

### Add a Comment
```json
POST /api/posts/1/comments
{
  "content": "Great post!",
  "userId": 1
}
```

## 🔐 Authentication Flow

1. **Register** a new user using `/api/register`
2. **Login** using `/api/login` to get a JWT token
3. **Include the token** in the Authorization header for protected endpoints:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

## 🏗️ Project Structure

```
src/main/java/org/sebas/blogbackendspringboot/
├── controller/          # REST controllers
│   ├── AuthController.java
│   ├── PostController.java
│   └── CommentController.java
├── service/            # Business logic
│   ├── PostService.java
│   ├── CommentService.java
│   ├── UserService.java
│   ├── JWTService.java
│   └── MyUserDetailsService.java
├── model/              # JPA entities
│   ├── User.java
│   ├── UserPrincipal.java
│   ├── Post.java
│   ├── Comment.java
│   ├── Category.java
│   └── Role.java
├── dto/                # Data Transfer Objects
│   ├── CreatePostDto.java
│   ├── CommentDto.java
│   ├── UserRegistrationDto.java
│   ├── UserLoginDto.java
│   └── CreateCommentDto.java
├── repo/               # Repository interfaces
│   ├── PostRepo.java
│   ├── CommentRepo.java
│   ├── UserRepo.java
│   └── CategoryRepo.java
├── SecurityConfig/     # Security configuration
│   ├── SecurityConfig.java
│   └── JWTFilter.java
└── exception/          # Exception handling
    └── GlobalExceptionHandler.java

src/test/java/org/sebas/blogbackendspringboot/
├── integration/        # Integration tests
│   ├── IntegrationTest.java
│   └── JWTAuthenticationIntegrationTest.java
├── controller/         # Controller unit tests
│   └── PostControllerTest.java
├── service/           # Service unit tests
│   └── PostServiceTest.java
└── TestSecurityConfig.java
```

## 🔐 Security Features

- **JWT Token Generation** - Automatic token creation on login
- **Password Encryption** - BCrypt password hashing
- **Token Validation** - Automatic token verification for protected endpoints
- **User Authentication** - Username/password authentication
- **Role-based Access** - User roles for authorization
- **Stateless Sessions** - No server-side session storage

## ✅ Validation Features

- **Input Validation** - All endpoints validate input data
- **Custom Error Messages** - Clear validation error responses
- **DTO Validation** - Separate validation for different operations
- **Global Exception Handling** - Structured error responses

## 🗄️ Database Schema

The application uses the following main entities:
- **User** - Blog users with username, email, and encrypted password
- **Post** - Blog posts with title, content, author, and category
- **Comment** - Comments on posts with content and user
- **Category** - Post categories for organization
- **Role** - User roles for authorization

## 🧪 Testing

### Test Coverage
The project includes comprehensive testing at multiple levels:

- **Unit Tests** - Service layer testing with Mockito
- **Integration Tests** - Full API endpoint testing with MockMvc
- **JWT Authentication Tests** - Complete authentication flow testing
- **Security Tests** - Protected endpoint access validation

### Running Tests
```bash
# Run all tests
mvn test
```

### Test Categories

#### Unit Tests
- **PostServiceTest** - Tests business logic for post operations
- **Controller Tests** - Tests REST endpoint behavior with mocked services

#### Integration Tests
- **IntegrationTest** - Tests complete post CRUD operations with database
- **JWTAuthenticationIntegrationTest** - Tests JWT authentication flow end-to-end

#### Test Features
- **H2 In-Memory Database** - Fast test execution
- **Test Data Setup** - Automatic test data creation and cleanup
- **Security Context** - Tests run with proper authentication context
- **Database Isolation** - Each test runs in isolated database state

## 📚 API Documentation

### OpenAPI/Swagger Integration
The project includes SpringDoc OpenAPI for automatic API documentation:

- **Interactive Documentation** - Available at `/swagger-ui.html`
- **API Specification** - OpenAPI 3.0 compliant
- **Request/Response Examples** - Built-in examples for all endpoints
- **Authentication Support** - JWT token input in Swagger UI

### Documentation Features
- **Endpoint Descriptions** - Detailed descriptions for all API operations
- **Request/Response Schemas** - Complete data model documentation
- **Validation Rules** - Documented input validation requirements
- **Error Responses** - Documented error codes and messages

## 📦 Build & Deployment

### Development
```bash
mvn clean package
```

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Sebastian - Blog Backend Spring Boot Project
