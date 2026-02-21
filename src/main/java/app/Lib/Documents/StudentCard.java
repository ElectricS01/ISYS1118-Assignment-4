package app.Lib.Documents;

public class StudentCard extends IdDocument {

  public StudentCard(
      String id,
      String personID,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    super(id, personID, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
  }

  @Override
  public boolean isValid() {
    if (areDatesInvalid()) return false;

    if (id == null || personID == null || name == null || dateOfBirth == null || country == null) return false;
    if (id.isEmpty() || personID.isEmpty() || name.isEmpty() || dateOfBirth.isEmpty() || country.isEmpty()) return false;

    return id.matches("\\d{12}");
  }

  @Override
  public boolean childrenOnly() {
    return true;
  }

  @Override
  protected String getFileName() {
    return "student_cards.csv";
  }

  @Override
  protected int getPersonCsvColumnIndex() {
    return 8;
  }
}
