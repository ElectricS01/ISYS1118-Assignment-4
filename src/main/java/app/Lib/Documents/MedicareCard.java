package app.Lib.Documents;

public class MedicareCard extends IdDocument {

  public MedicareCard(
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

    return id.matches("\\d{9}");
  }

  @Override
  protected String getFileName() {
    return "medicare_cards.csv";
  }
}
