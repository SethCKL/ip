package CleeseTest;

import cleese.Parser;
import cleese.Storage;
import task.TaskList;
import ui.Ui;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class CleeseTest {
    private Parser parser = new Parser();
    private Storage storage = new Storage("placeholder");
    private TaskList taskList = new TaskList();
    private Ui ui = new Ui();

    @Test
    public void testListCommand() {
        try {
            assertEquals(parser.handleCommand("list", taskList, ui, storage), "list");
        } catch (Exception e) {
            fail();
        }
    }
    @Test
    public void testByeCommand() {
        try {
            assertEquals(parser.handleCommand("bye", taskList, ui, storage), "bye");
        } catch (Exception e) {
            fail();
        }
    }
}
