package app.Lib;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DateHelperTest {

	static Stream<Arguments> validDateProvider() {
		return Stream.of(
				Arguments.of("01-01-2000", LocalDate.of(2000, 1, 1), "Standard date passes"),
				Arguments.of("31-12-1999", LocalDate.of(1999, 12, 31), "End of year passes"),
				Arguments.of("29-02-2024", LocalDate.of(2024, 2, 29), "Leap day passes"),
				Arguments.of("29-02-2000", LocalDate.of(2000, 2, 29), "Century leap day passes")
		);
	}

	@ParameterizedTest(name = "{2}")
	@MethodSource("validDateProvider")
	void testParseDate_Valid(String input, LocalDate expected, String label) {
		LocalDate parsed = DateHelper.parseDate(input);
		assertNotNull(parsed, "Date should parse: " + input);
		assertEquals(expected, parsed, "Parsed date does not match expected: " + input);
	}

	@ParameterizedTest(name = "{2}")
	@MethodSource("validDateProvider")
	void testIsValidDate_Valid(String input, LocalDate expected, String label) {
		assertTrue(DateHelper.isValidDate(input), "Date should be valid: " + input);
	}

	static Stream<Arguments> invalidDateProvider() {
		return Stream.of(
				Arguments.of("", "Empty string fails"),
				Arguments.of("   ", "Whitespace fails"),
				Arguments.of(null, "Null fails"),
				Arguments.of("31-02-2024", "Feb 31 impossible"),
				Arguments.of("30-02-2024", "Feb 30 impossible"),
				Arguments.of("31-04-2024", "April 31 invalid"),
				Arguments.of("01/01/2000", "Wrong separator fails"),
				Arguments.of("2000-01-01", "Wrong format fails"),
				Arguments.of("1-1-2000", "Missing leading zeros fails"),
				Arguments.of("01-13-2000", "Month 13 fails"),
				Arguments.of("32-01-2000", "Day 32 fails"),
				Arguments.of("00-01-2000", "Day zero fails"),
				Arguments.of("01-00-2000", "Month zero fails"),
				Arguments.of("29-02-2023", "Non-leap Feb 29 fails"),
				Arguments.of("29-02-1900", "Century non-leap Feb 29 fails"),
				Arguments.of("01-01-20", "Year too short fails"),
				Arguments.of("01-01-ABCD", "Year non-numeric fails")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidDateProvider")
	void testIsValidDate_Invalid(String input, String label) {
		assertFalse(DateHelper.isValidDate(input), "Date should be invalid: " + input);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidDateProvider")
	void testParseDate_Invalid(String input, String label) {
		assertNull(DateHelper.parseDate(input), "Date should fail to parse: " + input);
	}

	static Stream<Arguments> under18Provider() {
		return Stream.of(
				Arguments.of("22-02-2008", LocalDate.of(2026, 2, 21), true, "Under 18"),
				Arguments.of("21-02-2008", LocalDate.of(2026, 2, 21), false, "Exactly 18"),
				Arguments.of("20-02-2008", LocalDate.of(2026, 2, 21), false, "Just over 18"),
				Arguments.of("30-02-2008", LocalDate.of(2026,2,21), false, "Invalid date"),
				Arguments.of("22/02/2008", LocalDate.of(2026,2,21), false, "Invalid date format"),
				Arguments.of(null, LocalDate.of(2026,2,21), false, "Null input"),
				Arguments.of("29-02-2004", LocalDate.of(2022, 2, 28), true, "Leap day not yet 18"),
				Arguments.of("29-02-2004", LocalDate.of(2022, 3, 1), false, "Leap day just turned 18")
		);
	}

	@ParameterizedTest(name = "{3}")
	@MethodSource("under18Provider")
	void testIsOver18(String birthDate, LocalDate today, boolean expected, String label) {
		assertEquals(expected, DateHelper.isUnder18(birthDate, today));
	}
}
