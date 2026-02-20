package app.Lib;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateHelperTest {

	static Stream<org.junit.jupiter.params.provider.Arguments> validDateProvider() {
		return Stream.of(
				Arguments.of("01-01-2000", "Standard date passes"),
				Arguments.of("31-12-1999", "End of year passes"),
				Arguments.of("29-02-2024", "Leap day passes"),
				Arguments.of("29-02-2000", "Century leap day passes")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validDateProvider")
	void testIsValidDate_Valid(String input, String label) {
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
}
