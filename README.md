# Blog Backend API

A RESTful API for a blog system built with Spring Boot, featuring JWT authentication, post management, and comment functionality with comprehensive input validation.

## 🚀 Features

- **JWT Authentication & Authorization** - Secure login and registration with JWT tokens
- **Post Management** - Full CRUD operations for blog posts using CreatePostDto for all post-related endpoints
- **Comment System** - Add and retrieve comments on posts
- **Category Management** - Organize posts by categories
- **Search Functionality** - Search posts by title and author
- **User Management** - User registration and authentication
- **Input Validation** - Comprehensive validation for all endpoints
- **DTO Pattern** - Secure data transfer with validation (now using CreatePostDto for posts)
- **Global Exception Handling** - Structured error responses
- **Refactored Post Endpoints** - All post endpoints now use CreatePostDto instead of PostDto
- **Consistent API Responses** - Unified response structure for post operations

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database operations
- **MySQL** - Database
- **JWT** - JSON Web Tokens for authentication
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management

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

## 📚 API Endpoints

### Authentication
- `POST /api/register` - Register a new user
- `POST /api/login` - Login and get JWT token

### Posts (now using CreatePostDto)
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
│   ├── UpdatePostDto.java
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
```

## 🔐 Security Features

- **JWT Token Generation** - Automatic token creation on login
- **Password Encryption** - BCrypt password hashing
- **Token Validation** - Automatic token verification for protected endpoints
- **User Authentication** - Username/password authentication
- **Role-based Access** - User roles for authorization

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

Run tests with:
```bash
mvn test
```

## 📦 Build

Build the application:
```bash
mvn clean package
```

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

Sebastian - Blog Backend Spring Boot Project
