import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Patient extends User {

    private DossierMedical dossierMedical;
    private LocalDate date_Dernière_Consultation;

    public Patient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.setMotDePasse(motDePasse);
        this.adresse = adresse;
        this.dossierMedical = new DossierMedical();
    }

    public LocalDate get_Date_Dernière_Consultation() {
        return date_Dernière_Consultation;
    }

    public void set_Date_Dernière_Consultation(String date) {
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

    public DossierMedical getDossierMedical() {
        return dossierMedical;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128); // capacité initiale
        sb.append("Nom: ").append(nom).append(", \n");
        sb.append("Prénom: ").append(prenom).append(", \n");
        sb.append("Date de la dernière consultation: ").append(afficher_Date_Dernière_Consultation()).append(", \n");

        return sb.toString();
    }
}
