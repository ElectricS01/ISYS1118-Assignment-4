package app.Lib.Documents;

import app.TestHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PassportTest {

  @BeforeAll
  static void setup() {
    TestHelper.deleteCsvFiles();
  }

	static Stream<Arguments> validPassportProvider() {
		return Stream.of(
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Standard valid passport"),
				Arguments.of(new Passport("ZX999999", "33##abcdEF", "Bob", "15-05-1995", "Australia", "01-06-2021", "01-06-2031", "DFAT"), "Upper bound digits"),
				Arguments.of(new Passport("CD000001", "44##abcdEF", "Carol", "29-02-2004", "Australia", "01-01-2022", "01-01-2032", "DFAT"), "Leap year DOB valid"),
				Arguments.of(new Passport("EF123000", "55##abcdEF", "Dan", "31-12-1980", "Australia", "15-03-2019", "15-03-2029", "DFAT"), "Zeros in numeric portion"),
				Arguments.of(new Passport("GH654321", "66##abcdEF", "Eve", "10-10-1990", "Australia", "01-01-2018", "01-01-2028", "DFAT"), "Mixed digits valid")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validPassportProvider")
	void testIsValid_ValidPassport(Passport passport, String label) {
		assertTrue(passport.isValid());
	}

	static Stream<Arguments> invalidPassportProvider() {
		return Stream.of(
				Arguments.of(new Passport("A1234567", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Only one leading letter"),
				Arguments.of(new Passport("1B123456", "23##abcdEF", "Bob", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Starts with digit"),
				Arguments.of(new Passport("ab123456", "24##abcdEF", "Carol", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Lowercase letters not allowed"),
				Arguments.of(new Passport("ABC23456", "25##abcdEF", "Dan", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Three leading letters"),
				Arguments.of(new Passport("AB12345A", "26##abcdEF", "Eve", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Trailing character not digit"),
				Arguments.of(new Passport("AB12345", "27##abcdEF", "Frank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Too short"),
				Arguments.of(new Passport("AB1234567", "28##abcdEF", "Grace", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Too long"),
				Arguments.of(new Passport("AB12 456", "29##abcdEF", "Hank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Contains space"),
				Arguments.of(new Passport("AB12-456", "32##abcdEF", "Ivy", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Contains symbol"),

				Arguments.of(new Passport(null, "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Null ID"),
				Arguments.of(new Passport("AB123456", null, "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Null personID"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", null, "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Null name"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", null, "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Null DOB"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", null, "01-01-2020", "01-01-2030", "DFAT"), "Null country"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", null), "Null authority"),

				Arguments.of(new Passport("", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Empty ID"),
				Arguments.of(new Passport("AB123456", "", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Empty personID"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Empty name"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Empty DOB"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "", "01-01-2020", "01-01-2030", "DFAT"), "Empty country"),
				Arguments.of(new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", ""), "Empty authority"),

				Arguments.of(new Passport("AB123456", "33##abcdEF", "Jake", "31-02-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Invalid birth date"),
				Arguments.of(new Passport("AB123456", "34##abcdEF", "Kate", "01-01-2000", "Australia", "31-02-2020", "01-01-2030", "DFAT"), "Invalid issue date"),
				Arguments.of(new Passport("AB123456", "35##abcdEF", "Liam", "01-01-2000", "Australia", "01-01-2020", "31-02-2030", "DFAT"), "Invalid expiry date"),
				Arguments.of(new Passport("AB123456", "36##abcdEF", "Mia", "01-01-2000", "Australia", "01-01-2030", "01-01-2020", "DFAT"), "Expiry before issue"),
				Arguments.of(new Passport("AB123456", "37##abcdEF", "Noah", "01-01-2030", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Birth date in future")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidPassportProvider")
	void testIsValid_InvalidPassport(Passport passport, String label) {
		assertFalse(passport.isValid());
	}

	@Test
	void testConstants() {
		var passport = new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT");
		assertFalse(passport.childrenOnly());
		assertEquals("passports.csv", passport.getFileName());
		assertEquals(",authority", passport.extraCsvHeader());
		assertEquals(",DFAT", passport.extraCsvFields());
	}

	@Test
	void testSave_WritesValidPassword() {
		TestHelper.deleteCsvFiles();
		Path path = Paths.get("passports.csv");
		assertFalse(Files.exists(path));

		var passport = new Passport("AB123456", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT");
		assertTrue(passport.save());
		assertTrue(Files.exists(path));

		try {
			var lines = Files.readAllLines(path);
			assertEquals(2, lines.size());
			assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry,authority", lines.get(0));
			assertEquals("AB123456,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030,DFAT", lines.get(1));
		} catch (Exception e) {
			fail("Exception thrown while reading passports.csv: " + e.getMessage());
		}
	}
}
