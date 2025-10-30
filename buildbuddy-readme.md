# BuildBuddy Backend

A comprehensive Spring Boot backend for a hackathon collaboration platform that connects developers and designers to form teams, discover hackathons, and manage applications.

## 🚀 Features

- ✅ **JWT Authentication** - Secure auth with email verification
- ✅ **User Management** - Profile CRUD with resume uploads via Cloudinary
- ✅ **Team Formation** - Create and discover teams with advanced filtering
- ✅ **Hackathon Discovery** - Browse and filter hackathons from multiple platforms
- ✅ **Application System** - Apply to teams and manage applications
- ✅ **Favorites** - Save favorite hackathons
- ✅ **Role-Based Access** - USER and ADMIN roles
- ✅ **API Documentation** - Interactive Swagger/OpenAPI docs
- ✅ **Docker Support** - Easy deployment with Docker Compose

## 🛠️ Tech Stack

- **Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Build Tool:** Maven
- **Database:** MySQL 8.0
- **ORM:** Spring Data JPA (Hibernate)
- **Security:** Spring Security + JWT
- **Cloud Storage:** Cloudinary
- **Email:** Spring Mail
- **API Docs:** SpringDoc OpenAPI 3

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- MySQL 8.0+
- Cloudinary account (for file uploads)
- SMTP server (Gmail recommended)

## 🔧 Installation & Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd buildbuddy-backend
```

### 2. Create MySQL Database

```sql
CREATE DATABASE buildbuddy_db;
```

### 3. Configure Environment Variables

Create a `.env` file in the root directory:

```env
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret_key_minimum_256_bits
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

### 4. Update application.yml

The `application.yml` file is already configured to use environment variables from `.env`.

### 5. Build the project

```bash
mvn clean install
```

### 6. Run the application

```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`

## 🐳 Docker Setup

### Using Docker Compose

```bash
# Build and start containers
docker-compose up -d

# View logs
docker-compose logs -f

# Stop containers
docker-compose down
```

This starts both MySQL and the Spring Boot application.

## 📚 API Documentation

Once running, access the interactive API documentation:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs (JSON):** http://localhost:8080/api-docs

## 📡 API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login
- `GET /api/auth/verify?token={token}` - Verify email
- `POST /api/auth/refresh` - Refresh JWT token

### Users
- `GET /api/users/{id}` - Get user profile
- `PUT /api/users/{id}` - Update user profile
- `POST /api/users/upload-resume` - Upload resume

### Teams
- `POST /api/teams` - Create team post
- `GET /api/teams` - Get all teams (with filters)
- `GET /api/teams/{id}` - Get team by ID
- `PUT /api/teams/{id}` - Update team
- `DELETE /api/teams/{id}` - Delete team

### Hackathons
- `GET /api/hackathons` - Get all hackathons
- `POST /api/hackathons/sync` - Sync hackathons (Admin only)

### Applications
- `POST /api/applications` - Apply to a team
- `GET /api/applications/team/{id}` - Get team applicants
- `PUT /api/applications/{id}` - Update application status
- `GET /api/applications/user/{id}` - Get user applications

### Favorites
- `POST /api/favorites/{hackathonId}` - Add to favorites
- `DELETE /api/favorites/{hackathonId}` - Remove from favorites
- `GET /api/favorites` - Get user favorites

## 📁 Project Structure

```
com.buildbuddy/
├── auth/                   # Authentication & JWT
│   ├── controller/
│   ├── dto/
│   └── service/
├── user/                   # User management
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── team/                   # Team formation
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── hackathon/              # Hackathon listings
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── application/            # Application workflow
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── favorite/               # Favorites system
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── common/                 # Shared utilities
│   ├── dto/               # API responses
│   ├── entity/            # Base entity
│   └── exception/         # Custom exceptions
└── config/                 # Configuration
    ├── SecurityConfig
    ├── JwtAuthenticationFilter
    ├── CloudinaryConfig
    ├── CorsConfig
    └── OpenApiConfig
```

## 🗄️ Database Schema

### Core Tables
- **users** - User accounts and profiles
- **hackathons** - Hackathon listings
- **team_posts** - Team formation posts
- **applications** - Team join applications
- **favorites** - User favorite hackathons
- **user_skills** - User skill tags
- **team_required_skills** - Team skill requirements

## 🔐 Security Features

- **Password Encryption** - BCrypt hashing
- **JWT Authentication** - Stateless token-based auth
- **Email Verification** - Required for new accounts
- **Role-Based Access** - USER and ADMIN roles
- **Protected Endpoints** - Using `@PreAuthorize`
- **CORS Configuration** - Configured for React frontend

## 📧 Email Configuration (Gmail)

1. Enable 2-Factor Authentication in your Google account
2. Generate an App Password:
   - Go to Google Account Settings
   - Security → 2-Step Verification → App passwords
   - Generate password for "Mail"
3. Use the App Password in `MAIL_PASSWORD` environment variable

## ☁️ Cloudinary Setup

1. Create account at https://cloudinary.com
2. Get credentials from dashboard
3. Add to environment variables:
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`

## 🧪 Testing

Run tests:

```bash
mvn test
```

## 🎯 Sample Credentials

After running with seed data:

**User Account:**
- Email: `john@example.com`
- Password: `password123`

**Admin Account:**
- Email: `admin@buildbuddy.com`
- Password: `password123`

## 🐛 Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Verify credentials in `.env`
- Check database exists: `buildbuddy_db`

### Email Not Sending
- Verify SMTP credentials
- Ensure App Password is configured (for Gmail)
- Check firewall settings

### JWT Token Issues
- Ensure `JWT_SECRET` is at least 256 bits
- Check token expiration settings in `application.yml`

## 📝 API Request Examples

### Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Create Team (with auth token)

```bash
curl -X POST http://localhost:8080/api/teams \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Looking for Backend Developer",
    "description": "Building a social media platform...",
    "hackathonName": "HackMIT 2025",
    "requiredSkills": ["Java", "Spring Boot", "MySQL"],
    "experienceLevel": "INTERMEDIATE",
    "mode": "ONLINE",
    "memberLimit": 4
  }'
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For issues and questions:
- Open an issue on GitHub
- Email: support@buildbuddy.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- All contributors and supporters

---

**Built with ❤️ using Spring Boot**