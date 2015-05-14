package taskmanager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utility.TimeSpan;

public class TaskManController {
	private Company company;
	private BranchOffice activeOffice;
	private Developer activeDeveloper;
	private TaskManClock taskManClock;

	public TaskManController(LocalDateTime now) {
		company = new Company();
		taskManClock = new TaskManClock(now);
	}

	/**
	 * Log the user in and set his Branch office
	 * 
	 * @param activeDeveloper
	 * @param activeOffice
	 */
	public void logIn(Developer activeDeveloper, BranchOffice activeOffice) {
		setActiveDeveloper(activeDeveloper);
		setActiveOffice(activeOffice);
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
		activeOffice.getPlanner().getPlanning(task)
				.setTimeSpan(new TimeSpan(startTime, task.getDuration()));
		updateStatusAll();
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
		if (activeOffice.getPlanner().taskHasPlanning(task)) {
			activeOffice.getPlanner().getPlanning(task).setEndTime(endTime);
		}
		updateStatusAll();
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
		if (activeOffice.getPlanner().taskHasPlanning(task)) {
			activeOffice.getPlanner().getPlanning(task).setEndTime(endTime);
		}
		updateStatusAll();
	}

	/**
	 * Advances the time of TaskMan.
	 * 
	 * @param time
	 *            : new time
	 * @throws IllegalArgumentException
	 *             : thrown when the given time is invalid
	 */
	public void advanceTime(LocalDateTime time) {
		this.taskManClock.setTime(time);
	}

	/**
	 * Return all the tasks that do not have a planning yet.
	 * 
	 * @return set of tasks without a planning
	 */
	public Set<Task> getUnplannedTasks() {
		return activeOffice.getPlanner().getUnplannedTasks(
				activeOffice.getProjectExpert().getAllTasks());
	}

	/**
	 * returns 3 times at which a task could be planned so that all required
	 * developers and resources are available
	 * 
	 * @return A set of localdateTimes
	 */
	public Set<LocalDateTime> getPossibleStartTimes(Task task) {
		return activeOffice.getPlanner().getPossibleStartTimes(task, getTime(),
				activeOffice.getDeveloperExpert().getAllDevelopers());
	}

	/**
	 * Have the system select resources for the given task, during the given
	 * timeSpan
	 * 
	 * @param task
	 * @param timeSpan
	 * @return The selected resources
	 */
	public Set<Resource> selectResources(Task task, TimeSpan timeSpan) {
		Map<ResourceType, Integer> requirements = task
				.getRequiredResourceTypes();
		Set<Resource> selected = new HashSet<Resource>();
		if (requirements.isEmpty()) {
			return selected;
		} else {
			for (ResourceType type : requirements.keySet()) {
				ArrayList<Resource> available = new ArrayList<Resource>(
						activeOffice.getPlanner().resourcesOfTypeAvailableFor(
								type, task, timeSpan));
				selected.addAll(available.subList(0, requirements.get(type)));
			}
		}
		return selected;
	}

	/**
	 * Returns a list of the projects
	 * 
	 * @return projects: list of projects
	 */
	public List<Project> getAllProjects() {
		return Collections.unmodifiableList(activeOffice.getProjectExpert()
				.getAllProjects());
	}

	/**
	 * Returns the set of all resource types
	 * 
	 * @return resourcetypes : set of all resource types
	 */
	public Set<ResourceType> getAllResourceTypes() {
		return Collections.unmodifiableSet(activeOffice.getResourceExpert()
				.getAllResourceTypes());
	}

	/**
	 * Returns the unmodifiable set of all developers
	 * 
	 * @return developers : set of all developers
	 */
	public Set<Developer> getAllDevelopers() {
		return Collections.unmodifiableSet(activeOffice.getDeveloperExpert()
				.getAllDevelopers());
	}

	/**
	 * 
	 * Returns a set with all tasks of a given developer
	 * 
	 * @param dev
	 *            the active Developer
	 * @return All the tasks to which this developer is assigned.
	 */
	public Set<Task> getAllTasks(Developer dev) {
		Set<Task> tasks = new HashSet<Task>();
		for (Project project : getAllProjects()) {
			for (Task task : project.getAllTasks()) {
				if (activeOffice.getPlanner().taskHasPlanning(task)
						&& activeOffice.getPlanner().getPlanning(task)
								.getDevelopers().contains(dev)) {
					tasks.add(task);
				}
			}
		}
		return Collections.unmodifiableSet(tasks);
	}

	/**
	 * Creates a new project with the given arguments and adds the project to
	 * the list of projects
	 * 
	 * @param name
	 *            : name of the project
	 * @param description
	 *            : description of the project
	 * @param creationTime
	 *            : creation time of the project
	 * @param dueTime
	 *            : due time of the project
	 */
	public Project createProject(String name, String description,
			LocalDateTime creationTime, LocalDateTime dueTime) {
		return activeOffice.getProjectExpert().createProject(name, description,
				creationTime, dueTime);
	}

	/**
	 * Creates a new project with the given arguments and adds the project to
	 * the list of projects. The creationTime is set to the current time
	 * 
	 * @param name
	 *            : name of the project
	 * @param description
	 *            : description of the project
	 * @param dueTime
	 *            : due time of the project
	 */
	public Project createProject(String name, String description,
			LocalDateTime dueTime) {
		return activeOffice.getProjectExpert().createProject(name, description,
				getTime(), dueTime);
	}

	/**
	 * Creates a new developer with the given name. and adds the new developer
	 * to the set of all developers
	 * 
	 * @param name
	 *            : given name
	 */
	public Developer createDeveloper(String name) {
		return activeOffice.getDeveloperExpert().createDeveloper(name);
	}

	/**
	 * @return The user currently logged in
	 */
	public Developer getActiveDeveloper() {
		return activeDeveloper;
	}

	/**
	 * @return The branch office where the user is currently logged in
	 */
	public BranchOffice getActiveOffice() {
		return activeOffice;
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
	 * Update the status of all tasks
	 */
	private void updateStatusAll() {
		for (Task task : activeOffice.getProjectExpert().getAllTasks())
			activeOffice.getPlanner().updateStatus(task);
	}

	private void setActiveDeveloper(Developer activeDeveloper) {
		this.activeDeveloper = activeDeveloper;
	}

	private void setActiveOffice(BranchOffice activeOffice) {
		this.activeOffice = activeOffice;
	}
}