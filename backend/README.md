# Slate — Smart Classroom Management (Backend)

Core backend for the Slate smart classroom system: **Students, Classes, Attendance**, secured with **JWT** and **role-based access** (`ADMIN`, `TEACHER`, `STUDENT`).

## Stack
- Java 17, Spring Boot 3.3
- Spring Web, Spring Data JPA, Spring Security
- MySQL
- JWT (jjwt 0.12.5)
- Lombok

## 1. Setup

Create the database (or let it auto-create — see `application.properties`):

```sql
CREATE DATABASE slate_classroom;
```

Edit `src/main/resources/application.properties` with your MySQL credentials:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

Change `app.jwt.secret` before deploying anywhere real — never commit real secrets.

## 2. Run

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`. Tables are created automatically (`ddl-auto=update`).

## 3. Auth flow

Every endpoint except `/api/auth/**` requires a `Bearer` token.

**Register** (first user should be an ADMIN):
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Admin User","email":"admin@slate.com","password":"admin123","role":"ADMIN"}'
```

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@slate.com","password":"admin123"}'
```

Response includes a `token` — send it as `Authorization: Bearer <token>` on every other request.

## 4. Roles

| Action | ADMIN | TEACHER | STUDENT |
|---|---|---|---|
| Create/update/delete classes | ✅ | ✅ (update only) | ❌ |
| Delete a class | ✅ | ❌ | ❌ |
| Create/update/delete students | ✅ | ✅ | ❌ |
| Mark attendance | ✅ | ✅ | ❌ |
| View classes/students/attendance | ✅ | ✅ | ✅ |

## 5. Endpoints

### Classes
- `POST /api/classes` — create a class
- `GET /api/classes` — list all classes
- `GET /api/classes/{id}` — get one class
- `PUT /api/classes/{id}` — update
- `DELETE /api/classes/{id}` — delete (ADMIN only)

### Students
- `POST /api/students` — create a student
- `GET /api/students` — list all students
- `GET /api/students?classId=1` — list students in a class
- `GET /api/students/{id}` — get one student
- `PUT /api/students/{id}` — update
- `DELETE /api/students/{id}` — delete

### Attendance
- `POST /api/attendance/mark` — bulk mark attendance for a class on a date
  ```json
  {
    "classId": 1,
    "date": "2026-07-11",
    "records": [
      { "studentId": 1, "status": "PRESENT" },
      { "studentId": 2, "status": "ABSENT", "remarks": "informed sick leave" }
    ]
  }
  ```
  Re-posting for the same student + date **updates** the existing record instead of duplicating it.
- `GET /api/attendance/class/{classId}?date=2026-07-11` — attendance for a class on a date
- `GET /api/attendance/student/{studentId}` — full attendance history for a student
- `GET /api/attendance/student/{studentId}/summary` — present count, total days, attendance %

## 6. Example flow

1. Register an ADMIN, log in, save the token.
2. `POST /api/classes` → `{"name":"10","section":"B","academicYear":"2026-27"}`
3. `POST /api/students` → `{"fullName":"Rohan Iyer","rollNumber":"10B-01","classId":1}`
4. `POST /api/attendance/mark` for that class and today's date.
5. `GET /api/attendance/student/1/summary` → attendance percentage.

## 7. Next modules (not in this build)
- Engagement tracking
- AI-based learning analytics / insights
- Lesson & content management

These map directly onto the dashboard demo already built on the frontend (`Engagement`, `AI analytics`, `Lessons` tabs) and can be added as new entities + services following the same pattern used here.
