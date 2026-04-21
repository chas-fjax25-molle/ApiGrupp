# About
ApiGrupp is a small back-end project created by the Malmö group 3. It showcases two ways of user authorization: **JWT-based login** and **GitHub OAuth2 login**.

## How it works

The application is a Spring Boot REST API with an in-memory H2 database and MockMvc tests.

### Authentication

The project supports two authentication methods. Both protect API endpoints — one via JWT token, the other via a GitHub OAuth2 session.

Two ways to obtain authentication:

**1. Username/Password Login (`POST /login`)**
- Send a JSON body with `username` and `password`
- The server responds with a signed JWT token
- Use that token as `Authorization: Bearer <token>` on protected requests

**2. GitHub OAuth2 (browser flow)**
- Redirects to GitHub for authentication
- On success, GitHub redirects back and Spring Security establishes a session

### Endpoints

| Method | Path | Access | Description |
|--------|------|--------|-------------|
| `POST` | `/login` | Public | Returns a JWT token |
| `GET` | `/public` | Public | Returns all public messages |
| `GET` | `/private` | Authenticated | Returns all messages |
| `POST` | `/messages` | Authenticated | Creates a new message |
| `GET` | `/` | Public | Serves the frontend (`index.html`) |


---

## Running locally

### Prerequisites

- Java 21+
- Maven

### Start

- Run ``` mvn spring-boot:run ```
- In browser enter: http://localhost:8080
- Try it out by using pre-seeded users or follow OAuth Setup to verify via OAuth2 on GitHub 


### JWT testing Pre-seeded users

| Username | Password |
|----------|----------|
| `admin` | `admin123` |
| `user` | `password` |
| `testuser` | `test123` |

### OAuth Setup
1. Create A GitHub OAuth App ([create one here](https://github.com/settings/developers))

2. Homepage URL: http://localhost:8080

3. Authorization callback URL: http://localhost:8080/login/oauth2/code/github

4. Create a ```.env``` file in the project root:
   ```properties
   spring.security.oauth2.client.registration.github.client-id=YOUR_CLIENT_ID
   spring.security.oauth2.client.registration.github.client-secret=YOUR_CLIENT_SECRET```


