package app.Lib.Documents;

import app.Lib.AddPersonHelper;
import app.Person;
import app.TestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddPersonHelperTest {

	@BeforeAll
	public static void setup() {
		TestHelper.clearPeopleFile();
		new Person().addPerson("22##abcdEF", "John", "Doe", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
	}

	@Test
	public void testCheckPersonExists_ValidPerson() {
		assertTrue(AddPersonHelper.checkPersonExists("22##abcdEF"));
	}

	@Test
	public void testCheckPersonExists_InvalidPerson() {
		assertFalse(AddPersonHelper.checkPersonExists("33##abcdEF"));
	}
}
