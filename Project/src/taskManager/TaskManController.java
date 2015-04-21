package taskManager;

import java.time.LocalDateTime;

/**
 * The taskManController class controls every expert
 * 
 * @author Groep 8
 *
 */
public class TaskManController {

	private DeveloperExpert developerExpert;
	private ResourceExpert resourceExpert;
	private ProjectExpert projectExpert;
	private PlanningExpert planningExpert;

	private TaskManClock taskManClock;

	/**
	 * Constructor of TaskManController. When a new TaskManController has been
	 * created new expert classes will be created.
	 */
	public TaskManController(LocalDateTime now) {
		this.taskManClock = new TaskManClock(now);

		createDeveloperExpert();
		createResourceExpert();
		createProjectExpert();
		createPlanningExpert();
	}

	/**
	 * Creates a new planning expert
	 */
	private void createPlanningExpert() {
		this.planningExpert = new PlanningExpert();
	}

	/**
	 * Creates a new project expert
	 */
	private void createProjectExpert() {
		this.projectExpert = new ProjectExpert();
		this.taskManClock.register(projectExpert);
		this.projectExpert.handleTimeChange(getTime());
	}

	/**
	 * Creates a new resource expert
	 */
	private void createResourceExpert() {
		this.resourceExpert = new ResourceExpert();
	}

	/**
	 * Creates a new developer expert
	 */
	private void createDeveloperExpert() {
		this.developerExpert = new DeveloperExpert();
	}

	/**
	 * Returns the developer expert
	 * 
	 * @return developerExpert : developer expert
	 */
	public DeveloperExpert getDeveloperExpert() {
		return developerExpert;
	}

	/**
	 * Returns the resource expert
	 * 
	 * @return resourceExpert : resource expert
	 */
	public ResourceExpert getResourceExpert() {
		return resourceExpert;
	}

	/**
	 * Returns the project expert
	 * 
	 * @return projectExpert : project expert
	 */
	public ProjectExpert getProjectExpert() {
		return projectExpert;
	}

	/**
	 * Returns the planning expert
	 * 
	 * @return planningExpert : planning expert
	 */
	public PlanningExpert getPlanningExpert() {
		return planningExpert;
	}

	/**
	 * Saves the current state of the system. Only the last state is remembered
	 */
	public void saveSystem() {
		this.getProjectExpert().save();
		this.getDeveloperExpert().save();
		this.getPlanningExpert().save();
		this.getResourceExpert().save();
		this.taskManClock.save();
	}

	/**
	 * Loads the last saved state of the system
	 * 
	 * @return true if loaded, false if no previous state is found
	 */
	public boolean loadSystem() {
		return this.getProjectExpert().load()
				&& this.getDeveloperExpert().load()
				&& this.getPlanningExpert().load()
				&& this.getResourceExpert().load() && this.taskManClock.load();
	}

	/**
	 * 
	 * Advances the time of TaskMan. This will update the status of every task
	 * in every project of the project controller
	 * 
	 * @param time
	 *            : new time
	 * @throws IllegalArgumentException
	 *             : thrown when the given time is invalid
	 */
	public void advanceTime(LocalDateTime time) {
		this.taskManClock.setTime(time);
		this.getProjectExpert().handleTimeChange(this.taskManClock.getTime());
	}

	/**
	 * Returns the time
	 * 
	 * @return LocalDateTime : time
	 */
	public LocalDateTime getTime() {
		return this.taskManClock.getTime();
	}

	/**
	 * Tell the system execution of Task was started. And updates the status of
	 * all Tasks.
	 * 
	 * @param task
	 * @param startTime
	 */
	public void setExecuting(Task task, LocalDateTime startTime) {
		task.setExecuting(startTime);
		getProjectExpert().updateTaskStatus();
	}

	/**
	 * Tell the system execution of Task was finished. And updates the status of
	 * all Tasks.
	 * 
	 * @param task
	 * @param endTime
	 */
	public void setFinished(Task task, LocalDateTime endTime) {
		task.setFinished(endTime);
		getProjectExpert().updateTaskStatus();
	}

	/**
	 * Tell the system execution of Task failed. And updates the status of all
	 * Tasks.
	 * 
	 * @param task
	 * @param endTime
	 */
	public void setFailed(Task task, LocalDateTime endTime) {
		task.setFailed(endTime);
		getProjectExpert().updateTaskStatus();
	}
}
