package taskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The Project class describes a project in system. Every project has the same
 * details: name, description, creation time and a due time. A project contains
 * a list of his tasks and is allowed to create new tasks. Projects can be
 * finished or ongoing.
 * 
 * @author groep 8
 * 
 */

public class Project {

	private ArrayList<Task> tasks;
	private String name;
	private String description;
	private final LocalDateTime creationTime;
	private LocalDateTime dueTime;

	private LocalDateTime lastUpdateTime;

	/**
	 * Constructor of the Project class: Sets a new list of tasks
	 * 
	 * @param name
	 *            : name of the project
	 * @param description
	 *            : description of the project
	 * @param creationTime
	 *            : creation time of the project (only the date needed)
	 * @param dueTime
	 *            : due time of the project (only the date needed)
	 */
	 Project(String name, String description, LocalDateTime creationTime,
			LocalDateTime dueTime) {
		setName(name);
		setDescription(description);
		this.creationTime = creationTime;
		setDueTime(dueTime);
		this.tasks = new ArrayList<Task>();
		this.update(creationTime);
	}

	/**
	 * Creates a new task to the project and will add the task to the tasklist
	 * of the project
	 * 
	 * @param description : description of a task
	 * @param estimatedDuration : estimated duration of task
	 * @param acceptableDeviation : acceptable deviation of a task
	 */
	public void createTask(String description, Duration estimatedDuration, double acceptableDeviation) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, this.lastUpdateTime);
		this.addTask(task);
	}

	/**
	 * Creates a new task to the project and will add the task to the tasklist
	 * of the project
	 * 
	 * @param description : description of a task
	 * @param estimatedDuration : estimated duration of task
	 * @param acceptableDeviation : acceptable deviation of a task
	 */
	public void createTask(String description, Duration estimatedDuration,
			double acceptableDeviation, Task alernativeTask) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, this.lastUpdateTime, alernativeTask);
		this.addTask(task);
	}

	/**
	 * Creates a new task to the project and will add the task to the tasklist
	 * of the project
	 * 
	 * @param description : description of a task
	 * @param estimatedDuration : estimated duration of task
	 * @param acceptableDeviation : acceptable deviation of a task
	 */
	public void createTask(String description, Duration estimatedDuration,
			double acceptableDeviation, ArrayList<Task> dependencies) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, this.lastUpdateTime, dependencies);
		this.addTask(task);
	}

	/**
	 * Creates a new task to the project and will add the task to the tasklist
	 * of the project
	 * 
	 * @param description : description of a task
	 * @param estimatedDuration : estimated duration of task
	 * @param acceptableDeviation : acceptable deviation of a task
	 * @param alternativeTa
	 */
	public void createTask(String description, Duration estimatedDuration,
			double acceptableDeviation, Task alernativeTask,
			ArrayList<Task> dependencies) {
		Task task = new Task(description, estimatedDuration,
				acceptableDeviation, this.lastUpdateTime, alernativeTask,
				dependencies);
		this.addTask(task);
	}

	/**
	 * This method adds a given task to a project
	 * 
	 * @param task
	 *            : task to add to project
	 * @throws IllegalArgumentException
	 *             : thrown when the given task is not valid
	 */
	void addTask(Task task) {
		if (!canHaveTask(task)) {
			throw new IllegalArgumentException(
					"The given task is already in this project.");
		} else {
			this.getAllTasks().add(task);
		}
	}

	/**
	 * This method checks if a project can have a given task. It returns true if
	 * and only if the project does not contain the task yet and the task is not
	 * null
	 * 
	 * @param task
	 *            : given task to be added
	 * @return true if and only if the given task is not null and the task is
	 *         not already in the project
	 */
	private boolean canHaveTask(Task task) {
		return (!getAllTasks().contains(task) && task != null);
	}

	/**
	 * Returns the list of tasks of the project
	 * 
	 * @return list of tasks
	 */
	public List<Task> getAllTasks() {
		return tasks;
	}

	/**
	 * Returns true if and only if all tasks of the project are finished. It
	 * returns false if a task is unavailable or not yet available.
	 * 
	 * If a project does not have any tasks, the project is not finished.
	 * 
	 * @return true if and only if all tasks are finished
	 */
	private boolean hasFinished() {
		if (getAllTasks().size() != 0) {
			for (Task task : getAllTasks()) {
				if (task.getStatus() == TaskStatus.UNAVAILABLE
						|| task.getStatus() == TaskStatus.AVAILABLE) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the status of a project
	 * 
	 * @return ONGOING: if not all tasks are finished FINISHED: if all tasks are
	 *         finished
	 */
	public ProjectStatus getStatus() {
		if (hasFinished()) {
			return ProjectStatus.FINISHED;
		} else {
			return ProjectStatus.ONGOING;
		}
	}

	// TODO methode testen + documentatie
	public Duration getTotalDelay() {
		List<Task> allTasks = this.getAllTasks();
		for (Task task : allTasks) {
			if (task.getStatus() == TaskStatus.FINISHED
					|| task.getStatus() == TaskStatus.FAILED) {
			}

		}
		return null;
	}

	/**
	 * Sets the name of a project
	 * 
	 * @param name
	 *            : the given name of a project
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of a project
	 * 
	 * @return name of a project
	 */
	public String getName() {
		return name;
	}

	/**
	 * returns the description of a project
	 * 
	 * @return description of a project
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * sets a description for a project
	 * 
	 * @param description
	 *            : the given description
	 */
	private void setDescription(String description) {
		this.description = description;
	}

	/**
	 * returns the due time of project
	 * 
	 * @return dueTime of a project
	 */
	public LocalDateTime getDueTime() {
		return dueTime;
	}

	/**
	 * This method sets the due time of project
	 * 
	 * @param dueTime
	 *            : given due time of a project
	 * @throws IllegalArgumentException
	 *             : thrown when the given due time is not valid
	 */
	void setDueTime(LocalDateTime dueTime) throws IllegalArgumentException {
		if (!canHaveDueTime(dueTime)) {
			throw new IllegalArgumentException(
					"The given due time is not valid.");
		}
		this.dueTime = dueTime;
	}

	/**
	 * Determines if the given due time is valid. It returns true if and only if
	 * the given due time is after the creation time of the project or is equal
	 * to the creation time
	 * 
	 * @return true if and only if the due time is after the creation time or is
	 *         equal to the creation time
	 */
	private boolean canHaveDueTime(LocalDateTime dueTime) {
		return dueTime.isAfter(getCreationTime())
				|| dueTime.isEqual(getCreationTime());
	}

	public ProjectFinishingStatus willFinishOnTime() {
		if (this.getEstimatedFinishTime().isBefore(this.getDueTime())) {
			return ProjectFinishingStatus.ON_TIME;
		}
		return ProjectFinishingStatus.OVER_TIME;
	}

	/**
	 * Returns whether the project finished on time or not.
	 * 
	 * @return ON_TIME or OVER_TIME depending whether the project finished on
	 *         time or not.
	 * @throws IllegalStateException
	 *             if the project is not yet finished
	 */
	public ProjectFinishingStatus finishedOnTime() throws IllegalStateException {
		if (!this.hasFinished()) {
			throw new IllegalStateException("Project not yet finished!");
		} else {
			if (this.getEstimatedFinishTime().isAfter(this.getDueTime())) {
				return ProjectFinishingStatus.OVER_TIME;
			} else {
				return ProjectFinishingStatus.ON_TIME;
			}
		}
	}

	/**
	 * Updates the state of the object and it's tasks
	 * with state = lastupdatetime
	 * 
	 * @param time
	 *            the current time
	 */
	void update(LocalDateTime time) {
		for (Task task : this.getAllTasks()) {
			task.update(time);
		}
		this.lastUpdateTime = time;
	}

	/**
	 * Estimates the finish time.
	 * 
	 * 
	 * @return
	 */
	LocalDateTime getEstimatedFinishTime() {
		LocalDateTime estimatedFinishTime = this.lastUpdateTime;
		for (Task task : getAllTasks()) {
			LocalDateTime taskFinishTime = task.getEstimatedFinishTime();
			if (taskFinishTime.isAfter(estimatedFinishTime)) {
				estimatedFinishTime = task.getEstimatedFinishTime();
			}
		}
		return estimatedFinishTime;
	}

	/**
	 * returns the creation time of a project
	 * 
	 * @return creationTime of a project
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}
	
	public LocalDateTime getLastUpdateTime(){
		return lastUpdateTime;
	}
}
