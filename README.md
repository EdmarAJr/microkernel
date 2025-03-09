# Build instructions:

mvn install

# Execution instructions:

mvn exec:java -pl app

# New plugin creation instructions:

1. Create your plugin folder in "plugins"
2. Add you new plugin submodule in main pom.xml:

    <modules>
        <module>interfaces</module>
        <module>app</module>
        <module>plugins/myplugin</module>
        ADD IT HERE
    </modules>
    
3. Create your new plugin's pom.xml (check myplugin/pom.xml)
4. Remember to use plugin's package conventions:

    br/edu/ifba/inf008/plugins/<YourPluginNameInCamelCase>.java
    
5. Run "mvn install" and "mvn exec:java -pl app"

________________________________________________________________________
________________________________________________________________________

# Library Management System

This project is a Library management system built with Java and JavaFX. It allows users to manage books, loans, and fines.

### Prerequisites

- Java 21 or higher (recommended)
- Maven 3.6 or higher

### Setup on terminal

1. **Open the project:**

   ```sh
   cd microkernel
   ```

2. **Build the project:**

   ```sh
   mvn clean install
   ```

### Running the Application

1. **Run the application:**

   ```sh
   mvn exec:java -pl app
   ```

### Project Structure

- `src/main/java/br/edu/ifba/inf008/`: Contains the main application and core classes.
- `src/main/java/br/edu/ifba/inf008/model/`: Contains the model classes such as `User`, `Book`, `Loan`, etc.
- `src/main/java/br/edu/ifba/inf008/persistence/`: Contains the data persistence classes.
- `src/main/java/br/edu/ifba/inf008/shell/`: Contains the UI controller and screen classes.

### Features

- **User management:** Add users.
- **Book management:** Add books.
- **Loan management:** Borrow books.
   - **Search management:** Search for books.
- **Return management:** Return books.
  - **Fine management:** Manage fines for overdue books.
- **Book report management:** List all borrowed books and view loan history.
- **Book overdue report management:** View history overdue books.


### Notes

- Ensure that the `persistence.dat` file is in the correct location for data persistence.
- Modify the `FILE_PATH` in `DataPersistence.java` if needed.

### License

This project is licensed under the MIT License. See the `LICENSE` file for details.