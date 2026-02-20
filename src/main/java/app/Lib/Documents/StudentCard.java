package app.Lib.Documents;

public class StudentCard extends IdDocument {

  public StudentCard(
      String id,
      String personId,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    super(id, personId, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
  }

  @Override
  public boolean childrenOnly() {
    return true;
  }

  @Override
  protected String getFileName() {
    return "student_cards.txt";
  }

  @Override
  public boolean isValid() {
    if (areDatesInvalid()) return false;

    return id.matches("\\d{12}");
  }
}
