# Wolfpack Parking Management System

## 🚀 Development Environment Setup (IntelliJ)
1. Download the source code or clone this repository.
2. Open the project, go to `File -> Project structure``:
3. In Project, choose your Java SDK.
4. In Modules, mark `src` directory as "Sources", `test` directory as "Tests".
5. In Libraries, link to `lib/mariadb-java-client-3.2.0.jar`.
6. Apply and build your project.

## 🎮 Running the CLI Application
1. Execute the runner class `src/ParkingManagementRunner.java`

## Code Structure
- The file “src/ParkingManagementRunner.java” is the driver of the program.
- Files in “src/db_models” have classes containing database logic that implement the features of the parking management system.
- Files in “src/db” have classes containing database connection logic and common db utility functions.
- Directory "test" contain test cases that validate certain application logic using JUnit.
- Our application processes every task sequentially. A single database connection is created in main during application start and the same connection is reused for all tasks in the application.
