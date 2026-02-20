package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import app.Lib.Documents.IdDocument;
import app.Lib.Documents.Passport;

public class PersonTest {

    @Test
    public void testAddPerson_ValidNormalInputs() {
        File file = new File("people.csv");
        long fileSize = file.length();
        Person person = new Person();
        boolean validPerson = person.addPerson("56s_d%&fAB", "Ronald", "Foong", "32|Elizabeth Street|Melbourne|Victoria|Australia", "15-11-2006");
        assertTrue(validPerson);
        assertTrue(fileSize < file.length());

        fileSize = file.length();
        person = new Person();
        validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertTrue(validPerson);
        assertTrue(fileSize < file.length());
    }

    @Test
    public void testAddPerson_ValidLeapYearAndThirtyOneDayMonths() {
        File file = new File("people.csv");
        long fileSize = file.length();
        Person person = new Person();
        boolean validPerson = person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "29-02-2000");
        assertTrue(validPerson);
        assertTrue(fileSize < file.length());

        fileSize = file.length();
        person = new Person();
        validPerson = person.addPerson("24##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "31-01-1990");
        assertTrue(validPerson);
        assertTrue(fileSize < file.length());
    }

    @Test
    public void testAddPerson_NullPersonID() {
        Person person = new Person();
        boolean validPerson = person.addPerson(null, "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_PersonIDNotTenChar() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_PersonIDFirstTwoDigitsNotBetweenTwoAndNine() {
        Person person = new Person();
        boolean validPerson = person.addPerson("12##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("20##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_PersonIDLessThanTwoSpecialCharIndexThreeAndEight() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22#aabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22aaabcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_PersonIDLastTwoCharNotUpperCase() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdef", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22##abcd##", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_NullAddress() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", null, "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_InvalidAddressFormat() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "||||", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_WrongAddressInformation() {
        Person person = new Person();
		boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "Thirty-Eight|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "38|Highland Street|Melbourne|New South Wales|Australia", "15-11-1990");
		assertFalse(validPerson);

		person = new Person();
		validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "38|Highland Street|Melbourne|Victoria|United States", "15-11-1990");
		assertFalse(validPerson);

    }
    @Test
    public void testAddPerson_NullBirthDate() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", null);
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_InvalidBirthDateFormat() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "");
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
    }

    @Test
    public void testAddPerson_InvalidBirthDateValues() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "00-11-1990");
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
    }

    @Test
    public void testAddPerson_BirthDateInFuture() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-9999");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_NullOrEmptyFirstName() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", null, "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22##abcdEF", "", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    @Test
    public void testAddPerson_NullOrEmptyLastName() {
        Person person = new Person();
        boolean validPerson = person.addPerson("22##abcdEF", "First Name", null, "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);

        person = new Person();
        validPerson = person.addPerson("22##abcdEF", "First Name", "", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        assertFalse(validPerson);
    }

    //updatePersonDetails() tests
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

    //addID() tests
    @Test
    public void testAddID_ValidPassport() {
    Person person = new Person();
    person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
    IdDocument passport = new Passport(
                "AB123456",
                "First Name Last Name",
                "15-11-1990",
                "Australia",
                "01-01-1990",
                "15-11-1990",
                "AU-GOV"
        );
    boolean result = person.addID(passport);
    assertTrue(result);
    }
    @Test
    public void testAddID_InValidPassport() {
    Person person = new Person();
    person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
    IdDocument passport = new Passport(
                "23123456",
                "First Name Last Name",
                "15-11-1990",
                "Australia",
                "01-01-1990",
                "15-11-1990",
                "AU-GOV"
        );
    boolean result = person.addID(passport);
    assertFalse(result);
    }

}

