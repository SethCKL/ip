package cleese;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import exceptions.InvalidInputException;
import exceptions.NoDescException;
import task.Deadline;
import task.Event;
import task.Task;
import task.TaskList;
import task.Todo;
import ui.Ui;

public class Parser {
    /**
     * Processes the request from the user by decoding the user input and executes the necessary functions for
     * said request
     * @param string a whole line of raw user input
     * @param taskList to reference for any operations that involves TasksList
     * @param ui to reference for any operations that requires messages to be printed to console
     * @param storage to reference for any operations that involves reading or storing to file
     * @return a string which represents which operation was just carried out
     * @throws NoDescException exception thrown when no description is given for a Task
     * @throws InvalidInputException exception thrown when an invalid input is given by the user
     */
    public String handleCommand(String string, TaskList taskList, Ui ui, Storage storage)
                                throws NoDescException, InvalidInputException {
        // Declare and initialise DateTimeFormatter for expected format for date and time from user input
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/MM/yyyy HHmm");
        // splits the first word and the rest from the string read from user input
        String[] input = string.split(" ", 2);
        String command = input[0];
        String parameters = "";

        if (input.length > 1) {
            parameters = input[1];
        }

        switch (command) {
        case "bye":
            try {
                storage.saveToFile(taskList);
            } catch (Exception e) {
                return "Error faced when trying to save to file";
            }
            return ui.goodByeResponse();
        case "list":
            return ui.listResponse(taskList);
        case "mark":
        case "unmark":
            int selectedTaskNum = Integer.parseInt(parameters);
            Task selectedTask = taskList.get(selectedTaskNum - 1);
            String response = "";

            if (command.equals("mark")) {
                selectedTask.setDone();
                response = ui.markedMessageResponse(selectedTask);
            }

            if (command.equals("unmark")) {
                selectedTask.setUndone();
                response = ui.unmarkedMessageResponse(selectedTask);
            }
            return response;
        case "delete":
            int taskToDeleteIndex = Integer.parseInt(parameters) - 1;
            Task taskToDelete = taskList.get(taskToDeleteIndex);
            taskList.removeFromTaskList(taskToDelete);
            return ui.removedAckResponse(taskToDelete, taskList);
        case "todo":
        case "event":
        case "deadline":
            // detects empty description for todo tasks
            if (parameters.isEmpty()) {
                throw new NoDescException("todo");
            }

            String description = parameters;
            Task newTask = null;

            if (command.equals("todo")) {
                newTask = new Todo(description);
            } else {
                String descriptionAndSetAt = parameters;
                String[] split = descriptionAndSetAt.split("/", 2);
                String localDateTimeString = split[1].split(" ", 2)[1];
                LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeString, dateTimeFormatter);

                if (command.equals("event")) {
                    newTask = new Event(split[0], localDateTime);
                }

                if (command.equals("deadline")) {
                    newTask = new Deadline(split[0], localDateTime);
                }
            }
            taskList.add(newTask);
            return ui.addedAckResponse(newTask, taskList);
        case "find":
            String foundTasks = taskList.find(parameters);
            return ui.findMessageResponse(foundTasks);
        case "help":
            return ui.helpResponse();
        default:
            throw new InvalidInputException("Invalid Input");
        }
    }
}

