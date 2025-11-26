import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Patient extends User {

    private LocalDate date_Dernière_Consultation;

    public Patient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.set_MotDePasse(motDePasse);
        this.adresse = adresse;
        private final DossierMedical dossierMedical;
    }

    @SuppressWarnings("unused")
    private void Programmer_Consultation() {
        // Implémentation de la méthode Programmer_Consultation
    }

    public LocalDate get_Date_Dernière_Consultation() {
        return date_Dernière_Consultation;
    }

    @SuppressWarnings("unused")
    private void set_Date_Dernière_Consultation(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        this.date_Dernière_Consultation = LocalDate.parse(date, formatter);

    }

    public String afficher_Date_Dernière_Consultation() {
        if (date_Dernière_Consultation == null) {
            return "Aucune consultation enregistrée.";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date_Dernière_Consultation.format(formatter);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Date de la dernière consultation: ").append(afficher_Date_Dernière_Consultation()).append(", \n");

        return sb.toString();
    }
}
