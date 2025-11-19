import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

class Task {
  private static long nextId = 1;

  long id;
  String description;
  String status;
  String createdAt;
  String updatedAt;

  Task() {
  }

  // Constructor use when create new task
  Task(String decription, String status, String createdAt, String updatedAt) {
    this.id = nextId++;
    this.description = decription;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  // Constructor use when load task from file
  Task(long id, String decription, String status, String createdAt, String updatedAt) {
    this.id = id;
    this.description = decription;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static void updateNextId(long maxId) {
    nextId = maxId + 1;
  }

  // Helper method to convert Task to JSON string
  private static String escapeJson(String s) {
    return s.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n")
        .replace("\r", "\\r");
  }

  public String toJsonString() {
    return "  {\n" +
        "    \"id\": " + this.id + ",\n" +
        "    \"description\": \"" + escapeJson(this.description) + "\",\n" +
        "    \"status\": \"" + escapeJson(this.status) + "\",\n" +
        "    \"createdAt\": \"" + escapeJson(this.createdAt) + "\",\n" +
        "    \"updatedAt\": \"" + escapeJson(this.updatedAt) + "\"\n" +
        " }";
  }
}

public class TaskTracker {

  //
  private static final Path TASKS_FILE_PATH = Paths.get("data/tasks.json");
  private static List<Task> tasks = new ArrayList<>();

  // Ham de luu du lieu
  public static void saveTasksToFile(List<Task> allTask) {
    StringBuilder jsonBuilder = new StringBuilder();
    jsonBuilder.append("[\n");

    for (int i = 0; i < allTask.size(); i++) {
      jsonBuilder.append(allTask.get(i).toJsonString());
      if (i < allTask.size() - 1) {
        jsonBuilder.append(",\n");
      }
    }

    jsonBuilder.append("\n]\n");

    try {
      // Ensure directory exists
      if (TASKS_FILE_PATH.getParent() != null) {
        Files.createDirectories(TASKS_FILE_PATH.getParent());
      }

      String newContent = jsonBuilder.toString();

      // If file exists, compare content
      if (Files.exists(TASKS_FILE_PATH)) {
        String currentContent = Files.readString(TASKS_FILE_PATH);
        if (currentContent.equals(newContent)) {
          System.out.println("No changes detected. Skipping write.");
          return;
        }
      }

      // Write content
      Files.writeString(TASKS_FILE_PATH, newContent);
      System.out.println("Tasks saved successfully to " + TASKS_FILE_PATH.getFileName());

    } catch (IOException e) {
      System.err.println("Error saving tasks to file: " + e.getMessage());
    }
  }

  public static void loadTasksFromFile() throws IOException {

    if (!Files.exists(TASKS_FILE_PATH)) {
      Files.createDirectories(TASKS_FILE_PATH.getParent());
      Files.writeString(TASKS_FILE_PATH, "[]\n");
      System.out.println("No tasks.json found. Created new file.");
      return;
    }

    // Read file content
    try {
      String content = Files.readString(TASKS_FILE_PATH);

      // Check if file is empty
      if (content.trim().isEmpty()) {
        System.out.println("tasks.json is empty. Initializing new task list.");
        tasks.clear();
        return;
      }
      // Split content into individual task items
      String[] items = content.split("\\{");

      long maxId = 0;

      for (String item : items) {
        if (!item.contains("id"))
          continue;

        long id = Long.parseLong(item.split("\"id\":")[1].trim().split(",")[0].trim());
        String description = item.split("\"description\":")[1].trim().split("\"")[1];
        String status = item.split("\"status\":")[1].trim().split("\"")[1];
        String createdAt = item.split("\"createdAt\":")[1].trim().split("\"")[1];
        String updatedAt = item.split("\"updatedAt\":")[1].trim().split("\"")[1];

        // Tạo task từ dữ liệu có sẵn
        Task t = new Task(id, description, status, createdAt, updatedAt);
        tasks.add(t);

        if (id > maxId)
          maxId = id;
      }

      // Cập nhật nextId
      Task.updateNextId(maxId);
    } catch (Exception e) {
      System.err.println("Error: tasks.json is corrupted or invalid.");
      System.err.println("Please fix or delete the file and try again.");
      tasks.clear(); // Clear loaded tasks
      return;
    }
  }

  public static void main(String[] args) {
    try {
      loadTasksFromFile();
    } catch (Exception e) {
      System.err.println("Error loading tasks from file: " + e.getMessage());
      return;
    }
    if (args.length == 0) {
      printHelp();
      return;
    }
    // Xu ly user inputs & actions
    String command = args[0];

    switch (command) {
      case "add":
        handleAddCommand(args);
        break;

      case "update":
        handleUpdateCommand(args);
        break;

      case "delete":
        handleDeleteCommand(args);
        break;
      case "mark-in-progress":
        handleMarkInProgressCommand(args);
        break;
      case "mark-done":
        handleMarkDoneCommand(args);
        break;
      case "list": {
        handleListCommand(args);
        break;
      }

      default:
        System.err.println("Unknown command: " + command);
        printHelp();
        break;
    }
  }

  // --- Handle commands ---
  private static void handleEmptyList() {
    System.out.println("No tasks found.");
    System.out.println("Use 'add <description>' command to add a new task.");
  }

  private static void handleAddCommand(String[] args) {
    if (args.length < 2) {
      System.err.println("Error: 'add' command requires a description.");
      printHelp();
      return;
    }
    String description = args[1];
    if (description.trim().isEmpty()) {
      System.err.println("Error: Task description cannot be empty.");
      return;
    }
    String now = java.time.LocalDateTime.now().toString();
    Task newTask = new Task(description, "todo", now, now);
    tasks.add(newTask);
    saveTasksToFile(tasks);
    System.out.println("Task added successfully (ID: " + newTask.id + ")");
  }

  private static void handleUpdateCommand(String[] args) {
    if (tasks.isEmpty()) {
      handleEmptyList();
      return;
    }
    if (args.length < 3) {
      System.err.println("Error: 'update' command requires a description.");
      printHelp();
      return;
    }

    long id = -1;
    try {
      id = Long.parseLong(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Error: Invalid task ID format.");
      printHelp();
      return;
    }
    String description = args[2];
    if (description.trim().isEmpty()) {
      System.err.println("Error: Task description cannot be empty.");
      return;
    }
    String now = java.time.LocalDateTime.now().toString();
    boolean foundId = false;
    for (Task t : tasks) {
      if (t.id == id) {
        t.description = description;
        t.updatedAt = now;
        foundId = true;
        break;
      }
    }
    saveTasksToFile(tasks);
    System.out.println("Task update successfully with ID: " + id);
  }

  private static void handleDeleteCommand(String[] args) {
    if (tasks.isEmpty()) {
      handleEmptyList();
      return;
    }
    long id = -1;
    try {
      id = Long.parseLong(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Error: Invalid task ID format.");
      printHelp();
      return;
    }

    boolean foundId = false;
    Iterator<Task> iter = tasks.iterator();
    while (iter.hasNext()) {
      Task t = iter.next();
      if (t.id == id) {
        iter.remove();
        foundId = true;
        break;
      }
    }

    if (!foundId) {
      System.err.println("Error: Task ID not found.");
      return;
    }
    saveTasksToFile(tasks);
    System.out.println("Updated successfully" + id);
  }

  private static void handleMarkInProgressCommand(String[] args) {
    if (tasks.isEmpty()) {
      handleEmptyList();
      return;
    }
    long id = -1;
    try {
      id = Long.parseLong(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Error: Invalid task ID format.");
      printHelp();
      return;
    }

    String now = java.time.LocalDateTime.now().toString();
    boolean foundId = false;
    for (Task t : tasks) {
      if (t.id == id) {
        t.status = "in-progress";
        t.updatedAt = now;
        foundId = true;
        break;
      }
    }
    if (!foundId) {
      System.err.println("Error: Task ID not found.");
      return;
    }
    saveTasksToFile(tasks);
    System.out.println("Task marked as In Progress successfully with ID: " + id);
  }

  private static void handleMarkDoneCommand(String[] args) {
    if (tasks.isEmpty()) {
      handleEmptyList();
      return;
    }
    long id = -1;
    try {
      id = Long.parseLong(args[1]);
    } catch (NumberFormatException e) {
      System.err.println("Error: Invalid task ID format.");
      printHelp();
      return;
    }
    String now = java.time.LocalDateTime.now().toString();
    boolean foundId = false;
    for (Task t : tasks) {
      if (t.id == id) {
        t.status = "done";
        t.updatedAt = now;
        foundId = true;
        break;
      }
    }
    if (!foundId) {
      System.err.println("Error: Task ID not found.");
      return;
    }
    saveTasksToFile(tasks);
    System.out.println("Task marked as Done successfully with ID: " + id);
  }

  private static void handleListCommand(String[] args) {
    if (tasks.isEmpty()) {
      handleEmptyList();
      return;
    }
    int len = args.length;
    if (len < 2) {
      System.out.println("Listing all tasks:");

      // List all tasks
      for (Task t : tasks) {
        System.out
            .println("[ID]: " + t.id + ", Status: " + t.status + ", Description: " + t.description);
      }
    }
    // List by status
    else if (len == 2) {
      String filter = args[1];
      List<String> validFilters = List.of("todo", "in-progress", "done");

      if (!validFilters.contains(filter)) {
        System.err.println(
            "Error: Invalid filter. Use one of: todo, in-progress, done.");
        printHelp();
        return;
      }
      System.out.println("Listing tasks with status: " + filter);
      for (Task t : tasks) {
        if (filter.equals(t.status))
          System.out
              .println("[ID]: " + t.id + ", Status: " + t.status + ", Description: " + t.description);
      }
    } else {
      System.err.println("Error: 'list' command takes at most one argument.");
      printHelp();
      return;
    }
  }

  private static void printHelp() {
    System.out.println("Task Tracker Commands:");
    System.out.println("  add <description>               - Add a new task");
    System.out.println("  update <id> <new description>   - Update task description");
    System.out.println("  delete <id>                     - Delete a task");
    System.out.println("  mark-in-progress <id>           - Mark a task as In Progress");
    System.out.println("  mark-done <id>                  - Mark a task as Done");
    System.out.println("  list [todo|in-progress|done] - List tasks with optional filter");
  }
}
