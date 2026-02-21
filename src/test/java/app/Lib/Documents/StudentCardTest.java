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

public class StudentCardTest {

	@BeforeEach
	void setup() {
		TestHelper.deleteCsvFiles();
		TestHelper.createTestPerson();
	}

	static Stream<Arguments> validStudentCardProvider() {
		return Stream.of(
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Standard valid student card"),
				Arguments.of(new StudentCard("999999999999", "33##abcdEF", "Bob", "15-05-1995", "Australia", "01-06-2021", "01-06-2031"), "Upper bound digits"),
				Arguments.of(new StudentCard("000000000001", "44##abcdEF", "Carol", "29-02-2004", "Australia", "01-01-2022", "01-01-2032"), "Leap year DOB valid"),
				Arguments.of(new StudentCard("100000000000", "55##abcdEF", "Dan", "31-12-1980", "Australia", "15-03-2019", "15-03-2029"), "Zeros in numeric portion"),
				Arguments.of(new StudentCard("654321987654", "66##abcdEF", "Eve", "10-10-1990", "Australia", "01-01-2018", "01-01-2028"), "Mixed digits valid")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validStudentCardProvider")
	void testIsValid_ValidStudentCard(StudentCard card, String label) {
		assertTrue(card.isValid());
	}

	static Stream<Arguments> invalidStudentCardProvider() {
		return Stream.of(
				Arguments.of(new StudentCard("12345678901", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Too short (11 digits)"),
				Arguments.of(new StudentCard("1234567890123", "23##abcdEF", "Bob", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Too long (13 digits)"),
				Arguments.of(new StudentCard("12345678901A", "24##abcdEF", "Carol", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains letter at end"),
				Arguments.of(new StudentCard("A23456789012", "25##abcdEF", "Dan", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains letter at start"),
				Arguments.of(new StudentCard("123456 89012", "26##abcdEF", "Eve", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains space"),
				Arguments.of(new StudentCard("12345-789012", "27##abcdEF", "Frank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains symbol"),
				Arguments.of(new StudentCard("AB1234567890", "28##abcdEF", "Grace", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Letters instead of digits"),

				Arguments.of(new StudentCard(null, "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null ID"),
				Arguments.of(new StudentCard("123456789012", null, "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null personID"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", null, "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null name"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "Alice", null, "Australia", "01-01-2020", "01-01-2030"), "Null DOB"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "Alice", "01-01-2000", null, "01-01-2020", "01-01-2030"), "Null country"),

				Arguments.of(new StudentCard("", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty ID"),
				Arguments.of(new StudentCard("123456789012", "", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty personID"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty name"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "Alice", "", "Australia", "01-01-2020", "01-01-2030"), "Empty DOB"),
				Arguments.of(new StudentCard("123456789012", "22##abcdEF", "Alice", "01-01-2000", "", "01-01-2020", "01-01-2030"), "Empty country")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidStudentCardProvider")
	void testIsValid_InvalidStudentCard(StudentCard card, String label) {
		assertFalse(card.isValid());
	}

	@Test
	void testConstants() {
		var card = new StudentCard("123456789012", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(card.childrenOnly());
		assertEquals("student_cards.csv", card.getFileName());
		assertEquals("", card.extraCsvHeader());
		assertEquals("", card.extraCsvFields());
	}

	@Test
	void testSave_ValidStudentCard_Writes() {
		Path path = Paths.get("student_cards.csv");
		assertFalse(Files.exists(path));

		var card = new StudentCard("123456789012", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(card.save());
		assertTrue(Files.exists(path));

		try {
			var lines = Files.readAllLines(path);
			assertEquals(2, lines.size());
			assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry", lines.get(0));
			assertEquals("123456789012,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030", lines.get(1));
		} catch (Exception e) {
			fail("Exception thrown while reading student_cards.csv: " + e.getMessage());
		}
	}

	@Test
	void testSave_InvalidStudentCard_DoesNotWrite() {
		Path path = Paths.get("student_cards.csv");

		var card = new StudentCard("BAD", "33##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");

		assertFalse(card.save());
		assertFalse(Files.exists(path));
	}
}

