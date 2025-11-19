# Task Tracker CLI

`task-tracker-cli` is a simple command-line interface (CLI) application designed to track and manage your tasks. This project is an exercise to practice programming skills, including working with the filesystem, handling user inputs, and building a simple CLI application.

This project is built with no external libraries and stores all data in a local `tasks.json` file.

## ✨ Features

* **Add** new tasks.
* **Update** the description of existing tasks.
* **Delete** a task.
* **Mark** a task's status as `in-progress` or `done`.
* **List** all tasks.
* **Filter** and list tasks by their status: `todo`, `in-progress`, or `done`.

## 🚀 Installation & Setup

1.  Clone this repository to your local machine:
    ```bash
    git clone [https://github.com/nhatkhanhisme/task-tracker-cli.git](https://github.com/nhatkhanhisme/task-tracker-cli.git)
    cd task-tracker-cli
    ```

2.  Compile the project:
    ``` # For Java
      javac -d out src/*.java

    *(If using a scripting language like Python or Node.js, you can skip this and run the file directly).*

3.  (Optional) To make `task-cli` runnable from anywhere, you can move the executable file to a directory in your system's `PATH`.

## 📋 Usage

All commands follow the structure `task-cli [ACTION] [ARGUMENTS]`.


