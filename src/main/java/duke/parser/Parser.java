package duke.parser;

import duke.DukeException;
import duke.command.*;

import java.time.LocalDate;

/*
 * The Parser class is responsible for making sense of the user command.
 * It takes in a user command and returns the corresponding Command object.
 * It is also responsible for parsing the user input into the command and description.
 */
public class Parser {
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String LIST_COMMAND = "list";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String MARK_COMMAND = "mark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String BYE_COMMAND = "bye";

    /*
     * Parses the user input and returns the corresponding Command object.
     */
    public static Command parse(String fullCommand) throws DukeException {
        String[] commandParts = fullCommand.split(" ", 2);
        String command = commandParts[0].toLowerCase();
        switch (command) {
            case TODO_COMMAND:
                return parseTodoCommand(commandParts);
            case DEADLINE_COMMAND:
                return parseDeadlineCommand(commandParts);
            case EVENT_COMMAND:
                return parseEventCommand(commandParts);
            case LIST_COMMAND:
                return new ListCommand();
            case UNMARK_COMMAND:
                return parseUnmarkCommand(commandParts);
            case MARK_COMMAND:
                return parseMarkCommand(commandParts);
            case DELETE_COMMAND:
                return parseDeleteCommand(commandParts);
            case BYE_COMMAND:
                return new ByeCommand();
            case FIND_COMMAND:
                return new FindCommand(commandParts);
            default:
                throw new DukeException("I'm sorry, but I don't know what that means :-(");
        }
    }

    static Command parseTodoCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The description of a todo cannot be empty.");
        }
        return new TodoCommand(commandParts[1]);
    }

    static Command parseDeadlineCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The description of a deadline cannot be empty.");
        }
        String[] deadlineParts = commandParts[1].split(" /by ");
        if (deadlineParts.length == 1) {
            throw new DukeException("The date of a deadline cannot be empty.");
        }
        LocalDate by;
        try {
            by = LocalDate.parse(deadlineParts[1]);
        } catch (Exception e) {
            throw new DukeException("Please enter a valid date in the format yyyy-mm-dd");
        }
        return new DeadlineCommand(deadlineParts[0], by);
    }

    static Command parseEventCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The description of an event cannot be empty.");
        }
        String[] eventParts = commandParts[1].split(" /from ");
        if (eventParts.length == 1) {
            throw new DukeException("The start date of an event cannot be empty. " +
                    "Please use /from to specify the start date.");
        }
        String[] eventParts2 = eventParts[1].split(" /to ");
        if (eventParts2.length == 1) {
            throw new DukeException("The end date of an event cannot be empty. " +
                    "Please use /to to specify the end date.");
        }
        LocalDate from;
        LocalDate to;
        try {
            from = LocalDate.parse(eventParts2[0]);
            to = LocalDate.parse(eventParts2[1]);
        } catch (Exception e) {
            throw new DukeException("Please enter a valid date in the format yyyy-mm-dd.");
        }
        return new EventCommand(eventParts[0], from, to);
    }

    static Command parseMarkCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The index of a mark cannot be empty.");
        }
        int indexToMark;
        try {
            indexToMark = Integer.parseInt(commandParts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Invalid task index provided for marking. Please provide an integer.");
        }

        return new MarkCommand(indexToMark);
    }

    static Command parseUnmarkCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The index of an unmark cannot be empty.");
        }
        int indexToUnmark;
        try {
            indexToUnmark = Integer.parseInt(commandParts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Invalid task index provided for unmarking. Please provide an integer.");
        }
        return new UnmarkCommand(indexToUnmark);
    }

    static Command parseDeleteCommand(String[] commandParts) throws DukeException {
        if (commandParts.length == 1) {
            throw new DukeException("The index of a delete cannot be empty.");
        }
        int indexToDelete;
        try {
            indexToDelete = Integer.parseInt(commandParts[1]) - 1;
        } catch (NumberFormatException e) {
            throw new DukeException("Invalid task index provided for deletion. Please provide an integer.");
        }
        return new DeleteCommand(indexToDelete);
    }
}