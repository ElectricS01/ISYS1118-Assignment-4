package app;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class PersonTest {
	@Test
	public void testAddPerson() {
		// Test Valid Person
		Person person0 = new Person();
		boolean validPerson0 = person0.addPerson("56s_d%&fAB", "Ronald", "Foong", "32|Elizabeth Street|Melbourne|Victoria|Australia", "15-11-2006");
		assertTrue(validPerson0);
		Person person1 = new Person();
		boolean validPerson1 = person1.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertTrue(validPerson1);

		// Test Condition 1
		// Null personID
		Person person2 = new Person();
		boolean validPerson2 = person2.addPerson(null, "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson2);
		// personID is not 10 characters
		Person person3 = new Person();
		boolean validPerson3 = person3.addPerson("22##abcEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson3);
		Person person4 = new Person();
		boolean validPerson4 = person4.addPerson("", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson4);
		// First 2 numbers are not 2 - 9
		Person person5 = new Person();
		boolean validPerson5 = person5.addPerson("12##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson5);
		Person person6 = new Person();
		boolean validPerson6 = person6.addPerson("20##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson6);
		// Less than 2 special characters between 3 - 8
		Person person7 = new Person();
		boolean validPerson7 = person7.addPerson("22#aabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson7);
		Person person8 = new Person();
		boolean validPerson8 = person8.addPerson("22aaabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson8);
		// Last 2 characters are not upper case
		Person person9 = new Person();
		boolean validPerson9 = person9.addPerson("22##abcdef", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson9);
		Person person10 = new Person();
		boolean validPerson10 = person10.addPerson("22##abcd##", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson10);

		// Test Condition 2
		// Null address
		Person person11 = new Person();
		boolean validPerson11 = person11.addPerson("22##abcdEF", "First Name", "Last Name", null, "15-11-1990");
		assertFalse(validPerson11);
		// Wrong address format
		Person person12 = new Person();
		boolean validPerson12 = person12.addPerson("22##abcdEF", "First Name", "Last Name", "", "15-11-1990");
		assertFalse(validPerson12);
		Person person13 = new Person();
		boolean validPerson13 = person13.addPerson("22##abcdEF", "First Name", "Last Name", "||||", "15-11-1990");
		assertFalse(validPerson13);
		Person person14 = new Person();
		boolean validPerson14 = person14.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|", "15-11-1990");
		assertFalse(validPerson14);

		// Test Condition 3
		// Null birthDate
		Person person15 = new Person();
		boolean validPerson15 = person15.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", null);
		assertFalse(validPerson15);
		// Wrong format
		Person person16 = new Person();
		boolean validPerson16 = person16.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "");
		assertFalse(validPerson16);
		Person person17 = new Person();
		boolean validPerson17 = person17.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15/11/1990");
		assertFalse(validPerson17);
		Person person18 = new Person();
		boolean validPerson18 = person18.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "This-is-Test");
		assertFalse(validPerson18);

		// Extra Conditions
		// birthDate is in the future
		Person person19 = new Person();
		boolean validPerson19 = person19.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-9999");
		assertFalse(validPerson19);
		// firstName is null or empty string
		Person person20 = new Person();
		boolean validPerson20 = person20.addPerson("22##abcdEF", null, "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson20);
		Person person21 = new Person();
		boolean validPerson21 = person21.addPerson("22##abcdEF", "", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson21);
		// lastName is null or empty string
		Person person22 = new Person();
		boolean validPerson22 = person22.addPerson("22##abcdEF", "First Name", null, "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson22);
		Person person23 = new Person();
		boolean validPerson23 = person23.addPerson("22##abcdEF", "First Name", "", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson23);
	}

	@Test
	public void testAddID() {
	}
}
