package duke;

import duke.command.Command;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.tasklist.TaskList;
import duke.ui.Ui;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DukeApp extends Application{

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    private String filePath = "data/duke.txt";

    /**
     * Constructor for DukeApp.
     */
    public DukeApp() {
        ui = new Ui(this::handler);
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    public String handler(String input) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        PrintStream newOut = new PrintStream(outContent);
        System.setOut(newOut);

        try {
            Command c = Parser.parse(input);
            c.execute(tasks, ui, storage);
            storage.save(tasks);
            if (c.isExit()) {
                ui.exit();
            }
        } catch (DukeException e) {
            ui.showError(e.getMessage());
        } finally {
            System.setOut(originalOut);
        }
        return outContent.toString();
    }


    @Override
    public void start(Stage stage) {
        //Step 1. Setting up required components
        ui.start(stage);
    }
}
