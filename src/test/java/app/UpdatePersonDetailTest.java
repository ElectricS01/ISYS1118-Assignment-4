package app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public class UpdatePersonDetailTest {
    @Test
    public void testUpdatePersonDetails_ValidUpdateAddressMinor() {
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("23##abcdEF", "First Name", "Last Name", "99|New Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("people.csv"));
            for (String line : lines) {
                if (line.matches("23##abcdEF,First Name,Last Name,99\\|New Street\\|Melbourne\\|Victoria\\|Australia,15-11-1990.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testUpdatePersonDetails_InvalidUpdateAddressMinor() {
        // A person under 18 should NOT be able to update their address (Condition 1)
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-2010");
        boolean result = person.updatePersonDetails("23##abcdEF", "First Name", "Last Name", "99|New Street|Melbourne|Victoria|Australia", "15-11-2010");
        assertFalse(result);
    }

    @Test
    public void testUpdatePersonDetails_ValidUpdateBirthday() {
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-2010");
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("people.csv"));
            for (String line : lines) {
                if (line.matches("23##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-2010.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testUpdatePersonDetails_InvalidUpdateBirthday() {
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("23##abcdEF", "First Name", "Last Name", "99|New Street|Melbourne|Victoria|Australia", "15-11-2010");
        assertFalse(result);
    }

    @Test
    public void testUpdatePersonDetails_ValidUpdatePersonID() {
        Person person = new Person();
        person.addPerson("33##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("43##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("people.csv"));
            for (String line : lines) {
                if (line.matches("43##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-1990.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testUpdatePersonDetails_InvalidUpdatePersonID() {
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("33##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }

    @Test
    public void testUpdatePersonDetails_PersonDoesNotExist() {
    Person person = new Person();
    boolean result = person.updatePersonDetails("99##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
    assertFalse(result);
    }

   @Test
    public void testUpdatePersonDetails_InvalidUpdateIDEvenFirstDigit() {
        Person person = new Person();
        person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        boolean result = person.updatePersonDetails("32##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(result);
    }
}
