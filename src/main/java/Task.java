import java.time.format.DateTimeFormatter;

public class Task {
    protected String description;
    protected boolean isDone;

    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Task(String description) {
        this.description = description;
        this.isDone = false; // Task is not done by default
    }

    public String getStatusIcon() {
        return (isDone ? "x" : " ");
    }

    public void markAsDone() {
        // Mark task as done
        this.isDone = true;
    }

    public void markAsNotDone() {
        // Mark task as done
        this.isDone = false;
    }

    public String getDescription() {
        // Return task description
        return this.description;
    }

    public String toString() {
        // Return task status icon and description
        return "[" + this.getStatusIcon() + "] " + this.getDescription();
    }
}
