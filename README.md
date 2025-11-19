# Task Tracker CLI

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

A simple, robust Command Line Interface (CLI) application for tracking and managing your daily tasks.

This project is built entirely using **Native Java**, focusing on low-level file manipulation and custom data handling without relying on any external JSON libraries (like Gson or Jackson).

## 📋 Features

- **CRUD Operations:** Add, Update, and Delete tasks.
- **Status Management:** Mark tasks as `todo`, `in-progress`, or `done`.
- **Filtering:** List all tasks or filter them by specific status.
- **Persistent Storage:** Data is automatically saved to a JSON file (`data/tasks.json`).
- **Native Implementation:** Custom logic for JSON parsing and string manipulation.

## 🛠️ Prerequisites

- **Java Development Kit (JDK):** Version 11 or higher.
- **Terminal:** Command Prompt, PowerShell, or Bash.

## 🚀 Installation & Usage

### 1. Compile the Project

Open your terminal in the project directory and compile the Java source file:

```bash
javac -d out src/*.java
```

### 2. Run the application

Use the **java** command followed by the class name and your desired arguments:

#### Add a Task

```bash
java -cp out TaskTracker add "Task Title" "Task Description"
  #Output: Task added successfully (ID: 1)
```

#### Delete a task

```bash
java -cp out TaskTracker delete 1
  #Output: Task with ID 1 deleted successfully.
```

#### Update a task

```bash
java -cp out TaskTracker update 1 "Updated Title" "Updated Description"
  #Output: Task with ID 1 updated successfully.
```

#### Mark a task as done

```bash
java -cp out TaskTracker mark-done 1
  #Output: Task with ID 1 marked as done.
```

#### List all tasks

```bash
java -cp out TaskTracker list

  #Output: Displays all tasks with their details.
```

#### List tasks by status

```bash
java -cp out TaskTracker list todo
  #Output: Displays all tasks with status 'todo'.
```

## 📁 Project Structure

```
TaskTracker/
├── src/                  # Source code directory
│   └── TaskTracker.java  # Main application file

├── data/                 # Directory for storing task data
│   └── tasks.json       # JSON file for task storage
├── out/                  # Compiled classes output directory
└── README.md             # Project documentation
```

## 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgements

- Inspired by the need for simple task management tools.
- Thanks to the open-source community for continuous learning and support.

## 🤝 Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## 📞 Contact

For any questions or suggestions, please open an issue in the repository.
**Project URL:** [https://github.com/nhatkhanhisme/task-tracker-cli]

Happy Task Tracking! 🚀
