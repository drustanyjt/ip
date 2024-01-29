import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

enum Command {
    BYE, LIST, MARK, UNMARK, DEADLINE, EVENT, TODO, DELETE, DEFAULT
}

class DukeException extends Exception {
    public DukeException(String errorMessage) {
        super("OOPS!!! " + errorMessage);
    }
}

class Duke {
    private final ArrayList<Task> userInputLog;
    private boolean isAcceptingInput;

    public Duke() {
        this.userInputLog = new ArrayList<>();
        this.isAcceptingInput = true;
    }

    public void initChat() {
        System.out.println("____________________________________________________________");
        System.out.println("Hello! I'm Fluffy.");
        System.out.println("What can I do for you?");
        System.out.println("____________________________________________________________");

        Scanner scanner = new Scanner(System.in);

        while (isAcceptingInput) {
            System.out.print("$ ");
            try {
                processUserInput(scanner);
            } catch (DukeException e) {
                System.out.println(e.getMessage());
                // Ask for new user input

                // Clears scanner buffer
                scanner = new Scanner(System.in);
            }
            System.out.println("____________________________________________________________");
        }
    }

    private void processUserInput(Scanner scanner) throws DukeException {
        // Main event handler
        String input = scanner.next();

        Command command = getCommand(input);

        switch (command) {
            case BYE:
                handleBye();
                break;
            case LIST:
                handleList();
                break;
            case MARK:
                handleMark(scanner);
                break;
            case UNMARK:
                handleUnmark(scanner);
                break;
            case DEADLINE:
                handleDeadline(scanner);
                break;
            case EVENT:
                handleEvent(scanner);
                break;
            case TODO:
                handleTodo(scanner);
                break;
            case DELETE:
                handleDelete(scanner);
                break;
            default:
                handleDefault();
                break;
        }

        autoSave();
    }

    private Command getCommand(String input) {
        try {
            return Command.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Command.DEFAULT;
        }
    }

    private void handleBye() {
        System.out.println("Bye. Hope to see you again soon!");
        isAcceptingInput = false;
    }

    private void handleList() {
        for (int i = 0; i < userInputLog.size(); i++) {
            System.out.println(i + 1 + ". " + userInputLog.get(i));
        }
    }

    private void handleMark(Scanner scanner) throws DukeException {
        try {
            int indexToMark = scanner.nextInt();
            userInputLog.get(indexToMark - 1).markAsDone();
            System.out.println("Nice! I've marked this task as done:");
            System.out.println(userInputLog.get(indexToMark - 1));
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("Invalid task index provided for marking. Please provide a valid task index.");
        } catch (InputMismatchException e) {
            throw new DukeException("Invalid task index provided for marking. Please provide an integer.");
        }
    }

    private void handleUnmark(Scanner scanner) throws DukeException {
        try {
            int indexToUnmark = scanner.nextInt();
            userInputLog.get(indexToUnmark - 1).markAsNotDone();
            System.out.println("Nice! I've marked this task as undone:");
            System.out.println(userInputLog.get(indexToUnmark - 1));
        } catch (IndexOutOfBoundsException | NullPointerException | IllegalStateException e) {
            throw new DukeException("Invalid task index provided for unmarking. Please provide a valid task index.");
        }
    }

    private void handleDeadline(Scanner scanner) throws DukeException {
        try {
            String deadlineDescription = scanner.nextLine();
            String[] deadlineDescriptionArray = deadlineDescription.split(" /by ");
            if (deadlineDescriptionArray.length != 2) {
                throw new DukeException("Invalid deadline format. Please provide a description followed by '/by' and the deadline.");
            }
            Task deadlineTask = new Deadline(deadlineDescriptionArray[0], deadlineDescriptionArray[1]);
            userInputLog.add(deadlineTask);
            System.out.println("Got it. I've added this task:");
            System.out.println(deadlineTask);
            System.out.println("Now you have " + userInputLog.size() + " tasks in the list.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeException("Invalid deadline format. Please provide a description followed by '/by' and the deadline.");
        }
    }

    private void handleEvent(Scanner scanner) throws DukeException {
        try {
            String eventDescription = scanner.nextLine();
            String[] eventDescriptionArray = eventDescription.split(" /from ");
            if (eventDescriptionArray.length != 2) {
                throw new DukeException("Invalid event format. Please provide a description followed by '/from' and the start time '/to' and the end time.");
            }
            String eventTitle = eventDescriptionArray[0];
            eventDescriptionArray = eventDescriptionArray[1].split(" /to ");
            if (eventDescriptionArray.length != 2) {
                throw new DukeException("Invalid event format. Please provide a description followed by '/from' and the start time '/to' and the end time.");
            }
            Task eventTask = new Event(eventTitle, eventDescriptionArray[0], eventDescriptionArray[1]);
            userInputLog.add(eventTask);
            System.out.println("Got it. I've added this task:");
            System.out.println(eventTask);
            System.out.println("Now you have " + userInputLog.size() + " tasks in the list.");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DukeException("Invalid event format. Please provide a description followed by '/from' and the start time '/to' and the end time.");
        }
    }

    private void handleTodo(Scanner scanner) throws DukeException {
        String todoDescription = scanner.nextLine();
        if (todoDescription.isEmpty()) {
            throw new DukeException("Invalid todo format. Please provide a description for the todo task.");
        }
        Task todoTask = new Todo(todoDescription);
        userInputLog.add(todoTask);
        System.out.println("Got it. I've added this task:");
        System.out.println(todoTask);
        System.out.println("Now you have " + userInputLog.size() + " tasks in the list.");
    }

    private void handleDelete(Scanner scanner) throws DukeException {
        try {
            int indexToDelete = scanner.nextInt();
            Task deletedTask = userInputLog.remove(indexToDelete - 1);
            System.out.println("Noted. I've removed this task:");
            System.out.println(deletedTask);
            System.out.println("Now you have " + userInputLog.size() + " tasks in the list.");
        } catch (IndexOutOfBoundsException | NullPointerException | IllegalStateException e) {
            throw new DukeException("Invalid task index provided for deletion. Please provide a valid task index.");
        }
    }

    private void handleDefault() throws DukeException {
        throw new DukeException("Invalid instruction. Please provide a valid command.");
    }

    private void autoSave() throws DukeException{
        Path path = Paths.get(".", "data", "duke.txt");
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            PrintWriter writer = new PrintWriter(path.toFile(), StandardCharsets.UTF_8);
            for (Task task : userInputLog) {
                writer.println(task);
            }
            writer.close();
        } catch (Exception e) {
            throw new DukeException(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Duke fluffyInstance = new Duke();
        fluffyInstance.initChat();
    }
}
