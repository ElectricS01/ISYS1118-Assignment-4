package app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import app.Lib.Documents.DriversLicence;
import app.Lib.Documents.IdDocument;
import app.Lib.Documents.Passport;
import app.Lib.Documents.StudentCard;

public class AddIdTest {

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
                "82##abcdEF",
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
