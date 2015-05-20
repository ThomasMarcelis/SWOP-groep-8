package taskmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.Before;
import org.junit.Test;

import taskmanager.exception.UnplannableResourceTypeException;

public class DelegatedTaskExpertTester extends TaskManTester {

	private BranchOffice bruggeOffice;
	private Project project;

	@Before
	public void setUp() {
		super.setUp();
		bruggeOffice = this.tmc.createBranchOffice("Brugge");
		this.tmc.logIn(bruggeOffice);
		Developer me = this.tmc.createDeveloper("Jozef Stalin");
		this.tmc.logIn(me);
		project = this.createStandardProject(time.plusHours(300));
	}

	@Test
	public void delegateTaskTest() {
		Task myTask = this.createTask(project, Duration.ofHours(5));
		this.tmc.delegate(myTask, here);
		assertTrue(here.getDelegatedTaskExpert().getAllDelegatedTasks()
				.contains(myTask));
		assertTrue(here.getDelegatedTaskExpert().getOriginalOffice(myTask)
				.equals(bruggeOffice));
		this.tmc.logIn(here);
		this.tmc.logIn(dev);
		this.tmc.delegate(myTask, bruggeOffice);
		assertFalse(here.getDelegatedTaskExpert().getAllDelegatedTasks()
				.contains(myTask));
		assertTrue(bruggeOffice.getDelegatedTaskExpert().getAllDelegatedTasks()
				.contains(myTask));
	}

	@Test(expected = UnplannableResourceTypeException.class)
	public void planDelegatedTaskNoRessourceAvaillable() {
		ResourceType car = ResourceType.builder("car").build(
				tmc.getActiveOffice());
		car.createResource("red car");
		Task task = Task.builder("desc", Duration.ofHours(8), 0.5)
				.addRequiredResourceType(car, 1).build(project);
		tmc.delegate(task, bruggeOffice);
		tmc.logOut();
		tmc.logIn(bruggeOffice);
		Developer dev = tmc.createDeveloper("Office2Worker");
		tmc.getPlanner().createPlanning(time, task, dev).build();
	}

	@Test
	public void mementoTest() {
		Task myTask = this.createTask(project, Duration.ofHours(5));
		here.saveSystem(bruggeOffice);
		this.tmc.delegate(myTask, here);
		here.loadSystem(bruggeOffice);
		assertEquals(0, here.getDelegatedTaskExpert().getAllDelegatedTasks()
				.size());
	}
}
