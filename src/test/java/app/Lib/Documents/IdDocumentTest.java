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

public class IdDocumentTest {

	// Minimal subclass to test shared IdDocument logic in isolation
	private static class IdDocumentForTest extends IdDocument {
		IdDocumentForTest(String id, String personID, String name, String dob, String country, String issue, String expiry) {
			super(id, personID, name, dob, country, issue, expiry);
		}

		@Override
		public boolean isValid() {
			return !areDatesInvalid() && id != null && !id.isEmpty();
		}

		@Override
		protected String getFileName() {
			return "stub_documents.csv";
		}

		@Override
		protected int getPersonCsvColumnIndex() {
			return 5;
		}
	}

	@BeforeEach
	void setup() {
		TestHelper.deleteCsvFiles();
		try { Files.deleteIfExists(Paths.get("stub_documents.csv")); } catch (Exception ignored) {}
	}

	@Test
	void testGetPersonID() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertEquals("22##abcdEF", doc.getPersonID());
	}

	@Test
	void testChildrenOnly_DefaultFalse() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertFalse(doc.childrenOnly());
	}

	@Test
	void testExtraCsvHeader_DefaultEmpty() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertEquals("", doc.extraCsvHeader());
	}

	@Test
	void testExtraCsvFields_DefaultEmpty() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertEquals("", doc.extraCsvFields());
	}

	@Test
	void testGetCsvHeader() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry", doc.getCsvHeader());
	}

	@Test
	void testToCsvRow() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertEquals("ID1,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030", doc.toCsvRow());
	}

	@Test
	void testAreDatesInvalid_AllValid() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(doc.isValid());
	}

	static Stream<Arguments> invalidDateProvider() {
		return Stream.of(
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", null, "Australia", "01-01-2020", "01-01-2030"), "Null birth date"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", null, "01-01-2030"), "Null issue date"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", null), "Null expiry date"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2030", "Australia", "01-01-2020", "01-01-2030"), "Birth date in future"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2030", "01-01-2035"), "Issue date in future"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2025", "01-01-2020"), "Expiry before issue"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-06-2020", "Australia", "01-01-2020", "01-01-2030"), "Birth date after issue"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2010", "01-01-2015"), "Expiry in past"),
				Arguments.of(new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "31-02-2000", "Australia", "01-01-2020", "01-01-2030"), "Invalid date format")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidDateProvider")
	void testAreDatesInvalid_InvalidDates(IdDocumentForTest doc, String label) {
		assertFalse(doc.isValid());
	}

	@Test
	void testSave_NoPeopleCsv_ReturnsFalse() {
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertFalse(doc.save());
	}

	@Test
	void testSave_PersonNotInCsv_ReturnsFalse() {
		TestHelper.createTestPerson();
		var doc = new IdDocumentForTest("ID1", "99##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertFalse(doc.save());
	}

	@Test
	void testSave_UpdatesPeopleCsv() {
		TestHelper.createTestPerson();
		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(doc.save());

		try {
			var lines = Files.readAllLines(Path.of("people.csv"));
			boolean found = false;
			for (String line : lines) {
				if (line.startsWith("22##abcdEF") && line.contains("ID1")) {
					found = true;
					break;
				}
			}
			assertTrue(found, "people.csv should contain the document ID for the person");
		} catch (Exception e) {
			fail("Exception reading people.csv: " + e.getMessage());
		}
	}

	@Test
	void testSave_WritesDocumentCsv() {
		TestHelper.createTestPerson();
		Path path = Paths.get("stub_documents.csv");
		assertFalse(Files.exists(path));

		var doc = new IdDocumentForTest("ID1", "22##abcdEF", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030");
		assertTrue(doc.save());
		assertTrue(Files.exists(path));

		try {
			var lines = Files.readAllLines(path);
			assertEquals(2, lines.size());
			assertEquals("id,personID,name,dateOfBirth,country,dateOfIssue,dateOfExpiry", lines.get(0));
			assertEquals("ID1,22##abcdEF,Alice,01-01-2000,Australia,01-01-2020,01-01-2030", lines.get(1));
		} catch (Exception e) {
			fail("Exception reading stub_documents.csv: " + e.getMessage());
		}
	}
}
