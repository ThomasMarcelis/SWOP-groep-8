package ui;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import parser.Parser;
import taskManager.Developer;
import taskManager.Project;
import taskManager.Task;
import taskManager.Task.TaskBuilder;
import taskManager.TaskManController;
import ui.exception.ExitUseCaseException;
import utility.Summarizable;

public class UiTaskMan {

	private TaskManController taskManController;
	private Reader reader;
	private Developer activeDeveloper;

	UiTaskMan() {
		reader = new Reader();
		LocalDateTime now = LocalDateTime.of(2014, 1, 1, 8, 0);
		try {
			now = reader.getDate("Give the current time:");
		} catch (ExitUseCaseException e) {
		}

		System.out.println("Current time initialized on:\n" + now + "\n");
		taskManController = new TaskManController(now);
		askInitialState();
	}

	private void askInitialState() {
		while (true) {
			String fileName;
			try {
				fileName = reader
						.getString("Give a file for initialisation of the system:\n"
								+ "(press 2 to use ./input2.tman)");
			} catch (ExitUseCaseException e1) {
				System.out.println("Starting with an empty system");
				return;
			}
			if (fileName.equals("2"))
				fileName = "./input2.tman";
			try {
				Parser parser = new Parser();
				parser.parse(fileName, taskManController);
				System.out.println("Starting with system initialised from "
						+ fileName);
				return;
			} catch (IllegalArgumentException | FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void showProjects() throws ExitUseCaseException {
		System.out.println(Printer.listProjects(taskManController.getProjectExpert()
				.getAllProjects()));
		Project project = reader.select(taskManController.getProjectExpert().getAllProjects());
		System.out.println(Printer.full(project));
		Task task = reader.select(project.getAllTasks());
		System.out.println(Printer.full(task));
	}

	private void createProject() throws ExitUseCaseException {
		System.out.println("Creating a project\n"
				+ "Please fill in the following form:");
		String name = reader.getString("name");
		String description = reader.getString("description");
		LocalDateTime dueTime = reader.getDate("due time");
		if (dueTime == null) {
			System.out.println("Project creation aborted.");
			return;
		}
		taskManController.getProjectExpert().createProject(name, description, dueTime);
	}

	private void createTask() throws ExitUseCaseException {
		while (true) {
			System.out.println("Creating a task\n"
					+ "Please fill in the following form:\n"
					+ "Adding task to which project?");
			System.out.println(Printer.listProjects(taskManController.getProjectExpert()
					.getAllProjects()));
			Project project = reader.select(taskManController.getProjectExpert().getAllProjects());
			TaskBuilder builder = project.taskBuilder(reader
					.getString("Give a description:"), reader
					.getDuration("Give an estimate for the task duration:"),
					reader.getDouble("Give an acceptable deviation:"));
			while (reader
					.getBoolean("Is this task dependent on an other task?")) {
				System.out.println(Printer.listTasks(project.getAllTasks()));
				builder.addDependencies(reader.select(project.getAllTasks()));
			}
			if (reader.getBoolean("Is this an alternative to a failled task?")) {
				System.out.println(Printer.listTasks(project.getAllTasks()));
				builder.setOriginalTask(reader.select(project.getAllTasks()));
			}
			builder.build();
			return;
		}
	}

	private void planTask() throws ExitUseCaseException {
		System.out.println("TODO implement plan task");
	}

	private void updateTaskStatus() throws ExitUseCaseException {
		System.out.println("Updating the status of a task\n"
				+ "Please select a task:");
		ArrayList<Task> allTasks = new ArrayList<Task>();
		for (Project project : taskManController.getProjectExpert().getAllProjects()) {
			System.out.println(Printer.oneLine(project));
			System.out.println(Printer.listTasks(project.getAllTasks(),
					allTasks.size() + 1));
			allTasks.addAll(project.getAllTasks());
		}
		Task task = reader.select(allTasks);

		while (true) {
			try {
				task.updateStatus(
						reader.getDate("When did you start this task?"),
						reader.getDate("When did you finish this task?"),
						reader.getBoolean("Was this task failed?"));
				return;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void advanceTime() throws ExitUseCaseException {
		while (true) {
			try {
				taskManController.advanceTime(reader
						.getDate("Enter the new timestamp:"));
				return;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private void runSimulation() throws ExitUseCaseException {
		System.out.println("TODO implement run simulation");
	}

	private void selectDeveloper() throws ExitUseCaseException {
		List<Developer> developerslist = new ArrayList<Developer>(taskManController.getDeveloperExpert().getAllDevelopers());
		System.out.println(Printer.listDevelopers(developerslist));
		activeDeveloper = reader.select(developerslist);
	}

	private void printSwitchUserMenu(){
		System.out.println("\nSwitch user menu:\n" + "1: Projectmanager\t"
				+ "2: Developer\t"
				+ "9: Exit");
	}

	private void printProjectManagerMenu(){
		System.out
		.println("\nProject Manager Main menu:\n" + "1: Show projects\t"
				+ "2: Create project\t" + "3: Create task\n"
				+ "4: Plan task\t\t" + "5: Advance time\n" 
				+ "6: Run simulation\t"
				+ "9: Exit");
	}

	private void printDeveloperMenu(){
		System.out
		.println("\nDeveloper Main menu:\n" + "1: Show projects\t"
				+"2: Update task status\t" + "3: Advance time\t"
				+ "9: Exit");
	}


	void switchUserMenu(){
		while(true){
			try{
				printSwitchUserMenu();
				String choice = reader.getString("select user:");
				switch(choice){
				case "1":
					projectManagerMenu();
					break;
				case "2":
					selectDeveloper();
					developerMenu();
					break;
				case "9":
					reader.close();
					return;
				default:
					System.out.println("Invalid choice. (9 to exit)");
				}
			}
			catch(Exception e){

			}
		}		
	}

	void projectManagerMenu(){
		try {
			printProjectManagerMenu();
			String choice = reader.getString("Select an option");
			switch (choice) {
			case "9":
				reader.close();
				return;
			case "1":
				showProjects();
				break;
			case "2":
				createProject();
				break;
			case "3":
				createTask();
				break;
			case "4":
				planTask();
				break;
			case "5":
				advanceTime();
				break;
			case "6":
				runSimulation();
				break;
			default:
				System.out
				.println("Invalid choice, try again. (9 to exit)");
				break;
			}
		} catch (ExitUseCaseException e) {
			System.out
			.println("Use case exited, returning to the main menu.");
		}
		switchUserMenu();
	}

	void developerMenu(){
		try {
			printDeveloperMenu();
			String choice = reader.getString("Select an option");
			switch (choice) {
			case "9":
				reader.close();
				return;
			case "1":
				showProjects();
				break;
			case "2":
				updateTaskStatus();
				break;
			case "3":
				advanceTime();
				break;
			default:
				System.out
				.println("Invalid choice, try again. (9 to exit)");
				break;
			}
		} catch (ExitUseCaseException e) {
			System.out
			.println("Use case exited, returning to the main menu.");
		}
		switchUserMenu();
	}

	public static void main(String[] args) {
		System.out.println("Welcome to TaskMan");
		new UiTaskMan().switchUserMenu();
		System.out.println("Goodbye!");
	}

}
