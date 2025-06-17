
# Book Library Management System

The **Book Library Management System** is a web application designed to manage and display book details in a dynamic and user-friendly interface. The project combines **Java Servlets**,and **MySQL** to provide a robust library management solution.

## Features

- **Dynamic Book Management**: Displays book details such as `ID`, `Name`, `ISBN`, `Author`, `Publication`, `Quantity`, `Available Quantity`, `Price`, and `Image`.
- **Interactive Interface**: Responsive and visually appealing frontend with tables and forms.
- **Image Support**: Dynamically fetches and displays book cover images from database paths.
- **Sorting and Filtering**: Supports options for filtering book data based on various parameters.
- **Database Integration**: Retrieves data dynamically from a MySQL database.

## Technologies Used

### Backend:
- Java (Servlets)
- MySQL for data storage

### Frontend:
- HTML5, CSS3
- JavaScript for dynamic interactions

### Build Tool:
- Gradle

### Server:
- Apache Tomcat

---

## Prerequisites

To run the project locally, ensure you have:

1. **Java Development Kit (JDK)**: Version 8 or above.
2. **Apache Tomcat**: Configured server for deployment.
3. **MySQL Database**:
   - Create a `books` table in your database with appropriate columns (`id`, `name`, `isbn`, `author`, `publication`, `quantity`, `available_quantity`, `price`, `image`).
   - Insert sample data for testing purposes.
4. **Gradle**: For dependency management and building the project.

---

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/book-library-management.git
   ```

2. Open the project in your IDE (e.g., Eclipse, IntelliJ).

3. Configure the database connection in your code (e.g., `DBConnection.java`):
   ```java
   String url = "jdbc:mysql://localhost:3306/your-database-name";
   String username = "your-username";
   String password = "your-password";
   ```

4. Build the project using Gradle:
   ```bash
   gradle build
   ```

5. Deploy the `.war` file to Apache Tomcat.

6. Start the Tomcat server and navigate to:
   ```
   http://localhost:8080/your-project-name
   ```

---

## Output Screenshot

Here’s an example of the application interface displaying book details:

![Book Library Output](book_details.png)

---

## Project Directory Structure

```
book-library-management/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── servlets/   # Backend logic (Servlets)
│   │   ├── webapp/
│   │   │   ├── images/     # Backgrounds and icons
│   │   │   ├── css/        # Styling files
│   │   │   |
│   │   │   ├── WEB-INF/    # Configurations and libraries
├── build.gradle            # Gradle build file
├── README.md               # Documentation
└── book_details.png        # Output screenshot
```

---

## Future Enhancements

- Add user authentication for admin and library members.
- Implement CRUD operations to manage book data.
- Integrate advanced search and filter functionality.
- Generate PDF or Excel reports for book inventory.

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests for improvements and bug fixes.

