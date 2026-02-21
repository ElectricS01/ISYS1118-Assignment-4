package app.Lib.Documents;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PassportTest {

	static Stream<Arguments> validPassportProvider() {
		return Stream.of(
				Arguments.of(new Passport("AB123456", "P1", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Standard valid passport"),
				Arguments.of(new Passport("ZX999999", "P2", "Bob", "15-05-1995", "Australia", "01-06-2021", "01-06-2031", "DFAT"), "Upper bound digits"),
				Arguments.of(new Passport("CD000001", "P3", "Carol", "29-02-2004", "Australia", "01-01-2022", "01-01-2032", "DFAT"), "Leap year DOB valid"),
				Arguments.of(new Passport("EF123000", "P4", "Dan", "31-12-1980", "Australia", "15-03-2019", "15-03-2029", "DFAT"), "Zeros in numeric portion"),
				Arguments.of(new Passport("GH654321", "P5", "Eve", "10-10-1990", "Australia", "01-01-2018", "01-01-2028", "DFAT"), "Mixed digits valid")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("validPassportProvider")
	void testIsValid_ValidPassport(Passport passport, String label) {
		assertTrue(passport.isValid());
	}

	static Stream<Arguments> invalidPassportProvider() {
		return Stream.of(
				Arguments.of(new Passport("A1234567", "P1", "Alice", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Only one leading letter"),
				Arguments.of(new Passport("1B123456", "P2", "Bob", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Starts with digit"),
				Arguments.of(new Passport("ab123456", "P3", "Carol", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Lowercase letters not allowed"),
				Arguments.of(new Passport("ABC23456", "P4", "Dan", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Three leading letters"),
				Arguments.of(new Passport("AB12345A", "P5", "Eve", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Trailing character not digit"),
				Arguments.of(new Passport("AB12345", "P6", "Frank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Too short"),
				Arguments.of(new Passport("AB1234567", "P7", "Grace", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Too long"),
				Arguments.of(new Passport("AB12 456", "P8", "Hank", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Contains space"),
				Arguments.of(new Passport("AB12-456", "P9", "Ivy", "01-01-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Contains symbol"),
				Arguments.of(new Passport("AB123456", "P10", "Jake", "31-02-2000", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Invalid birth date"),
				Arguments.of(new Passport("AB123456", "P11", "Kate", "01-01-2000", "Australia", "31-02-2020", "01-01-2030", "DFAT"), "Invalid issue date"),
				Arguments.of(new Passport("AB123456", "P12", "Liam", "01-01-2000", "Australia", "01-01-2020", "31-02-2030", "DFAT"), "Invalid expiry date"),
				Arguments.of(new Passport("AB123456", "P13", "Mia", "01-01-2000", "Australia", "01-01-2030", "01-01-2020", "DFAT"), "Expiry before issue"),
				Arguments.of(new Passport("AB123456", "P14", "Noah", "01-01-2030", "Australia", "01-01-2020", "01-01-2030", "DFAT"), "Birth date in future")
		);
	}

	@ParameterizedTest(name = "{1}")
	@MethodSource("invalidPassportProvider")
	void testIsValid_InvalidPassport(Passport passport, String label) {
		assertFalse(passport.isValid());
	}
}
