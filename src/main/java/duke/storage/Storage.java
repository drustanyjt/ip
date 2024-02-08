package duke.storage;

import duke.DukeException;
import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;
import duke.tasklist.TaskList;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a storage for saving and loading tasks to and from the hard disk.
 */
public class Storage {
    protected String filePath;

    /**
     * Constructor for Storage.
     * @param filePath The file path of the storage.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves data to the hard disk.
     * @param tasks The list of tasks to be saved.
     * @throws DukeException If an error occurs during the saving of the tasks.
     */
    public void save(TaskList tasks) throws DukeException {
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            for (Task task : tasks.getTasks()) {
                fileWriter.write(encodeTask(task) + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            throw new DukeException("Error writing to file");
        }
    }

    /**
     * Loads data from the hard disk.
     * @return The list of tasks loaded from the hard disk.
     * @throws DukeException If an error occurs during the loading of the tasks.
     */
    public List<Task> load() throws DukeException {
        List<Task> tasks = new ArrayList<>();
        try {
            Files.createDirectories(Paths.get(filePath));
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                tasks.add(decodeTask(line));
            }
        } catch (IOException e) {
            throw new DukeException("Error reading from file");
        }
        return tasks;
    }

    /**
     * Encodes task to a String for saving to the hard disk.
     * Prepends the type of task to the task's fileString.
     *
     * @param task the Task to be encoded.
     * @return Encoded task as a String.
     */
    public String encodeTask(Task task) {
        return task.getType() + " | " + task.toFileString();
    }

    /**
     * Decodes task from a String read from the hard disk.
     * Extracts the type of task from the encoded task String and creates the task accordingly.
     *
     * @param encodedTaskString the encoded task String read from the hard disk.
     * @return Decoded task.
     * @throws DukeException If an error occurs during the decoding of the task.
     */
    public Task decodeTask(String encodedTaskString) throws DukeException{
        String[] taskDetails = encodedTaskString.split(" \\| ", 2);
        String taskType = taskDetails[0];
        String taskFileString = taskDetails[1];
        switch (taskType) {
            case "T":
                return Todo.TodoFromFileString(taskFileString);
            case "D":
                return Deadline.DeadlineFromFileString(taskFileString);
            case "E":
                return Event.EventFromFileString(taskFileString);
            default:
                throw new DukeException("Error reading from file");
        }
    }
}