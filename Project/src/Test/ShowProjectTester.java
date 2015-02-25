package Test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import TaskManager.Project;
import TaskManager.ProjectController;

public class ShowProjectTester {

	private ProjectController emptyController;
	
	private ProjectController singleProjectController;
	private Project project11;
	
	private ProjectController dualProjectController;
	private Project project21;
	private Project project22;
	
	@Before
	public void setUp() {
		emptyController = new ProjectController();
		singleProjectController = new ProjectController();
		project11 = new Project();
		singleProjectController.addProject(project11);
		dualProjectController = new ProjectController();
		project21 = new Project();
		project22 = new Project();
		dualProjectController.addProject(project21);
		dualProjectController.addProject(project22);
	}
	
	@Test
	public void testGetAllProjects() {
		assertEquals(emptyController.getAllProjects().size(),0);
		
		assertEquals(singleProjectController.getAllProjects().size(),1);
		assertTrue(singleProjectController.getAllProjects().contains(project11));
		
		assertEquals(dualProjectController.getAllProjects().size(),2);
		assertTrue(dualProjectController.getAllProjects().contains(project21));
		assertTrue(dualProjectController.getAllProjects().contains(project22));
	}
}