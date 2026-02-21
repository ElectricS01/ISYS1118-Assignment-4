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

public class MedicareCardTest {

	@BeforeEach
	void setup() {
		TestHelper.deleteCsvFiles();
		TestHelper.createTestPerson();
	}

	static Stream<Arguments> validMedicareCardProvider() {
		return Stream.of(
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Standard valid medicare card"),
				Arguments.of(new MedicareCard("999999999", "33##abcdEF", "Bob", "15-05-1995", "Australia", "01-06-2021", "01-06-2031"), "Upper bound digits"),
				Arguments.of(new MedicareCard("000000001", "44##abcdEF", "Carol", "29-02-2004", "Australia", "01-01-2022", "01-01-2032"), "Leap year DOB valid"),
				Arguments.of(new MedicareCard("100000000", "55##abcdEF", "Dan", "31-12-1980", "Australia", "15-03-2019", "15-03-2029"), "Zeros in numeric portion"),
				Arguments.of(new MedicareCard("654321987", "66##abcdEF", "Eve", "10-10-1990", "Australia", "01-01-2018", "01-01-2028"), "Mixed digits valid")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validMedicareCardProvider")
	void testIsValid_ValidMedicareCard(MedicareCard card, String label) {
		assertTrue(card.isValid());
	}

	static Stream<Arguments> invalidMedicareCardProvider() {
		return Stream.of(
				Arguments.of(new MedicareCard("12345678", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Too short (8 digits)"),
				Arguments.of(new MedicareCard("1234567890", "23##abcdEF", "Bob", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Too long (10 digits)"),
				Arguments.of(new MedicareCard("12345678A", "24##abcdEF", "Carol", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains letter at end"),
				Arguments.of(new MedicareCard("A23456789", "25##abcdEF", "Dan", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains letter at start"),
				Arguments.of(new MedicareCard("1234 5678", "26##abcdEF", "Eve", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains space"),
				Arguments.of(new MedicareCard("12345-789", "27##abcdEF", "Frank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Contains symbol"),
				Arguments.of(new MedicareCard("AB1234567", "28##abcdEF", "Grace", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Letters instead of digits"),

				Arguments.of(new MedicareCard(null, "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null ID"),
				Arguments.of(new MedicareCard("123456789", null, "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null personID"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", null, "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Null name"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "Alice", null, "Australia", "01-01-2020", "01-01-2030"), "Null DOB"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "Alice", "01-01-2000", null, "01-01-2020", "01-01-2030"), "Null country"),

				Arguments.of(new MedicareCard("", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty ID"),
				Arguments.of(new MedicareCard("123456789", "", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty personID"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "", "01-01-2000", "Australia", "01-01-2020", "01-01-2030"), "Empty name"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "Alice", "", "Australia", "01-01-2020", "01-01-2030"), "Empty DOB"),
				Arguments.of(new MedicareCard("123456789", "22##abcdEF", "Alice", "01-01-2000", "", "01-01-2020", "01-01-2030"), "Empty country")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidMedicareCardProvider")
	void testIsValid_InvalidMedicareCard(MedicareCard card, String label) {
		assertFalse(card.isValid());
	}

	@Test
	void testConstants() {
		var card = new MedicareCard("123456789", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertFalse(card.childrenOnly());
		assertEquals("medicare_cards.csv", card.getFileName());
		assertEquals("", card.extraCsvHeader());
		assertEquals("", card.extraCsvFields());
	}

	@Test
	void testSave_ValidMedicareCard_Writes() {
		Path path = Paths.get("medicare_cards.csv");
		assertFalse(Files.exists(path));

		var card = new MedicareCard("123456789", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(card.save());
		assertTrue(Files.exists(path));

		try {
			var lines = Files.readAllLines(path);
			assertEquals(2, lines.size());
			assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry", lines.get(0));
			assertEquals("123456789,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030", lines.get(1));
		} catch (Exception e) {
			fail("Exception thrown while reading medicare_cards.csv: " + e.getMessage());
		}
	}

	@Test
	void testSave_InvalidMedicareCard_DoesNotWrite() {
		Path path = Paths.get("medicare_cards.csv");

		var card = new MedicareCard("BAD", "33##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");

		assertFalse(card.save());
		assertFalse(Files.exists(path));
	}
}

