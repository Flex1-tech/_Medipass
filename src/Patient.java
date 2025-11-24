import java.time.LocalDate;

public class Patient extends User {

    private LocalDate date_Dernière_Consultation;
    public Patient(String nom, String prenom, String telephone, String motDePasse, String adresse) {
        super(); // Appelle le constructeur de la classe parente (User)
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.set_MotDePasse(motDePasse);  // Méthode héritée pour hacher le mot de passe
        this.adresse = adresse;
    }
    
    private void Programmer_Consultation() {
        // Implémentation de la méthode Programmer_Consultation
    }

    public LocalDate get_Date_Dernière_Consultation() {
        return date_Dernière_Consultation;
    }
    private void set_Date_Dernière_Consultation(LocalDate date) {
        this.date_Dernière_Consultation = date;
    }
}
