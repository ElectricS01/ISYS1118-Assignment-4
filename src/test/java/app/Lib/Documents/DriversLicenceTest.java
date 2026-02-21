package app.Lib.Documents;

import app.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DriversLicenceTest {

	@BeforeEach
	void setup() {
		TestHelper.deleteCsvFiles();
		TestHelper.createTestPerson();
	}

	static Stream<Arguments> validLicenceProvider() {
		return Stream.of(
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Standard valid licence"),
				Arguments.of(new DriversLicence("ZX87654321", "33##abcdEF", "Bob", "15-05-1995", "Australia", "01-06-2021", "01-06-2031", "2", "Motorbike"), "Upper bound digits"),
				Arguments.of(new DriversLicence("CD00000001", "44##abcdEF", "Carol", "29-02-2004", "Australia", "01-01-2022", "01-01-2032", "3", "Truck"), "Leap year DOB valid"),
				Arguments.of(new DriversLicence("EF12340000", "55##abcdEF", "Dan", "31-12-1980", "Australia", "15-03-2019", "15-03-2029", "1", "Car"), "Zeros in numeric portion"),
				Arguments.of(new DriversLicence("GH65432199", "66##abcdEF", "Eve", "10-10-1990", "Australia", "01-01-2018", "01-01-2028", "2", "Bus"), "Mixed digits valid")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validLicenceProvider")
	void testIsValid_ValidDriversLicence(DriversLicence licence, String label) {
		assertTrue(licence.isValid());
	}

	static Stream<Arguments> invalidLicenceProvider() {
		return Stream.of(
				Arguments.of(new DriversLicence("A123456789", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Only one leading letter"),
				Arguments.of(new DriversLicence("1B12345678", "23##abcdEF", "Bob", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Starts with digit"),
				Arguments.of(new DriversLicence("ab12345678", "24##abcdEF", "Carol", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Lowercase letters not allowed"),
				Arguments.of(new DriversLicence("ABC2345678", "25##abcdEF", "Dan", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Three leading letters"),
				Arguments.of(new DriversLicence("AB1234567A", "26##abcdEF", "Eve", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Trailing character not digit"),
				Arguments.of(new DriversLicence("AB1234567", "27##abcdEF", "Frank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Too short"),
				Arguments.of(new DriversLicence("AB1234567890", "28##abcdEF", "Grace", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Too long"),
				Arguments.of(new DriversLicence("AB12 45678", "29##abcdEF", "Hank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Contains space"),
				Arguments.of(new DriversLicence("AB12-45678", "32##abcdEF", "Ivy", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Contains symbol"),

				Arguments.of(new DriversLicence(null, "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Null ID"),
				Arguments.of(new DriversLicence("AB12345678", null, "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Null personID"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", null, "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Null name"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", null, "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Null DOB"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", null, "01-01-2020", "01-01-2030", "1", "Car"), "Null country"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", null), "Null vehicleType"),

				Arguments.of(new DriversLicence("", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Empty ID"),
				Arguments.of(new DriversLicence("AB12345678", "", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Empty personID"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Empty name"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "", "Australia", "01-01-2020", "01-01-2030", "1", "Car"), "Empty DOB"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "", "01-01-2020", "01-01-2030", "1", "Car"), "Empty country"),
				Arguments.of(new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", ""), "Empty vehicleType")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidLicenceProvider")
	void testIsValid_InvalidDriversLicence(DriversLicence licence, String label) {
		assertFalse(licence.isValid());
	}

	@Test
	void testConstants() {
		var licence = new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car");
		assertFalse(licence.childrenOnly());
		assertEquals("drivers_licences.csv", licence.getFileName());
		assertEquals(",version,vehicleType", licence.extraCsvHeader());
		assertEquals(",1,Car", licence.extraCsvFields());
	}

	@Test
	void testSave_ValidLicense_Writes() {
		Path path = Paths.get("drivers_licences.csv");
		assertFalse(Files.exists(path));

		var licence = new DriversLicence("AB12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car");
		assertTrue(licence.save());
		assertTrue(Files.exists(path));

		try {
			var lines = Files.readAllLines(path);
			assertEquals(2, lines.size());
			assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry,version,vehicleType", lines.get(0));
			assertEquals("AB12345678,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030,1,Car", lines.get(1));
		} catch (Exception e) {
			fail("Exception thrown while reading drivers_licences.csv: " + e.getMessage());
		}
	}

	@Test
	void testSave_InvalidLicense_DoesNotWrite() {
		Path path = Paths.get("drivers_licences.csv");

		var licence = new DriversLicence("BAD", "33##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "1", "Car");

		assertFalse(licence.save());
		assertFalse(Files.exists(path));
	}
}