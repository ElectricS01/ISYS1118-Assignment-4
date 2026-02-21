package app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.Lib.Documents.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AddIdTest {

    @BeforeEach
    public void setUp() throws IOException {
        TestHelper.deleteCsvFiles();
    }

    @Test
    public void testAddID_ValidPassport() {
        Person person = new Person();
        person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        IdDocument passport = new Passport(
                    "AB123456",
                    "22##abcdEF",
                    "First Name Last Name",
                    "15-11-1990",
                    "Australia",
                    "01-01-2020",
                    "01-01-2030",
                    "AU-GOV"
            );
        boolean result = person.addID(passport);
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("./people.csv"));
            for (String line : lines) {
                if (line.matches("22##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-1990,AB123456.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testAddID_ValidDriversLicence() {
        Person person = new Person();
        person.addPerson("23##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        IdDocument driversLicence = new DriversLicence(
                "AB12345678",
                "23##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030",
                "1",
                "CAR"
        );
        boolean result = person.addID(driversLicence);
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("./people.csv"));
            for (String line : lines) {
                if (line.matches("23##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-1990,.*,AB12345678.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testAddID_ValidMedicareCard() {
        Person person = new Person();
        person.addPerson("24##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        IdDocument medicareCard = new MedicareCard(
                "123456789",
                "24##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030"
        );
        boolean result = person.addID(medicareCard);
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("./people.csv"));
            for (String line : lines) {
                if (line.matches("24##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-1990,.*,.*,123456789.*")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testAddID_ValidStudentCard() {
        Person person = new Person();
        person.addPerson("25##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-2010");
        IdDocument studentCard = new StudentCard(
                "123456789012",
                "25##abcdEF",
                "First Last",
                "15-11-2010",
                "Australia",
                "01-01-2015",
                "01-01-2030"
        );
        boolean result = person.addID(studentCard);
        assertTrue(result);

        try {
            result = false;
            java.util.List<String> lines = Files.readAllLines(Path.of("./people.csv"));
            for (String line : lines) {
                if (line.matches("25##abcdEF,First Name,Last Name,32\\|Highland Street\\|Melbourne\\|Victoria\\|Australia,15-11-2010,.*,.*,.*,123456789012")) {
                    result = true;
                }
            }
            assertTrue(result);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }

    @Test
    public void testAddID_InValidPassport() {
        Person person = new Person();
        person.addPerson("22##abcdEF", "First Name", "Last Name", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");
        IdDocument passport = new Passport(
                    "23123456",
                    "22##abcdEF",
                    "First Name Last Name",
                    "15-11-1990",
                    "Australia",
                    "01-01-2020",
                    "01-01-2030",
                    "AU-GOV"
            );
        boolean result = person.addID(passport);
        assertFalse(result);
    }

    @Test
    public void testAddID_Passport_PersonIdMismatch() {
        Person person = new Person();
        person.addPerson("30##abcdEF", "First", "Last", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument passport = new Passport(
                "AB123456",
                "31##abcdEF", // mismatch with added person
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030",
                "AU-GOV"
        );

        assertFalse(person.addID(passport));
    }

    @Test
    public void testAddID_DriversLicence_InvalidLength() {
        Person person = new Person();
        person.addPerson("51##abcdEF", "First", "Last", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument dl = new DriversLicence(
                "AB1234567", // 9 chars
                "51##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030",
                "1",
                "CAR"
        );
        assertFalse(person.addID(dl));
    }

    @Test
    public void testAddID_DriversLicence_InvalidDigitsPartHasLetter() {
        Person person = new Person();
        person.addPerson("85##abcdEF", "First", "Last",
                "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument dl = new DriversLicence(
                "AB1234C678", // digits section contains letter
                "85##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030",
                "1",
                "CAR"
        );

        assertFalse(person.addID(dl));
    }

    @Test
    public void testAddID_MedicareCard_InvalidIdWrongLength() {
        Person person = new Person();
        person.addPerson("27##abcdEF", "First", "Last",
                "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument mc = new MedicareCard(
                "12345678", // 8 digits, should be 9
                "27##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030"
        );

        assertFalse(person.addID(mc));
    }

    @Test
    public void testAddID_StudentCard_InvalidIdWrongLength() {
        Person person = new Person();
        person.addPerson("28##abcdEF", "First", "Last",
                "32|Highland Street|Melbourne|Victoria|Australia", "15-11-2010");

        IdDocument sc = new StudentCard(
                "12345678901", // 11 digits, should be 12
                "28##abcdEF",
                "First Last",
                "15-11-2010",
                "Australia",
                "01-01-2015",
                "01-01-2030"
        );

        assertFalse(person.addID(sc));
    }

    @Test
    public void testAddID_StudentCard_Adult() {
        Person person = new Person();
        person.addPerson("73##abcdEF", "First", "Last", "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument student = new StudentCard(
                "123456789012",
                "73##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030"
        );

        assertFalse(person.addID(student));
    }

    @Test
    public void testAddID_Passport_ExpiredDocument() {
        Person person = new Person();
        person.addPerson("32##abcdEF", "First", "Last",
                "32|Highland Street|Melbourne|Victoria|Australia", "15-11-1990");

        IdDocument passport = new Passport(
                "AB123456",
                "32##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2010",
                "01-01-2020", // expired
                "AU-GOV"
        );

        assertFalse(person.addID(passport));
    }

    @Test
    public void testAddID_PersonMustExist() {
        Person person = new Person(); // no addPerson call

        IdDocument passport = new Passport(
                "AB123456",
                "90##abcdEF",
                "First Last",
                "15-11-1990",
                "Australia",
                "01-01-2020",
                "01-01-2030",
                "AU-GOV"
        );

        assertFalse(person.addID(passport));
    }
}
