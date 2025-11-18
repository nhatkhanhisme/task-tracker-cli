import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class Task {
  private static long nextId = 1;

  long id;
  String description;
  String status;
  String createdAT;
  String updatedAT;

  Task() {
  }

  // Constructor use when create new task
  Task(String decription, String status, String createdAt, String updatedAt) {
    this.id = nextId++;
    this.description = decription;
    this.status = status;
    this.createdAT = createdAt;
    this.updatedAT = updatedAt;
  }

  // Constructor use when load task from file
  Task(long id, String decription, String status, String createdAt, String updatedAt) {
    this.id = id;
    this.description = decription;
    this.status = status;
    this.createdAT = createdAt;
    this.updatedAT = updatedAt;
  }

  public static void updateNextId(long maxId) {
    nextId = maxId + 1;
  }

  public String toJsonString() {
    return "  {\n" +
        "    \"id\": " + this.id + ",\n" +
        "    \"description\": \"" + this.description + "\",\n" +
        "    \"status\": \"" + this.status + "\",\n" +
        "    \"createdAT\": \"" + this.createdAT + "\",\n" +
        "    \"updatedAT\": \"" + this.updatedAT + "\"\n" +
        " }";
  }
}

public class TaskTracker {

  //
  private static final Path TASKS_FILE_PATH = Paths.get("tasks.json");
  private static List<Task> tasks = new ArrayList<>();
  // Ham de luu du lieu
  public static void saveTasksToFile(List<Task> allTask) {
    StringBuilder jsonBuilder = new StringBuilder();
    jsonBuilder.append("[\n"); // Bat dau cua mang JSON

    // Them moi task vao mang
    for (int i = 0; i < allTask.size(); i++) {
      jsonBuilder.append(allTask.get(i).toJsonString());

      // Neu khong phai task cuoi cung thi them dau phay
      if (i < allTask.size() - 1) {
        jsonBuilder.append(",\n");
      }
    }
    jsonBuilder.append("\n]\n"); // Ket thuc cua mang JSON

    try {
      Files.writeString(TASKS_FILE_PATH, jsonBuilder.toString());
      System.out.println("Tasks saved to file: tasks.json  successfully.");
    } catch (Exception e) {
      // TODO: handle exception
      System.err.println("Error saving tasks to file: " + e.getMessage());
    }
  }

public static void  loadTasksFromFile() {

  if (!Files.exists(TASKS_FILE_PATH)) {
    System.out.println("No tasks.json found. Starting fresh.");
    return;
  }

  try {
    String content = Files.readString(TASKS_FILE_PATH);

    // Tách từng object (cách đơn giản)
    String[] items = content.split("\\{");

    long maxId = 0;

    for (String item : items) {
      if (!item.contains("id")) continue;

      long id = Long.parseLong(item.split("\"id\":")[1].trim().split(",")[0].trim());
      String description = item.split("\"description\":")[1].trim().split("\"")[1];
      String status = item.split("\"status\":")[1].trim().split("\"")[1];
      String createdAt = item.split("\"createdAT\":")[1].trim().split("\"")[1];
      String updatedAt = item.split("\"updatedAT\":")[1].trim().split("\"")[1];

      // Tạo task từ dữ liệu có sẵn
      Task t = new Task(id, description, status, createdAt, updatedAt);
      tasks.add(t);

      if (id > maxId) maxId = id;
    }

    // Cập nhật nextId
    Task.updateNextId(maxId);
  } catch (Exception e) {
    System.err.println("Error loading tasks: " + e.getMessage());
    return;
  }
}

  public static void main(String[] args) {
    loadTasksFromFile();

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
  private static void handleAddCommand(String[] args) {
    if (args.length < 2) {
      System.err.println("Error: 'add' command requires a description.");
      printHelp();
      return;
    }
    String dcrs = args[1];
    if (dcrs.trim().isEmpty()) {
      System.err.println("Error: Task description cannot be empty.");
      return;
    }
    String now = java.time.LocalDateTime.now().toString();
    Task newTask = new Task(dcrs, "todo", now, now);
    tasks.add(newTask);
    saveTasksToFile(tasks);
    System.out.println("Task added successfully with ID: " + newTask.id);
  }
  private static void handleUpdateCommand(String[] args) {
    // Implementation here
  }
  private static void handleDeleteCommand(String[] args) {
    // Implementation here
  }
  private static void handleMarkDoneCommand(String[] args) {
    // Implementation here
  }
  private static void handleListCommand(String[] args) {
    // Implementation here
    for (Task t : tasks) {
      System.out.println("ID: " + t.id + ", Description: " + t.description + ", Status: " + t.status);
    }
  }

  private static void printHelp() {
    System.out.println("Task Tracker Commands:");
    System.out.println("  add <description>               - Add a new task");
    System.out.println("  update <id> <new description>   - Update task description");
    System.out.println("  delete <id>                     - Delete a task");
    System.out.println("  mark-in-progress <id>           - Mark a task as In Progress");
    System.out.println("  mark-done <id>                  - Mark a task as Done");
    System.out.println("  list [all|pending|in-progress|done] - List tasks with optional filter");
  }
}
