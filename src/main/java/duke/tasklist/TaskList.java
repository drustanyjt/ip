package duke.tasklist;

import duke.DukeException;
import duke.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents a list of tasks.
 */
public class TaskList {

    private List<Task> tasks;

    /**
     * Constructor for TaskList.
     * @param tasks The list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructor for TaskList. Creates an empty list of tasks.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Adds a task to the list of tasks.
     * @param task The task to be added.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the list of tasks.
     * @param index The index of the task to be deleted.
     * @throws DukeException If the index is out of bounds.
     */
    public void deleteTask(int index) throws DukeException {
        if (index < 0 || index >= tasks.size()) {
            throw new DukeException("The task number is out of bounds.");
        }
        tasks.remove(index);
    }

    /**
     * Retrieves a task from the list of tasks.
     * @param index The index of the task to be retrieved.
     * @return The task to be retrieved.
     * @throws DukeException If the index is out of bounds.
     */
    public Task getTask(int index) throws DukeException{
        if (index < 0 || index >= tasks.size()) {
            throw new DukeException("The task number is out of bounds.");
        }
        return tasks.get(index);
    }

    /**
     * Filters the tasks in the task list based on a lambda expression.
     * @return A new TaskList object containing the filtered tasks.
     */
    public TaskList filterTasks(Predicate<Task> predicate) {
        List<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (predicate.test(task)) {
                filteredTasks.add(task);
            }
        }
        return new TaskList(filteredTasks);
    }

    /**
     * Retrieves the size of the list of tasks.
     * @return The size of the list of tasks.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Retrieves the list of tasks.
     * @return The list of tasks.
     */
    public List<Task> getTasks() {
        return tasks;
    }
}
