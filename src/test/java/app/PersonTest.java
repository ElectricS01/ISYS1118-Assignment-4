package app;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.nio.file.Files;
import java.nio.file.Path;


public class PersonTest {
	@Test
	public void testAddPerson() {
		Person person = null;
		boolean validPerson;

		// Test Valid Input
		// Valid Person details
		person = new Person();
		validPerson = person.addPerson("56s_d%&fAB", "Ronald", "Foong", "32|Elizabeth Street|Melbourne|Victoria|Australia", "15-11-2006");
		assertTrue(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertTrue(validPerson);

		person = new Person();
		validPerson = person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "29-02-2000");
		assertTrue(validPerson);

		person = new Person();
		validPerson = person.addPerson("24##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "31-01-1990");
		assertTrue(validPerson);

		// TXT file has been created
		assertTrue(Files.exists(Path.of("./people/56s_d%&fAB.txt")));
		assertTrue(Files.exists(Path.of("./people/22##abcdEF.txt")));
		assertTrue(Files.exists(Path.of("./people/23##abcdEF.txt")));
		assertTrue(Files.exists(Path.of("./people/24##abcdEF.txt")));

		// Test Condition 1
		// Null personID
		person = new Person();
		validPerson = person.addPerson(null, "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// personID is not 10 characters
		person = new Person();
		validPerson = person.addPerson("22##abcEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// First 2 numbers are not between 2 - 9
		person = new Person();
		validPerson = person.addPerson("12##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("20##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// Less than 2 special characters between 3 - 8
		person = new Person();
		validPerson = person.addPerson("22#aabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22aaabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// Last 2 characters are not upper case
		person = new Person();
		validPerson = person.addPerson("22##abcdef", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcd##", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// Test Condition 2
		// Null address
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", null, "15-11-1990");
		assertFalse(validPerson);

		// Wrong address format
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "||||", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|", "15-11-1990");
		assertFalse(validPerson);

		// Test Condition 3
		// Null birthDate
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", null);
		assertFalse(validPerson);

		// Wrong format
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15/11/1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "5-2-24");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "This-is-Test");
		assertFalse(validPerson);

		//  Invalid dates
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "00-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-00-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "29-02-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "30-02-2000");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "29-02-1700");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "31-04-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "32-05-1990");
		assertFalse(validPerson);

		// Extra Conditions
		// birthDate is in the future
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-9999");
		assertFalse(validPerson);

		// firstName is null or empty string
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", null, "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		// lastName is null or empty string
		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", null, "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);
	}

	@Test
	public void testAddID() {
	}
}
