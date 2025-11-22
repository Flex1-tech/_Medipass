
public class Patient extends User {
    private String date_Dernière_Consultation;
    private boolean consulter() {
        // Implémentation de la méthode consulter
        return true;
    }
    private void Programmer_Consultation() {
        // Implémentation de la méthode Programmer_Consultation
    }

    public String get_Date_Dernière_Consultation() {
        return date_Dernière_Consultation;
    }
    private void set_Date_Dernière_Consultation(String date) {
        this.date_Dernière_Consultation = date;
    }
}
