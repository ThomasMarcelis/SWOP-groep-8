package TaskManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * The class: ProjectController ...
 * 
 * @author Groep 8
 */

public class ProjectController {
	
	private ArrayList<Project> projects;
	private Clock clock;

	/**
	 * Default constructor : sets a new empty list of projects
	 */
	//TODO commentaar
	public ProjectController(Clock clock){
		projects = new ArrayList<Project>();
		this.clock = clock;
	}

	/**
	 * Creates a new project with the given arguments and adds the project
	 * to the list of projects
	 * 
	 * @param name: name of the project
	 * @param description: description of the project
	 * @param creationTime: creation time of the project (only the date needed)
	 * @param dueTime: due time of the project (only the date needed)
	 */
	public void createProject(String name, String description, LocalDate creationTime, LocalDate dueTime){
		Project project = new Project(name, description, creationTime, dueTime);
		this.addProject(project);
	}

	/**
	 * Adds a given project to the list of projects.
	 * 
	 * @param project: project to be added
	 * @throws IllegalArgumentException //TODO
	 */
	public void addProject(Project project) throws IllegalArgumentException {
		if(!canHaveProject(project)){
			throw new IllegalArgumentException("The given project is already in this project.");		}
		else {
			getAllProjects().add(project);
		}
	}

	/**
	 * Checks if the project controller can have the given project. This is
	 * true if and only if the given project is not yet in the project controller
	 * and the project is not null 
	 *  
	 * @param project: given project to be added
	 * @return true if and only if the project controller does not contain the project yet and the project is not null
	 */
	private boolean canHaveProject(Project project){
		return (!getAllProjects().contains(project) && project != null);
	}

	/**
	 * Returns a list of the projects
	 * 
	 * @return projects: list of projects
	 */
	public List<Project> getAllProjects() {
		return projects;
	}

	//TODO commentaar
	public Clock getClock() {
		return clock;
	}
}
