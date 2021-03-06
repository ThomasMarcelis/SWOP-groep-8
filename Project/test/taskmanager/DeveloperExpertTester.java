package taskmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import taskmanager.Developer;
import taskmanager.DeveloperExpert;

public class DeveloperExpertTester {

	private DeveloperExpert developerExpert;

	@Before
	public void SetUp() {
		developerExpert = new DeveloperExpert();
	}

	@Test
	public void testCreateDeveloper() {
		developerExpert.createDeveloper("Bob");
		assertEquals(1, developerExpert.getAllDevelopers().size());
		ArrayList<Developer> devList = new ArrayList<Developer>();
		devList.addAll(developerExpert.getAllDevelopers());
		assertEquals("Bob", devList.get(0).getName());
	}

	@Test
	public void testGetAllDevelopersWithNoDevelopersReturnsAnEmptySet() {
		assertTrue(developerExpert.getAllDevelopers().isEmpty());
		assertFalse(developerExpert.getAllDevelopers() == null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testCannotHaveTheSameDeveloper() {
		developerExpert.createDeveloper("Bob");
		ArrayList<Developer> devList = new ArrayList<Developer>();
		devList.addAll(developerExpert.getAllDevelopers());
		developerExpert.addDeveloper((devList.get(0)));
	}
}
