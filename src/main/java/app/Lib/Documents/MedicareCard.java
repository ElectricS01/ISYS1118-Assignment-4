package app.Lib.Documents;

public class MedicareCard extends IdDocument {

  public MedicareCard(
      String id,
      String name,
      String dateOfBirth,
      String country,
      String dateOfIssue,
      String dateOfExpiry) {
    super(id, name, dateOfBirth, country, dateOfIssue, dateOfExpiry);
  }

  @Override
  public boolean isValid() {
    if (!areDatesValid()) return false;

    return id.matches("\\d{9}");
  }
}
